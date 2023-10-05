package com.example.personaltodo.core.util

sealed class FilterState {
    object Empty : FilterState()
    data class FilteringList<T>(val eventList: List<T>): FilterState()
}