package me.denniss.handshake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

public class RequestUtil {
    private static RequestQueue mRequestQueue;
    private static Context context;
    public static GoogleApiClient mApiClient;
    public static void init(Context context)
    {
        RequestUtil.context = context;

        mRequestQueue = Volley.newRequestQueue(context);
    }


    public static void sendGesture(String gesture, BusinessCard card, Response.Listener listener)
    {
        Bitmap bm = BitmapFactory.decodeFile(card.getImageUrl());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        JSONObject object = new JSONObject();

        Location l = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        try
        {
            if(l!=null)
            {
                object.put("lat",l.getLatitude());
                object.put("lon",l.getLongitude());
            }
            object.put("imageBase64",encodedImage);
            object.put("gesture",gesture);
            object.put("businessName", card.getBusinessName());
            object.put("name", card.getName());
            object.put("email", card.getEmail());
            object.put("phone", card.getNumber());
            object.put("fax", card.getFax());
            object.put("address", card.getAddress());
            object.put("jobTitle", card.getJobTitle());
            object.put("website", card.getWebsite());
            File f = new File(card.getImageUrl());

            object.put("imageName", f.getName());

        }catch (JSONException e){}

        mRequestQueue.add(new JsonObjectRequest(JsonObjectRequest.Method.POST,"http://r.denniss.me/shake",object,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"ERROR MAKING REQUEST",Toast.LENGTH_SHORT).show();
            }
        }));
    }



}
