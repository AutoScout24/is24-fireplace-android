package de.scout.fireplace.feature.bus

import io.reactivex.processors.PublishProcessor

@Deprecated("Not thread safe")
internal class RxBus private constructor() {

  fun send(o: Any) = bus.onNext(o)

  fun toObservable() = bus!!

  companion object {

    private val bus = PublishProcessor.create<Any>()

    val instance: RxBus get() = RxBus()
  }
}
