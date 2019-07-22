package com.pss9.rider.ride

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pss9.rider.R
import com.pss9.rider.RiderAidApplication
import com.pss9.rider.ant.AntBikeDevice
import com.pss9.rider.presenter.PositionPresenter
import com.pss9.rider.presenter.PresenterService
import com.pss9.rider.presenter.TelemetryPresenter
import com.pss9.rider.presenter.TimePresenter
import com.pss9.rider.ride.module.Position
import com.pss9.rider.ride.module.TeleSensor
import com.pss9.rider.ride.module.Tick
import com.pss9.rider.util.UnitUtils
import com.pss9.rider.util.WindowsUtils
import java.text.SimpleDateFormat
import java.util.*

class Telemetry : Fragment(), TimePresenter.View, TelemetryPresenter.View, PositionPresenter.View {
    private lateinit var tvTimer: TextView
    private lateinit var tvCadence: TextView
    private lateinit var tvSpeed: TextView
    private lateinit var tvDistance: TextView

    private lateinit var tvDistanceUnit: TextView
    private lateinit var tvSpeedUnit: TextView

    private var presenters = mutableListOf<PresenterService>()
    private var formatter: SimpleDateFormat =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }

    private val device: AntBikeDevice = RiderAidApplication.ant

    private var unit = 0
    private val pref by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenters.addAll(listOf(Tick(this), TeleSensor(this, device), Position(context!!, this)))

        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.telemetry_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTimer = view.findViewById(R.id.tv_time_value)
        tvCadence = view.findViewById(R.id.tv_cad_value)
        tvSpeed = view.findViewById(R.id.tv_spd_value)
        tvDistance = view.findViewById(R.id.tv_dist_value)

        tvDistanceUnit = view.findViewById(R.id.tv_dist_unit)
        tvSpeedUnit = view.findViewById(R.id.tv_spd_unit)
    }

    override fun onResume() {
        super.onResume()
        setDisplayUnits()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val p = presenters.first()

        menu.findItem(R.id.action_record).isVisible = !p.isActive()
        menu.findItem(R.id.action_stop).isVisible = p.isActive()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_record, R.id.action_stop -> {
                for (p in presenters) {
                    p.start()
                }

                WindowsUtils.lockScreenDim(activity!!.window, item.itemId == R.id.action_record)
                activity!!.invalidateOptionsMenu()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun updateTime(second: Long) {
        tvTimer.text = formatter.format(Date(second * 1000))
    }

    override fun updateCadence(cad: Long) {
        tvCadence.text = cad.toString()
    }

    override fun updateSpeed(spd: Long) {
        tvSpeed.text = UnitUtils.convertCalcSpeed(spd, unit).toString()
    }

    override fun updateDistance(distance: Double) {
        tvDistance.text = UnitUtils.convertDistance(distance, unit).toString()
    }

    private fun setDisplayUnits() {

        val str = pref.getString(getString(R.string.unit_type_key), getString(R.string.unit_type_default))
        unit = if (str == getString(R.string.unit_speed_mph)) {
            R.string.unit_speed_mph
        } else {
            R.string.unit_speed_kmh
        }

        if (unit == R.string.unit_speed_mph) {
            tvDistanceUnit.text = getString(R.string.unit_mile)
            tvSpeedUnit.text = getString(R.string.unit_speed_mph)
        } else {
            tvDistanceUnit.text = getString(R.string.unit_km)
            tvSpeedUnit.text = getString(R.string.unit_speed_kmh)
        }
    }
}
