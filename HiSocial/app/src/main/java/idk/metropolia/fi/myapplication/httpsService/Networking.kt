package idk.metropolia.fi.myapplication.httpsService

import com.example.ahao9.socialevent.httpsService.Service.httpService
import com.example.ahao9.socialevent.utils.LogUtils
import com.google.gson.JsonObject
import idk.metropolia.fi.myapplication.model.EventsResponse
import idk.metropolia.fi.myapplication.model.MyEventObject
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SearchPlacesResultObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object Networking {
    private const val BASE_URL = "https://api.hel.fi/linkedevents/v1/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(NetworkServices::class.java)!!


    private const val POST_BASE_URL = "https://linkedcourses-api.test.hel.ninja/linkedcourses-test/v1/"
    private val testRetrofit = Retrofit.Builder()
            .baseUrl(POST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val testService = testRetrofit.create(TestNetworkServices::class.java)


    interface TestNetworkServices {

        @Headers("ApiKey:14OGbx613yeIIoUm2sF6")
        @POST("event/")
        fun postNewEvent(@Body eventObj: MyEventObject): Call<JsonObject>
    }

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

        // http://api.hel.fi/linkedevents/v1/place?page_size=1000
        @GET("place/")
        fun searchPlacesResult(@Query("format") format: String = "json",
                               @Query("page") page: String = "1",
                               @Query("page_size") page_size: String = "30"
        ): Call<SearchPlacesResultObject>

        // http://api.hel.fi/linkedevents/v1/event/?format=json&include=location,keywords&text=" + text + "
        @GET("event/")
        fun searchEvent(@Query("format") format: String = "json",
                        @Query("include") include: String = "location",
                        @Query("text") keyword: String,
                        @Query("location") location: String,
                        @Query("start") start: String = "today",
                        @Query("end") end: String
        ): Call<SearchEventsResultObject>

        @GET("event/")
        fun searchEvent(@Query("format") format: String = "json",
                        @Query("include") include: String = "location",
                        @Query("text") keyword: String,
                        @Query("start") start: String = "today",
                        @Query("end") end: String
        ): Call<SearchEventsResultObject>

        @GET("event/")
        fun searchEvent(
                @Query("include") include: String = "location",
                @Query("text") keyword: String,
                @Query("start") start: String = "today",
                @Query("location") location: String
        ): Call<SearchEventsResultObject>

        @GET("event/")
        fun searchEvent(
                @Query("include") include: String = "location",
                @Query("text") keyword: String,
                @Query("start") start: String = "today"
        ): Call<SearchEventsResultObject>
    }

}