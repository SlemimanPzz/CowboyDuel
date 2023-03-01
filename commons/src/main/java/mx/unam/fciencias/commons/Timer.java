package mx.unam.fciencias.commons;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Timer implements Runnable {

    private boolean isCanceled;
    private final ImageView WEAPON;
    private final short SECONDS_TO_COUNT;

    public Timer(ImageView WEAPON, short SECONDS_TO_COUNT) {
        if(SECONDS_TO_COUNT < 1 ){
            throw new IllegalArgumentException("Need more time on counter");
        }
        this.WEAPON = WEAPON;
        this.SECONDS_TO_COUNT = SECONDS_TO_COUNT;
    }


    public void cancel(){
        isCanceled = true;
        WEAPON.post(()-> WEAPON.setVisibility(View.INVISIBLE));
    }


    @Override
    public void run() {
        short time = 0;
        while (time < SECONDS_TO_COUNT){
            if(isCanceled) return;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                Log.d(Timer.class.getSimpleName(),"Thread Interrpted on counter");
            }
            time++;
            WEAPON.post(() -> WEAPON.setVisibility(View.VISIBLE));
        }
    }
}
