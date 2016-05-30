
package com.billy.utilily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlayerDbAdapter {

    private static final String TAG = "PlayerDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_SCORE = "score";

    private static final String DATABASE_NAME = "rank";
    private static final String DATABASE_TABLE = "record";
    private static final int DATABASE_VERSION = 1;

    /*
     * SQL statement create
     */
    private static final String DATABASE_CREATE =
            "CREATE TABLE record (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player TEXT NOT NULL," +
                    "score TEXT NOT NULL); ";

    private final Context mContext;

    /*
     * constructor, context use to create DB;
     */
    public PlayerDbAdapter(Context context) {
        this.mContext = context;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS record");
            onCreate(db);
        }
    }

    /*
     * Open the database. If it cannot be opened, try to create a new instance
     * of the database. If it cannot be created, throw an exception to signal
     * the failure
     */

    public PlayerDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /*
     * CRUD implement below create read update delete
     */

    public long InsertRecord(String player, String score) {
        ContentValues recordValues = new ContentValues();
        recordValues.put(KEY_PLAYER, player);
        recordValues.put(KEY_SCORE, score);
        return mDb.insert(DATABASE_TABLE, null, recordValues);
    }

    /*
     * fetchRecord By Id
     */

    public Cursor fectchRecord(long rowId) throws SQLException {

        String[] columns = {
                KEY_ROW_ID, KEY_PLAYER, KEY_SCORE
        };

        Cursor mCursor = mDb.query(
                DATABASE_TABLE,
                columns,
                KEY_ROW_ID + "=" + rowId,
                null,
                null,
                null,
                null,
                null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*
     * fetch ALL Record in DESC order
     */

    public Cursor fetchAllRecordByDESC() {

        String[] columns = {
                KEY_ROW_ID, KEY_PLAYER, KEY_SCORE
        };

        String ORDER_BY_SCORE = KEY_SCORE + " DESC";

        return mDb.query(DATABASE_TABLE, columns, null, null, null, null, ORDER_BY_SCORE);
    }

    /*
     * update Record by Id
     */

    public boolean updateRecord(long rowId, String player, int score) {
        ContentValues recordValues = new ContentValues();
        /*
         * KEY_PLAYER, player ���a KEY_SCORE, score ����
         */
        recordValues.put(KEY_PLAYER, player);
        recordValues.put(KEY_SCORE, score);

        /*
         * Returns the number of rows affected
         */
        return mDb.update(DATABASE_TABLE, recordValues, KEY_ROW_ID + "=" + rowId, null) > 0;
    }

    /*
     * delete Record by Id
     */

    public boolean deleteRecord(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROW_ID + "-" + rowId, null) > 0;
    }

}
