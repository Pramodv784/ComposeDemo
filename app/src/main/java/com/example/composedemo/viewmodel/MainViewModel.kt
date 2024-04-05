package com.example.composedemo.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composedemo.data.MainRepository
import com.example.composedemo.extension.toLocalList
import com.example.composedemo.model.LocalData
import com.example.composedemo.utils.DefaultPaginator
import com.example.composedemo.utils.NetworkHelper

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val application: Application
) : ViewModel() {

    private var _state: MainState by mutableStateOf(MainState())

    val state get() = _state


    private var localRepo: List<LocalData> = repository.getRepo()

    private val _searchText = MutableLiveData("")
    val searchText get() = _searchText


    private val pager = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { loading ->
            _state = state.copy(loading = loading)

        },
        onRequest = { key ->

            repository.getItem(key, 10, _searchText.value!!)

        },
        getNextKey = {
            state.page + 1

        },
        onError = { error ->
            _state = state.copy(error = error?.message)

        },
        onSuccess = { data, newKey ->
            val items = data.toLocalList()
            _state = state.copy(
                data = state.data + items,
                endReached = items.isEmpty(),
                page = newKey
            )
            if (_state.data.size >= 15 && localRepo.isEmpty()) {
                for (i in 1..15) {
                    repository.addRepo(state.data[i])
                }
            }
        }
    )
    init {
        loadLocalData()
    }

    private fun loadLocalData() {
        if (!NetworkHelper.isNetworkAvailable(application)) {
            viewModelScope.launch {
                localRepo = repository.getRepo()
                if (localRepo.isNotEmpty()) {
                    _state.data = localRepo
                }
            }
        }
    }

    fun onLoadNextData() {
        viewModelScope.launch {
            pager.loadNextItems()
        }
    }

    fun getData(text: String) {
        _state.data = emptyList()
        _searchText.value = text
        if (NetworkHelper.isNetworkAvailable(application)) {
            onLoadNextData()
        } else {
            Toast.makeText(application, "Please connect to the internet", Toast.LENGTH_SHORT).show()
        }



    }

}

data class MainState(
    val loading: Boolean = false,
    var data: List<LocalData> = emptyList(),
    var error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)
