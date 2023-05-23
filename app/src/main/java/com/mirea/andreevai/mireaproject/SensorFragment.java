package com.mirea.andreevai.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mirea.andreevai.mireaproject.databinding.FragmentSensorBinding;

public class SensorFragment extends Fragment implements SensorEventListener {
    private FragmentSensorBinding binding;
    private SensorManager sensorManager;
    private Sensor tempSensor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SensorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sensore.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance(String param1, String param2) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSensorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            binding.tvTemp.setText("Температура:" + Float.toString(temperature) + "°C");
            if (temperature > 50) {
                binding.tvResult.setText("Cейчас вы станите поджаркой!");
            } else if (temperature > 25) {
                binding.tvResult.setText("Надо бы охладиться!");
            } else if (temperature >= 20) {
                binding.tvResult.setText("Отличная погода для прогулок!");
            } else if (temperature >= 10 && temperature < 20) {
                binding.tvResult.setText("Приятная погода.");
            } else if (temperature >= 0 && temperature < 10){
                binding.tvResult.setText("Как-то прохладненько.");
            }
            else if (temperature >= -20 && temperature < 0){
                binding.tvResult.setText("Может лучше остаться дома?)");
            }
            else if (temperature < -20 ) {
                binding.tvResult.setText("Есть кто живой?");
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}