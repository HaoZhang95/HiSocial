package idk.metropolia.fi.myapplication.httpsService

import com.apollographql.apollo.ApolloClient
import idk.metropolia.fi.myapplication.adapter.ApolloLongAdapter
import idk.metropolia.fi.myapplication.adapter.ApolloPolylineAdapter
import okhttp3.OkHttpClient
import type.CustomType

object Apollo {

    private const val BASE_URL = "https://api.digitransit.fi/routing/v1/routers/hsl/index/graphql"

    private val okHttpClient = OkHttpClient.Builder().build()

    private val polylineAdapter = ApolloPolylineAdapter()

    private val longAdapter = ApolloLongAdapter() //

    val apolloClient =
            ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttpClient)
                    .addCustomTypeAdapter(CustomType.POLYLINE, polylineAdapter)
                    .addCustomTypeAdapter(CustomType.LONG, longAdapter)
                    .build()
}