package de.scout.fireplace.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractActivity
import kotlinx.android.synthetic.main.activity_settings.criteriaCellar
import kotlinx.android.synthetic.main.activity_settings.criteriaGarage
import kotlinx.android.synthetic.main.activity_settings.criteriaKitchen
import kotlinx.android.synthetic.main.activity_settings.livingSpace
import kotlinx.android.synthetic.main.activity_settings.netRentCold
import kotlinx.android.synthetic.main.activity_settings.rooms
import kotlinx.android.synthetic.main.activity_settings.what
import kotlinx.android.synthetic.main.toolbar.toolbar
import javax.inject.Inject

class SettingsActivity : AbstractActivity() {

  @Inject internal lateinit var repository: SettingsRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    setUpSupportActionBar(supportActionBar!!)

    setUpWhat()
    setUpNetRentCold()
    setUpLivingSpace()
    setUpRooms()

    setUpFurtherCriteria()
  }

  override fun getLayoutId() = R.layout.activity_settings

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
    criteriaKitchen.isChecked = repository.hasKitchen
    criteriaKitchen.setOnCheckedChangeListener { _, isChecked -> repository.hasKitchen = isChecked }

    criteriaGarage.isChecked = repository.hasKitchen
    criteriaGarage.setOnCheckedChangeListener { _, isChecked -> repository.hasGarage = isChecked }

    criteriaCellar.isChecked = repository.hasKitchen
    criteriaCellar.setOnCheckedChangeListener { _, isChecked -> repository.hasCellar = isChecked }
  }

  companion object {

    fun start(context: Context) {
      context.startActivity(Intent(context, SettingsActivity::class.java))
    }
  }
}
