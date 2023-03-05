package mx.unam.fciencias.commons;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


/**
 * Plays the fireing sound.
 *
 * Here how to use worker:
 *
 * <a href="url">https://developer.android.com/topic/libraries/architecture/workmanager/advanced/worker?hl=es-419</a>
 */
public class SoundPlayer extends Worker {

    public SoundPlayer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.fire);
        try {
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            Log.d(SoundPlayer.class.getSimpleName(), "Firing Sound Failed");
            return Result.failure();
        }
        Log.d(SoundPlayer.class.getSimpleName(), "Freeing sound success");
        return Result.success();

    }
}

