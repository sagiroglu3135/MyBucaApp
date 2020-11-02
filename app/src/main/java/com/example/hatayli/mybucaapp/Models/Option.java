package com.example.hatayli.mybucaapp.Models;

import android.graphics.Bitmap;

//Bu Class ana sayfadaki Restoran , Taxi vb seçeneklerin modeli olarak kullanılacaktır.
public class Option {

    Bitmap opt_img;
    String opt_txt;


    public Bitmap getOpt_img() {
        return opt_img;
    }

    public void setOpt_img(Bitmap opt_img) {
        this.opt_img = opt_img;
    }

    public String getOpt_txt() {
        return opt_txt;
    }

    public void setOpt_txt(String opt_txt) {
        this.opt_txt = opt_txt;
    }
}
