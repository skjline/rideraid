package com.pss9.rider.ride

import android.graphics.Color
import android.location.Location
import android.os.Bundle
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

    private var location: PositionPresenter? = null

    private var zoom = 15f
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        location = GeoLocationHolder(context!!, this).apply {
            start()
        }

        map.setOnCameraMoveListener {
            zoom = map.cameraPosition.zoom
            Log.wtf("CameraMoved", "Zoom Level: ${map.cameraPosition.zoom}")
        }

    }

    override fun updateDistance(distance: Double) {
//        Toast.makeText(context!!, "Distance: $distance", Toast.LENGTH_LONG).show()
    }

    override fun updatePosition(location: Location) {
        val position = LatLng(location.latitude, location.longitude)
        points.add(position)

        map.apply {
            clear()
            addMarker(maker.apply { position(position) })
            addPolyline(PolylineOptions().addAll(points).color(Color.GREEN))
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        position.latitude + offset(),
                        position.longitude
                    ), zoom
                )
            )
        }
    }

    override fun showWarning(title: String, message: String) {
        Toast.makeText(context!!, "$title: $message", Toast.LENGTH_LONG).show()
    }

    private fun offset(): Double {

        val rate = if (zoom < 16f) {
            zoom / 16f
        } else {
            0.0f
        }
        return 0.004 * rate
    }
}


