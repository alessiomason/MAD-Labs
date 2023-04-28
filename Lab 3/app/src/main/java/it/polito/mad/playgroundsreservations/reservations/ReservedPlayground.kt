package it.polito.mad.playgroundsreservations.reservations

import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation

data class ReservedPlayground(
    val playground: Playground,
    val reservations: List<Reservation>
)