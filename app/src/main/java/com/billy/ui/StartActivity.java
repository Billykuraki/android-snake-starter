
package com.billy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

    private static final String TAG = "StartActivity";

    private Button mStartButton;
    private Button mRankButton;
    private Button mExitButton;

    private ProgressBar mProgress;
    private TextView mProgressText;

    private EditText input;

    private long mLastLeaveTime = 0;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mProgressText.setText("Game Loading");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showSnakeActivity();
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.layout_start_activity);
        mStartButton = findViewById(R.id.btn_start);
        mRankButton = findViewById(R.id.btn_rank);
        mExitButton = findViewById(R.id.btn_exit);
        mProgress = findViewById(R.id.progress_bar);
        mProgressText = findViewById(R.id.progressText);
    }

    private void initListener() {
        mStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // initialize and show dialog
                initDialogAndShow();
            }
        });

        mRankButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to rank activity
                Intent intent = new Intent(StartActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        mExitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Exit app
                finish();
            }

        });
    }

    private void initDialogAndShow() {
        input = new EditText(this);
        new AlertDialog.Builder(this).setTitle(R.string.dialog_title)
               .setView(input)
               .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invisibleAllButton();
                        showProgress();
                    }
               })
               .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // remove dialog
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        long delayTime = 3000;
        if (mLastLeaveTime == 0) {
            Toast.makeText(this, R.string.toast_message_leave, Toast.LENGTH_SHORT).show();
            mLastLeaveTime = System.currentTimeMillis();
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastLeaveTime < delayTime) {
                finish();
            } else {
                // clear
                mLastLeaveTime = 0;
            }
        }
    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.VISIBLE);
        mProgressText.setText(R.string.progress);
        mProgress.setMax(100);
        mProgress.setProgress(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int increment = 0;
                while (increment < 100) {
                    mProgress.setProgress(increment);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    increment++;
                }
                Message msg = new Message();
                mHandler.sendMessageDelayed(msg, 1000);
            }
        }).start();
    }

    private void showSnakeActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra("username", input.getText().toString());
        this.startActivity(intent);
    }

    private void invisibleAllButton() {
        mStartButton.setVisibility(View.INVISIBLE);
        mRankButton.setVisibility(View.INVISIBLE);
        mExitButton.setVisibility(View.INVISIBLE);
    }
}
