package com.example.hatayli.mybucaapp.Models;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MyRestApiInterface {

    @GET("{placeType}")
    Observable<List<Place>> getPlaces(@Path("placeType") String placeType);

    @GET("{placeType}")
    Call<List<Place>> getPlace(@Path("placeType") String placeType);

}
