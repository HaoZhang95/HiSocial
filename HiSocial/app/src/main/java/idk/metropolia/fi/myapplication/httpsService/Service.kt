package com.example.ahao9.socialevent.httpsService

import idk.metropolia.fi.myapplication.model.EventsResponse
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.model.SingleEventObject
import com.example.ahao9.socialevent.utils.LogUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 22:18 2018/10/20
 * @ Description：Build for Metropolia project
 */
object Service {
    private val httpService: EventHttpService by lazy {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(5.toLong(), TimeUnit.SECONDS)

        Retrofit.Builder()
                .baseUrl("https://api.hel.fi/linkedevents/v1/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build()
                .create(EventHttpService::class.java)
    }

    fun findInitializedEvents(subscriber: Subscriber<EventsResponse>, include: String, start: String){
        httpService.findInitializedEvents(include,start)
                .onErrorReturn {
                    LogUtils.e("httpService --> ${it.localizedMessage}")
                    return@onErrorReturn null
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    // https://api.hel.fi/linkedevents/v1/search/?page=1&page_size=4&type=event&input=dance&start=2018-11-01
    fun loadCommingSoonEvents(subscriber: Subscriber<SearchEventsResultObject>,
                              page: String, page_size: String, type: String, input: String, start: String){
        httpService.loadCommingSoonEvents(page, page_size, type, input, start)
                .onErrorReturn {
                    LogUtils.e("httpService --> ${it.localizedMessage}")
                    return@onErrorReturn null
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    fun findEventById(subscriber: Subscriber<SingleEventObject>, id: String){

        httpService.findEventById(id)
                .onErrorReturn {
                    LogUtils.e("findEventById --> ${it.message}")
                    LogUtils.e("findEventById --> ${it.localizedMessage}")
                    return@onErrorReturn null
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    fun loadPlaceById(subscriber: Subscriber<SingleEventLocationObject>, id: String){

        LogUtils.e("loadPlaceById: -> $id")

        httpService.findPlaceById(id)
                .onErrorReturn {
                    LogUtils.e("loadPlaceById: -> ${it.message}")
                    return@onErrorReturn null
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
