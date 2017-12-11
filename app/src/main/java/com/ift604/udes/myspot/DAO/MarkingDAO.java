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

import MySpotLibrary.Entites.Marking;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class MarkingDAO {

    private static final String PATH = "marking/";

    public interface OnSendMarking
    {
        void onSendMarking(Marking marking);
        void errorOnSendMarking(VolleyError error);
    }

    public static void sendMarking(final MarkingDAO.OnSendMarking answer, Context context, long playerId, long territoryId, long delay) {
        final String path = Server.URL + PATH + "addMark/" + playerId + "/" + territoryId + "/" + delay;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                Marking marking;
                try {
                    marking = new ObjectMapper().readValue(response, Marking.class);
                } catch (IOException e) {
                    answer.errorOnSendMarking(new VolleyError());
                    return;
                }

                answer.onSendMarking(marking);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                answer.errorOnSendMarking(error);
            }
        });

        // Add the request to the queue
        Server.getInstance(context).addToRequestQueue(stringRequest);
    }
}
