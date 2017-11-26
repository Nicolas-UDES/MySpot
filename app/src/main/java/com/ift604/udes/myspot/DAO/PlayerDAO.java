package com.ift604.udes.myspot.DAO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ift604.udes.myspot.Entites.Player;
import com.ift604.udes.myspot.Entites.Territory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class PlayerDAO {

    private static final String PATH = "player/";

    public interface OnGetPlayer
    {
        void onGetPlayer(Player player);
        void errorOnGetPlayer(VolleyError error);
    }

    public interface OnCreatePlayer
    {
        void onCreatePlayer(Player player);
        void errorOnGetPlayer(VolleyError error);
    }

    public static void getPlayer(final PlayerDAO.OnGetPlayer answer, Context context, int playerId) {
        final String path = Server.URL + PATH + "get/" + playerId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                Player player = new Gson().fromJson(response, Player.class);
                answer.onGetPlayer(player);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer.errorOnGetPlayer(error);
            }
        });

        // Add the request to the queue
        Server.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void createPlayer(final PlayerDAO.OnCreatePlayer answer, Context context, String username) {
        final String path = Server.URL + PATH + "create/" + username;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                Player player = new Gson().fromJson(response, Player.class);
                answer.onCreatePlayer(player);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer.errorOnGetPlayer(error);
            }
        });

        // Add the request to the queue
        Server.getInstance(context).addToRequestQueue(stringRequest);
    }
}
