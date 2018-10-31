package idk.metropolia.fi.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 14:10 2018/10/21
 * @ Description：Find events
 *
 * https://api.hel.fi/linkedevents/v1/event/?include=location&start=2018-10-26
 */

data class EventsResponse(
        @SerializedName("meta") val meta: Meta,
        @SerializedName("data") val dataList: List<SingleBeanData>
)

data class Meta(
        @SerializedName("count") val count: Int, // 4
        @SerializedName("next") val next: Any, // null
        @SerializedName("previous") val previous: Any // null
)

data class SingleBeanData(
        @SerializedName("id") val id: String, // espoo:146665
        @SerializedName("location") val location: Location,
        @SerializedName("keywords") val keywords: List<Keyword>,
        @SerializedName("super_event") val superEvent: Any, // null
        @SerializedName("event_status") val eventStatus: String, // EventScheduled
        @SerializedName("external_links") val externalLinks: List<Any>,
        @SerializedName("offers") val offers: List<Any>,
        @SerializedName("data_source") val dataSource: String, // espoo
        @SerializedName("publisher") val publisher: String, // espoo:kaupunki
        @SerializedName("sub_events") val subEvents: List<Any>,
        @SerializedName("in_language") val inLanguage: List<Any>,
        @SerializedName("audience") val audience: List<Any>,
        @SerializedName("created_time") val createdTime: String, // 2018-10-02T14:16:36.868885Z
        @SerializedName("last_modified_time") val lastModifiedTime: String, // 2018-10-02T14:16:36.945943Z
        @SerializedName("date_published") val datePublished: String, // 2018-10-02T09:00:00Z
        @SerializedName("start_time") val startTime: String, // 2018-10-23T14:30:00Z
        @SerializedName("end_time") val endTime: String, // 2018-10-23T16:30:00Z
        @SerializedName("custom_data") val customData: Any, // null
        @SerializedName("super_event_type") val superEventType: Any, // null
        @SerializedName("name") val name: Name,
        @SerializedName("location_extra_info") val locationExtraInfo: LocationExtraInfo,
        @SerializedName("provider") val provider: Provider,
        @SerializedName("short_description") val shortDescription: ShortDescription?,
        @SerializedName("description") val description: Description,
        @SerializedName("info_url") val infoUrl: InfoUrl,
        @SerializedName("@id") val web_id: String, // https://api.hel.fi/linkedevents/v1/event/espoo:146665/
        @SerializedName("@context") val context: String, // http://schema.org
        @SerializedName("@type") val type: String, // Event/LinkedEvent
        @SerializedName("images") val images: List<Image>
) : Serializable {
    data class Location(
            @SerializedName("id") val id: String, // tprek:15490
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
            @SerializedName("position") val position: Position,
            @SerializedName("name") val name: Name,
            @SerializedName("street_address") val streetAddress: StreetAddress,
            @SerializedName("telephone") val telephone: Telephone,
            @SerializedName("description") val description: Any, // null
            @SerializedName("address_locality") val addressLocality: AddressLocality,
            @SerializedName("info_url") val infoUrl: InfoUrl,
            @SerializedName("@id") val web_id: String, // https://api.hel.fi/linkedevents/v1/place/tprek:15490/
            @SerializedName("@context") val context: String, // http://schema.org
            @SerializedName("@type") val type: String // Place
    ) {
        data class Division(
                @SerializedName("type") val type: String, // muni
                @SerializedName("ocd_id") val ocdId: String, // ocd-division/country:fi/kunta:espoo
                @SerializedName("municipality") val municipality: Any, // null
                @SerializedName("name") val name: Name
        ) {
            data class Name(
                    @SerializedName("sv") val sv: String, // Esbo
                    @SerializedName("fi") val fi: String // Espoo
            )
        }

        data class Name(
                @SerializedName("sv") val sv: String, // Mankkaan päiväkoti
                @SerializedName("en") val en: String, // Mankkaan päiväkoti
                @SerializedName("fi") val fi: String // Mankkaan päiväkoti
        )

        data class InfoUrl(
                @SerializedName("sv") val sv: String, // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
                @SerializedName("en") val en: String, // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
                @SerializedName("fi") val fi: String // http://www.espoo.fi/fi-FI/Kasvatus_ja_opetus/Varhaiskasvatus/Varhaiskasvatuksen_toimipaikat/Kunnalliset_paivakodit/Tapiola/Mankkaan_paivakoti
        )

        data class Position(
                @SerializedName("type") val type: String, // Point
                @SerializedName("coordinates") val coordinates: List<Double>
        )


        data class StreetAddress(
                @SerializedName("sv") val sv: String, // Gamla Mankans gränd 2 B
                @SerializedName("en") val en: String, // Vanhan-Mankkaan kuja 2 B
                @SerializedName("fi") val fi: String // Vanhan-Mankkaan kuja 2 B
        )

        data class AddressLocality(
                @SerializedName("sv") val sv: String, // Esbo
                @SerializedName("en") val en: String, // Espoo
                @SerializedName("fi") val fi: String // Espoo
        )

        data class Telephone(
                @SerializedName("fi") val fi: String // +358 50 369 4431
        )
    }

    data class Keyword(
            @SerializedName("@id") val id: String // https://api.hel.fi/linkedevents/v1/keyword/yso:p8268/
    )

    data class Description(
            @SerializedName("fi") val fi: String, // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
            @SerializedName("en") val en: String, // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
            @SerializedName("sv") val sv: String // <p>Seilimäki-Lukupuron asemakaavan muutoksen tavoitteena on Mankkaan päiväkodin laajennus ja peruskorjaus. Osallistumis- ja arviointisuunnittelma on nähtävillä 1.-30.10.2018.</p><p>Suunnitelmiin voi tutustua <strong>asukastilaisuudessa tiistaina 23.10.2018</strong> Mankkaan päiväkodissa (Vanhan-Mankkaan kuja 2B) klo 17.30-19.30.</p><p>Tervetuloa!</p><p></p>
    )

    data class Name(
            @SerializedName("fi") val fi: String // Asukastilaisuus Mankkaan päiväkodin laajennuksesta
    )

    data class Provider(
            @SerializedName("fi") val fi: String // Espoon kaupunkisuunnittelukeskus<br>
    )

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
    )

    data class InfoUrl(
            @SerializedName("fi") val fi: String // https://www.espoo.fi/fi-FI/Asuminen_ja_ymparisto/Kaavoitus/Asemakaava/Asemakaavoituskohteet/Tapiola/SeilimakiLukupuro_240121
    )

    data class LocationExtraInfo(
            @SerializedName("fi") val fi: String // Mankkaan päiväkoti
    )


    data class ShortDescription(
            @SerializedName("fi") val fi: String? // Tervetuloa tapaamaan Seilimäki-Lukupuro kaavahankkeen suunnittelijoita Mankkaan päiväkotiin, Vanhan-Mankkaan kuja 2B tiistaina 23.10.2018 klo 17.30-19.30. Mankkaan päiväkotia laajennetaan ja peruskorjataan.
    )
}

