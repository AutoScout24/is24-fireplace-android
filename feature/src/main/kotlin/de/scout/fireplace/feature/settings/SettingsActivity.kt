package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.databinding.ActivitySettingsBinding
import de.scout.fireplace.feature.extensions.getDataBinding
import de.scout.fireplace.feature.extensions.getViewModel
import kotlinx.android.synthetic.main.activity_settings.criteriaBalcony
import kotlinx.android.synthetic.main.activity_settings.criteriaBasement
import kotlinx.android.synthetic.main.activity_settings.criteriaLift
import kotlinx.android.synthetic.main.activity_settings.livingSpace
import kotlinx.android.synthetic.main.activity_settings.netRentCold
import kotlinx.android.synthetic.main.activity_settings.rooms
import kotlinx.android.synthetic.main.activity_settings.what
import kotlinx.android.synthetic.main.toolbar.toolbar
import javax.inject.Inject

class SettingsActivity : DaggerAppCompatActivity() {

  private lateinit var binding: ActivitySettingsBinding

  @Inject internal lateinit var factory: ViewModelProvider.Factory

  @Inject internal lateinit var repository: SettingsRepository
  @Inject internal lateinit var configuration: SettingsConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = getDataBinding(R.layout.activity_settings) { model = getViewModel(factory) }

    setSupportActionBar(toolbar)
    setUpSupportActionBar(supportActionBar!!)

    setUpWhat()
    setUpNetRentCold()
    setUpLivingSpace()
    setUpRooms()

    setUpFurtherCriteria()
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

  private fun setUpWhat() {
    what.check(if (repository.what == "apartmentrent") R.id.rent else R.id.purchase)
    what.setOnCheckedChangeListener { _, checkedId -> repository.what = if (checkedId == R.id.rent) "apartmentrent" else "apartmentbuy" }
  }

  private fun setUpNetRentCold() {
    netRentCold.setMinValue(repository.minimumPrice)
    netRentCold.setMaxValue(repository.maximumPrice)
    netRentCold.setFilter(CurrencyFormatInputFilter())
    netRentCold.addOnMinChangeListener { repository.minimumPrice = it.toInt() }
    netRentCold.addOnMaxChangeListener { repository.maximumPrice = it.toInt() }
  }

  private fun setUpLivingSpace() {
    livingSpace.setMinValue(repository.minimumSpace)
    livingSpace.setMaxValue(repository.maximumSpace)
    livingSpace.setFilter(NumberRangeInputFilter(10, 500))
    livingSpace.addOnMinChangeListener { repository.minimumSpace = it.toInt() }
    livingSpace.addOnMaxChangeListener { repository.maximumSpace = it.toInt() }
  }

  private fun setUpRooms() {
    rooms.setMinValue(repository.minimumRooms)
    rooms.setMaxValue(repository.maximumRooms)
    rooms.setFilter(NumberRangeInputFilter(1, 25))
    rooms.addOnMinChangeListener { repository.minimumRooms = it.toInt() }
    rooms.addOnMaxChangeListener { repository.maximumRooms = it.toInt() }
  }

  private fun setUpFurtherCriteria() {
    if (!configuration.isCriteriaEnabled()) {
      return
    }

    criteriaBalcony.isChecked = repository.hasBalcony
    criteriaBalcony.setOnCheckedChangeListener { _, isChecked -> repository.hasBalcony = isChecked }

    criteriaBasement.isChecked = repository.hasBasement
    criteriaBasement.setOnCheckedChangeListener { _, isChecked -> repository.hasBasement = isChecked }

    criteriaLift.isChecked = repository.hasLift
    criteriaLift.setOnCheckedChangeListener { _, isChecked -> repository.hasLift = isChecked }
  }

  companion object {

    fun start(context: Context) {
      context.startActivity(Intent(context, SettingsActivity::class.java))
    }
  }
}
