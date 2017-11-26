package com.ift604.udes.myspot.Android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.SupportMapFragment;
import com.ift604.udes.myspot.DAO.PlayerDAO;
import com.ift604.udes.myspot.Entites.Player;
import com.ift604.udes.myspot.R;
import com.ift604.udes.myspot.Utility.Functions;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class MeActivity extends Activity implements PlayerDAO.OnGetPlayer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity);

        PlayerDAO.getPlayer(this, getApplicationContext(), 0);
    }

    @Override
    public void onGetPlayer(Player player) {
        int maxSize = (int) (Functions.levelToBladerSize(player.getLevel()) * 100);

        ProgressBar stomach = (ProgressBar) findViewById(R.id.progressbarStomach);
        stomach.setMax(maxSize);
        stomach.setProgress(((int) player.getStomach() * 100));

        ProgressBar urine = (ProgressBar) findViewById(R.id.progressbarUrine);
        stomach.setMax(maxSize);
        urine.setProgress(((int) player.getBlader() * 100));

        ProgressBar strength = (ProgressBar) findViewById(R.id.progressbarStrength);
        stomach.setMax(300);
        strength.setProgress(((int) player.getUrineStrength() * 100));
    }

    @Override
    public void errorOnGetPlayer(VolleyError error) {
        Log.e("gettingNearTerritoriesError", error.getMessage());
    }
}
