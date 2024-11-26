package com.example.um_flintapplication.apiRequests
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    /*
        @GET specifies the request type and takes the request URL as a parameter
            A post request for example would use @POST
        Any parameters that need to be fed through the URL need {}
            @Path specifies which part of the path is being replaced,
             with the next parameter being the name and type
        The last piece of the function is the expected response type.
            We will only be getting lists back, but the type is important
                ** See Models.kt for more info **
     */
    @GET("/news/get/{items}")
    suspend fun getNews(@Path("items") num: Int): List<NewsItem>

    @GET("/events/get/{items}")
    suspend fun getEvents(@Path("items") num: Int): List<EventItem>

    @GET("/announcements/{items}")
    suspend fun getAnnouncements(@Path("items") num: Int): List<AnnouncementItem>

    @POST("/auth/callback")
    suspend fun getCallback(): AuthCallback

    @GET("/schedule/locations/buildings")
    suspend fun getBuildings(): List<Buildings>

    @GET("/schedule/locations/building/{building}/rooms")
    suspend fun getRooms(@Path("building") building: String): List<BuildingRooms>

    @GET("/schedule/room/{room}/availabilities")
    suspend fun getRoomTimes(@Path("room") room: Int): List<RoomAvailable>
}