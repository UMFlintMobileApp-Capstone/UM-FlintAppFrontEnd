package com.example.um_flintapplication

data class Message(
    val sender: String,  // Name or identifier of the sender
    val content: String, // The message text
    val timestamp: String // Time when the message was sent
)