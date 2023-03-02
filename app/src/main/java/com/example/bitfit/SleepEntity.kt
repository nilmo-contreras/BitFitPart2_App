package com.example.bitfit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "sleep_table")
data class SleepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sleepDate") val sleepDate: String?,
    @ColumnInfo(name = "sleepLength") val sleepLength: Int?,
    @ColumnInfo(name = "sleepRating") val sleepRating: Int?,
    @ColumnInfo(name = "sleepNotes") val sleepNotes: String?
)