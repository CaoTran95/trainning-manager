package com.example.trainingmanager.data.model

import com.google.gson.annotations.SerializedName

data class WorkoutData(
    @SerializedName("_id")
    val id: String? = null,
    val assignments: List<Assignment?> = listOf(),
)