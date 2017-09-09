package de.scout.fireplace.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.view.MenuItem
import dagger.Binds
import dagger.Provides
import dagger.android.support.DaggerAppCompatActivity

abstract class AbstractActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutId())
  }

  @LayoutRes
  protected abstract fun getLayoutId(): Int

  override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && super.shouldShowRequestPermissionRationale(permission)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }

  @dagger.Module
  abstract class Module<in T : FragmentActivity> {

    @Provides
    fun activity(activity: T): FragmentActivity = activity
  }
}
