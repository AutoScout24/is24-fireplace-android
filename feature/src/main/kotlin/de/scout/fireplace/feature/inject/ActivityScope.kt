package de.scout.fireplace.feature.inject

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope
@Retention(RUNTIME)
internal annotation class ActivityScope
