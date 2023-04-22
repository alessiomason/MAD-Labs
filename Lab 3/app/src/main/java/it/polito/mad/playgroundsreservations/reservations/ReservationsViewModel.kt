package it.polito.mad.playgroundsreservations.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.polito.mad.playgroundsreservations.database.Database
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.ReservationsDao
import kotlinx.coroutines.launch

class ReservationsViewModel(application: Application): AndroidViewModel(application) {
    private val reservationsDao: ReservationsDao

    init {
        val db = Database.getDatabase(application.applicationContext)
        reservationsDao = db.reservationsDao()
    }

    val reservations = reservationsDao.getAllReservations()

    fun save(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.save(reservation)
        }
    }

    fun update(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.update(reservation)
        }
    }

    fun delete(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.delete(reservation)
        }
    }
}