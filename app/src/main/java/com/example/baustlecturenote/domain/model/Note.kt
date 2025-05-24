package com.example.baustlecturenote.domain.model

data class Note(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val pdfUrl: String,
    val assetId: String,
    val createdAt: String
)