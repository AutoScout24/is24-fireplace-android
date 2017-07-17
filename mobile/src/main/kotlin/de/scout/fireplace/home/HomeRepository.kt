package de.scout.fireplace.home

import com.google.firebase.database.DatabaseReference
import de.scout.fireplace.models.Expose
import javax.inject.Inject

internal class HomeRepository @Inject constructor(private val reference: DatabaseReference) {

  fun like(expose: Expose) {
    reference.child(expose.id).setValue(true)
  }

  fun pass(expose: Expose) {
    reference.child(expose.id).setValue(false)
  }
}
