package de.scout.fireplace.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import de.scout.fireplace.BuildConfig
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractActivity
import de.scout.fireplace.bus.RxBus
import de.scout.fireplace.bus.events.TopCardEvent
import de.scout.fireplace.bus.events.TopCardLongPressedEvent
import de.scout.fireplace.bus.events.TopCardPressedEvent
import de.scout.fireplace.models.Expose
import de.scout.fireplace.network.ErrorHandler
import de.scout.fireplace.network.SchedulingStrategy
import de.scout.fireplace.search.SearchClient
import de.scout.fireplace.settings.SettingsActivity
import de.scout.fireplace.ui.FloatingCardStackEvent
import de.scout.fireplace.ui.FloatingCardStackLayout
import de.scout.fireplace.ui.FloatingCardView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class HomeActivity : AbstractActivity() {

  private val disposables: CompositeDisposable = CompositeDisposable()

  private var provider: FusedLocationProviderClient? = null
  private var page = 1

  @BindView(R.id.coordinator) internal lateinit var coordinator: CoordinatorLayout
  @BindView(R.id.title) internal lateinit var title: TextView

  @BindView(R.id.stack) internal lateinit var stack: FloatingCardStackLayout

  @BindView(R.id.action_settings) internal lateinit var settings: ImageButton
  @BindView(R.id.action_pass) internal lateinit var pass: ImageButton
  @BindView(R.id.action_like) internal lateinit var like: ImageButton

  @Inject internal lateinit var client: SearchClient
  @Inject internal lateinit var matcher: EventMatcher
  @Inject internal lateinit var reporting: HomeReporting
  @Inject internal lateinit var configuration: HomeConfiguration
  @Inject internal lateinit var navigation: ExposeNavigation

  @Inject internal lateinit var handler: ErrorHandler
  @Inject internal lateinit var strategy: SchedulingStrategy

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setUpActionBar()
    setUpActionButtons()

    setUpLocationProvider()
    setUpPipeline()
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

    RxBus.getInstance().toObserverable()
        .filter { event -> event is TopCardEvent }
        .subscribe { event -> onTopCardEvent(event as TopCardEvent) }
  }

  private fun onTopCardEvent(topCardEvent: TopCardEvent) {
    if (topCardEvent is TopCardPressedEvent && topCardEvent.expose != null) {
      onTopCardPressed(topCardEvent.expose)
      return
    }

    if (topCardEvent is TopCardLongPressedEvent && topCardEvent.expose != null) {
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

  override fun getLayoutId(): Int {
    return R.layout.activity_home
  }

  private fun setUpActionBar() {
    if (configuration.isSettingsEnabled()) {
      settings.visibility = View.VISIBLE
    }
  }

  private fun setUpActionButtons() {
    ViewCompat.setElevation(pass, resources.getDimension(R.dimen.action_elevation))
    ViewCompat.setElevation(like, resources.getDimension(R.dimen.action_elevation))
  }

  private fun check(location: Location): Location {
    if (location.latitude == 0.0 && location.longitude == 0.0) {
      title.setTextColor(ContextCompat.getColor(this, R.color.light_blue))

      location.latitude = SCOUT_LATITUDE.toDouble()
      location.longitude = SCOUT_LONGITUDE.toDouble()
    } else {
      title.setTextColor(ContextCompat.getColor(this, android.R.color.black))
    }

    return location
  }

  private fun fetchNearbyResults(location: Location) {
    disposables += client.search(location, page++, CARD_PAGE_SIZE)
        .flatMapObservable { (_, _, _, _, _, _, results) -> Observable.fromIterable(results) }
        .subscribe(Consumer { expose ->
          val card = FloatingCardView(this@HomeActivity)
          stack.add(card)

          card.bind(expose)
        }, handler)
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

    navigation.invoke(expose)
  }

  private fun addFragment(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
        .add(R.id.coordinator, fragment)
        .addToBackStack(null)
        .commit()
  }

  @OnClick(R.id.action_settings)
  internal fun onSettingsClick() {
    reporting.settings()
    SettingsActivity.start(this)
  }

  @OnClick(R.id.action_like)
  internal fun onLikeClick() {
    if (!stack.hasChildren()) {
      showSnackbar(R.string.error_listings_unavailable)
      return
    }

    val view = stack.topChild
    reporting.manual()
    view.approve()
  }

  @OnClick(R.id.action_pass)
  internal fun onPassClick() {
    if (!stack.hasChildren()) {
      showSnackbar(R.string.error_listings_unavailable)
      return
    }

    val view = stack.topChild
    reporting.manual()
    view.dismiss()
  }

  private fun onMatch(expose: Expose) {
    val fragment = MatchFragment()
    fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Base_Dialog_Fullscreen)
    fragment.retainInstance = true

    reporting.match(expose)
    fragment.setExpose(expose)
    addFragment(fragment)
  }

  private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
  }

  companion object {

    private val CARD_RELOAD_SIZE = 2
    private val CARD_PAGE_SIZE = 4

    private val REQUEST_CODE_PERMISSION = 0x14

    private val SCOUT_LATITUDE = 52.512500f
    private val SCOUT_LONGITUDE = 13.431020f

    fun start(context: Context) {
      context.startActivity(Intent(context, HomeActivity::class.java))
    }
  }
}
