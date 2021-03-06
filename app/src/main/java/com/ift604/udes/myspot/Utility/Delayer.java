package com.ift604.udes.myspot.Utility;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Squirrel on 2017-11-23.
 */

public class Delayer {

    public static Timer delay(int time, final Runnable run) {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(run);
                    }
                },
                time
        );
        return timer;
    }
}
