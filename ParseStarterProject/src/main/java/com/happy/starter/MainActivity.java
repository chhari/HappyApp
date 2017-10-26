/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.happy.starter;

import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    protected static final String TAG = "MainActivity";
    RecyclerView rvItem;
    ListView requestListView;
    ArrayList<String> requests = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    SwipeRefreshLayout swiper;
    SharedPreferences sharedPreferences;
    ArrayList<Double> requestLatitudes = new ArrayList<Double>();
    ArrayList<Double> requestLongitudes = new ArrayList<Double>();

    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<Double> findmax = new ArrayList<Double>();

    LocationManager locationManager;

    LocationListener locationListener;

    int index = 0 ;

    ArrayList<Item> datas = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        RecyclerView rvItem = (RecyclerView) findViewById(R.id.rvItem);

        rvItem.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

        rvItem.setLayoutManager(manager);

        ArrayList<Item> itemList = generatedDummy();

        ItemAdapter2 adapter = new ItemAdapter2(getApplicationContext(), itemList);

        rvItem.setAdapter(adapter);

        int i = adapter.getItemCount();

    }

    public  ArrayList<Item> generatedDummy(){

            String string = (String) getIntent().getSerializableExtra("mystring");
            String[] parts = string.split("/");
            String str = parts[parts.length-1];
            String link  = string.replace(str,"");
            String[] lower1 = str.split("\\.");
            Integer lower2 = Integer.valueOf(lower1[0]);
            for (int i = 0; i < 16; i++) {
                int k = (lower2 -i);
                String str1 = "00";
                String numstring = Integer.toString(k);
                String str2 = ".jpg";
                String mainstring = str1 + numstring + str2;
                String maindata = link + mainstring;
                Item item = new Item();
                item.img = maindata;
                datas.add(item);
                Log.i("maindata", maindata);

            }

        return datas;
        // adapter.notifyDataSetChanged();
    }


}
