package idk.metropolia.fi.myapplication.adapter

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue

class ApolloLongAdapter: CustomTypeAdapter<Long> {
    override fun encode(value: Long): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLNumber(value)
    }

    override fun decode(value: CustomTypeValue<*>): Long = value.value.toString().toLong()
}

class ApolloPolylineAdapter: CustomTypeAdapter<String> {
    override fun encode(value: String): CustomTypeValue<String> {
        return CustomTypeValue.GraphQLJsonString(value)
    }

    override fun decode(value: CustomTypeValue<*>): String = value.value.toString()
}