
package com.billy.utilily;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.billy.ui.MainActivity;
import com.billy.ui.GameBoardView;

public class SwipeDetector implements View.OnTouchListener {

    private static final String TAG = "SwipeDetector";

    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    private GameBoardView mGameBoardView;

    public SwipeDetector(GameBoardView gameBoard) {
        mGameBoardView = gameBoard;
    }

    public void onRightToLeftSwipe() {
        Log.d(TAG, "Snake move to LEFT!");
        mGameBoardView.moveSnake(MainActivity.MOVE_LEFT);
    }

    public void onLeftToRightSwipe() {
        Log.d(TAG, "Snake move to RIGHT!");
        mGameBoardView.moveSnake(MainActivity.MOVE_RIGHT);
    }

    public void onTopToBottomSwipe() {
        Log.d(TAG, "Snake move to DOWN!");
        mGameBoardView.moveSnake(MainActivity.MOVE_DOWN);
    }

    public void onBottomToTopSwipe() {
        Log.d(TAG, "Snake move to TOP!");
        mGameBoardView.moveSnake(MainActivity.MOVE_UP);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGameBoardView.getGameState() == GameBoardView.RUNNING) {
            switch (event.getAction()) {
            // �����U
                case MotionEvent.ACTION_DOWN: {
                    // ��o�_�l�y���I
                    downX = event.getX();
                    downY = event.getY();
                    return true; // consume ACTION DOWN event
                }  //-------- ACTION DOWN event end 

                // ������}
                case MotionEvent.ACTION_UP: {
                    // v.performClick();

                    // ��o���}�y���I
                    upX = event.getX();
                    upY = event.getY();

                    // delta = �����U - ������}
                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    /*
                     * X��delta�j��Y��delta   �}�l�P�_ swipe horizontal
                     */
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // left to right
                            if (deltaX < 0) {
                                onLeftToRightSwipe();
                                return true; // consume ACTION UP event
                            }

                            // right to left
                            if (deltaX > 0) {
                                onRightToLeftSwipe();
                                return true; // consume ACTION UP event
                            }
                        } else {
                            Log.i(TAG, "Horizontal Swipe was only " + Math.abs(deltaX)
                                    + " long, need at least " + MIN_DISTANCE);
                            return false; // We don't consume the event
                        }
                    }

                    /*
                     * Y��delta�j��X��delta   �}�l�P�_ swipe vertical
                     */
                    else {
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            // top to down
                            if (deltaY < 0) {
                                onTopToBottomSwipe();
                                return true; // consume ACTION UP event
                            }
                            //down to up
                            if (deltaY > 0) {
                                onBottomToTopSwipe();
                                return true; // consume ACTION UP event
                            }
                        } else {
                            Log.i(TAG, "Vertical Swipe was only " + Math.abs(deltaX)
                                    + " long, need at least " + MIN_DISTANCE);
                            return false; // We don't consume the event
                        }
                    }
                    return true; // consume ACTION UP event
                }  // --------ACTION DOWN end
            }
        } else {
            // default moving snake
            mGameBoardView.moveSnake(MainActivity.MOVE_UP);
        }
        return false;
    }
}
