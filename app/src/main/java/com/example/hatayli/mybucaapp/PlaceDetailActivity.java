package com.example.hatayli.mybucaapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hatayli.mybucaapp.Models.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        place=(Place) bundle.getSerializable("data");
        int drawableResId=intent.getIntExtra("drawableResID",R.drawable.background);
        ImageView imgPlaceDetail=findViewById(R.id.img_place_detail);
        imgPlaceDetail.setImageDrawable(getResources().getDrawable(drawableResId));

        TextView tvPlaceNameDetail=findViewById(R.id.tv_placeName_detail);
        tvPlaceNameDetail.setText(place.getName());

        TextView tvDistanceDetail = findViewById(R.id.textView_detail_distance);
        if(place.getDistanceFromUser() != -1)
            tvDistanceDetail.setText(((int) place.getDistanceFromUser())+" m");
        else
            tvDistanceDetail.setText(getString(R.string.text_unknown_distance));

        TextView tvPlaceUserRateDetail=findViewById(R.id.tv_place_userRating_detail);
        tvPlaceUserRateDetail.setText(place.getUserRating()+"");

        TextView tvPlacePhoneNumberDetail=findViewById(R.id.tv_phoneNumber_detail);
        tvPlacePhoneNumberDetail.setText(place.getPhoneNumber());

        RatingBar rbPlaceUserRate=findViewById(R.id.rb_userRate_detail);
        rbPlaceUserRate.setRating(Float.parseFloat(place.getUserRating()+""));

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

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(place.getLatitude(),place.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(place.getName()+"("+place.getUserRating()+")"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
    }

    public void resetMapCameraPositionHome(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(),place.getLongitude())));
    }
}
