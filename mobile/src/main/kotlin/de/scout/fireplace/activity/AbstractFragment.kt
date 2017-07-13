package de.scout.fireplace.activity

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.support.AndroidSupportInjection

abstract class AbstractFragment : AppCompatDialogFragment() {

  private lateinit var unbinder: Unbinder

  override fun onAttach(context: Context?) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(getLayoutId(), container, false)
  }

  @LayoutRes
  protected abstract fun getLayoutId(): Int

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view!!)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    unbinder.unbind()
  }
}
