
package com.billy.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.billy.utility.SwipeDetector;

public class MainActivity extends Activity {

    private static final String TAG = "Snake";

    public static int MOVE_LEFT = 0;
    public static int MOVE_UP = 1;
    public static int MOVE_DOWN = 2;
    public static int MOVE_RIGHT = 3;

    private GameBoardView mGameBoardView;

    private static String KEY = "snake-view";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initGameBoard();
        // first time start
        if (icicle == null) {
            mGameBoardView.setMode(GameBoardView.READY);
        } else {
            // restore game state
            Bundle bundle = icicle.getBundle(KEY);
            if (bundle != null) {
                mGameBoardView.restoreState(bundle);
            } else {
                mGameBoardView.setMode(GameBoardView.PAUSE);
            }
        }
    }

    private void initGameBoard() {
        setContentView(R.layout.layout_main_activity);
        mGameBoardView = findViewById(R.id.snake);
        mGameBoardView.setDependentViews(
                (TextView) findViewById(R.id.text),
                (TextView) findViewById(R.id.user),
                (TextView) findViewById(R.id.score));
        mGameBoardView.setOnTouchListener(new SwipeDetector(mGameBoardView));
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        icicle.putBundle(KEY, mGameBoardView.saveState());
    }

    @Override
    public void onBackPressed() {
        // do nothing, in case user back to main activity
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                mGameBoardView.moveSnake(MOVE_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mGameBoardView.moveSnake(MOVE_RIGHT);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mGameBoardView.moveSnake(MOVE_DOWN);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mGameBoardView.moveSnake(MOVE_LEFT);
                break;
        }
        return super.onKeyDown(keyCode, msg);
    }
}
