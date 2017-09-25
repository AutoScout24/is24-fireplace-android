package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.databinding.FragmentSettingsRentBinding
import de.scout.fireplace.feature.extensions.getDataBinding
import de.scout.fireplace.feature.extensions.getViewModel
import kotlinx.android.synthetic.main.fragment_settings_rent.furtherCriteria
import kotlinx.android.synthetic.main.fragment_settings_rent.hasBalcony
import kotlinx.android.synthetic.main.fragment_settings_rent.hasBasement
import kotlinx.android.synthetic.main.fragment_settings_rent.hasLift
import kotlinx.android.synthetic.main.fragment_settings_rent.maxRentCold
import javax.inject.Inject

internal class RentSettingsFragment : DaggerFragment() {

  private lateinit var binding: FragmentSettingsRentBinding

  @Inject internal lateinit var factory: ViewModelProvider.Factory
  @Inject internal lateinit var repository: RentSettingsRepository
  @Inject internal lateinit var configuration: SettingsConfiguration
  @Inject internal lateinit var formatter: CurrencyFormatter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = getDataBinding(layoutInflater, R.layout.fragment_settings_rent, container)
    binding.model = getViewModel(factory)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    maxRentCold.formatter = { formatter.format(it) }

    if (!configuration.isCriteriaEnabled()) {
      furtherCriteria.visibility = View.GONE
      return
    }
  }
}
