package idk.metropolia.fi.myapplication.adapter

data class Coordinate(val longitude: Double, val latitude: Double)
data class Geometry(val type: String, val coordinates: List<Double>){}
data class LocationProperties(val id: String, val gid: String, val layer: String, val source: String, val name: String, val postalcode: String?, val country: String, val region: String, val  localadmin: String?, val locality: String?, val label: String){}
data class Location(val name: String? = null, val description: String? = null, val lon: Float? = null, val lat: Float? = null,
                    val type: String? = null, val geometry: Geometry? = null, val properties: LocationProperties? = null) {}

data class LocationSearchResult(val type: String, val features: List<Location>) {}