package com.example.lenovopc.jagrati;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Events extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getEvents();
    }

    private void getEvents() {
        final String classesURL = apiURL + "/events/";

        JsonArrayRequest req = new JsonArrayRequest(
                classesURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        initializeEvents(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Show error message here.
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "JWT " + jwtVal);
                return headers;
            }
        };

        queue.add(req);
    }

    private void initializeEvents(JSONArray events) {
        for (int i=0; i < events.length(); i++) {
            try{
                JSONObject event = events.getJSONObject(i);
                String id = event.getString("id");
                String eventType = event.getString("_type");
                String eventTitle = event.getString("title");
                String createdAt = event.getString("created_at");

                // TODO: Inflate layout
            } catch (JSONException e) {
                // TODO: Show error here
            }

        }
    }
}