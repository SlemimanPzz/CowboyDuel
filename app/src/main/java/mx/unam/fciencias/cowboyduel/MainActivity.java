package mx.unam.fciencias.cowboyduel;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mx.unam.fciencias.commons.Pedometer;
import mx.unam.fciencias.commons.SoundPlayer;
import mx.unam.fciencias.commons.Timer;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private Pedometer pedometer;
    private Timer asyncCounter;
    private ExecutorService singleThreadProducer;
    private ImageView gunView;
    private Button botonInicio;
    private byte step;
    public static final byte SECONDS_TO_COUNT = 3;

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonInicio = findViewById(R.id.boton_inicio);
        gunView = findViewById(R.id.gun_iv);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager != null){
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

        if(stepDetectorSensor == null){
            sensorManager = null;
        } else {
            pedometer = new Pedometer(sensorManager, gunView);
        }

    }

    @Override
    protected void onPause() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        killCounter();
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void init(){
        gunView.setVisibility(View.INVISIBLE);
        botonInicio.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            if (!checkActivityRecognitionPermission()){
                ActivityCompat.requestPermissions(
            this,
                       new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
         0);
            }
        }
    }

    @TargetApi(29)
    private boolean checkActivityRecognitionPermission(){
        return ContextCompat.checkSelfPermission(
this,
          Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
    }

    public void countdown(View botonInicio){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        botonInicio.setVisibility(View.INVISIBLE);
        checkSteps();
    }

    public void fire(View gun) {

        WorkRequest myWorkRequest = OneTimeWorkRequest.from(SoundPlayer.class);
        WorkManager.getInstance(this).enqueue(myWorkRequest);
        gun.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 3000);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Aumentando los pasos
        step++;

        if(step >= 3){
            sensorManager.unregisterListener(this);
            //Mostrando el arma
            gunView.setVisibility(View.VISIBLE);

            //Reinciando contador
            step = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void checkSteps(){

        if(sensorManager == null){
            startTimer();
            return;
        }

        sensorManager.registerListener(pedometer, stepDetectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void startTimer(){

        if(singleThreadProducer == null){
            singleThreadProducer = Executors.newSingleThreadExecutor();
        }

        asyncCounter = new Timer(gunView, SECONDS_TO_COUNT);
        singleThreadProducer.execute(asyncCounter);

    }


    private void killCounter(){

        if(sensorManager != null){
            sensorManager.unregisterListener(this);
        } else if (singleThreadProducer != null){
            singleThreadProducer.shutdown();
            singleThreadProducer = null;
        }

    }

}