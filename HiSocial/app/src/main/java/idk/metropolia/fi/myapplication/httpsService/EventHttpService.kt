package com.example.ahao9.socialevent.httpsService

import idk.metropolia.fi.myapplication.model.EventsResponse
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.model.SingleEventObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

// https://api.hel.fi/linkedevents/v1/event/?include=location&start=2018-10-26&end=2018-10-25&keyword=yso:p2250
// https://api.hel.fi/linkedevents/v1/search/?page=1&page_size=3&type=event&input=dance&start=2018-10-26
interface EventHttpService {

    /**
     * https://api.hel.fi/linkedevents/v1/event/?include=location&start=2018-10-26
     */
    @GET("event/")
    fun findInitializedEvents(@Query("include") include: String,
                              @Query("start") start: String): Observable<EventsResponse>

    /**
     * // https://api.hel.fi/linkedevents/v1/event/?include=location&start=2018-10-26&end=2018-10-25&keyword=yso:p2250
     */
    @GET("event/")
    fun findEventsBySearch(@Query("include") include: String,
                   @Query("start") start: String,
                   @Query("end") end: String,
                   @Query("keyword") keyword: String
                 ): Observable<EventsResponse>

    // https://api.hel.fi/linkedevents/v1/event/espoo%3A146665/    id=espoo:146665
    // id.replace(":","%3A")这里有一个坑
    @GET("event/{id}")
    fun findEventById(@Path(value = "id", encoded = true) id: String): Observable<SingleEventObject>

    // https://api.hel.fi/linkedevents/v1/place/tprek:26429/
    @GET("place/{id}")
    fun findPlaceById(@Path(value = "id", encoded = true) id: String): Observable<SingleEventLocationObject>

    // https://api.hel.fi/linkedevents/v1/search/?page=1&page_size=4&type=event&input=dance&start=2018-11-01
    @GET("search/")
    fun loadCommingSoonEvents(@Query("page") page: String,
                           @Query("page_size") page_size: String,
                           @Query("type") type: String,
                           @Query("input") input: String,
                           @Query("start") start: String
    ): Observable<SearchEventsResultObject>
}
