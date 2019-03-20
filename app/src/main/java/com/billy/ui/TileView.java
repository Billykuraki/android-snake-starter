
package com.billy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TileView extends View {

    private static final String TAG = "TileView";

    protected static int mTileSize;
    protected static int mXTileCount;
    protected static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;

    //Create a new paint with default settings.
    private final Paint mPaint = new Paint();

    // mDrawables sDrawable
    private Bitmap[] mDrawables;

    // mTileGrid tile
    private int[][] mTileGrid;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTileSize(context, attrs);
        setBackground(context.getDrawable(R.drawable.game_background));
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTileSize(context, attrs);
        setBackground(context.getDrawable(R.drawable.game_background));
    }

    private void initTileSize(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
            mTileSize = a.getDimensionPixelSize(R.styleable.TileView_tileSize, 40);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    // reset all tiles to zero
    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTiles(0, x, y);
            }
        }
    }

    public void loadDrawable(int key, Drawable d) {
        //ARGB_8888 each pixel store in 4 bytes
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Config.ARGB_8888);
        //Construct a canvas with the specified bitmap to draw into.
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, mTileSize, mTileSize);
        d.draw(canvas);
        mDrawables[key] = bitmap;
    }

    public void setTiles(int tileIndex, int x, int y) {
        mTileGrid[x][y] = tileIndex;
    }

    public void resetDrawableArray(int tileCount) {
        mDrawables = new Bitmap[tileCount];
    }
    //Called when the view should render its content.
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                if (mTileGrid[x][y] > 0) {
                    //Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint, transformed by the current matrix.
                    canvas.drawBitmap(mDrawables[mTileGrid[x][y]], mXOffset + x * mTileSize,
                            mYOffset + y * mTileSize, mPaint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        Log.d(TAG, "onSizeChanged!!!!");
        Log.d(TAG, "width=" + w + ",height=" + h);
        mXTileCount = (int) Math.floor(w / mTileSize);
        mYTileCount = (int) Math.floor(h / mTileSize);
        Log.d(TAG, "mTileSize=" + mTileSize);
        Log.d(TAG, "X:" + mXTileCount + ",Y:" + mYTileCount);

        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);
        Log.d(TAG, "Xoffset=" + mXOffset);
        Log.d(TAG, "Yoffset=" + mYOffset);
        mTileGrid = new int[mXTileCount][mYTileCount];
        clearTiles();
    }
}
