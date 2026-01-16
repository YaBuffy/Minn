package com.example.minn.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val members: List<User>,
    val lastMessage: String = "",
    val lastMessageTimestamp: Timestamp? = null,
    val lastSenderId: String = ""
)
