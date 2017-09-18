package de.scout.fireplace.feature.settings

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.ActionBar
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.activity.ActivityCompanion
import kotlinx.android.synthetic.main.activity_settings.pager
import kotlinx.android.synthetic.main.toolbar.toolbar

class SettingsActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    setSupportActionBar(toolbar)
    setUpSupportActionBar(supportActionBar!!)

    pager.adapter = SettingsPagerAdapter(supportFragmentManager)
  }

  override fun onBackPressed() {
    if (pager.currentItem == 0) {
      super.onBackPressed()
      return
    }

    pager.currentItem = pager.currentItem - 1
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> onBackPressed().run { true }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun setUpSupportActionBar(actionBar: ActionBar) {
    actionBar.setDisplayHomeAsUpEnabled(true)
  }

  private class SettingsPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    override fun getItem(position: Int): Fragment {
      return when (position) {
        0 -> RentSettingsFragment()
        1 -> BuySettingsFragment()
        else -> throw RuntimeException()
      }
    }

    override fun getCount() = 2
  }

  companion object : ActivityCompanion<Any>(Any(), SettingsActivity::class) {

    fun startForResult(activity: Activity, request: Int) = startForResult(activity, request, {})
  }
}
