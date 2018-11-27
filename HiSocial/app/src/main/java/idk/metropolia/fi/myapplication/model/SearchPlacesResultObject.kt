package idk.metropolia.fi.myapplication.model
import com.google.gson.annotations.SerializedName


/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 23:07 2018/10/26
 * @ Description：search places by keyword
 * https://api.hel.fi/linkedevents/v1/search/?page=1&page_size=5&type=place&input=dance
 */
data class SearchPlacesResultObject(
        @SerializedName("meta") val meta: Meta = Meta(),
        @SerializedName("data") val data: List<Data> = listOf()
) {

    data class Meta(
            @SerializedName("count") val count: Int = 0, // 1
            @SerializedName("next") val next: String = "", // null
            @SerializedName("previous") val previous: String = "" // null
    )

    data class Data(
            @SerializedName("id") val id: String = "", // tprek:57030
            @SerializedName("divisions") val divisions: List<Division> = listOf(),
            @SerializedName("created_time") val createdTime: Any? = Any(), // null
            @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-10-25T06:10:15.407560Z
            @SerializedName("custom_data") val customData: Any? = Any(), // null
            @SerializedName("email") val email: String = "", // toimisto@dbdcdance.com
            @SerializedName("contact_type") val contactType: Any? = Any(), // null
            @SerializedName("address_region") val addressRegion: Any? = Any(), // null
            @SerializedName("postal_code") val postalCode: String = "", // 00540
            @SerializedName("post_office_box_num") val postOfficeBoxNum: Any? = Any(), // null
            @SerializedName("address_country") val addressCountry: Any? = Any(), // null
            @SerializedName("deleted") val deleted: Boolean = false, // false
            @SerializedName("n_events") val nEvents: Int = 0, // 0
            @SerializedName("data_source") val dataSource: String = "", // tprek
            @SerializedName("image") val image: Any? = Any(), // null
            @SerializedName("publisher") val publisher: String = "", // ahjo:u021600
            @SerializedName("parent") val parent: Any? = Any(), // null
            @SerializedName("replaced_by") val replacedBy: Any? = Any(), // null
            @SerializedName("position") val position: Position = Position(),
            @SerializedName("name") val name: Name? = null,
            @SerializedName("street_address") val streetAddress: StreetAddress = StreetAddress(),
            @SerializedName("telephone") val telephone: Telephone = Telephone(),
            @SerializedName("description") val description: Any? = Any(), // null
            @SerializedName("address_locality") val addressLocality: AddressLocality = AddressLocality(),
            @SerializedName("info_url") val infoUrl: Any? = Any(), // null
            @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/place/tprek:57030/
            @SerializedName("@context") val context: String = "", // http://schema.org
            @SerializedName("@type") val type: String = "", // Place
            @SerializedName("resource_type") val resourceType: String = "", // place
            @SerializedName("score") val score: Double = 0.0 // 2.3216887
    ) {
        data class Division(
                @SerializedName("type") val type: String = "", // muni
                @SerializedName("ocd_id") val ocdId: String = "", // ocd-division/country:fi/kunta:helsinki
                @SerializedName("municipality") val municipality: Any? = Any(), // null
                @SerializedName("name") val name: Name = Name()
        ) {
            data class Name(
                    @SerializedName("sv") val sv: String = "", // Helsingfors
                    @SerializedName("fi") val fi: String = "" // Helsinki
            )
        }

        data class Position(
                @SerializedName("type") val type: String = "", // Point
                @SerializedName("coordinates") val coordinates: List<Double> = listOf()
        )

        data class Name(
                @SerializedName("sv") val sv: String = "", // Finskspråkig eftermiddagsverksamhet / Kalasataman peruskoulu, Danny's Bollywood Dance Crew Oy
                @SerializedName("en") val en: String = "", // Afternoon activities / Kalasatama Comprehensive School, Danny's Bollywood Dance Crew Oy
                @SerializedName("fi") val fi: String? = null // Iltapäivätoiminta / Kalasataman peruskoulu, Danny's Bollywood Dance Crew Oy
        )

        data class AddressLocality(
                @SerializedName("sv") val sv: String = "", // Helsingfors
                @SerializedName("en") val en: String = "", // Helsinki
                @SerializedName("fi") val fi: String = "" // Helsinki
        )

        data class StreetAddress(
                @SerializedName("sv") val sv: String = "", // Polarisgatan 1
                @SerializedName("en") val en: String = "", // Polariksenkatu 1
                @SerializedName("fi") val fi: String = "" // Polariksenkatu 1
        )

        data class Telephone(
                @SerializedName("fi") val fi: String = "" // 010-5012277
        )
    }
}







