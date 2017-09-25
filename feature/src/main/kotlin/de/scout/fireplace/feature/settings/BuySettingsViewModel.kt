package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.stepango.rxdatabindings.observe
import de.scout.fireplace.feature.R.id.hasBalcony
import de.scout.fireplace.feature.R.id.hasBasement
import de.scout.fireplace.feature.R.id.hasLift
import de.scout.fireplace.feature.extensions.plusAssign
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class BuySettingsViewModel @Inject constructor(repository: BuySettingsRepository) : ViewModel() {

  private val disposable = CompositeDisposable()

  val maxRentCold = ObservableInt(repository.maxRentCold)
  val minLivingSpace = ObservableInt(repository.minLivingSpace)
  val minRooms = ObservableInt(repository.minRooms)

  init {
    disposable += maxRentCold.observe().subscribe { repository.maxRentCold = it }
    disposable += minLivingSpace.observe().subscribe { repository.minLivingSpace = it }
    disposable += minRooms.observe().subscribe { repository.minRooms = it }
  }

  override fun onCleared() {
    disposable.clear()
  }
}
