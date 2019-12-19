package com.pss9.rider.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.pss9.rider.service.presenter.PositionPresenter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class GeoLocationHolder(context: Context, private val view: PositionPresenter.View) :
    PositionPresenter {

    private val publish = PublishSubject.create<Location>()
    private val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var active = false

    private var location: Location? = null
    private var listener: PositionUpdateListener = object :
        PositionUpdateListener {
        override fun onPositionChanged(location: Location) {
            publish.onNext(location)
        }
    }

    private val locationListener = object : LocationListener {
        var distance: Double = 0.toDouble()
        var last: Location? = null

        override fun onLocationChanged(location: Location) {
            if (last == null) {
                distance = 0.0
                last = location
            }

            listener.let {
                distance += calculateDistance(last, location)

                last = location
                it.onPositionChanged(location)
                view.updateDistance(distance)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        @SuppressLint("MissingPermission")
        override fun onProviderEnabled(provider: String) {
            active = true
            reset()

            val locationProvider = locationManager.getBestProvider(createFineCriteria(), true)
            locationManager.requestLocationUpdates(locationProvider, 250, 0f, this)
        }

        override fun onProviderDisabled(provider: String) {
            active = false
            reset()
            locationManager.removeUpdates(this)
        }

        private fun reset() {
            last = null
            distance = 0.0
        }
    }

    override fun start() {
        initializeLocationService()
    }

    override fun stop() {
        dispose()
    }

    override fun isActive(): Boolean = active

    fun onPositionChanged(): Observable<Location> = publish

    private fun dispose() {
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        locationManager.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    private fun initializeLocationService() {
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        val locationProvider = locationManager.getBestProvider(createFineCriteria(), true)

        if (!TextUtils.isEmpty(locationProvider) && locationProvider != "passive") {
            locationManager.requestLocationUpdates(locationProvider, 0, 0f, locationListener)
            return
        }

        view.showWarning(
            "Location Error",
            "Unable to find location provider\r\nWould like to enable location settings now?"
        )
    }

    private fun createFineCriteria(): Criteria {

        val criteria = Criteria()

        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isAltitudeRequired = true
        criteria.isBearingRequired = true
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.powerRequirement = Criteria.POWER_HIGH

        return criteria
    }

    private fun calculateDistance(from: Location?, to: Location) =
        from?.let { to.distanceTo(from).toDouble() } ?: 0.toDouble()

    private interface PositionUpdateListener {
        fun onPositionChanged(location: Location)
    }

}
