package de.scout.fireplace.home

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.scout.fireplace.feature.R
import de.scout.fireplace.activity.AbstractFragment
import de.scout.fireplace.models.Expose
import de.scout.fireplace.ui.RoundedTransform
import kotlinx.android.synthetic.main.fragment_match.actionContinue
import kotlinx.android.synthetic.main.fragment_match.actionView
import kotlinx.android.synthetic.main.fragment_match.address
import kotlinx.android.synthetic.main.fragment_match.attributes
import kotlinx.android.synthetic.main.fragment_match.frame
import kotlinx.android.synthetic.main.fragment_match.heading
import kotlinx.android.synthetic.main.fragment_match.image
import kotlinx.android.synthetic.main.fragment_match.like
import javax.inject.Inject

class MatchFragment : AbstractFragment() {

  private var expose: Expose? = null

  @Inject internal lateinit var reporting: MatchReporting
  @Inject internal lateinit var navigation: ExposeNavigation

  override fun getLayoutId(): Int {
    return R.layout.fragment_match
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ViewCompat.setElevation(heading, resources.getDimensionPixelSize(R.dimen.default_elevation) * 1.5F)
    ViewCompat.setElevation(like, resources.getDimensionPixelSize(R.dimen.default_elevation) * 1.5F)
    ViewCompat.setElevation(frame, resources.getDimensionPixelSize(R.dimen.default_elevation).toFloat())

    ViewCompat.setElevation(actionView, resources.getDimension(R.dimen.default_elevation))
    ViewCompat.setElevation(actionContinue, resources.getDimension(R.dimen.default_elevation))

    val expose = this.expose ?: return
    bindImage(image, expose)
    bindAddress(address, expose)
    bindAttributes(attributes, expose)

    actionView.setOnClickListener { onViewClick() }
    actionContinue.setOnClickListener { onContinueClick() }
  }

  private fun bindImage(view: ImageView, expose: Expose) {
    view.post {
      Picasso.with(context)
          .load(expose.getPictureFor(view))
          .transform(RoundedTransform(24))
          .into(image, object : Callback {
            override fun onSuccess() {
              ViewCompat.setElevation(view, R.dimen.action_elevation.toFloat())
            }

            override fun onError() {
              view.visibility = View.GONE
            }
          })
    }
  }

  private fun bindAddress(view: TextView, expose: Expose) {
    var address = expose.address.line
    address = address.substring(0, address.indexOf(','))

    view.text = String.format("%s  -  %s", expose.address.distance, address)
  }

  private fun bindAttributes(view: TextView, expose: Expose) {
    view.text = TextUtils.join("    ", expose.attributes)
  }

  fun setExpose(expose: Expose) {
    this.expose = expose
  }

  private fun onViewClick() {
    val expose = this.expose ?: return

    reporting.details(expose)
    navigation(expose)
  }

  private fun onContinueClick() {
    val expose = this.expose ?: return
    reporting.ignore(expose)

    activity
        .supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
        .remove(this)
        .commit()
  }
}
