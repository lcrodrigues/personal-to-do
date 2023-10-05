package com.example.personaltodo.featureevents.presentation.ui.model

enum class ChipFilterOptions(val value: String) {
    CINEMA("Cinema"),
    GASTRONOMY("Gastronomy"),
    SHOW("Show"),
    MEDIA("Media"),
    OTHER("Other"),
    TO_BE_DONE("To Be Done")
}

fun getChipFilterValues(): List<String> {
    return enumValues<ChipFilterOptions>().map { it.value }
}

fun getChipFilterByValue(value: String): ChipFilterOptions? {
    return ChipFilterOptions.values().find { it.value == value }
}