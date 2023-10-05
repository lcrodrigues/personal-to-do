package com.example.personaltodo.featureevents.presentation.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.personaltodo.featureevents.presentation.ui.model.ChipFilterOptions
import com.example.personaltodo.featureevents.presentation.ui.model.getChipFilterValues

@Composable
fun ChipFilterList(selectedTags: List<ChipFilterOptions>, onSelectChip: (String) -> Unit) {
    val chipValues = getChipFilterValues()
    val selectedTagsValue = selectedTags.map {
        it.value
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        items(chipValues) {
            ChipFilterItem(
                name = it,
                isSelected = selectedTagsValue.contains(it),
                onSelectChip = onSelectChip
            )
        }
    }
}