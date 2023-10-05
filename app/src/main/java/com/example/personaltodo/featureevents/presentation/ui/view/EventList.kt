package com.example.personaltodo.featureevents.presentation.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.personaltodo.R
import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.presentation.ui.model.ChipFilterOptions
import com.example.personaltodo.featureevents.presentation.ui.state.HandleChipFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventList(
    eventList: List<Event>,
    queryValue: String,
    onQueryValueChange: (String) -> Unit,
    chipFilterState: HandleChipFilterState,
    onToggleChipFilterState: () -> Unit,
    onSelectChip: (String) -> Unit,
    selectedChips: List<ChipFilterOptions>,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = queryValue,
                onValueChange = onQueryValueChange,
                label = {
                    Text(text = "Search by name:")
                },
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .weight(0.9F),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                )
            )

            IconButton(onClick = {
                onToggleChipFilterState()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_filter_list_24),
                    contentDescription = null,
                    modifier = Modifier.weight(0.1F),
                    tint = when (chipFilterState) {
                        is HandleChipFilterState.Inactive -> MaterialTheme.colorScheme.onSurfaceVariant
                        is HandleChipFilterState.Active -> MaterialTheme.colorScheme.secondary
                    }
                )
            }
        }

        if (chipFilterState is HandleChipFilterState.Active) {
            ChipFilterList(selectedChips, onSelectChip)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(eventList) { event ->
                EventCell(event)
            }
        }
    }
}