package de.scout.fireplace.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerAppCompatActivity
import de.scout.fireplace.feature.BuildConfig
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.activity.ActivityCompanion
import de.scout.fireplace.feature.bus.RxBus
import de.scout.fireplace.feature.bus.events.TopCardEvent
import de.scout.fireplace.feature.bus.events.TopCardLongPressEvent
import de.scout.fireplace.feature.bus.events.TopCardPressEvent
import de.scout.fireplace.feature.databinding.ActivityHomeBinding
import de.scout.fireplace.feature.extensions.getDataBinding
import de.scout.fireplace.feature.extensions.getViewModel
import de.scout.fireplace.feature.extensions.plusAssign
import de.scout.fireplace.feature.home.HomeActivity.Companion.IntentOptions
import de.scout.fireplace.feature.models.Expose
import de.scout.fireplace.feature.network.ErrorHandler
import de.scout.fireplace.feature.search.SearchClient
import de.scout.fireplace.feature.search.SearchClient.NoListingsException
import de.scout.fireplace.feature.settings.SettingsActivity
import de.scout.fireplace.feature.ui.FloatingCardStackEvent
import de.scout.fireplace.feature.ui.FloatingCardView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.actionLike
import kotlinx.android.synthetic.main.activity_home.actionPass
import kotlinx.android.synthetic.main.activity_home.coordinator
import kotlinx.android.synthetic.main.activity_home.empty
import kotlinx.android.synthetic.main.activity_home.stack
import kotlinx.android.synthetic.main.activity_home.toggle
import kotlinx.android.synthetic.main.toolbar_home.actionSettings
import kotlinx.android.synthetic.main.toolbar_home.heading
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

  private val disposables: CompositeDisposable = CompositeDisposable()

  private lateinit var binding: ActivityHomeBinding

  private var provider: FusedLocationProviderClient? = null
  private var page = 1

  @Inject internal lateinit var factory: ViewModelProvider.Factory

  @Inject internal lateinit var client: SearchClient
  @Inject internal lateinit var matcher: EventMatcher
  @Inject internal lateinit var reporting: HomeReporting
  @Inject internal lateinit var configuration: HomeConfiguration
  @Inject internal lateinit var navigation: ExposeNavigation
  @Inject internal lateinit var handler: ErrorHandler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = getDataBinding(R.layout.activity_home)
    binding.model = getViewModel(factory)

    setUpActionBar()
    setUpActionButtons()

    setUpLocationProvider()
    setUpPipeline()
    setUpSettings()
    setUpToggle()
    setUpLike()
    setUpPass()
  }

  private fun setUpLocationProvider() {
    provider = LocationServices.getFusedLocationProviderClient(this)
  }

  private fun setUpPipeline() {
    disposables += stack.events()
        .doOnNext { event ->
          if (matcher.match(event)) {
            onMatch(event.expose)
          }
        }
        .doOnNext { (expose, type) ->
          if (type == FloatingCardStackEvent.Type.LIKE) {
            reporting.like(expose)
          } else if (type == FloatingCardStackEvent.Type.PASS) {
            reporting.pass(expose)
          }
        }
        .filter { (_, _, count) -> count - 1 <= CARD_RELOAD_SIZE }
        .subscribe { (_, _, _) -> getLastLocation() }

    RxBus.toObserverable()
        .filter { event -> event is TopCardEvent }
        .subscribe { event -> onTopCardEvent(event as TopCardEvent) }
  }

  private fun setUpSettings() {
    actionSettings.setOnClickListener {
      reporting.settings()
      SettingsActivity.start(this)
    }
  }

  private fun setUpToggle() {
    binding.toggle.setOnClickListener {
      stack.clear()
      getLastLocation()
    }
  }

  private fun setUpLike() {
    actionLike.setOnClickListener {
      if (stack.hasChildren()) {
        val view = stack.topChild
        reporting.manual()
        view.approve()
      }
    }
  }

  private fun setUpPass() {
    actionPass.setOnClickListener {
      if (stack.hasChildren()) {
        val view = stack.topChild
        reporting.manual()
        view.dismiss()
      }
    }
  }

  private fun onTopCardEvent(topCardEvent: TopCardEvent) {
    if (topCardEvent is TopCardPressEvent) {
      onTopCardPressed(topCardEvent.expose)
      return
    }

    if (topCardEvent is TopCardLongPressEvent) {
      onTopCardLongPressed(topCardEvent.expose)
      return
    }
  }

  public override fun onStart() {
    super.onStart()

    if (!checkPermissions()) {
      requestPermissions()
    } else {
      getLastLocation()
    }
  }

  private fun checkPermissions(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

  private fun requestPermissions() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      showSnackbar(R.string.location_permission_rationale, android.R.string.ok, View.OnClickListener { startPermissionRequest() })
    } else {
      startPermissionRequest()
    }
  }

  private fun showSnackbar(@StringRes resId: Int) {
    Snackbar.make(coordinator, getString(resId), Snackbar.LENGTH_LONG).show()
  }

  private fun showSnackbar(@StringRes testResId: Int, @StringRes actionResId: Int, listener: View.OnClickListener) {
    Snackbar.make(coordinator, getString(testResId), Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(actionResId), listener)
        .show()
  }

  private fun startPermissionRequest() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSION)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (requestCode == REQUEST_CODE_PERMISSION) {
      if (grantResults.size >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLastLocation()
      } else {
        showSnackbar(R.string.location_permission_rationale, R.string.label_settings, View.OnClickListener {
          startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
          })
        })
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun getLastLocation() {
    provider?.lastLocation
        ?.addOnCompleteListener(this) { task ->
          if (task.isSuccessful && task.result != null) {
            fetchNearbyResults(check(task.result))
          } else {
            showSnackbar(R.string.error_location_unavailable)
          }
        }
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack()
      return
    }

    super.onBackPressed()
  }

  override fun onDestroy() {
    disposables.clear()
    super.onDestroy()
  }

  private fun setUpActionBar() {
    if (configuration.isSettingsEnabled()) {
      actionSettings.visibility = View.VISIBLE
    }
  }

  private fun setUpActionButtons() {
    ViewCompat.setElevation(actionPass, resources.getDimension(R.dimen.action_elevation))
    ViewCompat.setElevation(actionLike, resources.getDimension(R.dimen.action_elevation))
  }

  private fun check(location: Location): Location {
    if (location.latitude == 0.0 && location.longitude == 0.0) {
      heading.setTextColor(ContextCompat.getColor(this, R.color.light_blue))

      location.latitude = SCOUT_LATITUDE.toDouble()
      location.longitude = SCOUT_LONGITUDE.toDouble()
    } else {
      heading.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }

    return location
  }

  private fun fetchNearbyResults(location: Location) {
    disposables += client.search(location, if (toggle.isChecked) "apartmentrent" else "apartmentbuy", page++)
        .flatMapObservable { (_, _, _, _, _, _, results) -> Observable.fromIterable(results) }
        .doOnComplete { empty.visibility = View.GONE }
        .subscribe({ expose ->
          val card = FloatingCardView(this)
          stack.add(card)

          card.bind(expose)
        }, { throwable ->
          if (throwable is NoListingsException) {
            empty.visibility = View.VISIBLE
          } else {
            handler.accept(throwable)
          }
        })
  }

  private fun onTopCardPressed(expose: Expose) {
    if (!configuration.isGalleryEnabled()) {
      return
    }

    val fragment = GalleryFragment()
    fragment.bind(expose.pictures)
    fragment.retainInstance = true
    reporting.gallery(expose)
    addFragment(fragment)
  }

  private fun onTopCardLongPressed(expose: Expose) {
    if (!configuration.isShortcutEnabled()) {
      return
    }

    navigation(expose)
  }

  private fun addFragment(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
        .add(R.id.coordinator, fragment)
        .addToBackStack(null)
        .commit()
  }

  private fun onMatch(expose: Expose) {
    val fragment = MatchFragment()
    fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Dialog_Fullscreen)
    fragment.retainInstance = true

    reporting.match(expose)
    fragment.setExpose(expose)
    addFragment(fragment)
  }

  companion object : ActivityCompanion<IntentOptions>(IntentOptions, HomeActivity::class) {

    private val CARD_RELOAD_SIZE = 2

    private val REQUEST_CODE_PERMISSION = 0x14

    private val SCOUT_LATITUDE = 52.512500f
    private val SCOUT_LONGITUDE = 13.431020f

    object IntentOptions
  }
}
