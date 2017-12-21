package com.ift604.udes.myspot.Android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.SupportMapFragment;
import com.ift604.udes.myspot.DAO.DrinkingDAO;
import com.ift604.udes.myspot.DAO.PlayerDAO;
import com.ift604.udes.myspot.DAO.Server;
import com.ift604.udes.myspot.Entites.ServerId;
import com.ift604.udes.myspot.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import MySpotLibrary.Entites.*;
import MySpotLibrary.Entites.Player;

import static MySpotLibrary.BLL.PlayerBLL.calculateLiquids;
import static MySpotLibrary.BLL.PlayerBLL.levelToBladerSize;


/**
 * Created by Squirrel on 2017-11-26.
 */

public class MeActivity extends Activity implements PlayerDAO.OnGetPlayer, DrinkingDAO.OnGetNonEmptyDrinkings {

    private static final int MULTI = 1000;

    private Player player;
    private Timer timer;
    private List<Drinking> drinkings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity);

        PlayerDAO.getPlayer(this, getApplicationContext(), ServerId.getServerId());
    }

    @Override
    public void onGetPlayer(Player player) {
        this.player = player;
        int maxSize = (int) (levelToBladerSize(player.getLevel()) * MULTI);

        ProgressBar stomach = (ProgressBar) findViewById(R.id.progressbarStomach);
        stomach.setMax(maxSize);

        ProgressBar urine = (ProgressBar) findViewById(R.id.progressbarUrine);
        urine.setMax(maxSize);

        ProgressBar strength = (ProgressBar) findViewById(R.id.progressbarStrength);
        strength.setMax(3 * MULTI);
        strength.setProgress((int) (player.getUrineStrength() * MULTI));

        TextView name = (TextView) findViewById(R.id.textViewName);
        name.setText(player.getUsername());

        TextView level = (TextView) findViewById(R.id.textViewLevel);
        level.setText("Level " + player.getLevel());

        DrinkingDAO.getNonEmptyDrinkings(this, getApplicationContext(), player.getId());
    }

    @Override
    public void onGetNonEmptyDrinkings(List<Drinking> drinkings) {
        this.drinkings = drinkings;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        updateLiquids();
                    }
                });
            }
        }, 0, 5000);
    }

    private void updateLiquids() {
        double maxSize = levelToBladerSize(player.getLevel());
        MySpotLibrary.BLL.PlayerBLL.Liquids liquids = calculateLiquids(drinkings, player);

        ProgressBar stomachView = (ProgressBar) findViewById(R.id.progressbarStomach);
        stomachView.setProgress((int) (Math.min(liquids.stomach, maxSize) * MULTI));

        ProgressBar bladerView = (ProgressBar) findViewById(R.id.progressbarUrine);
        bladerView.setProgress((int) (Math.min(liquids.blader, maxSize) * MULTI));
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void errorOnGetNonEmptyDrinkings(VolleyError error) {
        Log.e("errorOnGetNonEmptyDrinkings", Server.toString(error));
    }

    @Override
    public void errorOnGetPlayer(VolleyError error) {
        Log.e("errorOnGetPlayer", Server.toString(error));
    }
}
