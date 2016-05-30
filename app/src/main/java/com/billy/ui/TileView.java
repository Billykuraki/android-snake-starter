
package com.billy.ui;

import android.content.Context;
import android.content.res.Resources;
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

    // mTileArray sDrawable
    private Bitmap[] mTileArray;

    // mTileGrid tile
    private int[][] mTileGrid;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTileSize(context, attrs);
        setBackgroundDrawable(context);
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTileSize(context, attrs);
        setBackgroundDrawable(context);
    }

    private void initTileSize(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
            // if no return default tile size=24
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

    public void loadTile(int key, Drawable tile) {
        //ARGB_8888 each pixel store in 4 bytes
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Config.ARGB_8888);
        //Construct a canvas with the specified bitmap to draw into.
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        //Drawable.draw 畫進此bitmap中
        tile.draw(canvas);
        mTileArray[key] = bitmap;
    }

    public void setTiles(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    public void resetTile(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }
    //Called when the view should render its content.
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                if (mTileGrid[x][y] > 0) {
                    //Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint, transformed by the current matrix.
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x * mTileSize,
                            mYOffset + y * mTileSize, mPaint);
                }
            }
        }
    }

    protected void setBackgroundDrawable(Context context) {
        Resources r = context.getResources();
        setBackgroundDrawable(r.getDrawable(R.drawable.game_background));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Math.floor ±Ë¥h¤p¼Æ ¨ú±oX Y Tile­Ó¼Æ

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
