package com.example.hatayli.mybucaapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.hatayli.mybucaapp.Adapters.mRvPlacesAdapter;
import com.example.hatayli.mybucaapp.Models.ApiClient;
import com.example.hatayli.mybucaapp.Models.MyRestApiInterface;
import com.example.hatayli.mybucaapp.Models.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    public static ProgressBar pb;
    private final static int REQUEST_lOCATION = 101;
    List<MarkerOptions> markers = new ArrayList<>();
    List<Place> places = new ArrayList<>();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pb = findViewById(R.id.progressBar2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Default mMap cameraPosition setting.
        LatLng mainMarker = new LatLng(38.3724207, 27.1928948);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainMarker, 12));

        //checking network connection.
        if (!isNetworkConnected()) {


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

        } else {
            callRequests();
        }

    }

    private void callRequests() {
        //Api interface instance
        MyRestApiInterface apiInterface = ApiClient.getClient().create(MyRestApiInterface.class);

        //cafe, restaurant and cabstand
        String[] options = this.getResources().getStringArray(R.array.options);
        //Multiple request
        List<Observable<List<Place>>> requests = new ArrayList<>();
        for (int i = 0; i < options.length; i++) {
            requests.add(apiInterface.getPlaces(options[i]));
        }


        //RxJava zip operations for multiple request.
        Observable.zip(
                requests,
                (Function<Object[], Object>) objects -> {

                    //cafe,restaurant and cabstand results
                    List<List<Place>> newList = new ArrayList<>();
                    for (Object o : objects) {
                        newList.add((List<Place>) o);
                    }

                    return newList;
                })
                .doFinally(() -> handler.post(() -> {

                    for (MarkerOptions mo : markers) {
                        mMap.addMarker(mo);
                    }

                    LatLng mainMarker = new LatLng(38.3724207, 27.1928948);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainMarker, 12));
                    pb.setVisibility(View.GONE);
                })).subscribe(
                o -> {
                    List<List<Place>> allPlaces = (List<List<Place>>) o;


                    int i = 0;
                    for (List<Place> item : allPlaces) {

                        for (Place p : item) {
                            places.add(p);
                            int height = 70;
                            int width = 70;
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getApplicationContext().getResources().getDrawable(MainActivity.drawableArray[i]);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
                            markers.add(new MarkerOptions()
                                    .position(latLng)
                                    .title(p.getName() + " " + "(" + p.getUserRating() + ")")
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            );
                        }
                        i++;
                    }

                }, e -> {

                }
        );
    }

    //https://gelecegiyazanlar.turkcell.com.tr/soru/internet-baglantisi-androidte-nasil-kontrol-edilir
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_lOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
