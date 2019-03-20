
package com.billy.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.billy.utility.SoundEffect;
import com.billy.utility.SoundService;

public class GameBoardView extends TileView {

    private final static String TAG = "SnakeView";
    /*
     * current mode of application
     */
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    private int mDirection = NORTH;
    private int mNextDirection = NORTH;

    public static final int NORTH = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static final int EAST = 4;

    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;
    private static final int ANDROID = 4;
    private static final int APPLE = 5;
    private static final int ANDROID_HEAD = 6;
    private static final int BAD_APPLE = 7;

    private long mScore = 0;
    private long mMoveDelay = 300;
    private long mLastMove;

    private TextView mStatusText;
    private TextView mPlayerText;
    private TextView mScoreText;

    private List<Coordinate> mSnakeTrails = new ArrayList<>();
    private List<Coordinate> mAppleList = new ArrayList<>();
    private List<Coordinate> mBadAppleList = new ArrayList<>();

    private static final Random random = new Random();

    private SoundEffect mSoundEffect;
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            update();
            invalidate();
        }

        public void sleep(long delayMillis) {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawableResources();
        initSoundResource(context);
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDrawableResources();
        initSoundResource(context);
    }

    private void initDrawableResources() {
        setFocusable(true);
        resetDrawableArray(8);

        Context c = getContext();
        loadDrawable(RED_STAR, c.getDrawable(R.drawable.redstar));
        loadDrawable(YELLOW_STAR, c.getDrawable(R.drawable.yellowstar));
        loadDrawable(GREEN_STAR, c.getDrawable(R.drawable.greenstar));
        loadDrawable(ANDROID_HEAD, c.getDrawable(R.drawable.androidhead));
        loadDrawable(ANDROID, c.getDrawable(R.drawable.android));
        loadDrawable(APPLE, c.getDrawable(R.drawable.apple));
        loadDrawable(BAD_APPLE, c.getDrawable(R.drawable.badapple));
    }

    private void initSoundResource(Context context) {
        mSoundEffect = new SoundEffect(context);
    }

    private void initNewGame() {
        mSnakeTrails.clear();
        mAppleList.clear();

        for (int i = 5; i >= 2; i--) {
            this.mSnakeTrails.add(new Coordinate(i, 5));
        }

        // snake default direction
        mNextDirection = SOUTH;

        addRandomApple(APPLE);
        addRandomApple(APPLE);
        addRandomApple(BAD_APPLE);
        addRandomApple(BAD_APPLE);

        mMoveDelay = 300;
        mScore = 0;

        Bundle bundle = ((Activity) getContext()).getIntent().getExtras();
        String username = bundle.getString("username");

        mPlayerText.setText(username == null ? "player" : username);
        mScoreText.setText("" + mScore);
    }

    public void moveSnake(int direction) {
        if (direction == MainActivity.MOVE_UP) {
            /*
             * At the beginning of the game, or the end of a previous one,
             * we should start a new game if UP key is clicked.
             */
            if (mMode == READY) {
                initNewGame();
                setMode(RUNNING);
                update();
                return;
            }

            /*
             * If the game is merely paused, we should just continue where
             * we left off.
             */
            if (mMode == PAUSE) {
                setMode(RUNNING);
                update();
                return;
            }
            // new start position.
            if (mDirection != SOUTH) {
                mNextDirection = NORTH;
            }
            return;

        }

        if (direction == MainActivity.MOVE_DOWN && mDirection != NORTH) {
            mNextDirection = SOUTH;
            return;
        }

        if (direction == MainActivity.MOVE_LEFT && mDirection != EAST) {
            mNextDirection = WEST;
            return;
        }

        if (direction == MainActivity.MOVE_RIGHT && mDirection != WEST) {
            mNextDirection = EAST;
        }
    }

    private void enableBackgroundSound(boolean isEnable) {
        Log.d(TAG, "enableSound" + isEnable);
        Context context = getContext();
        Intent intent = new Intent(context, SoundService.class);
        if (isEnable) {
            Log.d(TAG, "start service!!");
            context.startService(intent);
        } else {
            context.stopService(intent);
        }
    }

    public void setDependentViews(TextView textView, TextView playerView, TextView scoreView) {
        mStatusText = textView;
        mPlayerText = playerView;
        mScoreText = scoreView;
    }

    public void setMode(int newMode) {
        int prevMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING && prevMode != RUNNING) {
            mStatusText.setVisibility(View.INVISIBLE);
            update();
            enableBackgroundSound(true);
            return;
        }

        if (newMode == PAUSE) {
            setTextAndVisible(R.string.mode_pause);
            enableBackgroundSound(false);
        }

        if (newMode == READY) {
            setTextAndVisible(R.string.mode_ready);
        }

        if (newMode == LOSE) {
            setTextAndVisible(R.string.mode_lose);
            enableBackgroundSound(false);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // go to rank activity
            showRankActivity();
            mSoundEffect.release();
        }
    }

    private void showRankActivity(){
        Intent intent = new Intent(getContext(), RankingActivity.class);
        intent.putExtra("player", mPlayerText.getText());
        intent.putExtra("score", mScoreText.getText());
        getContext().startActivity(intent);
    }

    public int getGameState() {
        return mMode;
    }

    private void setTextAndVisible(int mode) {
        Resources r = getContext().getResources();
        CharSequence text = r.getText(mode);
        mStatusText.setText(text);
        mStatusText.setVisibility(View.VISIBLE);
    }

    public void update() {
        if (mMode == RUNNING) {
            long now = System.currentTimeMillis();
            if (now - mLastMove > mMoveDelay) {
                clearTiles();
                updateWalls();
                updateApple();
                mLastMove = now;
            }
            mRedrawHandler.sleep(mMoveDelay);
        }
    }

    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTiles(GREEN_STAR, x, 0);
            setTiles(GREEN_STAR, x, mYTileCount - 1);
        }
        for (int y = 1; y < mYTileCount; y++) {
            setTiles(GREEN_STAR, 0, y);
            setTiles(GREEN_STAR, mXTileCount - 1, y);
        }
    }

    private void updateApple() {
        for (Coordinate c : mAppleList) {
            setTiles(APPLE, c.x, c.y);
        }

        for (Coordinate c : mBadAppleList) {
            setTiles(BAD_APPLE, c.x, c.y);
        }
    }

    private void updateSnake() {
        boolean isSnakeGrown = false;

        if(mSnakeTrails.isEmpty()){
            setMode(LOSE);
            return;
        }

        Coordinate head = mSnakeTrails.get(0);
        // default head coordinate
        Coordinate newHead = new Coordinate(1, 1);
        // Coordinate newHead = null;

        mDirection = mNextDirection;
        switch (mDirection) {
            case NORTH:
                newHead = new Coordinate(head.x, head.y - 1);
                break;
            case SOUTH:
                newHead = new Coordinate(head.x, head.y + 1);
                break;
            case WEST:
                newHead = new Coordinate(head.x - 1, head.y);
                break;
            case EAST:
                newHead = new Coordinate(head.x + 1, head.y);
                break;
        }

        if (isCollisionWithBorder(newHead)) {
            mSoundEffect.onLoseGame();
            setMode(LOSE);
            return;
        }

        if (isBodyCollision(newHead)) {
            mSoundEffect.onLoseGame();
            setMode(LOSE);
            return;
        }

        if (isEatAppleAndRemove(newHead)) {
            mSoundEffect.onEatApple();
            addRandomApple(APPLE);
            mScore++;
            mScoreText.setText("" + mScore);
            mMoveDelay *= 0.9;
            isSnakeGrown = true;
        }

        if (isEatBadAppleAndRemove(newHead)) {
            mSoundEffect.onEatBadApple();
            addRandomApple(BAD_APPLE);
            if(!mSnakeTrails.isEmpty()){
                                         
                mSnakeTrails.remove(mSnakeTrails.size() - 1);
            }else{
                setMode(LOSE);
            }
        }


        mSnakeTrails.add(0, newHead);
        if (!isSnakeGrown) {
            mSnakeTrails.remove(mSnakeTrails.size() - 1);
        }
        refreshSnake();
    }

    private boolean isCollisionWithBorder(Coordinate head) {
        return head.x < 1 || head.y < 1 || head.x > mXTileCount - 2 || head.y > mYTileCount - 2;
    }

    private void addRandomApple(int which) {
        int newX = 1 + random.nextInt(mXTileCount - 2);
        int newY = 1 + random.nextInt(mYTileCount - 2);

        Coordinate applePos = new Coordinate(newX, newY);
        // check if position already been occupied.
        while (isBodyCollision(applePos)) {
            applePos.x = 1 + random.nextInt(mXTileCount - 2);
            applePos.y = 1 + random.nextInt(mYTileCount - 2);
        }

        switch (which) {
            case APPLE:
                mAppleList.add(applePos);
                break;
            case BAD_APPLE:
                mBadAppleList.add(applePos);
                break;
        }

    }

    public Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putIntArray("mSnakeTrails", coordinateListToArray(mSnakeTrails));
        bundle.putIntArray("mAppleList", coordinateListToArray(mAppleList));
        bundle.putInt("mDirection", mDirection);
        bundle.putInt("mNextDirection", mNextDirection);
        bundle.putLong("mMoveDelay", mMoveDelay);
        bundle.putLong("mScore", mScore);
        return bundle;
    }

    public void restoreState(Bundle bundle) {
        setMode(PAUSE);

        mSnakeTrails = coordinateArrayToList(bundle.getIntArray("mSnakeTrails"));
        mAppleList = coordinateArrayToList(bundle.getIntArray("mAppleList"));
        mDirection = bundle.getInt("mDirection");
        mNextDirection = bundle.getInt("mNextDirection");
        mMoveDelay = bundle.getLong("mMoveDelay");
        mScore = bundle.getLong("mScore");
    }

    private List<Coordinate> coordinateArrayToList(int[] coordArray) {

        List<Coordinate> Coordinates = new ArrayList<>();
        for (int i = 0; i < coordArray.length; i += 2) {
            Coordinate coordinate = new Coordinate(coordArray[i], coordArray[i + 1]);
            Coordinates.add(coordinate);
        }
        return Coordinates;
    }

    private int[] coordinateListToArray(List<Coordinate> coordArray) {
        int[] primitiveArray = new int[coordArray.size() * 2];
        int i = 0;
        for (Coordinate c : coordArray) {
            primitiveArray[i++] = c.x;
            primitiveArray[i++] = c.y;
        }
        return primitiveArray;
    }

    private boolean isEatAppleAndRemove(Coordinate head) {

        for (Coordinate apple : mAppleList) {
            if (apple.equals(head)) {
                mAppleList.remove(apple);
                return true;
            }
        }
        return false;
    }

    private boolean isEatBadAppleAndRemove(Coordinate head) {
        for (Coordinate BadApple : mBadAppleList) {
            if (BadApple.equals(head)) {
                mBadAppleList.remove(BadApple);
                return true;
            }
        }
        return false;

    }

    private boolean isBodyCollision(Coordinate head) {
        for (int x = 0; x < mSnakeTrails.size(); x++) {
            if (mSnakeTrails.get(x).equals(head))
                return true;
        }
        return false;
    }

    private void refreshSnake() {
        int index = 0;
        for (Coordinate c : mSnakeTrails) {
            if (index == 0) {
                setTiles(ANDROID_HEAD, c.x, c.y);
            } else {
                setTiles(ANDROID, c.x, c.y);
            }
            index++;
        }
    }

    private class Coordinate {
        int x;
        int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean equals(Coordinate other) {
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "coordinate[" + this.x + "," + this.y + "]";
        }
    }
}
