package it.polito.mad.playgroundsreservations.database

import it.polito.mad.playgroundsreservations.profile.Gender

data class User(
    val id: String,
    val username: String,
    var firstName: String,
    var lastName: String,
    var bio: String?,
    var gender: Gender?,
    var phone: String?,
    var location: String?,
    var rating: Double = 0.0,
)
