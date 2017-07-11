package de.scout.fireplace.network;

import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;

public class SchedulingStrategy {

  private final Scheduler subscriber;
  private final Scheduler observer;

  public SchedulingStrategy(Scheduler subscriber, Scheduler observer) {
    this.subscriber = subscriber;
    this.observer = observer;
  }

  public <T> SingleTransformer<T, T> single() {
    return upstream -> upstream
        .subscribeOn(subscriber)
        .observeOn(observer);
  }

  public <T> MaybeTransformer<T, T> maybe() {
    return upstream -> upstream
        .subscribeOn(subscriber)
        .observeOn(observer);
  }

  public <T> ObservableTransformer<T, T> observable() {
    return upstream -> upstream
        .subscribeOn(subscriber)
        .observeOn(observer);
  }

  public <T> FlowableTransformer<T, T> flowable() {
    return upstream -> upstream
        .subscribeOn(subscriber)
        .observeOn(observer);
  }
}
