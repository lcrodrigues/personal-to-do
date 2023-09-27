package com.example.personaltodo.featureevents.data.remote.dto

import com.example.personaltodo.featureevents.domain.model.SuggestedBy

data class SuggestedByDTO(
    val id: Int,
    val name: String
)

fun SuggestedByDTO.toSuggestedBy() = SuggestedBy(
    id,
    name
)
