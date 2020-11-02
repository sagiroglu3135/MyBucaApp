package com.example.hatayli.mybucaapp.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.hatayli.mybucaapp.MainActivity;
import com.example.hatayli.mybucaapp.MapsActivity;
import com.example.hatayli.mybucaapp.Models.Place;

import com.example.hatayli.mybucaapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class AsyncAllPlaceList extends AsyncTask<Void, Void, List<Place>> {


    Context context;
    GoogleMap map;
    List<MarkerOptions> markers;
    public AsyncAllPlaceList(Context context,GoogleMap map) {
        this.context = context;
        this.map=map;
        markers= new ArrayList<>();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<Place> doInBackground(Void... voids) {
        List<Place> places= new ArrayList<>();
        String baseUrl = "http://hsprojects.somee.com/api/";
        String[] optionsArray=context.getResources().getStringArray(R.array.options);
        for (int a=0;a<optionsArray.length;a++){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(baseUrl+optionsArray[a]).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {

                    //<Place>
                    //<IsActive>false</IsActive>
                    //<Latitude>38.3883472</Latitude>
                    //<Longitude>27.149325</Longitude>
                    //<Name>Temel Reis Cafe</Name>
                    //<PhoneNumber>(0232) 448 82 77</PhoneNumber>
                    //<UserRating>4.3</UserRating>
                    //</Place>
                    JSONArray array = new JSONArray(response.body().string());
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String isActive = object.getString("isActive");
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

                            places.add(place);
                            int height = 70;
                            int width = 70;
                            BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(MainActivity.drawableArray[a]);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                            LatLng latLng= new LatLng(place.getLatitude(),place.getLongitude());
                           markers.add(new MarkerOptions()
                                    .position(latLng)
                                    .title(place.getName()+" "+"("+place.getUserRating()+")")
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            );

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


        }
        return places;

    }


    @Override
    protected void onPostExecute(List<Place> places) {
        super.onPostExecute(places);
        // Add a marker in Sydney and move the camera
        LatLng mainMarker = new LatLng(38.3724207,27.1928948);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mainMarker, 12));
        for (MarkerOptions mo:markers){
            map.addMarker(mo);
        }
        MapsActivity.pb.setVisibility(View.GONE);

    }
}
