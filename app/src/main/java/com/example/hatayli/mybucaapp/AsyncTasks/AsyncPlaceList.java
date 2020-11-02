package com.example.hatayli.mybucaapp.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.hatayli.mybucaapp.Adapters.mRvPlacesAdapter;
import com.example.hatayli.mybucaapp.Models.Place;
import com.example.hatayli.mybucaapp.PlaceActivity;
import com.example.hatayli.mybucaapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.hatayli.mybucaapp.PlaceActivity.adapter;


public class AsyncPlaceList extends AsyncTask<Void, Void, List<Place>> {

    String urlKey;
    Context context;

    public AsyncPlaceList(String urlKey, Context context) {
        this.urlKey = urlKey;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PlaceActivity.shimmerFLayout.startShimmer();
    }

    @Override
    protected List<Place> doInBackground(Void... voids) {
        List<Place> list = new ArrayList<>();
        String url = "http://hsprojects.somee.com/api/" + urlKey;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {

                JSONArray array = new JSONArray(response.body().string());
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String isActive = String.valueOf(object.getBoolean("isActive"));
                        String latitude = object.getString("latitude");
                        String longitude = object.getString("longitude");
                        String name = object.getString("name");
                        String phoneNumber = object.getString("phoneNumber");
                        String userRating = object.getString("userRating");

                        Place place = new Place();
                        place.setActive(Boolean.parseBoolean(isActive));
                        place.setLatitude(Double.parseDouble(latitude));
                        place.setLongitude(Double.parseDouble(longitude));
                        place.setName(name);
                        place.setPhoneNumber(phoneNumber);
                        place.setUserRating(Double.parseDouble(userRating));

                        list.add(place);

                    }
                    //Log.v("resultt", "List Size:" + list.size());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            //Log.v("resultt", "ioe" + e.getMessage());
        } catch (JSONException e) {
            //Log.v("resultt", "jsone" + e.getMessage());
           e.printStackTrace();
        }
        return list;

    }


    @Override
    protected void onPostExecute(List<Place> places) {
        super.onPostExecute(places);
        PlaceActivity.relativeLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));
        int myDrawableResID=R.drawable.background;
        switch (urlKey) {
            case "cafe":
                myDrawableResID = R.drawable.cafe;
                break;
            case "restaurant":
                myDrawableResID = R.drawable.restaurant;
                break;
            case "cabstand":
                myDrawableResID = R.drawable.cabstand;
                break;
        }

        PlaceActivity.placeList=places;
        PlaceActivity.shimmerFLayout.stopShimmer();
        adapter=new mRvPlacesAdapter(context, places,myDrawableResID);
        PlaceActivity.recyclerView.setAdapter(adapter);
        PlaceActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PlaceActivity.shimmerFLayout.setVisibility(View.GONE);

    }
}
