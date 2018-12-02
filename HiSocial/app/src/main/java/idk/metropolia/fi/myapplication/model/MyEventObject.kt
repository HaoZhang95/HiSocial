package idk.metropolia.fi.myapplication.model

import com.google.gson.annotations.SerializedName


data class MyEventObject(
        @SerializedName("name") var name: Name = Name(),
        @SerializedName("location") var location: Location = Location(),
        @SerializedName("start_time") var startTime: String = "", // 2018-10-31
        @SerializedName("end_time") var endTime: String = "", // 2019-10-31
        @SerializedName("short_description") var shortDescription: ShortDescription = ShortDescription(),
        @SerializedName("keywords") var keywords: MutableList<Keyword> = mutableListOf(),
        @SerializedName("description") var description: Description = Description(),
        @SerializedName("offers") var offers: MutableList<Offer> = mutableListOf()
) {
    data class Location(
            @SerializedName("@id") var id: String = "" // https://linkedcourses-api.test.hel.ninja/linkedcourses-test/v1/place/tprek:7259/
    )

    data class Keyword(
            @SerializedName("@id") var id: String = "" // https://linkedcourses-api.test.hel.ninja/linkedcourses-test/v1/keyword/yso:p4354/
    )

    data class ShortDescription(
            @SerializedName("en") var fi: String = "" // no short description
    )

    data class Name(
            @SerializedName("en") var fi: String = "" // Test Event
    )

    data class Offer(
            @SerializedName("is_free") var isFree: Boolean = false // true
    )

    data class Description(
            @SerializedName("en") var fi: String = "" // Sample.......
    )
}

