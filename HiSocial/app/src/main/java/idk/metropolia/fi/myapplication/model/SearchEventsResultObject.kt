package idk.metropolia.fi.myapplication.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 23:01 2018/10/26
 * @ Description：Search a bunch of events by keyword, date...
 *
 * https://api.hel.fi/linkedevents/v1/search/?page=1&page_size=3&type=event&input=dance&start=2018-10-26&end=2018-11-1
 */

data class SearchEventsResultObject(
        @SerializedName("meta") val meta: Meta = Meta(),
        @SerializedName("data") val data: List<SingleBeanData> = listOf()
) {
    data class Meta(
            @SerializedName("count") val count: Int = 0, // 3
            @SerializedName("next") val next: Any? = Any(), // null
            @SerializedName("previous") val previous: Any? = Any() // null
    )

//    data class SingleBeanInSearch(
//            @SerializedName("id") val id: String = "", // helsinki:afsx7jjahq
//            @SerializedName("location") val location: Location = Location(),
//            @SerializedName("keywords") val keywords: List<Keyword> = listOf(),
//            @SerializedName("super_event") val superEvent: Any? = Any(), // null
//            @SerializedName("event_status") val eventStatus: String = "", // EventScheduled
//            @SerializedName("external_links") val externalLinks: List<Any> = listOf(),
//            @SerializedName("offers") val offers: List<Offer> = listOf(),
//            @SerializedName("data_source") val dataSource: String = "", // helsinki
//            @SerializedName("publisher") val publisher: String = "", // ytj:0586977-6
//            @SerializedName("sub_events") val subEvents: List<Any> = listOf(),
//            @SerializedName("in_language") val inLanguage: List<Any> = listOf(),
//            @SerializedName("audience") val audience: List<Any> = listOf(),
//            @SerializedName("created_time") val createdTime: String = "", // 2018-08-28T08:27:32.800380Z
//            @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-08-28T08:27:32.800401Z
//            @SerializedName("date_published") val datePublished: Any? = Any(), // null
//            @SerializedName("start_time") val startTime: String = "", // 2018-11-01T17:00:00Z
//            @SerializedName("end_time") val endTime: String? = "", // null
//            @SerializedName("custom_data") val customData: Any? = Any(), // null
//            @SerializedName("super_event_type") val superEventType: Any? = Any(), // null
//            @SerializedName("name") val name: Name? = null,
//            @SerializedName("location_extra_info") val locationExtraInfo: LocationExtraInfo = LocationExtraInfo(), // null
//            @SerializedName("provider") val provider: Provider? = null,
//            @SerializedName("short_description") val shortDescription: ShortDescription? = null,
//            @SerializedName("description") val description: Description? = null,
//            @SerializedName("info_url") val infoUrl: InfoUrl? = null,
//            @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/event/helsinki:afsx7jjahq/
//            @SerializedName("@context") val context: String = "", // http://schema.org
//            @SerializedName("@type") val type: String = "", // Event/LinkedEvent
//            @SerializedName("images") val images: List<Image> = listOf(),
//            @SerializedName("resource_type") val resourceType: String = "", // event
//            @SerializedName("score") val score: Double = 0.0 // 3.0151842
//    ): Serializable {
//        data class Location(
//                @SerializedName("@id") val id: String = "" // https://api.hel.fi/linkedevents/v1/place/tprek:9302/
//        ): Serializable
//
//        data class LocationExtraInfo(
//                @SerializedName("en") val en: String = "",
//                @SerializedName("fi") val fi: String = "",
//                @SerializedName("sv") val sv: String = ""
//        ): Serializable
//
//        data class Keyword(
//                @SerializedName("@id") val id: String = "" // https://api.hel.fi/linkedevents/v1/keyword/yso:p360/
//        ): Serializable
//
//        data class Offer(
//                @SerializedName("is_free") val isFree: Boolean = false, // false
//                @SerializedName("price") val price: Price? = null,
//                @SerializedName("description") val description: Description = Description(),
//                @SerializedName("info_url") val infoUrl: InfoUrl = InfoUrl()
//        ) : Serializable{
//            data class Description(
//                    @SerializedName("en") val en: String = "",
//                    @SerializedName("fi") val fi: String = ""
//            ): Serializable
//
//            data class Price(
//                    @SerializedName("en") val en: String = "", // 30€
//                    @SerializedName("fi") val fi: String = "" // 30€
//            ): Serializable
//
//            data class InfoUrl(
//                    @SerializedName("en") val en: String = "", // https://www.lippu.fi/tickets.html?fun=erdetail&affiliate=HKK&doc=erdetaila&erid=2257874
//                    @SerializedName("fi") val fi: String = "" // https://www.lippu.fi/tickets.html?fun=erdetail&affiliate=HKK&doc=erdetaila&erid=2257874
//            ): Serializable
//        }
//
//        data class Provider(
//                @SerializedName("en") val en: String = "", // Helsinki Dance Company
//                @SerializedName("fi") val fi: String = "" // Helsinki Dance Company
//        ): Serializable
//
//        data class Name(
//                @SerializedName("en") val en: String = "", // Helsinki Dance Company: La Fille Mal Gardée
//                @SerializedName("fi") val fi: String = "" // Helsinki Dance Company: Huonosti vartioitu tyttö
//        ): Serializable
//
//        data class ShortDescription(
//                @SerializedName("en") val en: String = "", // Helsinki Dance Company is presenting a new contemporary dance version of the classical ballet La Fille Mal Gardée at Helsinki City Theatre.
//                @SerializedName("fi") val fi: String = "" // Huonosti vartioitu tyttö on vahvasti tanssin taitoon ja ilmaisuvoimaan luottava esitys, jonka inspiraationa on samanniminen klassinen baletti menneiltä
//        ): Serializable
//
//        data class Description(
//                @SerializedName("en") val en: String = "", // <p>Helsinki Dance Company is presenting a new contemporary dance version of the classical ballet La Fille Mal Gardée at Helsinki City Theatre.</p><p>Helsinki City Theatre, Small Stage<br/>1.11. at 19.00</p><p>Tickets 30€ from Lippupiste</p>
//                @SerializedName("fi") val fi: String = "" // <p>Huonosti vartioitu tyttö on vahvasti tanssin taitoon ja ilmaisuvoimaan luottava esitys, jonka inspiraationa on samanniminen klassinen baletti menneiltä vuosisadoilta.</p><p>Kuten muutkin baletin toteuttajat kautta aikain, Helsinki Dance Company ottaa versiossaan huomattavia vapauksia suhteessa tarinaan, tyyliin ja toteutukseen, mutta flirttailee avoimesti klassisen baletin traditionaaliselle muodolle. Lisäksi lava on lastattu vahvalla tanssitaidolla.</p><p>Esitys on henkilökuva naisesta – toimijasta – joka on kasvanut miettimään uusiksi omaa kunnollisuuttaan ja kiltteyttään. Tässä esityksessä miehet on ohennettu liikkuviksi kulisseiksi, noloja eläinosuuksia esittäviksi sivuhenkilöiksi ja avustajiksi ylväälle muotokuvalle täysikasvuisesta naisesta.</p><p>Helsingin kaupunginteatteri, Pieni näyttämö<br/>1.11. klo 19</p><p>Liput 30€ Lippupisteestä<br/></p>
//        ): Serializable
//
//        data class InfoUrl(
//                @SerializedName("en") val en: String = "", // https://hkt.fi/esitykset/huonosti-vartioitu-tytto/
//                @SerializedName("fi") val fi: String = "" // https://hkt.fi/esitykset/huonosti-vartioitu-tytto/
//        ): Serializable
//
//        data class Image(
//                @SerializedName("id") val id: Int = 0, // 46771
//                @SerializedName("license") val license: String = "", // event_only
//                @SerializedName("created_time") val createdTime: String = "", // 2018-08-28T08:12:35.084145Z
//                @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-08-28T08:12:35.084171Z
//                @SerializedName("name") val name: String = "", // Huonosti vartioitu tyttö
//                @SerializedName("url") val url: String = "", // https://api.hel.fi/linkedevents/media/images/huonostivart11.jpg
//                @SerializedName("cropping") val cropping: String = "", // 442,0,1478,1037
//                @SerializedName("photographer_name") val photographerName: String = "", // Helsingin kaupunginteatteri (c) Marko Mäkinen
//                @SerializedName("data_source") val dataSource: String = "", // helsinki
//                @SerializedName("publisher") val publisher: String = "", // ytj:0586977-6
//                @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/image/46771/
//                @SerializedName("@context") val context: String = "", // http://schema.org
//                @SerializedName("@type") val type: String = "" // ImageObject
//        ): Serializable
//    }
}















