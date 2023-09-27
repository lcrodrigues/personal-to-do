package com.example.personaltodo.featureevents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltodo.core.exceptions.NetworkException
import com.example.personaltodo.core.util.AppState
import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.usecase.GetEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEvents: GetEvents
) : ViewModel() {

    private val _eventState = MutableStateFlow<AppState<List<Event>>>(AppState.Init)
    val eventState: StateFlow<AppState<List<Event>>> = _eventState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    fun fetchEvents() {
        _eventState.update {
            AppState.Loading
        }

        viewModelScope.launch(Dispatchers.IO) {
            getEvents().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.isNotEmpty()) {
                            _eventState.update { AppState.Success(data = result.data) }
                        } else {
                            _eventState.update { AppState.Empty }
                        }
                    }

                    is Resource.Error -> {
                        val data = result.data

                        _eventFlow.emit(UIEvent.ShowSnackbar(result.error.message ?: "Error!"))

                        if (result.error is NetworkException) {
                            _eventState.update { AppState.InternetError(data) }
                        } else {
                            _eventState.update { AppState.GenericError(data) }
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}