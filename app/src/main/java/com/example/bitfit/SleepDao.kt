package com.example.bitfit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_table")
    fun getAll(): Flow<List<SleepEntity>>

    @Insert
    fun insertAll(articles: List<SleepEntity>)

    @Insert
    fun insert(article: SleepEntity)

    @Query("DELETE FROM sleep_table")
    fun deleteAll()

    @Query("SELECT avg(sleepLength) FROM sleep_table")
    fun getAverageLength(): Double

    @Query("SELECT avg(sleepRating) FROM sleep_table")
    fun getAverageRating(): Double
}