package com.example.hatayli.mybucaapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hatayli.mybucaapp.Models.Place;
import com.example.hatayli.mybucaapp.PlaceActivity;
import com.example.hatayli.mybucaapp.PlaceDetailActivity;
import com.example.hatayli.mybucaapp.R;

import java.util.ArrayList;
import java.util.List;

public class mRvPlacesAdapter extends RecyclerView.Adapter<mRvPlacesAdapter.mViewHolder> {

    Context context;
    List<Place> places;
    Bitmap bitmap;
    int drawableResID;

    public mRvPlacesAdapter(Context context, List<Place> places, int drawableResID) {
        this.context = context;
        this.places = places;
        this.drawableResID=drawableResID;
        Drawable myDrawable = context.getResources().getDrawable(drawableResID);
        bitmap = ((BitmapDrawable) myDrawable).getBitmap();
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.place_row_layout,viewGroup,false);
        mViewHolder holder=new mViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, int i) {
        Place place=places.get(i);
        mViewHolder.img_plc.setImageBitmap(bitmap);
        mViewHolder.img_star.setImageResource(R.drawable.ic_star);
        mViewHolder.tv_plc_name.setText(place.getName());
        mViewHolder.tv_plc_user_rating.setText(place.getUserRating()+"");
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_plc;
        ImageView img_star;
        TextView tv_plc_user_rating;
        TextView tv_plc_name;
    public mViewHolder(@NonNull View itemView) {
        super(itemView);
        img_plc=itemView.findViewById(R.id.img_place);
        img_star=itemView.findViewById(R.id.img_star);
        tv_plc_user_rating=itemView.findViewById(R.id.tv_userRating);
        tv_plc_name=itemView.findViewById(R.id.tv_placeName);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            int itemPosition = PlaceActivity.recyclerView.getChildLayoutPosition(v);
            if(places.get(itemPosition).isActive()){
                Intent intent= new Intent(context,PlaceDetailActivity.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("data",places.get(itemPosition));
                intent.putExtra("drawableResID",drawableResID);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else{

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Information");
                alertDialogBuilder.setMessage("This place is out of service.");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }

        }
    }

    public void updateList(List<Place> list){
        places= new ArrayList<>();
        places.addAll(list);
        notifyDataSetChanged();
    }

}