//dataList class MyEventBean(
//        @SerializedName("meta") val meta: Meta,
//        @SerializedName("dataList") val dataList: List<EventBeanData>
//) {
//    dataList class Meta(
//            @SerializedName("count") val count: Int, // 4
//            @SerializedName("next") val next: Any, // null
//            @SerializedName("previous") val previous: Any // null
//    )
//}
//
//dataList class EventBeanData(
//        @SerializedName("`web_id`") val idData: String, // espoo:147718
//        @SerializedName("location") val location: Location,
//        @SerializedName("keywords") val keywords: List<Keyword>,
//        @SerializedName("super_event") val superEvent: Any, // null
//        @SerializedName("event_status") val eventStatus: String, // EventScheduled
//        @SerializedName("external_links") val externalLinks: List<Any>,
//        @SerializedName("offers") val offers: List<Any>,
//        @SerializedName("data_source") val dataSource: String, // espoo
//        @SerializedName("publisher") val publisher: String, // espoo:kaupunki
//        @SerializedName("sub_events") val subEvents: List<Any>,
//        @SerializedName("in_language") val inLanguage: List<Any>,
//        @SerializedName("audience") val audience: List<Audience>,
//        @SerializedName("created_time") val createdTime: String, // 2018-10-16T13:16:02.419705Z
//        @SerializedName("last_modified_time") val lastModifiedTime: String, // 2018-10-16T14:16:18.135492Z
//        @SerializedName("date_published") val datePublished: String, // 2018-10-16T12:22:00Z
//        @SerializedName("start_time") val startTime: String, // 2018-10-23T15:00:00Z
//        @SerializedName("end_time") val endTime: String, // 2018-10-23T16:30:00Z
//        @SerializedName("custom_data") val customData: Any, // null
//        @SerializedName("super_event_type") val superEventType: Any, // null
//        @SerializedName("name") val name: Name,
//        @SerializedName("location_extra_info") val locationExtraInfo: LocationExtraInfo,
//        @SerializedName("provider") val provider: Provider,
//        @SerializedName("short_description") val shortDescription: Any, // null
//        @SerializedName("description") val description: Description,
//        @SerializedName("info_url") val infoUrl: InfoUrl,
//        @SerializedName("@`web_id`") val `web_id`: String, // https://api.hel.fi/linkedevents/v1/event/espoo:147718/
//        @SerializedName("@context") val context: String, // http://schema.org
//        @SerializedName("@type") val type: String, // Event/LinkedEvent
//        @SerializedName("images") val images: List<Any>
//) {
//    dataList class Location(
//            @SerializedName("`web_id`") val idLocation: String, // espoo:23
//            @SerializedName("divisions") val divisions: List<Any>,
//            @SerializedName("created_time") val createdTime: Any, // null
//            @SerializedName("last_modified_time") val lastModifiedTime: String, // 2018-04-26T15:18:43.346804Z
//            @SerializedName("custom_data") val customData: Any, // null
//            @SerializedName("email") val email: Any, // null
//            @SerializedName("contact_type") val contactType: Any, // null
//            @SerializedName("address_region") val addressRegion: Any, // null
//            @SerializedName("postal_code") val postalCode: String,
//            @SerializedName("post_office_box_num") val postOfficeBoxNum: Any, // null
//            @SerializedName("address_country") val addressCountry: Any, // null
//            @SerializedName("deleted") val deleted: Boolean, // false
//            @SerializedName("n_events") val nEvents: Int, // 78
//            @SerializedName("data_source") val dataSource: String, // espoo
//            @SerializedName("image") val image: Any, // null
//            @SerializedName("publisher") val publisher: String, // espoo:kaupunki
//            @SerializedName("parent") val parent: Any, // null
//            @SerializedName("replaced_by") val replacedBy: Any, // null
//            @SerializedName("position") val position: Any, // null
//            @SerializedName("name") val name: Name,
//            @SerializedName("street_address") val streetAddress: StreetAddress,
//            @SerializedName("telephone") val telephone: Any, // null
//            @SerializedName("description") val description: Any, // null
//            @SerializedName("address_locality") val addressLocality: AddressLocality,
//            @SerializedName("info_url") val infoUrl: InfoUrl,
//            @SerializedName("@`web_id`") val `web_id`: String, // https://api.hel.fi/linkedevents/v1/place/espoo:23/
//            @SerializedName("@context") val context: String, // http://schema.org
//            @SerializedName("@type") val type: String // Place
//    ) {
//        dataList class Name(
//                @SerializedName("fi") val fi: String // Lasten kulttuurikeskus Pikku-Aurora
//        )
//
//        dataList class StreetAddress(
//                @SerializedName("fi") val fi: String // J?rvenper?ntie 1-3
//        )
//
//        dataList class AddressLocality(
//                @SerializedName("fi") val fi: String
//        )
//
//        dataList class InfoUrl(
//                @SerializedName("sv") val sv: String,
//                @SerializedName("en") val en: String,
//                @SerializedName("fi") val fi: String // http://www.pikkuaurora.fi
//        )
//
//    }
//
//    dataList class Audience(
//            @SerializedName("@`web_id`") val `web_id`: String // https://api.hel.fi/linkedevents/v1/keyword/yso:p4363/
//    )
//
//    dataList class Description(
//            @SerializedName("fi") val fi: String // <p>Tervetuloa Tilanhoitajantaloon puuhaamaan DIY juttuja - illan aiheena avainnauhat. Pajaan ovat tervetulleita kaiken ik?iset. Lapset oman aikuisen kanssa. Vapaa p??sy!</p><p>Seuraava AuroranILTA on ke 28.11.!</p><p>AuroranILTA on yhdess? tekemist? ja olemista. Illoissa on vaihtuvia sis?lt?j?. Auroran illoissa voit ehdottaa toimintaa, jota voisimme tehd? yhdess? Aurorassa. <a href="https://www.facebook.com/hashtag/yhdess?tehty?source=feed_text&amp;__xts__%5B0%5D=68.ARCxkGjhhcvxPZ1U40nUReKZRcLnSdxZ2UkzgGv1hob8uh6trkrYb8o6-xANLG5RLiKWy1Ovv7QlWiR7wUPMgBv-F4q10XhQs03vqZLJobEUs8e2bYCTzU8DbJf7m9Mx2ecQ_TYjLZw9OVVRBuyJtDT1vmVaXx5JpuS5xxt47YOOXgkw55vqd46YFtGnysGnATnoxivu5k0Vt-9dG8D2TQup&amp;__tn__=%2ANK-R"><u>#yhdess?tehty</u></a></p>
//    )
//
//    dataList class Keyword(
//            @SerializedName("@`web_id`") val `web_id`: String // https://api.hel.fi/linkedevents/v1/keyword/yso:p4363/
//    )
//
//    dataList class Provider(
//            @SerializedName("fi") val fi: String // Lasten kulttuurikeskus Aurora
//    )
//
//    dataList class LocationExtraInfo(
//            @SerializedName("fi") val fi: String // Lasten kulttuurikeskus Aurora, Tilanhoitajantalo
//    )
//
//    dataList class Name(
//            @SerializedName("fi") val fi: String // AuroranILTA: DIY-ilta
//    )
//
//    dataList class InfoUrl(
//            @SerializedName("fi") val fi: String // http://www.lastenkulttuurikeskusaurora.fi
//    )
//}
//
//
//
//
//
//
//
//
//
//
//
