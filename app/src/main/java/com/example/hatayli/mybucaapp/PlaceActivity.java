package com.example.hatayli.mybucaapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hatayli.mybucaapp.Adapters.mRvPlacesAdapter;
import com.example.hatayli.mybucaapp.Models.ApiClient;
import com.example.hatayli.mybucaapp.Models.MyRestApiInterface;
import com.example.hatayli.mybucaapp.Models.Place;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,LocationListener {

    private static final int GPS_TIME_INTERVAL = 3000;
    private static final int GPS_DISTANCE= 0;

    private final static int REQUEST_lOCATION = 101;

    private static final short SORT_TYPE_BY_NAME = 0;
    private static final short SORT_TYPE_BY_DISTANCE = 1;
    private static final short SORT_TYPE_BY_RATING = 2;

    public static ShimmerFrameLayout shimmerFLayout;
    public static RecyclerView recyclerView;
    public static mRvPlacesAdapter adapter;
    public static RelativeLayout relativeLayout;
    public static List<Place> placeList;
    private Toolbar toolbar;
    private LocationManager locationManager;

    private int sortType = 0;
    private boolean distancesCalculated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Intent intent=getIntent();
        final String key=intent.getStringExtra("urlKey");
        String text=intent.getStringExtra("text");//Activity title
        initViews();
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Api interface instance
        MyRestApiInterface apiInterface= ApiClient.getClient().create(MyRestApiInterface.class);


        //checking network connection.
        if(!isNetworkConnected()){


            //show alert dialog about internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Warning");
            alertDialogBuilder.setMessage("Device need to netwok connection");
            alertDialogBuilder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else{

            //call spesific request.
            Call<List<Place>> call= apiInterface.getPlace(key);
            call.enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    if(response.isSuccessful()){
                        List<Place> places= response.body();
                        PlaceActivity.placeList=places;

                        int myDrawableResID=R.drawable.background;

                        switch (key) {
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

                        shimmerFLayout.stopShimmer();
                        adapter=new mRvPlacesAdapter(PlaceActivity.this, places,myDrawableResID);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        shimmerFLayout.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {
                    Log.d("mLog",t.toString());
                    shimmerFLayout.stopShimmer();
                }
            });

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGPSPermission();
    }

    private void initViews() {
        shimmerFLayout=findViewById(R.id.sfl_place);
        recyclerView=findViewById(R.id.rv_place);
        relativeLayout=findViewById(R.id.place_relativeLayout);
        toolbar=findViewById(R.id.toolBarPlace);
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2, menu);
        MenuItem mi = menu.findItem(R.id.mi_search);
        SearchView searchView = (SearchView) mi.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID=item.getItemId();

        if(itemID==R.id.by_name){
            sortType = SORT_TYPE_BY_NAME;
            sortList();
            return true;
        }
        else if(itemID==R.id.by_user_rating){
            sortType = SORT_TYPE_BY_RATING;
            sortList();
            return true;
        }
        else if(itemID==R.id.by_distance){
            if(!distancesCalculated){
                Toast.makeText(this,R.string.text_getting_location_try_again,Toast.LENGTH_SHORT).show();
                checkGPSPermission();
            }
            sortType = SORT_TYPE_BY_DISTANCE;
            sortList();
            return true;
        }
        else if(itemID==android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }

    private void checkGPSPermission()
    {
        if (ActivityCompat.checkSelfPermission(PlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            else
                alertGPS();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_lOCATION);
            }
            else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                alertGPS();
            }
        }
    }
    //https://gelecegiyazanlar.turkcell.com.tr/soru/internet-baglantisi-androidte-nasil-kontrol-edilir
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void alertGPS(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.text_warning));
        alertDialogBuilder.setMessage(getString(R.string.text_open_gps));
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }

    private void sortList(){
        switch (sortType){
            case SORT_TYPE_BY_NAME:
                Collections.sort(placeList,Place.sortByName);
                adapter.updateList(placeList);
                break;
            case SORT_TYPE_BY_DISTANCE:
                Collections.sort(placeList,Place.sortByDistance);
                adapter.updateList(placeList);
                break;
            case SORT_TYPE_BY_RATING:
                Collections.sort(placeList,Place.sortByUserRating);
                adapter.updateList(placeList);
                break;
        }
    }

    private void calculateDistances(Location currLocation) {
        if(placeList == null)
            return;
        //for location distance calculate results
        float[] results = new float[3];
        for (Place p: placeList){
            Location.distanceBetween(p.getLatitude(),p.getLongitude(),currLocation.getLatitude(),currLocation.getLongitude(),results);
            p.setDistanceFromUser(results[0]);
        }
        distancesCalculated = true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        List<Place> newList= new ArrayList<>();
        for (Place plc :placeList ){
            if(plc.getName().toLowerCase().contains(s.toLowerCase())){
                newList.add(plc);
            }
        }
        adapter.updateList(newList);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_lOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        alertGPS();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        calculateDistances(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(),"GPS Provider enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(),"GPS Provider Disabled",Toast.LENGTH_SHORT).show();
    }
}
