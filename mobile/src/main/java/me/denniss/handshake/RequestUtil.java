package me.denniss.handshake;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tomer on 2/21/2015.
 */
public class RequestUtil {
    private static RequestQueue mRequestQueue;
    private static Context context;
    public static GoogleApiClient mApiClient;
    public static void init(Context context)
    {
        RequestUtil.context = context;

        mRequestQueue = Volley.newRequestQueue(context);
    }


    public static void sendGesture(int gesture, Response.Listener listener)
    {
        JSONObject object = new JSONObject();
        Location l = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        try
        {
            if(l!=null)
            {
                object.put("lat",l.getLatitude());
                object.put("lon",l.getLongitude());

            }
            object.put("gesture",gesture);
        }catch (JSONException e){}

        mRequestQueue.add(new JsonObjectRequest(JsonObjectRequest.Method.POST,"http://r.denniss.me/shake",object,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"ERROR MAKING REQUEST",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public static void getStuff(Response.Listener listener)
    {

        mRequestQueue.add(new JsonObjectRequest("http://echo.jsontest.com/key/value/one/two",null,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"ERROR MAKING REQUEST",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
