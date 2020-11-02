package com.example.hatayli.mybucaapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hatayli.mybucaapp.MainActivity;
import com.example.hatayli.mybucaapp.Models.Option;
import com.example.hatayli.mybucaapp.PlaceActivity;
import com.example.hatayli.mybucaapp.R;

import java.util.List;

public class mRvOptionsAdapter extends RecyclerView.Adapter<mRvOptionsAdapter.mViewHolder> {


    Context context;
    List<Option> options;

    public mRvOptionsAdapter(Context context, List<Option> options) {
        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.opt_row_layout, viewGroup, false);
        mViewHolder holder = new mViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, int i) {
        Option option = options.get(i);
        mViewHolder.options_img.setImageBitmap(option.getOpt_img());
        mViewHolder.options_text.setText(option.getOpt_txt());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView options_img;
        TextView options_text;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            options_img = itemView.findViewById(R.id.opt_img);
            options_text = itemView.findViewById(R.id.opt_txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = MainActivity.rv_options.getChildLayoutPosition(v);
            Intent intent;
            //Toast.makeText(context,itemPosition+"",Toast.LENGTH_SHORT).show();
            switch (options.get(itemPosition).getOpt_txt().toLowerCase()) {
                case "cafes":
                    intent = new Intent(v.getContext(), PlaceActivity.class);
                    intent.putExtra("urlKey", "cafe");
                    intent.putExtra("text",options.get(itemPosition).getOpt_txt().toUpperCase());
                    context.startActivity(intent);
                    break;
                case "restaurants":
                    intent = new Intent(v.getContext(), PlaceActivity.class);
                    intent.putExtra("urlKey", "restaurant");
                    intent.putExtra("text",options.get(itemPosition).getOpt_txt().toUpperCase());
                    context.startActivity(intent);
                    break;
                case "cabstands":
                    intent = new Intent(v.getContext(), PlaceActivity.class);
                    intent.putExtra("urlKey", "cabstand");
                    intent.putExtra("text",options.get(itemPosition).getOpt_txt().toUpperCase());
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
