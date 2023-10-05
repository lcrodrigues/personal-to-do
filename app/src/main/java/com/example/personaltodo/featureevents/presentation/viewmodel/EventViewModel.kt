package com.example.personaltodo.featureevents.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltodo.core.exceptions.NetworkException
import com.example.personaltodo.core.util.AppState
import com.example.personaltodo.core.util.FilterState
import com.example.personaltodo.core.util.Resource
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.domain.usecase.FilterList
import com.example.personaltodo.featureevents.domain.usecase.GetEvents
import com.example.personaltodo.featureevents.presentation.ui.model.ChipFilterOptions
import com.example.personaltodo.featureevents.presentation.ui.model.getChipFilterByValue
import com.example.personaltodo.featureevents.presentation.ui.state.HandleChipFilterState
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
    private val getEvents: GetEvents,
    private val filterList: FilterList
) : ViewModel() {

    private val _eventState = MutableStateFlow<AppState<List<Event>>>(AppState.Init)
    val eventState: StateFlow<AppState<List<Event>>> = _eventState.asStateFlow()

    private val _filterByQueryState =
        MutableStateFlow<FilterState>(value = FilterState.Empty)
    val filterByQueryState: StateFlow<FilterState> = _filterByQueryState.asStateFlow()

    private val _handleQueryFilterState = mutableStateOf("")
    val handleQueryFilterState: State<String> = _handleQueryFilterState

    private val _handleChipFilterState =
        MutableStateFlow<HandleChipFilterState>(HandleChipFilterState.Inactive)
    val handleChipFilterState: StateFlow<HandleChipFilterState> =
        _handleChipFilterState.asStateFlow()

    private val _snackbarEventFlow = MutableSharedFlow<UIEvent>()
    val snackbarEventFlow: SharedFlow<UIEvent> = _snackbarEventFlow.asSharedFlow()

    private fun getSelectedTags(): ArrayList<ChipFilterOptions> {
        return ArrayList(
            if (handleChipFilterState.value is HandleChipFilterState.Active) {
                (handleChipFilterState.value as HandleChipFilterState.Active).tags
            } else {
                emptyList()
            }
        )
    }

    fun addNewTag(tag: String) {
        val selectedTags = getSelectedTags()
        val newTag = getChipFilterByValue(tag)
        if (newTag != null) {
            if (selectedTags.contains(newTag)) {
                selectedTags.remove(newTag)
            } else {
                selectedTags.add(newTag)
            }

            _handleChipFilterState.update {
                HandleChipFilterState.Active(selectedTags)
            }
        }

        filter()
    }

    fun onQueryValueChange(query: String) {
        _handleQueryFilterState.value = query
        filter()
    }

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

                        _snackbarEventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.error.message ?: "Error!"
                            )
                        )

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

    private fun filter() {
        if (eventState.value is AppState.Success) {
            val list = (eventState.value as AppState.Success<List<Event>>).data
            if (list.isNotEmpty()) {
                val query = handleQueryFilterState.value
                filterByName(query, list)
            }
        }
    }

    private fun filterByName(query: String, list: List<Event>) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                if (handleChipFilterState.value is HandleChipFilterState.Active) {
                    filterByTag(list)
                } else {
                    _filterByQueryState.update { FilterState.Empty }
                }
            } else {
                filterList.filterByName(query, list).onEach { eventList ->
                    if (handleChipFilterState.value is HandleChipFilterState.Active) {
                        filterByTag(eventList)
                    } else {
                        _filterByQueryState.update {
                            FilterState.FilteringList(eventList)
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    fun toggleChipFilterState() {
        viewModelScope.launch {
            when (handleChipFilterState.value) {
                is HandleChipFilterState.Inactive -> {
                    _handleChipFilterState.update {
                        HandleChipFilterState.Active()
                    }
                }

                is HandleChipFilterState.Active -> {
                    _handleChipFilterState.update {
                        HandleChipFilterState.Inactive
                    }

                    _filterByQueryState.update {
                        FilterState.Empty
                    }
                }
            }
        }
    }

    private fun filterByTag(list: List<Event>) {
        viewModelScope.launch {
            filterList.filterByTag(getSelectedTags(), list).onEach { filteredList ->
                _filterByQueryState.update {
                    FilterState.FilteringList(filteredList)
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}