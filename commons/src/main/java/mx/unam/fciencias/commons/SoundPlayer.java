package mx.unam.fciencias.commons;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class SoundPlayer extends Worker {

    // public static final String ACTION_FIRE = "mx.nachintoch.firstappexample.action.FIRE";


    public SoundPlayer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;

    }
}

