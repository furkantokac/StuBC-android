package com.furkantokac.stubc;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class JsonHttpRequestHandler {

    private Cache cache;
    private Network network;
    private RequestQueue requestQueue;

    private Activity parent;

    public JsonHttpRequestHandler(Activity activity) {
        parent = activity;
        cache = new DiskBasedCache(parent.getExternalCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public Map<String, String> createParams() {
        return new HashMap<>();
    }

    public void makeRequest(String url, Map params) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error on request", "Description : " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjReq);
    }

    private void responseReceived(JSONObject response) {
        String resp = "None";
        try {
            resp = response.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JsonHttpRequestHandler", "Runned successfuly. Resp : " + resp);
    }
}
