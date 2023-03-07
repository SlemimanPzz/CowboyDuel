package mx.unam.fciencias.cowboyduel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView gunView;
    private Button botonInicio;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonInicio = findViewById(R.id.boton_inicio);
    }

    /*
    @Override
    protected void onPause() {
        if (sensorManager != null){
            sensorManager.unregisterListener(this);
        }
        super.onPause();
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        //implementar init();
        //init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
        botonInicio.setVisibility(View.INVISIBLE);
        // Agregar funcion de contar pasos
    }

}