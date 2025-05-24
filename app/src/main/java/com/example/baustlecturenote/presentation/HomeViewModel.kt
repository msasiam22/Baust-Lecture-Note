package com.example.baustlecturenote.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baustlecturenote.domain.model.Note
import com.example.baustlecturenote.domain.repository.Repository
import com.example.baustlecturenote.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _notes = mutableStateOf<Resource<List<Note>>>(Resource.Loading(true))
    val notes = _notes

    private var allNotes: List<Note>? = emptyList()

    init {
        fetchNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            repository.getPdfs().collect { result ->
                _notes.value = result
                if (result is Resource.Success) {
                    allNotes = result.data
                }
            }
        }
    }

    fun searchNotes(query: String) {
        if (query.isEmpty()) {
            _notes.value = Resource.Success(allNotes)
        } else {
            val filtered = allNotes?.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.description.contains(query, ignoreCase = true) ||
                        note.category.contains(query, ignoreCase = true)
            }
            _notes.value = Resource.Success(filtered)
        }
    }
}