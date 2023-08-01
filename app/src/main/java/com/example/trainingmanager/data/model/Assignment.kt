package com.example.trainingmanager.data.model

import com.google.gson.annotations.SerializedName

data class Assignment(
    @SerializedName("_id")
    val id: String? = null,
    val status: Int? = null,
    val title: String? = null,
    val exercises_count: Int? = null,
)