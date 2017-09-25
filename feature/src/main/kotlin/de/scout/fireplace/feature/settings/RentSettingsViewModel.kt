package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.stepango.rxdatabindings.observe
import de.scout.fireplace.feature.extensions.plusAssign
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class RentSettingsViewModel @Inject constructor(repository: RentSettingsRepository) : ViewModel() {

  private val disposable = CompositeDisposable()

  val maxRentCold = ObservableInt(repository.maxRentCold)
  val minLivingSpace = ObservableInt(repository.minLivingSpace)
  val minRooms = ObservableInt(repository.minRooms)

  val hasBalcony = ObservableBoolean(repository.hasBalcony)
  val hasBasement = ObservableBoolean(repository.hasBasement)
  val hasLift = ObservableBoolean(repository.hasLift)

  init {
    disposable += maxRentCold.observe().subscribe { repository.maxRentCold = it }
    disposable += minLivingSpace.observe().subscribe { repository.minLivingSpace = it }
    disposable += minRooms.observe().subscribe { repository.minRooms = it }

    disposable += hasBalcony.observe().subscribe { repository.hasBalcony = it }
    disposable += hasBasement.observe().subscribe { repository.hasBasement = it }
    disposable += hasLift.observe().subscribe { repository.hasLift = it }
  }

  override fun onCleared() {
    disposable.clear()
  }
}
