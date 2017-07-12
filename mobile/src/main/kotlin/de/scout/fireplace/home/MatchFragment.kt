package de.scout.fireplace.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractFragment
import de.scout.fireplace.models.Expose
import de.scout.fireplace.ui.CircleTransform

class MatchFragment : AbstractFragment() {

  private var expose: Expose? = null

  @BindView(R.id.avatar) internal lateinit var avatar: ImageView

  override fun getLayoutId(): Int {
    return R.layout.fragment_match
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val expose = this.expose ?: return

    avatar.post {
      Picasso.with(context)
          .load(expose.getPictureFor(avatar))
          .transform(CircleTransform())
          .into(avatar, object : Callback {
            override fun onSuccess() {
              ViewCompat.setElevation(avatar, R.dimen.action_elevation.toFloat())
            }

            override fun onError() {
              avatar.visibility = View.GONE
            }
          })
    }
  }

  fun setExpose(expose: Expose) {
    this.expose = expose
  }

  @OnClick(R.id.action_view)
  internal fun onViewClick() {
    val expose = this.expose ?: return

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(String.format(EXPOSE_URI, expose.id))

    activity.startActivity(intent)
  }

  @OnClick(R.id.action_continue)
  internal fun onContinueClick() {
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
