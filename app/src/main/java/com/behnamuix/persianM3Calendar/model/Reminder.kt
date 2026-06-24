package com.behnamuix.persianM3Calendar.model

import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val note: String? = null
)