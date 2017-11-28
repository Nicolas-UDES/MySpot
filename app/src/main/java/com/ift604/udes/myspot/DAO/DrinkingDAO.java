package com.ift604.udes.myspot.DAO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ift604.udes.myspot.Entites.Drinking;
import com.ift604.udes.myspot.Entites.Player;
import com.ift604.udes.myspot.Entites.Territory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class DrinkingDAO {

    private static final String PATH = "drinking/";

    public interface OnGetNonEmptyDrinkings
    {
        void onGetNonEmptyDrinkings(List<Drinking> player);
        void errorOnGetNonEmptyDrinkings(VolleyError error);
    }

    public static void getNonEmptyDrinkings(final DrinkingDAO.OnGetNonEmptyDrinkings answer, Context context, int playerId) {
        final String path = Server.URL + PATH + "getNonEmpty/" + playerId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                List<Drinking> drinkings;
                try {
                    drinkings = new ObjectMapper().readValue(response, new TypeReference<List<Drinking>>(){});
                } catch (IOException e) {
                    answer.errorOnGetNonEmptyDrinkings(new VolleyError());
                    return;
                }

                answer.onGetNonEmptyDrinkings(drinkings);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer.errorOnGetNonEmptyDrinkings(error);
            }
        });

        // Add the request to the queue
        Server.getInstance(context).addToRequestQueue(stringRequest);
    }
}
