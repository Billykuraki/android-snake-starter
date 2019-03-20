
package com.billy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.persistence.Player;
import com.billy.persistence.PlayerDao;
import com.billy.persistence.PlayerDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankingActivity extends Activity {

    PlayerDao mDao;
    RecyclerView mRankList;
    RankAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_hall_of_fame);

        mRankList = findViewById(R.id.rank_list);
        mRankList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RankAdapter();
        mRankList.setAdapter(mAdapter);
        mDao = PlayerDatabase.getInstance(this).playerDao();
        mAdapter = new RankAdapter();


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String player = bundle.getString("player");
            String score = bundle.getString("score");
            Log.d("QQQ", player + " " + score + "cool!");
            mDao.insertPlayer(new Player(player, score));
        }
        new QueryTask().execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<Player>> {

        @Override
        protected List<Player> doInBackground(Void... voids) {
           return mDao.findAllPlayers();
        }

        @Override
        protected void onPostExecute(List<Player> data) {
            mAdapter.setData(data);
        }
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

        private List<Player> mData = new ArrayList<>();


        public void setData(List<Player> data) {
            Log.d("TEST", "set Data !!!!!!!" + data.isEmpty());
            for (Player p: data) {
                Log.d("TEST", p.getName() + " " + p.getScore());
            }
            mData.addAll(data);
            notifyDataSetChanged();
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
