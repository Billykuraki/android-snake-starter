
package com.billy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.persistence.Player;
import com.billy.persistence.PlayerViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankingActivity extends FragmentActivity {

    private RecyclerView mPlayersList;
    private PlayerListAdapter mAdapter;
    private PlayerViewModel mPlayerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_hall_of_fame);

        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        mPlayerViewModel.getAllPlayers().observeForever(new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mAdapter.setData(players);
            }
        });

        mPlayersList = findViewById(R.id.rank_list);
        mAdapter = new PlayerListAdapter();
        mPlayersList.setAdapter(mAdapter);
        mPlayersList.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            final String player = bundle.getString("player");
            final String score = bundle.getString("score");
            mPlayerViewModel.insert(new Player(player, score));
        }
    }


    private class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder> {

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
