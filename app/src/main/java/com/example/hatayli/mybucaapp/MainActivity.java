package com.example.hatayli.mybucaapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hatayli.mybucaapp.Adapters.mRvOptionsAdapter;
import com.example.hatayli.mybucaapp.Models.Option;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static public RecyclerView rv_options;
    //Toolbar toolbar;
    List<Option> opt_list;
    public static int[] drawableArray={R.drawable.cafe,R.drawable.restaurant,R.drawable.cabstand};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createOptions();//Ana sayfada görüntülenen seçenekler oluşturuluyor.
        rv_options = findViewById(R.id.rv_options);
        rv_options.setNestedScrollingEnabled(false);//nestedscrollview gibi davranması için.
       //toolbar = findViewById(R.id.toolBar);
        //toolbar.inflateMenu(R.menu.menu_1);
        //setSupportActionBar(toolbar);


        mRvOptionsAdapter mRvOptAdapter = new mRvOptionsAdapter(this, opt_list);
        rv_options.setAdapter(mRvOptAdapter);
        rv_options.setHasFixedSize(true);
        rv_options.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1,menu);
        return true;
    }

    private void createOptions() {
        opt_list = new ArrayList<>();
        Drawable myDrawable;
        Bitmap bitmap;

        String[] optionsArray=getResources().getStringArray(R.array.options);
        String[] optionsNamesArray=getResources().getStringArray(R.array.optionsName);
        for (int i=0;i<optionsArray.length;i++){
            Option o= new Option();
            o.setOpt_txt(optionsNamesArray[i]);
            myDrawable = getResources().getDrawable(drawableArray[i]);
            bitmap = ((BitmapDrawable) myDrawable).getBitmap();
            o.setOpt_img(bitmap);
            opt_list.add(o);
        }

    }



    public void getMapActivity(MenuItem item) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
