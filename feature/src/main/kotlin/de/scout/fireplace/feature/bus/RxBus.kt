package de.scout.fireplace.bus

import io.reactivex.processors.PublishProcessor

@Deprecated("RxBus is not thread safe")
internal object RxBus {

  private val bus = PublishProcessor.create<Any>()

  fun send(o: Any) = bus.onNext(o)

  fun toObserverable() = bus

  fun hasSubscribers() = bus.hasSubscribers()
}
