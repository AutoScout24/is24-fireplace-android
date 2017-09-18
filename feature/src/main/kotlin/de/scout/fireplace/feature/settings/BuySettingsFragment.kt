package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.databinding.FragmentSettingsBuyBinding
import de.scout.fireplace.feature.extensions.getDataBinding
import de.scout.fireplace.feature.extensions.getViewModel
import kotlinx.android.synthetic.main.fragment_settings_rent.maxRentCold
import kotlinx.android.synthetic.main.fragment_settings_rent.minLivingSpace
import kotlinx.android.synthetic.main.fragment_settings_rent.minRooms
import javax.inject.Inject

internal class BuySettingsFragment : DaggerFragment() {

  private lateinit var binding: FragmentSettingsBuyBinding

  @Inject internal lateinit var factory: ViewModelProvider.Factory
  @Inject internal lateinit var repository: BuySettingsRepository

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = getDataBinding(layoutInflater, R.layout.fragment_settings_buy, container)
    binding.model = getViewModel(factory)
    return binding.root
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    maxRentCold.listener = { repository.maxRentCold = it }
    minLivingSpace.listener = { repository.minLivingSpace = it }
    minRooms.listener = { repository.minRooms = it }
  }
}
