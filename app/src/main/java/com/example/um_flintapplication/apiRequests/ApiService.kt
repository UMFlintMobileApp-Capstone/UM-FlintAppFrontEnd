package com.example.um_flintapplication.apiRequests
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getNews(@Path("items") num: Int): ApiResponse<List<NewsItem>>

    @GET("/events/get/{items}")
    suspend fun getEvents(@Path("items") num: Int): ApiResponse<List<EventItem>>

    @GET("/announcements/{items}")
    suspend fun getAnnouncements(@Path("items") num: Int): ApiResponse<List<AnnouncementItem>>

    @POST("/auth/callback")
    suspend fun getCallback(): ApiResponse<AuthCallback>

    @GET("/schedule/locations/buildings")
    suspend fun getBuildings(): ApiResponse<List<Buildings>>

    @GET("/schedule/locations/building/{building}/rooms")
    suspend fun getRooms(@Path("building") building: String): ApiResponse<List<BuildingRooms>>

    @GET("/schedule/room/{room}/availabilities")
    suspend fun getRoomTimes(@Path("room") room: Int): ApiResponse<List<RoomAvailable>>

    @GET("/schedule/colleges")
    suspend fun getColleges(): ApiResponse<List<Colleges>>

    @GET("/schedule/{college}/advisors")
    suspend fun getAdvisors(@Path("college") college: Int): ApiResponse<List<Advisors>>

    @POST("/schedule/room")
    suspend fun scheduleRoom(@Query("location") location: Int, @Query("startTime") startTime: String,
                             @Query("endTime") endTime: String): ApiResponse<GenericResponse>

    @POST("/announcements")
    suspend fun addAnnouncement(@Query("title") title: String,
                                @Query("description") description: String,
                                @Query("dateStart") dateStart: String,
                                @Query("dateEnd") dateEnd: String,
                                @Query("role") role: Int): ApiResponse<GenericResponse>

    @GET("/users/me")
    suspend fun getMyDetails(): ApiResponse<UserDetails>

    @GET("/messages/")
    suspend fun getMessageThreads(): ApiResponse<List<Thread>>

    @GET("/messages/chat/{id}")
    suspend fun getMessages(@Path("id") id: String): ApiResponse<List<MessageThread>>

    @DELETE("/messages/chat/{id}")
    suspend fun deleteMessageThread(@Path("id") id: String): ApiResponse<GenericResponse>

    @POST("/messages/create/thread")
    suspend fun createThread(@Query("recipient") email: String): ApiResponse<CreateThread>
}