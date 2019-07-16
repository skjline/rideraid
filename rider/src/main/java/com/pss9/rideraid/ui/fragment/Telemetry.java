package com.pss9.rideraid.ui.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.pss9.rideraid.R;
import com.pss9.rideraid.RiderAidApplication;
import com.pss9.rideraid.ant.AntBikeDevice;
import com.pss9.rideraid.presenter.PositionPresenter;
import com.pss9.rideraid.presenter.PresenterActions;
import com.pss9.rideraid.presenter.TelemetryPresenter;
import com.pss9.rideraid.presenter.TimePresenter;
import com.pss9.rideraid.presenter.module.Position;
import com.pss9.rideraid.presenter.module.TeleSensor;
import com.pss9.rideraid.presenter.module.Tick;
import com.pss9.rideraid.util.UnitUtils;
import com.pss9.rideraid.util.WindowsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Telemetry extends Fragment
        implements TimePresenter.View, TelemetryPresenter.View, PositionPresenter.View {
    @BindView(R.id.tv_time_value)
    TextView tvTimer;
    @BindView(R.id.tv_cad_value)
    TextView tvCadence;
    @BindView(R.id.tv_spd_value)
    TextView tvSpeed;
    @BindView(R.id.tv_dist_value)
    TextView tvDistance;

    @BindView(R.id.tv_dist_unit)
    TextView tvDistanceUnit;
    @BindView(R.id.tv_spd_unit)
    TextView tvSpeedUnit;

    Unbinder unbinder;

    private PresenterActions[] presenters;
    private SimpleDateFormat formatter;

    AntBikeDevice device = RiderAidApplication.ant;

    private int unit;

    @SuppressLint("SimpleDateFormat")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Suppressing simple data format since the locale doesn't matter
        formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        presenters = new PresenterActions[]{
                new Tick(this),
                new TeleSensor(this, device),
                new Position(getContext(), this)};

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        Log.d(getClass().getSimpleName(), "Register shared preference listener");
        pref.registerOnSharedPreferenceChangeListener(listener);

        String str = pref.getString(getString(R.string.unit_type_key), getString(R.string.unit_type_default));
        unit = str.equals(getString(R.string.unit_speed_mph)) ? R.string.unit_speed_mph : R.string.unit_speed_kmh;

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telemetry_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        setDisplayUnits(unit);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(getClass().getSimpleName(), "Unregister shared preference listener");
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

        PresenterActions p = presenters[0];
        menu.findItem(R.id.action_record).setVisible(!p.isActive());
        menu.findItem(R.id.action_stop).setVisible(p.isActive());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_record:
                for (PresenterActions p : presenters) {
                    p.start();
                }

                WindowsUtils.lockScreenDim(getActivity().getWindow(), true);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_stop:
                for (PresenterActions p : presenters) {
                    p.stop();
                }

                WindowsUtils.lockScreenDim(getActivity().getWindow(), false);
                getActivity().invalidateOptionsMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTime(long time) {
        tvTimer.setText(formatter.format(new Date(time * 1000)));
    }

    @Override
    public void updateCadence(long cad) {
        tvCadence.setText(String.valueOf(cad));
    }

    @Override
    public void updateSpeed(long spd) {
        tvSpeed.setText(String.valueOf(UnitUtils.convertCalcSpeed(spd, unit)));
    }

    @Override
    public void updateDistance(double distance) {
        tvDistance.setText(String.valueOf(UnitUtils.convertDistance(distance, unit)));
    }

    private void setDisplayUnits(int selection) {
        if (selection == R.string.unit_speed_mph) {
            tvDistanceUnit.setText(getString(R.string.unit_mile));
            tvSpeedUnit.setText(getString(R.string.unit_speed_mph));
            return;
        }

        tvDistanceUnit.setText(getString(R.string.unit_km));
        tvSpeedUnit.setText(getString(R.string.unit_speed_kmh));
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
        if (key.equals(getString(R.string.unit_type_key))) {
            String str = sharedPreferences.getString(getString(R.string.unit_type_key), getString(R.string.unit_type_default));
            unit = str.equals(getString(R.string.unit_speed_mph)) ? R.string.unit_speed_mph : R.string.unit_speed_kmh;
            setDisplayUnits(unit);
        }
    };
}
