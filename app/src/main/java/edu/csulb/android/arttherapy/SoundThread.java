package edu.csulb.android.arttherapy;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by KEYUR on 21-03-2017.
 * Time: 19:49
 * Project: ArtTherapy
 */

class SoundThread extends Thread {
    private String name;
    private Context context;

    SoundThread(String name, Context context) {
        this.name = name;
        this.context = context;
        start();
    }

    @Override
    public void run() {
        MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.eraser);
        mPlayer.start();
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
