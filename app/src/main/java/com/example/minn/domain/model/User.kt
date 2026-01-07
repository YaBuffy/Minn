package com.example.minn.domain.model


data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val avatar: String = "",
    val bio: String = "",
    val gender: Gender = Gender.NOT_SPECIFIED,
)

enum class Gender{
    MALE,
    FEMALE,
    OTHER,
    NOT_SPECIFIED
}
