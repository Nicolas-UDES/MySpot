package com.ift604.udes.myspot.DAO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ift604.udes.myspot.Entites.Territory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class TerritoryDAO {

    private static final String PATH = "territory/";

    public interface OnGetTerritories
    {
        void getTerritories(List<Territory> territories);
        void errorOnGetTerritories(VolleyError error);
    }

    public static void getTerritories(final OnGetTerritories answer, Context context) {
        final String path = Server.URL + PATH + "getAll";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                Type listType = new TypeToken<List<Territory>>(){}.getType();
                List<Territory> territories = new Gson().fromJson(response, listType);
                answer.getTerritories(territories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer.errorOnGetTerritories(error);
            }
        });

        // Add the request to the queue
        Server.getInstance(context).addToRequestQueue(stringRequest);
    }
}
