package com.pss9.rider.ride

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.pss9.rider.R
import com.pss9.rider.service.GeoLocationHolder
import com.pss9.rider.service.presenter.PositionPresenter

class MapsFragment : Fragment(), OnMapReadyCallback, PositionPresenter.View {

    private var last: LatLng? = null
    private var location: PositionPresenter? = null

    private val maker = MarkerOptions()
    private val points = mutableListOf<LatLng>()

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_maps, container, false)


    override fun onResume() {
        super.onResume()

        (childFragmentManager.findFragmentById(R.id.view_google_map) as SupportMapFragment).getMapAsync(
            this
        )
    }

    override fun onPause() {
        super.onPause()

        location?.stop()
        location = null

        persistZoomLevel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        location = GeoLocationHolder(context!!, this).apply {
            start()
        }

        restorePreviousZoomLevel()
    }

    override fun updateDistance(distance: Double) {

    }

    override fun updatePosition(location: Location) {
        val position = LatLng(location.latitude, location.longitude)

        points.add(position)

        last?.let {
            val distance = location.distanceTo(Location(LocationManager.GPS_PROVIDER).apply {
                latitude = it.latitude
                longitude = it.longitude
            })

            // if distance is under 5 meters do not attempt to re-position the map
            if (distance <= 5) {
                return
            }
        }

        last = position

        map.apply {
            clear()
            addMarker(maker.apply { position(position) })
            addPolyline(PolylineOptions().addAll(points).color(Color.GREEN))
            animateCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        position.latitude + offset(),
                        position.longitude
                    )
                )
            )
        }
    }

    override fun showWarning(title: String, message: String) {
        Toast.makeText(context!!, "$title: $message", Toast.LENGTH_LONG).show()
    }

    private fun persistZoomLevel() {
        val z = map.cameraPosition.zoom.toInt().toString()
        val pref = context!!.getSharedPreferences(
            context!!.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        pref.edit().putString(context!!.getString(R.string.zoom_level_key), z).apply()
    }

    private fun restorePreviousZoomLevel() {
        val pref = context!!.getSharedPreferences(
            context!!.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        val z = pref.getString(
            context!!.getString(R.string.zoom_level_key),
            context!!.getString(R.string.map_zoom_level_16)
        )!!.toFloat()

        // experiment result:
        // requires at least a 3 ~ 5 sec delay for properly restoring the zoom level without an issue
        Handler().postDelayed({
            map.moveCamera(CameraUpdateFactory.zoomTo(z))
            Log.wtf("Zoom", "Recovering Zoom Level: $z")
        }, 3000)
    }

    private fun offset(): Double {
        val z = map.cameraPosition.zoom
        val rate = if (z < 16f) {
            z / 16f
        } else {
            0.0f
        }
        return 0.004 * rate
    }
}


