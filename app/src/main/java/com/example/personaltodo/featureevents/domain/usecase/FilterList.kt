package com.example.personaltodo.featureevents.domain.usecase

import com.example.personaltodo.featureevents.domain.model.Event
import com.example.personaltodo.featureevents.presentation.ui.model.ChipFilterOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.filterList

class FilterList {
    fun filterByName(rawQuery: String, listToFilter: List<Event>): Flow<List<Event>> = flow {
        var searchResultList = listToFilter

        if (rawQuery.isNotEmpty()) {
            val query = rawQuery.lowercase().trim()

            searchResultList = listToFilter.filterList {
                val lowercaseName = name.lowercase()
                lowercaseName.contains(query.trim())
            }
        }

        emit(searchResultList)
    }

    fun filterByTag(tags: List<ChipFilterOptions>, list: List<Event>): Flow<List<Event>> = flow {
        if (tags.isEmpty()) {
            emit(list)
        } else {
            val filteredList = arrayListOf<Event>()

            tags.forEach { chipOption ->
                val selectedFilter = list.filter {
                    if (chipOption == ChipFilterOptions.TO_BE_DONE) {
                        it.isDone == "false"
                    } else {
                        it.eventType.name.lowercase() == chipOption.value.lowercase()
                    }
                } - filteredList.toSet()

                filteredList.addAll(selectedFilter)
            }

            emit(filteredList)
        }
    }
}