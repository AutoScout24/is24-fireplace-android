package de.scout.fireplace.bus;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

public class RxBus {

  private static final PublishProcessor<Object> bus = PublishProcessor.create();
  //    private static final BehaviorSubject<Object> bus = BehaviorSubject.create();

  // If multiple threads are going to emit events to this
  // then it must be made thread-safe like this instead
  //    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

  public void send(Object o) {
    bus.onNext(o);
  }

  public Flowable<Object> toObserverable() {
    return bus;
  }

  public boolean hasSubscribers() {
    return bus.hasSubscribers();
  }

  public static RxBus getInstance() {
    return new RxBus();
  }

  private RxBus() {
    // No instances.
  }
}
