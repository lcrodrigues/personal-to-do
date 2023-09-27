package com.example.personaltodo.featureevents.presentation.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personaltodo.core.util.AppState
import com.example.personaltodo.featureevents.presentation.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EventListComponent(paddingValues: PaddingValues, snackbarHostState: SnackbarHostState) {
    val viewModel: EventViewModel = hiltViewModel()
    val state = viewModel.eventState.collectAsState().value
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
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
        when (state) {
            is AppState.Loading -> {
                LoadingComponent()
            }

            is AppState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.data) { event ->
                        EventCellComponent(event)
                    }
                }
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