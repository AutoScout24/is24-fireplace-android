package de.scout.fireplace.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.MenuItem
import butterknife.ButterKnife
import dagger.android.support.DaggerAppCompatActivity

abstract class AbstractActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutId())
    ButterKnife.bind(this)
  }

  @LayoutRes
  protected abstract fun getLayoutId(): Int

  override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && super.shouldShowRequestPermissionRationale(permission)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }

      else -> return super.onOptionsItemSelected(item)
    }
  }
}
