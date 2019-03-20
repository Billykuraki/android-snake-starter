
package com.billy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.persistence.Player;
import com.billy.persistence.PlayerDao;
import com.billy.persistence.PlayersDatabase;

import java.util.List;

import androidx.annotation.NonNull;

public class RankingActivity extends Activity {

    PlayerDao mDao;
    RecyclerView mRankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_hall_of_fame);

        mRankList = findViewById(R.id.rank_list);
        mRankList.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String player = (String) bundle.get("player");
            String score = String.valueOf(bundle.get("score"));

            mDao = PlayersDatabase.getInstance(this).playerDao();
            mDao.insertPlayer(new Player(player, score));
        }
        mRankList.setAdapter(new RankAdapter(mDao.findAllPlayers()));
    }

    private class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView playerText;
            TextView scoreText;

            MyViewHolder(View v) {
                super(v);
                this.playerText = v.findViewById(R.id.player_text);
                this.scoreText = v.findViewById(R.id.score_text);
            }

        }

        private List<Player> mData;

        RankAdapter (List<Player> data) {
            mData = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rank_row, parent, false);
            return new MyViewHolder(v);
        }

        @NonNull
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Player player = mData.get(position);
            holder.playerText.setText(player.getName());
            holder.scoreText.setText(player.getScore());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RankingActivity.this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
