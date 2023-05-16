package it.polito.mad.playgroundsreservations.reservations

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import it.polito.mad.playgroundsreservations.database.*
import kotlinx.coroutines.launch

class ReservationsViewModel(application: Application): AndroidViewModel(application) {
    private val reservationsDao: ReservationsDao
    private val userDao: UserDao
    private val playgroundsDao: PlaygroundsDao
    private val playgroundRatingsDao: PlaygroundRatingsDao
    val dbUpdated: Boolean

    init {
        val db = Database.getDatabase(application.applicationContext)
        reservationsDao = db.reservationsDao()
        userDao = db.userDao()
        playgroundsDao = db.playgroundsDao()
        playgroundRatingsDao = db.playgroundRatingsDao()

        val sharedPref = application.getSharedPreferences("dbPreferences", Context.MODE_PRIVATE)
        val savedDbVersion = sharedPref.getInt("dbVersion", 0)
        val currentDbVersion = db.openHelper.readableDatabase.version
        dbUpdated = savedDbVersion > currentDbVersion || savedDbVersion == 0

        if (dbUpdated) {
            with(sharedPref.edit()) {
                putInt("dbVersion", currentDbVersion)
                apply()
            }

            val playgroundsList = mutableListOf<Playground>()
            playgroundsList.add(Playground(
                name = "Tennis Center",
                address = "Sports Center Avenue",
                sport = Sports.TENNIS))
            playgroundsList.add(Playground(
                name = "Basketball Center",
                address = "Sports Center Avenue",
                sport = Sports.BASKETBALL))
            playgroundsList.add(Playground(
                name = "Football Center",
                address = "Sports Center Avenue",
                sport = Sports.FOOTBALL))
            playgroundsList.add(Playground(
                name = "Volleyball Center",
                address = "Sports Center Avenue",
                sport = Sports.VOLLEYBALL))
            playgroundsList.add(Playground(
                name = "Golf Center",
                address = "Sports Center Avenue",
                sport = Sports.GOLF))


            playgroundsList.forEach { p ->
                viewModelScope.launch {
                    val id = playgroundsDao.save(p)
                    val rating = PlaygroundRating(
                        playgroundId = id.toInt(),
                        rating = (0..5).random(),
                        description = ""
                    )
                    playgroundRatingsDao.save(rating)
                }
            }
        }
    }

    val playgrounds = playgroundsDao.getAllPlaygrounds()
    val reservations = reservationsDao.getAllReservations()

    fun getReservationsBySport(sport: Sports): LiveData<List<Reservation>> {
        return reservationsDao.getReservationsBySport(sport)
    }

    fun getReservedPlaygrounds(sport: Sports): LiveData<Map<Reservation, Playground>> {
        return reservationsDao.getReservedPlaygroundsBySport(sport)
    }

    fun getUserReservations(userId: Int): LiveData<List<Reservation>> {
        return reservationsDao.getUserReservations(userId)
    }

    fun getPlayground(playgroundId: Int, playgroundState: MutableState<Playground?>) {
        viewModelScope.launch {
            playgroundState.value = playgroundsDao.getPlayground(playgroundId)
        }
    }

    fun getPlaygroundAverageRating(playgroundId: Int): LiveData<Double> {
        return playgroundsDao.getPlaygroundAverageRating(playgroundId)
    }

    fun saveReservation(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.save(reservation)
        }
    }

    fun updateReservation(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.update(reservation)
        }
    }

    fun deleteReservation(reservation: Reservation) {
        viewModelScope.launch {
            reservationsDao.delete(reservation)
        }
    }

    fun savePlaygroundRating(playgroundRating: PlaygroundRating) {
        viewModelScope.launch {
            playgroundRatingsDao.save(playgroundRating)
        }
    }
}