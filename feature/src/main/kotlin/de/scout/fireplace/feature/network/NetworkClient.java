package de.scout.fireplace.feature.network;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
public @interface NetworkClient {

  Type value();

  enum Type {
    ANONYMOUS,
    AUTHENTICATED
  }
}
