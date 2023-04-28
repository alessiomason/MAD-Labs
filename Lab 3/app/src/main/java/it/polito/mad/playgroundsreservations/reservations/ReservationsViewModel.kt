package it.polito.mad.playgroundsreservations.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.polito.mad.playgroundsreservations.database.*
import kotlinx.coroutines.launch
import java.util.Date

class ReservationsViewModel(application: Application): AndroidViewModel(application) {
    private val reservationsDao: ReservationsDao
    private val userDao: UserDao
    private val playgroundsDao: PlaygroundsDao

    init {
        val db = Database.getDatabase(application.applicationContext)
        reservationsDao = db.reservationsDao()
        userDao = db.userDao()
        playgroundsDao = db.playgroundsDao()
    }

    val reservations = reservationsDao.getAllReservations()

    fun getReservationsBySport(sport: Sports): LiveData<List<Reservation>> {
        return reservationsDao.getReservationsBySport(sport)
    }

    fun getReservedPlaygrounds(sport: Sports, date: Date): MutableLiveData<MutableList<ReservedPlayground>> {
        val reservedPlaygrounds = MutableLiveData<MutableList<ReservedPlayground>>(mutableListOf())

        val playgroundsBySport = playgroundsDao.getPlaygroundsBySport(sport)
        playgroundsBySport.observe(getApplication()) { playgrounds ->
            for (playground in playgrounds) {
                val reservations = reservationsDao.getReservationsByPlayground(playground.id)
                reservations.observe(getApplication()) { reservationsList ->
                    val reservedPlayground = ReservedPlayground(playground, reservationsList)
                    reservedPlaygrounds.value?.add(reservedPlayground)
                }
            }
        }

        return reservedPlaygrounds
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