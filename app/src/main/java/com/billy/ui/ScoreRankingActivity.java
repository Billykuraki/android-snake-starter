
package com.billy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.billy.utility.PlayerDbAdapter;

public class ScoreRankingActivity extends ListActivity {

    private PlayerDbAdapter mDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.ranklist_layout);

        mDbAdapter = new PlayerDbAdapter(this);
        mDbAdapter.open();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String player = (String) bundle.get("player");
            String score = String.valueOf(bundle.get("score"));
            insertNewRecord(player, score);
        }
        // fill Data into List
        fillData();
    }

    private void insertNewRecord(String player, String score) {
        mDbAdapter.InsertRecord(player, score);
    }

    private void fillData() {
        // get all the row from database

        Cursor mRecordCursor = mDbAdapter.fetchAllRecordByDESC();
        startManagingCursor(mRecordCursor);

        String[] mFromColumns = new String[] {
                PlayerDbAdapter.KEY_PLAYER,
                PlayerDbAdapter.KEY_SCORE
        };

        int[] mToFields = new int[] {
                R.id.player_text, R.id.score_text
        };

        SimpleCursorAdapter mAdapter =
                new SimpleCursorAdapter(
                        this, // Current context
                        R.layout.record_layout, // Layout for single row
                        mRecordCursor, // Cursor
                        mFromColumns, // Cursor Columns to use
                        mToFields, // Layout fields to use
                        0 // No flags
                );
        setListAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScoreRankingActivity.this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
