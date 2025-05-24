package com.example.baustlecturenote.data.remote.response

data class ListNoteDto(
    val _id: String,
    val title: String,
    val category: String,
    val description: String,
    val pdfUrl: String,
    val assetId: String,
    val createdAt: String
)