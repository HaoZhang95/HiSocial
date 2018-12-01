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
            @SerializedName("next") val next: String? = null, // null
            @SerializedName("previous") val previous: Any? = Any() // null
    )

    data class SingleBeanData(
            @SerializedName("id") val id: String ="", // espoo:146665
            @SerializedName("location") val location: Location? = null,
            @SerializedName("keywords") val keywords: List<Keyword> = listOf(),
            @SerializedName("super_event") val superEvent: Any ="", // null
            @SerializedName("event_status") val eventStatus: String ="", // EventScheduled
            @SerializedName("external_links") val externalLinks: List<Any> = listOf(),
            @SerializedName("offers") val offers: List<Offer> = listOf(),
            @SerializedName("data_source") val dataSource: String="", // espoo
            @SerializedName("publisher") val publisher: String? = null, // espoo:kaupunki
            @SerializedName("sub_events") val subEvents: List<Any> = listOf() ,
            @SerializedName("in_language") val inLanguage: List<Any> = listOf(),
            @SerializedName("audience") val audience: List<Any> = listOf(),
            @SerializedName("created_time") val createdTime: String ="", // 2018-10-02T14:16:36.868885Z
            @SerializedName("last_modified_time") val lastModifiedTime: String = "", // 2018-10-02T14:16:36.945943Z
            @SerializedName("date_published") val datePublished: String ="", // 2018-10-02T09:00:00Z
            @SerializedName("start_time") val startTime: String = "", // 2018-10-23T14:30:00Z
            @SerializedName("end_time") val endTime: String? = null, // 2018-10-23T16:30:00Z
            @SerializedName("custom_data") val customData: Any = "", // null
            @SerializedName("super_event_type") val superEventType: Any ="", // null
            @SerializedName("name") val name: Name? = null,
            @SerializedName("location_extra_info") val locationExtraInfo: LocationExtraInfo? = null,
            @SerializedName("provider") val provider: Provider? = null,
            @SerializedName("short_description") val shortDescription: ShortDescription? = null,
            @SerializedName("description") val description: Description? = null,
            @SerializedName("info_url") val infoUrl: InfoUrl? = null,
            @SerializedName("@id") val web_id: String ="", // https://api.hel.fi/linkedevents/v1/event/espoo:146665/
            @SerializedName("@context") val context: String ="", // http://schema.org
            @SerializedName("@type") val type: String ="", // Event/LinkedEvent
            @SerializedName("@progress") var progress: Boolean = false, // Event/LinkedEvent
            @SerializedName("images") val images: List<Image> = listOf()
    ) : Serializable {
        data class Location(
                @SerializedName("id") val id: String? = null, // tprek:15490
                @SerializedName("divisions") val divisions: List<Division>,
                @SerializedName("created_time") val createdTime: Any, // null
                @SerializedName("last_modified_time") val lastModifiedTime: String, // 2018-10-09T10:09:16.845124Z
                @SerializedName("custom_data") val customData: Any, // null
                @SerializedName("email") val email: Any, // null
                @SerializedName("contact_type") val contactType: Any, // null
                @SerializedName("address_region") val addressRegion: Any, // null
                @SerializedName("postal_code") val postalCode: String, // 02180
                @SerializedName("post_office_box_num") val postOfficeBoxNum: Any, // null
                @SerializedName("address_country") val addressCountry: Any, // null
                @SerializedName("deleted") val deleted: Boolean, // false
                @SerializedName("n_events") val nEvents: Int, // 1
                @SerializedName("data_source") val dataSource: String, // tprek
                @SerializedName("image") val image: Any, // null
                @SerializedName("publisher") val publisher: String, // ahjo:u021600
                @SerializedName("parent") val parent: Any, // null
                @SerializedName("replaced_by") val replacedBy: Any, // null
                @SerializedName("position") val position: Position?,
                @SerializedName("name") val name: Name,
                @SerializedName("street_address") val streetAddress: StreetAddress,
                @SerializedName("telephone") val telephone: Telephone,
                @SerializedName("description") val description: Any, // null
                @SerializedName("address_locality") val addressLocality: AddressLocality?,
                @SerializedName("info_url") val infoUrl: InfoUrl,
                @SerializedName("@id") val web_id: String, // https://api.hel.fi/linkedevents/v1/place/tprek:15490/
                @SerializedName("@context") val context: String, // http://schema.org
                @SerializedName("@type") val type: String // Place
        ): Serializable {
            data class Division(
                    @SerializedName("type") val type: String, // muni
                    @SerializedName("ocd_id") val ocdId: String, // ocd-division/country:fi/kunta:espoo
                    @SerializedName("municipality") val municipality: Any, // null
                    @SerializedName("name") val name: Name
            ): Serializable {
                data class Name(
                        @SerializedName("sv") val sv: String, // Esbo
                        @SerializedName("fi") val fi: String // Espoo
                ) : Serializable
            }

            data class Name(
                    @SerializedName("sv") val sv: String, // Mankkaan päiväkoti
                    @SerializedName("en") val en: String, // Mankkaan päiväkoti
                    @SerializedName("fi") val fi: String // Mankkaan päiväkoti
            ): Serializable

            data class InfoUrl(
                    @SerializedName("sv") val sv: String, // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
                    @SerializedName("en") val en: String, // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
                    @SerializedName("fi") val fi: String // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
            ): Serializable

            data class Position(
                    @SerializedName("type") val type: String, // Point
                    @SerializedName("coordinates") val coordinates: List<Double>?
            ): Serializable

            data class StreetAddress(
                    @SerializedName("sv") val sv: String, // Gamla Mankans gränd 2 B
                    @SerializedName("en") val en: String, // Vanhan-Mankkaan kuja 2 B
                    @SerializedName("fi") val fi: String // Vanhan-Mankkaan kuja 2 B
            ): Serializable

            data class AddressLocality(
                    @SerializedName("sv") val sv: String, // Esbo
                    @SerializedName("en") val en: String, // Espoo
                    @SerializedName("fi") val fi: String // Espoo
            ): Serializable

            data class Telephone(
                    @SerializedName("fi") val fi: String // +358 50 369 4431
            ): Serializable
        }

        data class Offer(
                @SerializedName("is_free") val isFree: Boolean = false, // false
                @SerializedName("price") val price: Price? = null,
                @SerializedName("description") val description: Description = Description(),
                @SerializedName("info_url") val infoUrl: InfoUrl = InfoUrl()
        ) : Serializable{
            data class Description(
                    @SerializedName("en") val en: String = "",
                    @SerializedName("fi") val fi: String = ""
            ): Serializable

            data class Price(
                    @SerializedName("en") val en: String = "", // 30€
                    @SerializedName("fi") val fi: String = "" // 30€
            ): Serializable

            data class InfoUrl(
                    @SerializedName("en") val en: String = "", // https://www.lippu.fi/tickets.html?fun=erdetail&affiliate=HKK&doc=erdetaila&erid=2257874
                    @SerializedName("fi") val fi: String = "" // https://www.lippu.fi/tickets.html?fun=erdetail&affiliate=HKK&doc=erdetaila&erid=2257874
            ): Serializable
        }

        data class Keyword(
                @SerializedName("@id") val id: String // https://api.hel.fi/linkedevents/v1/keyword/yso:p8268/
        ): Serializable

        data class Description(
                @SerializedName("fi") val fi: String, // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
                @SerializedName("en") val en: String, // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
                @SerializedName("sv") val sv: String // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
        ): Serializable

        data class Name(
                @SerializedName("fi") val fi: String // Asukastilaisuus Mankkaan päiväkodin laajennuksesta
        ): Serializable

        data class Provider(
                @SerializedName("fi") val fi: String // Espoon kaupunkisuunnittelukeskus<br>
        ): Serializable

        data class Image(
                @SerializedName("id") val id: Int, // 47519
                @SerializedName("license") val license: String, // cc_by
                @SerializedName("created_time") val createdTime: String, // 2018-10-02T14:16:36.848156Z
                @SerializedName("last_modified_time") val lastModifiedTime: String, // 2018-10-02T14:16:36.848179Z
                @SerializedName("name") val name: String,
                @SerializedName("url") val url: String, // https://www.espoo.fi/download/noname/{3AC232E1-DC64-4BFB-83E5-5887AFB7A271}/107334
                @SerializedName("cropping") val cropping: String,
                @SerializedName("photographer_name") val photographerName: Any, // null
                @SerializedName("data_source") val dataSource: String, // espoo
                @SerializedName("publisher") val publisher: String, // espoo:kaupunki
                @SerializedName("@id") val web_id: String, // https://api.hel.fi/linkedevents/v1/image/47519/
                @SerializedName("@context") val context: String, // http://schema.org
                @SerializedName("@type") val type: String // ImageObject
        ): Serializable

        data class InfoUrl(
                @SerializedName("fi") val fi: String // https://www.espoo.fi/fi-FI/Asuminen_ja_ymparisto/Kaavoitus/Asemakaava/Asemakaavoituskohteet/Tapiola/SeilimakiLukupuro_240121
        ): Serializable

        data class LocationExtraInfo(
                @SerializedName("fi") val fi: String // Mankkaan päiväkoti
        ): Serializable

        data class ShortDescription(
                @SerializedName("fi") val fi: String? // Tervetuloa tapaamaan Seilimäki-Lukupuro kaavahankkeen suunnittelijoita Mankkaan päiväkotiin, Vanhan-Mankkaan kuja 2B tiistaina 23.10.2018 klo 17.30-19.30. Mankkaan päiväkotia laajennetaan ja peruskorjataan.
        ): Serializable
    }

}















