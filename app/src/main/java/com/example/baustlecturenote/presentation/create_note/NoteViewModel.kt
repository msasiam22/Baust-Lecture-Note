package com.example.baustlecturenote.presentation.create_note

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baustlecturenote.domain.repository.Repository
import com.example.baustlecturenote.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var state by mutableStateOf(CreateNoteState())
        private set

    fun onEvent(event: CreateNoteEvent) {
        when (event) {
            is CreateNoteEvent.UpdateTitle -> {
                state = state.copy(title = event.title)
            }
            is CreateNoteEvent.UpdateDescription -> {
                state = state.copy(description = event.description)
            }
            is CreateNoteEvent.UpdateCategory -> {
                state = state.copy(category = event.category)
            }
            is CreateNoteEvent.UpdatePdfUri -> {
                state = state.copy(
                    pdfUri = event.uri,
                    pdfFileName = event.uri?.lastPathSegment?.substringAfterLast("/") ?: "No PDF selected"
                )
            }
            is CreateNoteEvent.SubmitNote -> {
                uploadNote(event.context)
            }
        }
    }

    private fun uploadNote(context: Context) {
        viewModelScope.launch {
            // Validate inputs
            if (state.title.isBlank() || state.description.isBlank() || state.pdfUri == null) {
                state = state.copy(
                    error = "Please fill all fields and select a PDF",
                    isLoading = false
                )
                return@launch
            }

            state = state.copy(isLoading = true, error = null)

            // Convert Uri to File
            val pdfFile = state.pdfUri?.let { uri ->
                convertUriToFile(context, uri)
            } ?: run {
                state = state.copy(
                    isLoading = false,
                    error = "Failed to process PDF file"
                )
                return@launch
            }

            // Call repository to upload
            repository.uploadPdf(
                title = state.title,
                category = state.category,
                description = state.description,
                pdfFile = pdfFile
            ).collectLatest { result ->
                state = when (result) {
                    is Resource.Loading -> state.copy(isLoading = result.isLoading)
                    is Resource.Success -> {
                        // Reset state on success
                        state.copy(
                            isLoading = false,
                            success = true,
                            title = "",
                            description = "",
                            category = "User",
                            pdfUri = null,
                            pdfFileName = "No PDF selected"
                        )
                    }
                    is Resource.Error -> state.copy(
                        isLoading = false,
                        error = result.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    // Utility function to convert Uri to File
    private fun convertUriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, uri.lastPathSegment?.substringAfterLast("/") ?: "temp.pdf")
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// UI state data class
data class CreateNoteState(
    val title: String = "",
    val description: String = "",
    val category: String = "CSE101",
    val pdfUri: Uri? = null,
    val pdfFileName: String = "No PDF selected",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

// Sealed class for UI events
sealed class CreateNoteEvent {
    data class UpdateTitle(val title: String) : CreateNoteEvent()
    data class UpdateDescription(val description: String) : CreateNoteEvent()
    data class UpdateCategory(val category: String) : CreateNoteEvent()
    data class UpdatePdfUri(val uri: Uri?) : CreateNoteEvent()
    data class SubmitNote(val context: Context) : CreateNoteEvent()
}