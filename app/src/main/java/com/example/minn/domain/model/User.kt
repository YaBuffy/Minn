package com.example.minn.domain.model

import androidx.annotation.StringRes
import com.example.minn.R
import com.google.firebase.Timestamp


data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val nameLower: String = "",
    val surname: String = "",
    val avatar: String = "",
    val bio: String = "",
    val gender: Gender = Gender.NOT_SPECIFIED,
    val birthdate: Timestamp? = null
)

enum class Gender(@StringRes val labelRes: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    OTHER(R.string.gender_other),
    NOT_SPECIFIED(R.string.gender_not_specified)
}


