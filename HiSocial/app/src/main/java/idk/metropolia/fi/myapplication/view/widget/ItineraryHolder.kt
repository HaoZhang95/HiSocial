package idk.metropolia.fi.myapplication.view.widget

object ItineraryHolder {
    private var itinerary: ItineraryPlanQuery.Itinerary? = null

    fun set(itinerary: ItineraryPlanQuery.Itinerary) { ItineraryHolder.itinerary = itinerary }

    fun get() = itinerary
}