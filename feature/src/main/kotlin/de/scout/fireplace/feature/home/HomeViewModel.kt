package de.scout.fireplace.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

internal class HomeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  class Factory @Inject constructor(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(kls: Class<T>?): T {
      return HomeViewModel(application) as T
    }
  }
}
