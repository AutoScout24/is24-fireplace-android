package de.scout.fireplace.network;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface NetworkClient {

  Type value();

  enum Type {
    ANONYMOUS,
    AUTHENTICATED
  }
}
