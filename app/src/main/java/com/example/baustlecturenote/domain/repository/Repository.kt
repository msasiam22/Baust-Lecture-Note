package com.example.baustlecturenote.domain.repository

import com.example.baustlecturenote.data.remote.response.NoteDto
import com.example.baustlecturenote.domain.model.Note
import com.example.baustlecturenote.domain.model.User
import com.example.baustlecturenote.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Repository {
    suspend fun signup(email: String, password: String): Flow<Resource<User>>
    suspend fun login(email: String, password: String): Flow<Resource<User>>
    suspend fun uploadPdf(
        title: String,
        category: String,
        description: String,
        pdfFile: File
    ): Flow<Resource<NoteDto>>
    suspend fun getPdfs(): Flow<Resource<List<Note>>>
    suspend fun deletePdf(noteId: String): Flow<Resource<Boolean>>
}