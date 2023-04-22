package it.polito.mad.playgroundsreservations.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import it.polito.mad.playgroundsreservations.database.*
import kotlinx.coroutines.launch

class ReservationsViewModel(application: Application): AndroidViewModel(application) {
    private val reservationsDao: ReservationsDao
    private val userDao: UserDao

    init {
        val db = Database.getDatabase(application.applicationContext)
        reservationsDao = db.reservationsDao()
        userDao = db.userDao()
    }

    val reservations = reservationsDao.getAllReservations()

    fun getReservationsBySport(sport: Sports): LiveData<List<Reservation>> {
        return reservationsDao.getReservationsBySport(sport)
    }

    fun getUserReservations(userId: Int): LiveData<List<Reservation>> {
        return reservationsDao.getUserReservations(userId)
    }

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