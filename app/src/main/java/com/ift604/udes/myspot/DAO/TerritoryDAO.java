package com.ift604.udes.myspot.DAO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import MySpotLibrary.Entites.Territory;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class TerritoryDAO {

    private static final String PATH = "territory/";

    public interface OnGetTerritories
    {
        void onGetTerritories(List<Territory> territories);
        void errorOnGetTerritories(VolleyError error);
    }

    public static void getTerritories(final OnGetTerritories answer, Context context) {
        final String path = Server.URL + PATH + "getAll";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Territory> territories;

                try {
                    territories = new ObjectMapper().readValue(response, new TypeReference<List<Territory>>(){});
                } catch (IOException e) {
                    answer.errorOnGetTerritories(new VolleyError());
                    return;
                }

                answer.onGetTerritories(territories);
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
