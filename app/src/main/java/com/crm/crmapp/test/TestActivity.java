package com.crm.crmapp.test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.crm.crmapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends Activity {


    Context context;
    ArrayList<String> expenseTypeList;
    String date;


    String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Integer.parseInt("");

        String[] splitdate = date.split("-");

        HashMap hashMap = new HashMap();
    }

    public void showSettingsDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);

        int dec_min_value = 5;
        int inc_max_value = 20;


        dialog.setContentView(R.layout.layout_dialog_settings);
        ImageButton imgbtn_dec = dialog.findViewById(R.id.imgbtn_dec);
        ImageButton imgbtn_inc = dialog.findViewById(R.id.imgbtn_inc);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);

        imgbtn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgbtn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });



    }


}
