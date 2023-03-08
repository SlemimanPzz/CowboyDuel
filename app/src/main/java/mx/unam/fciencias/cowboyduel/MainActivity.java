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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView gunView;
    private Button botonInicio;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private byte step;
    public static final byte SECONDS_TO_COUNT = 3;
    private ExecutorService singleThreadProducer;
    private DrawTimer asyncCounter;

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
        }

    }

    @Override
    protected void onPause() {

        killCounter();

        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //implementar init();
        //init();
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
        // Agregar funcion de contar pasos
        checkSteps();
    }

    public void fire(View gun) {
        Worker.doWork();

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

    private void checkSteps(){

        if(sensorManager == null){
            statertTime();
            return;
        }

        sensorManager.registerListener(this, stepDetectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void statertTime(){

        if(singleThreadProducer == null){
            singleThreadProducer = Executors.newSingleThreadExecutor();
        }

        asyncCounter = new DrawTimer(gunView, SECONDS_TO_COUNT);
        singleThreadProducer.execute(asyncCounter);

    }


    private void killCounter(){

        if(sensorManager != null){

            sensorManager.unregisterListener(this);

        }else if(singleThreadProducer != null){

            singleThreadProducer.shutdown();
            singleThreadProducer = null;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}