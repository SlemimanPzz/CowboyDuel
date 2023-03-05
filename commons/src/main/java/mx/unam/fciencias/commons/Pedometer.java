package mx.unam.fciencias.commons;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Pedometer implements SensorEventListener {

    public static final byte STEPS_TO_COUNT = 3;

    private byte steps;


    @NonNull
    private final SensorManager SENSOR_MANAGER;



    @NonNull
    private final ImageView GUN_VIEW;

    public Pedometer(@NonNull SensorManager SENSOR_MANAGER, @NonNull ImageView GUN_VIEW) {
        this.SENSOR_MANAGER = SENSOR_MANAGER;
        this.GUN_VIEW = GUN_VIEW;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        steps++;
        if(steps >= STEPS_TO_COUNT) {
            SENSOR_MANAGER.unregisterListener(this);
            GUN_VIEW.setVisibility(View.VISIBLE);
            steps = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}






}
