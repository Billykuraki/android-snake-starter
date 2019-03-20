
package com.billy.utility;


import com.billy.ui.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class SoundService extends Service  {

    public static final String TAG = "Service";

    private MediaPlayer mMediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent) {
        // not allow to bind
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "create service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "init Player and prepare");
        initMediaPlayer();
        mMediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(this,R.raw.angry_birds_theme_song);
        mMediaPlayer.setLooping(true);
        //mMediaPlayer.setVolume(1.0f, 1.0f);      
        //mMediaPlayer.prepareAsync(); // prepare async to not block main thread;
             
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }
}
