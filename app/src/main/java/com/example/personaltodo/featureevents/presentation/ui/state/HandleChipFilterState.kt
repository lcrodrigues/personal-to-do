package com.example.personaltodo.featureevents.presentation.ui.state

import com.example.personaltodo.featureevents.presentation.ui.model.ChipFilterOptions

sealed class HandleChipFilterState {
    object Inactive: HandleChipFilterState()
    data class Active(val tags: List<ChipFilterOptions> = arrayListOf()): HandleChipFilterState()
}