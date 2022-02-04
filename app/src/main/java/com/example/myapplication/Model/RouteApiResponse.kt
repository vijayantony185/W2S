package com.example.myapplication.Model

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class GeocodedWaypoint {
    var geocoder_status: String? = null
    var place_id: String? = null
    var types: List<String>? = null
}

class Northeast {
    var lat = 0.0
    var lng = 0.0
}

class Southwest {
    var lat = 0.0
    var lng = 0.0
}

class Bounds {
    var northeast: Northeast? = null
    var southwest: Southwest? = null
}

class Distance {
    var text: String? = null
    var value = 0
}

class Duration {
    var text: String? = null
    var value = 0
}

class EndLocation {
    var lat = 0.0
    var lng = 0.0
}

class StartLocation {
    var lat = 0.0
    var lng = 0.0
}

class Polyline {
    var points: String? = null
}

class Step {
    var distance: Distance? = null
    var duration: Duration? = null
    var end_location: EndLocation? = null
    var html_instructions: String? = null
    var polyline: Polyline? = null
    var start_location: StartLocation? = null
    var travel_mode: String? = null
    var maneuver: String? = null
}

class Leg {
    var distance: Distance? = null
    var duration: Duration? = null
    var end_address: String? = null
    var end_location: EndLocation? = null
    var start_address: String? = null
    var start_location: StartLocation? = null
    var steps: List<Step>? = null
    var traffic_speed_entry: List<Any>? = null
    var via_waypoint: List<Any>? = null
}

class OverviewPolyline {
    var points: String? = null
}

class Route {
    var bounds: Bounds? = null
    var copyrights: String? = null
    var legs: List<Leg>? = null
    var overview_polyline: OverviewPolyline? = null
    var summary: String? = null
    var warnings: List<Any>? = null
    var waypoint_order: List<Any>? = null
}

class RouteApiResponse {
    var geocoded_waypoints: List<GeocodedWaypoint>? = null
    var routes: List<Route>? = null
    var status: String? = null
}
