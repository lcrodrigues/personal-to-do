package com.example.personaltodo.featureevents.presentation.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personaltodo.core.util.AppState
import com.example.personaltodo.core.util.FilterState
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.presentation.ui.state.HandleChipFilterState
import com.example.personaltodo.featureevents.presentation.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EventListComponent(paddingValues: PaddingValues, snackbarHostState: SnackbarHostState) {
    val viewModel: EventViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val requestState = viewModel.eventState.collectAsState().value
    val filterState = viewModel.filterByQueryState.collectAsState().value

    val handleChipFilterState = viewModel.handleChipFilterState.collectAsState().value

    fun onQueryValueChange(value: String) {
        viewModel.onQueryValueChange(value)
    }

    fun onSelectChip(tag: String) {
        viewModel.addNewTag(tag)
    }

    fun onToggleChipFilterState() {
        viewModel.toggleChipFilterState()
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEventFlow.collectLatest { event ->
            when (event) {
                is EventViewModel.UIEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.fetchEvents()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background,
    ) {
        when (requestState) {
            is AppState.Loading -> {
                LoadingComponent()
            }

            is AppState.Success -> {
                val list = when (filterState) {
                    is FilterState.Empty -> {
                        requestState.data
                    }

                    is FilterState.FilteringList<*> -> {
                        filterState.eventList
                    }
                }

                @Suppress("UNCHECKED_CAST")
                EventList(
                    eventList = list as List<Event>,
                    queryValue = viewModel.handleQueryFilterState.value,
                    onQueryValueChange = ::onQueryValueChange,
                    chipFilterState = handleChipFilterState,
                    onToggleChipFilterState = ::onToggleChipFilterState,
                    onSelectChip = ::onSelectChip,
                    selectedChips = if (handleChipFilterState is HandleChipFilterState.Active){
                        handleChipFilterState.tags
                    } else {
                        emptyList()
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }

            is AppState.Empty -> {
                Text(text = "No items in your list!")
            }

            is AppState.GenericError -> {
                Text(text = "Some generic error.")
            }

            is AppState.Init -> {
                Text(text = "Welcome!")
            }

            is AppState.InternetError -> {
                Text(text = "Some connection error.")
            }
        }
    }
}