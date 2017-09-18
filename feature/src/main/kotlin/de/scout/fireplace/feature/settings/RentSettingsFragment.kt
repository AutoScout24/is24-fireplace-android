package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.databinding.FragmentSettingsRentBinding
import kotlinx.android.synthetic.main.fragment_settings_rent.criteriaBalcony
import kotlinx.android.synthetic.main.fragment_settings_rent.criteriaBasement
import kotlinx.android.synthetic.main.fragment_settings_rent.criteriaLift
import kotlinx.android.synthetic.main.fragment_settings_rent.livingSpace
import kotlinx.android.synthetic.main.fragment_settings_rent.netRentCold
import kotlinx.android.synthetic.main.fragment_settings_rent.rooms
import javax.inject.Inject

internal class RentSettingsFragment : DaggerFragment() {

  private lateinit var binding: FragmentSettingsRentBinding

  @Inject internal lateinit var factory: ViewModelProvider.Factory

  @Inject internal lateinit var repository: SettingsRepository
  @Inject internal lateinit var configuration: SettingsConfiguration

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_settings_rent, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setUpNetRentCold()
    setUpLivingSpace()
    setUpRooms()

    setUpFurtherCriteria()
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
}
