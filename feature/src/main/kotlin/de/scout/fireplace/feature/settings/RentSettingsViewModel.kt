package de.scout.fireplace.feature.settings

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import javax.inject.Inject

internal class RentSettingsViewModel @Inject constructor(application: Application, repository: RentSettingsRepository) : AndroidViewModel(application) {

  val maxRentCold = ObservableInt(repository.maxRentCold)
  val minLivingSpace = ObservableInt(repository.minLivingSpace)
  val minRooms = ObservableInt(repository.minRooms)

  val hasBalcony = ObservableBoolean(repository.hasBalcony)
  val hasBasement = ObservableBoolean(repository.hasBasement)
  val hasLift = ObservableBoolean(repository.hasLift)
}
