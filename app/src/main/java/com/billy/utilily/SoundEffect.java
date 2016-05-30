
package com.billy.utilily;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.billy.ui.R;

public class SoundEffect {

    private static final String TAG = "SoundEffect";

    private SoundPool mSoundPool;

    private int[] mSoundId;

    public SoundEffect(Context context) {
        initSound(context);
    }

    private void initSound(Context context) {
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundId = new int[4];
        mSoundId[0] = mSoundPool.load(context, R.raw.angry_birds_bird_fly_sound_effect, 1);
        mSoundId[1] = mSoundPool.load(context, R.raw.angry_birds_rio_bird_noise_sound, 1);
        mSoundId[2] = mSoundPool.load(context, R.raw.angry_birds_red_bird_yell_sound_effect, 1);
        mSoundId[3] = mSoundPool.load(context, R.raw.angry_birds_red_bird_yelling_sound_effect, 1);
    }

    public void onButtonClick() {
        if (mSoundId[0] != 0) {
            mSoundPool.play(mSoundId[0], 1.0f, 1.0f, 1, 0, 1.0f);
        } else {
            Log.d(TAG, "Load Sound Click fail!!");
        }
    }

    public void onEatApple() {
        if (mSoundId[1] != 0) {
            mSoundPool.play(mSoundId[1], 1.0f, 1.0f, 1, 0, 1.0f);
        } else {
            Log.d(TAG, "Load Sound eat Apple fail!!");
        }

    }
    
  

    public void onLoseGame() {
        if (mSoundId[2] != 0) {
            mSoundPool.play(mSoundId[2], 1.0f, 1.0f, 1, 0, 1.0f);
        } else {
            Log.d(TAG, "Load Sound LoseGame fail!!");
        }

    }
    
    
    public void onEatBadApple() {
        if (mSoundId[3] != 0) {
            mSoundPool.play(mSoundId[3], 1.0f, 1.0f, 1, 0, 1.0f);
        } else {
            Log.d(TAG, "Load Sound eat Apple fail!!");
        }
    }
    
    public void release() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }
}
