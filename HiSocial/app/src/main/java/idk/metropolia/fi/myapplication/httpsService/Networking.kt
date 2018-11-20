package idk.metropolia.fi.myapplication.httpsService

import com.google.gson.JsonObject
import idk.metropolia.fi.myapplication.model.EventsResponse
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object Networking {
    private const val BASE_URL = "https://api.hel.fi/linkedevents/v1/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(NetworkServices::class.java)!!


    interface NetworkServices {
        @GET("search/")
        fun loadCommingSoonEvents3(@Query("page") page: String,
                                   @Query("page_size") page_size: String,
                                   @Query("type") type: String,
                                   @Query("input") input: String,
                                   @Query("start") start: String
        ): Call<SearchEventsResultObject>

        // https://api.hel.fi/linkedevents/v1/event/?page=1&page_size=4&type=event&start=2018-11-20&include=location
        @GET("event/")
        fun loadEventsByKeywordType(@Query("format") format: String = "json",
                                    @Query("include") include: String = "location",
                                    @Query("keyword") keyword: String,
                                    @Query("page_size") page_size: String = "10",
                                    @Query("start") start: String = "today"
        ): Call<EventsResponse>

        // http://api.hel.fi/linkedevents/v1/event/?format=json&include=location&keyword=yso:p4363&page_size=10&start=today&end=today
        @GET("event/")
        fun loadEventsByKeywordType(@Query("format") format: String = "json",
                                   @Query("include") include: String = "location",
                                   @Query("keyword") keyword: String,
                                   @Query("page_size") page_size: String = "10",
                                   @Query("start") start: String = "today",
                                   @Query("end") end: String
        ): Call<EventsResponse>

        // https://api.hel.fi/linkedevents/v1/event/?page=1&page_size=4&type=event&start=2018-11-20&include=location
        @GET("event/")
        fun loadEventsByPageNumber(@Query("format") format: String = "json",
                                    @Query("include") include: String = "location",
                                    @Query("page") page: String = "1",
                                    @Query("page_size") page_size: String = "10",
                                    @Query("type") type: String = "event",
                                    @Query("start") start: String = "today"
        ): Call<EventsResponse>
    }

}