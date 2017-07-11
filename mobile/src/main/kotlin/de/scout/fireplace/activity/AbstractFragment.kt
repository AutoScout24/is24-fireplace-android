package de.scout.fireplace.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.support.DaggerFragment

abstract class AbstractFragment : DaggerFragment() {

  private lateinit var unbinder: Unbinder

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
