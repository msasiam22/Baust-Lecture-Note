package com.example.baustlecturenote.data.remote.response

data class UserDto(
    val message: String,
    val userId: String,
    val token: String,
    val role: String
)
