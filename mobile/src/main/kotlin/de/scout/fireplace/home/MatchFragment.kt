package de.scout.fireplace.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractFragment
import de.scout.fireplace.models.Expose
import de.scout.fireplace.ui.RoundedTransform
import javax.inject.Inject

class MatchFragment : AbstractFragment() {

  private var expose: Expose? = null

  @BindView(R.id.heading) internal lateinit var heading: LinearLayout
  @BindView(R.id.frame) internal lateinit var frame: LinearLayout
  @BindView(R.id.like) internal lateinit var like: ImageView

  @BindView(R.id.image) internal lateinit var image: ImageView
  @BindView(R.id.address) internal lateinit var address: TextView
  @BindView(R.id.attributes) internal lateinit var attributes: TextView

  @BindView(R.id.action_view) internal lateinit var viewProperty: Button
  @BindView(R.id.action_continue) internal lateinit var keepSwiping: Button

  @Inject internal lateinit var reporting: MatchReporting

  override fun getLayoutId(): Int {
    return R.layout.fragment_match
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ViewCompat.setElevation(heading, resources.getDimensionPixelSize(R.dimen.default_elevation) * 1.5F)
    ViewCompat.setElevation(like, resources.getDimensionPixelSize(R.dimen.default_elevation) * 1.5F)
    ViewCompat.setElevation(frame, resources.getDimensionPixelSize(R.dimen.default_elevation).toFloat())

    ViewCompat.setElevation(viewProperty, resources.getDimension(R.dimen.default_elevation))
    ViewCompat.setElevation(keepSwiping, resources.getDimension(R.dimen.default_elevation))

    val expose = this.expose ?: return
    bindImage(image, expose)
    bindAddress(address, expose)
    bindAttributes(attributes, expose)
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

  @OnClick(R.id.action_view)
  internal fun onViewClick() {
    val expose = this.expose ?: return
    reporting.details(expose)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(String.format(EXPOSE_URI, expose.id))

    activity.startActivity(intent)
  }

  @OnClick(R.id.action_continue)
  internal fun onContinueClick() {
    val expose = this.expose ?: return
    reporting.ignore(expose)

    activity
        .supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
        .remove(this)
        .commit()
  }

  companion object {

    private val EXPOSE_URI = "https://www.immobilienscout24.de/expose/%s"
  }
}
