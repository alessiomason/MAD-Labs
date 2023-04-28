package it.polito.mad.playgroundsreservations.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface PlaygroundsDao {
    @Query("SELECT * FROM playgrounds")
    fun getAllPlaygrounds(): LiveData<List<Playground>>

    @Query("SELECT * FROM playgrounds WHERE sport = :sport")
    fun getPlaygroundsBySport(sport: Sports): LiveData<List<Playground>>
}