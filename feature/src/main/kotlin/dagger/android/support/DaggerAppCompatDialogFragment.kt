package dagger.android.support

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

abstract class DaggerAppCompatDialogFragment : AppCompatDialogFragment(), HasFragmentInjector, HasSupportFragmentInjector {

  @Inject internal lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>
  @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

  override fun onAttach(context: Context?) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun fragmentInjector() = frameworkFragmentInjector

  override fun supportFragmentInjector() = supportFragmentInjector
}
