package idk.metropolia.fi.myapplication.model
import com.google.gson.annotations.SerializedName


/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 21:50 2018/10/26
 * @ Description：find event by id
 * https://api.hel.fi/linkedevents/v1/event/espoo%3A142343/
 */

data class SingleEventObject(
        @SerializedName("id") val id: String = "", // espoo:142343
        @SerializedName("location") val location: Location = Location(),
        @SerializedName("keywords") val keywords: List<Keyword> = listOf(),
        @SerializedName("super_event") val superEvent: Any? = Any(), // null
        @SerializedName("event_status") val eventStatus: String = "", // EventScheduled
        @SerializedName("external_links") val externalLinks: List<Any> = listOf(),
        @SerializedName("offers") val offers: List<Any> = listOf(),
        @SerializedName("data_source") val dataSource: String = "", // espoo
        @SerializedName("publisher") val publisher: String = "", // espoo:kaupunki
        @SerializedName("sub_events") val subEvents: List<Any> = listOf(),
        @SerializedName("in_language") val inLanguage: List<Any> = listOf(),
        @SerializedName("audience") val audience: List<Any> = listOf(),
        @SerializedName("created_time") val createdTime: Any? = Any(), // null
        @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-07-05T14:20:24.377377Z
        @SerializedName("date_published") val datePublished: String = "", // 2018-05-25T11:48:00Z
        @SerializedName("start_time") val startTime: String = "", // 2018-11-06T11:30:00Z
        @SerializedName("end_time") val endTime: String = "", // 2018-11-06T13:15:00Z
        @SerializedName("custom_data") val customData: Any? = Any(), // null
        @SerializedName("super_event_type") val superEventType: Any? = Any(), // null
        @SerializedName("name") val name: Name = Name(),
        @SerializedName("location_extra_info") val locationExtraInfo: LocationExtraInfo = LocationExtraInfo(),
        @SerializedName("provider") val provider: Any? = Any(), // null
        @SerializedName("short_description") val shortDescription: ShortDescription = ShortDescription(),
        @SerializedName("description") val description: Description = Description(),
        @SerializedName("info_url") val infoUrl: Any? = Any(), // null
        @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/event/espoo:142343/
        @SerializedName("@context") val context: String = "", // http://schema.org
        @SerializedName("@type") val type: String = "", // Event/LinkedEvent
        @SerializedName("images") val images: List<Image> = listOf()
) {
    data class Location(
            @SerializedName("@id") val id: String = "" // https://api.hel.fi/linkedevents/v1/place/tprek:26429/
    )

    data class Keyword(
            @SerializedName("@id") val id: String = "" // https://api.hel.fi/linkedevents/v1/keyword/yso:p372/
    )

    data class Name(
            @SerializedName("sv") val sv: String = "", // Eftermiddagsdans
            @SerializedName("fi") val fi: String = "" // Day dance
    )

    data class LocationExtraInfo(
            @SerializedName("sv") val sv: String = "", // Sellosalen
            @SerializedName("fi") val fi: String = "" // Sello hall
    )

    data class ShortDescription(
            @SerializedName("sv") val sv: String = "", // Under de populära eftermiddagsdanserna är det Pop &amp; Jazz Konservatorios dansorkester som står för musiken.
            @SerializedName("fi") val fi: String = "" // At our popular day dances, music is performed by the dance band of the Pop &amp; Jazz Conservatory.
    )

    data class Description(
            @SerializedName("sv") val sv: String = "", // <p> <strong> <br> <br>Tis 6.11 kl. 13.30</strong></p><p>Under de populära eftermiddagsdanserna är det Pop &amp; Jazz Konservatorios dansorkester som står för musiken.</p><p>Fritt inträde</p>
            @SerializedName("fi") val fi: String = "" // <p> <strong> </strong></p><p> <strong>Tue 6 November at 13:30</strong></p><p>At our popular day dances, music is performed by the dance band of the Pop &amp; Jazz Conservatory.</p><p>Admission free</p>
    )

    data class Image(
            @SerializedName("id") val id: Int = 0, // 44570
            @SerializedName("license") val license: String = "", // cc_by
            @SerializedName("created_time") val createdTime: String = "", // 2018-05-25T12:22:49.353423Z
            @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-05-25T12:22:49.353444Z
            @SerializedName("name") val name: String = "",
            @SerializedName("url") val url: String = "", // https://www.espoo.fi/download/noname/{20898C3D-9D90-4465-92C8-CB1081CD94DB}/103717
            @SerializedName("cropping") val cropping: String = "",
            @SerializedName("photographer_name") val photographerName: Any? = Any(), // null
            @SerializedName("data_source") val dataSource: String = "", // espoo
            @SerializedName("publisher") val publisher: String = "", // espoo:kaupunki
            @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/image/44570/
            @SerializedName("@context") val context: String = "", // http://schema.org
            @SerializedName("@type") val type: String = "" // ImageObject
    )
}












