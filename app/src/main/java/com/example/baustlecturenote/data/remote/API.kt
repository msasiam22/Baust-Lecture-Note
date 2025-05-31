package com.example.baustlecturenote.data.remote

import com.example.baustlecturenote.data.remote.response.ListNoteDto
import com.example.baustlecturenote.data.remote.response.NoteDto
import com.example.baustlecturenote.data.remote.response.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface API {
    @POST("auth/signup")
    suspend fun signup(
        @Body credentials: Map<String, String>
    ): UserDto

    @POST("auth/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): UserDto

    @Multipart
    @POST("notes")
    suspend fun uploadPdf(
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part pdf: MultipartBody.Part
    ): NoteDto

    @GET("notes")
    suspend fun getPdfs(): List<ListNoteDto>

    @DELETE("notes/{id}")
    suspend fun deletePdf(
        @Path("id") noteId: String
    ): Map<String, String>

    companion object{
        const val BASE_URL = "https://baust-backend.onrender.com/api/"
    }
}