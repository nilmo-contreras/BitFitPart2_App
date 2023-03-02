package com.example.bitfit

import java.util.*

data class DisplaySleep(
    val sleepDate: String?,
    val sleepLength: Int?,
    val sleepRating: Int?,
    val sleepNotes: String?
) : java.io.Serializable