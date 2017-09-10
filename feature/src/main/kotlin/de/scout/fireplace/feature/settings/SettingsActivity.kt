package de.scout.fireplace.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import de.scout.fireplace.feature.R
import de.scout.fireplace.activity.AbstractActivity
import kotlinx.android.synthetic.main.activity_settings.criteriaBalcony
import kotlinx.android.synthetic.main.activity_settings.criteriaLift
import kotlinx.android.synthetic.main.activity_settings.criteriaNewBuild
import kotlinx.android.synthetic.main.activity_settings.livingSpace
import kotlinx.android.synthetic.main.activity_settings.netRentCold
import kotlinx.android.synthetic.main.activity_settings.rooms
import kotlinx.android.synthetic.main.activity_settings.what
import kotlinx.android.synthetic.main.toolbar.toolbar
import javax.inject.Inject

class SettingsActivity : AbstractActivity() {

  @Inject internal lateinit var repository: SettingsRepository
  @Inject internal lateinit var configuration: SettingsConfiguration

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
    if (!configuration.isCriteriaEnabled()) {
      return
    }

    criteriaLift.isChecked = repository.hasLift
    criteriaLift.setOnCheckedChangeListener { _, isChecked -> repository.hasLift = isChecked }

    criteriaBalcony.isChecked = repository.hasBalcony
    criteriaBalcony.setOnCheckedChangeListener { _, isChecked -> repository.hasBalcony = isChecked }

    criteriaNewBuild.isChecked = repository.isNewBuild
    criteriaNewBuild.setOnCheckedChangeListener { _, isChecked -> repository.isNewBuild = isChecked }
  }

  companion object {

    fun start(context: Context) {
      context.startActivity(Intent(context, SettingsActivity::class.java))
    }
  }
}
