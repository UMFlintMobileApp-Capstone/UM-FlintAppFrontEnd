package com.example.um_flintapplication.apiRequests

/*
    This is what is used to map the responses from the API so that we can reference them
        Each response will give a list of JSON objects back
        Not every parameter may be important, hence why NewsItem only has 3 variables
 */

data class NewsItem(
    val id: Int,
    val title: String,
    val excerpt: String
)

data class EventItem(
    val id: Int,
    val title: String,
    val photo: String
)