package idk.metropolia.fi.myapplication.model

import com.google.gson.annotations.SerializedName

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 22:39 2018/10/26
 * @ Description：find location by event location id
 * https://api.hel.fi/linkedevents/v1/event/espoo%3A142343/
 */
data class SingleEventLocationObject(
        @SerializedName("id") val id: String = "", // tprek:26429
        @SerializedName("divisions") val divisions: List<Division> = listOf(),
        @SerializedName("created_time") val createdTime: String = "", // null
        @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-06-11T10:10:46.861406Z
        @SerializedName("custom_data") val customData: String = "", // null
        @SerializedName("email") val email: String = "", // null
        @SerializedName("contact_type") val contactType: String = "", // null
        @SerializedName("address_region") val addressRegion: String = "", // null
        @SerializedName("postal_code") val postalCode: String = "", // 02070
        @SerializedName("post_office_box_num") val postOfficeBoxNum: String = "", // null
        @SerializedName("address_country") val addressCountry: String = "", // null
        @SerializedName("deleted") val deleted: Boolean = false, // false
        @SerializedName("n_events") val nEvents: Int = 0, // 258
        @SerializedName("data_source") val dataSource: String = "", // tprek
        @SerializedName("image") val image: String = "", // null
        @SerializedName("publisher") val publisher: String = "", // ahjo:u021600
        @SerializedName("parent") val parent: String = "", // null
        @SerializedName("replaced_by") val replacedBy: String = "", // null
        @SerializedName("position") val position: Position = Position(),
        @SerializedName("name") val name: Name = Name(),
        @SerializedName("street_address") val streetAddress: StreetAddress = StreetAddress(),
        @SerializedName("telephone") val telephone: Telephone = Telephone(),
        @SerializedName("description") val description: Description = Description(), // null
        @SerializedName("address_locality") val addressLocality: AddressLocality = AddressLocality(),
        @SerializedName("info_url") val infoUrl: InfoUrl = InfoUrl(),
        @SerializedName("@id") val web_id: String = "", // https://api.hel.fi/linkedevents/v1/place/tprek:26429/
        @SerializedName("@context") val context: String = "", // http://schema.org
        @SerializedName("@type") val type: String = "" // Place
) {
    data class Division(
            @SerializedName("type") val type: String = "", // muni
            @SerializedName("ocd_id") val ocdId: String = "", // ocd-division/country:fi/kunta:espoo
            @SerializedName("municipality") val municipality: String = "", // null
            @SerializedName("name") val name: Name = Name()
    ) {
        data class Name(
                @SerializedName("sv") val sv: String = "", // Esbo
                @SerializedName("fi") val fi: String = "" // Espoo
        )
    }

    data class Name(
            @SerializedName("sv") val sv: String = "", // Sellosalen
            @SerializedName("en") val en: String = "", // Sello Hall
            @SerializedName("fi") val fi: String = "" // Sellosali
    )

    data class Description(
            @SerializedName("sv") val sv: String = "", // Sellosalen
            @SerializedName("en") val en: String = "", // Sello Hall
            @SerializedName("fi") val fi: String = "" // Sellosali
    )

    data class Position(
            @SerializedName("type") val type: String = "", // Point
            @SerializedName("coordinates") val coordinates: List<Double> = listOf()
    )

    data class Telephone(
            @SerializedName("fi") val fi: String = "" // 09 8165 7912
    )

    data class StreetAddress(
            @SerializedName("sv") val sv: String = "", // Musikantplatsen 1 A
            @SerializedName("fi") val fi: String = "" // Soittoniekanaukio 1 A
    )

    data class InfoUrl(
            @SerializedName("sv") val sv: String = "", // http://espoo.fi/sv-FI/Kultur_och_idrott/Kultur/Kulturcentrum_och_hus/Sellosalen
            @SerializedName("en") val en: String = "", // http://espoo.fi/en-US/Culture_and_sport/Culture/Cultural_Centre_and_cultural_houses/Sello_Hall
            @SerializedName("fi") val fi: String = "" // http://espoo.fi/fi-FI/Kulttuuri_ja_liikunta/Kulttuuri/Kulttuurikeskukset_ja_talot/Sellosali
    )

    data class AddressLocality(
            @SerializedName("sv") val sv: String = "", // Esbo
            @SerializedName("en") val en: String = "", // Espoon kaupunki
            @SerializedName("fi") val fi: String = "" // Espoon kaupunki
    )
}





