package com.example.baustlecturenote.data.repository

import coil.network.HttpException
import com.example.baustlecturenote.data.remote.API
import com.example.baustlecturenote.data.remote.response.ListNoteDto
import com.example.baustlecturenote.data.remote.response.NoteDto
import com.example.baustlecturenote.data.remote.response.UserDto
import com.example.baustlecturenote.domain.model.Note
import com.example.baustlecturenote.domain.model.User
import com.example.baustlecturenote.domain.repository.Repository
import com.example.baustlecturenote.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.File
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: API
) : Repository {

    override suspend fun signup(email: String, password: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading(true));
            val user = try {
                api.signup(mapOf("email" to email, "password" to password)).toDomain()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }

            emit(Resource.Loading(false));
            emit(Resource.Success(user))
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading(true));
            val user = try {
                api.login(mapOf("email" to email, "password" to password)).toDomain()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }
            println(user)

            emit(Resource.Loading(false));
            emit(Resource.Success(user))
        }
    }

    override suspend fun uploadPdf(
        title: String,
        category: String,
        description: String,
        pdfFile: File
    ): Flow<Resource<NoteDto>> {
        return flow {
            emit((Resource.Loading(true)))
            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val pdfPart = MultipartBody.Part.createFormData(
                "pdf",
                pdfFile.name,
                pdfFile.asRequestBody("application/pdf".toMediaTypeOrNull())
            )
            val note = try {
                api.uploadPdf(titleBody, categoryBody, descriptionBody, pdfPart)
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }
            println(note)
            emit(Resource.Loading(false))
            emit(Resource.Success(note))
        }
    }

    override suspend fun getPdfs(): Flow<Resource<List<Note>>> {
        return flow {
            emit(Resource.Loading(true))
            val notes = try {
                withTimeout(50_000) {
                    api.getPdfs().map { it.toDomain() }
                }
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }
            println(notes)
            emit(Resource.Loading(false))
            emit(Resource.Success(notes))
        }
    }

    override suspend fun deletePdf(noteId: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = try {
                api.deletePdf(noteId)
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
                return@flow
            }
            emit(Resource.Loading(false))
            if (response["message"].equals("Note deleted successfully")){
                emit(Resource.Success(true))
            }else {
                emit(Resource.Error("Failed to delete note"))
            }
        }
    }
}

fun UserDto.toDomain(): User {
    return User(
        id = userId,
        role = role,
        token = token,
    )
}

fun ListNoteDto.toDomain(): Note {
    return Note(
        id = _id,
        title = title,
        category = category,
        description = description,
        pdfUrl = pdfUrl,
        assetId = assetId,
        createdAt = createdAt
    )
}