package com.happy.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.id;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rvItem;
    ListView requestListView;
    ArrayList<String> requests = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    SwipeRefreshLayout swiper;
    SharedPreferences sharedPreferences;
    ArrayList<Double> requestLatitudes = new ArrayList<Double>();
    ArrayList<Double> requestLongitudes = new ArrayList<Double>();
    ItemAdapter adapter;

    ArrayList<Item> itemList;
    private EndlessRecyclerViewScrollListener scrollListener;

    LocationManager locationManager;

    LocationListener locationListener;

    int index = 0 ;

    ArrayList<Item> datas = new ArrayList<Item>();

    public void updateListView(Location location) {

        if (location != null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");

            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            ParseUser.getCurrentUser().put("location",geoPointLocation);
            query.whereNear("location", geoPointLocation);

            query.setLimit(10000);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        requests.clear();
                        requestLongitudes.clear();
                        requestLatitudes.clear();
                        if (objects.size() > 0) {
                            ParseObject k = objects.get(0);
                            ParseGeoPoint requestLocation1 = (ParseGeoPoint) k.get("location");
                            String string = (String) k.get("Link");
                            requests.add(string);
                            String string1 = (String) k.get("language");
                            Log.i("linkyo",string);
                            sharedPreferences.edit().putString("link",string).apply();
                            sharedPreferences.edit().putString("language",string1).apply();

                        }

                    }
                }
            });

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (internet_connection()){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = Main2Activity.this;
                Intent in = new Intent(context,Main3Activity.class);
                context.startActivity(in);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("link","").equals("")){
            if (ParseUser.getCurrentUser() == null) {

                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (e == null) {

                            Log.i("Info", "Anonymous login successful");

                        } else {

                            Log.i("Info", "Anonymous login failed");
                        }
                    }
                });
            }
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    updateListView(location);
                    ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                    ParseUser.getCurrentUser().put("location", parseGeoPoint);

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                Log.i("request", "not dones");
                            }

                        }
                    });
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (Build.VERSION.SDK_INT < 23) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

            } else {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


                } else {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (lastKnownLocation != null) {

                        updateListView(lastKnownLocation);
                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        ParseUser.getCurrentUser().put("location", parseGeoPoint);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    Log.i("request", "not dones");
                                }
                            }
                        });
                    }
                }
            }

       }



        RecyclerView rvItem = (RecyclerView) findViewById(R.id.rvItem);

        rvItem.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);

        rvItem.setLayoutManager(manager);

        itemList = generatedDummy();
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ArrayList<Item> itemlist4 = new ArrayList<Item>();
                itemlist4 = generatedDummy();
                itemList = itemlist4;
                adapter.notifyDataSetChanged();

            }
        };

            int c = sharedPreferences.getInt("numRun",0);
            c++;
            sharedPreferences.edit().putInt("numRun",c).commit();
        // Adds the scroll listener to RecyclerView`
            if(sharedPreferences.getInt("numRun",0) > 8){
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
                String lang = sharedPreferences.getString("language","English");
                query.whereEqualTo("language", lang);
                sharedPreferences.edit().putInt("numRun",0).commit();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {

                            if (objects.size() > 0) {
                                ParseObject k = objects.get(0);
                                String string2 = (String) k.get("Link");
                                String string1 = (String) k.get("language");
                                Log.i("linkyo",string2);
                                sharedPreferences.edit().putString("link",string2).apply();

                            }

                        }
                    }
                });

            }
        rvItem.addOnScrollListener(scrollListener);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);

        adapter = new ItemAdapter(getApplicationContext(), itemList,swiper);

        rvItem.setAdapter(adapter);


        int i = adapter.getItemCount();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                ArrayList<Item> itemlist5 = new ArrayList<Item>();
                itemlist5 = generatedDummy();
                adapter.swap(itemlist5);
                swiper.setRefreshing(false);

            }
        });
        }else{
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "No internet connection.",
                    Snackbar.LENGTH_LONG);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //recheck internet connection and call DownloadJson if there is internet
                }
            }).show();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();
        ArrayList<Item> itemlist3 = new ArrayList<Item>();
        itemlist3 = generatedDummy();
        adapter.swap(itemlist3);

        //Toast.makeText(getApplicationContext(),"New Data at top",Toast.LENGTH_SHORT).show();
        //Context context = Main2Activity.this;
        //Intent in = new Intent(context,Main3Activity.class);
        //context.startActivity(in);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ArrayList<Item> itemlist6 = new ArrayList<Item>();
            itemlist6 = generatedDummy();
            adapter.swap(itemlist6);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent menuIntent1 = new Intent(this, ImportPhotos.class);
            startActivity(menuIntent1);

            // Handle the camera action
        }else if (id == R.id.nav_manage) {
            Intent menuIntent = new Intent(this, CategoriesList.class);
            startActivity(menuIntent);

        }
        else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "App test demo      ");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.happy.starter");
            startActivity(Intent.createChooser(i, "Share URL"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  ArrayList<Item> generatedDummy(){
        if(sharedPreferences.getString("link","").equals("")) {

            Integer lower2 = 1;
            Integer upper2 = 833;
            Random rand = new Random();
            String link = "https://s3.ap-south-1.amazonaws.com/imagesfull/rjvc/";
            Toast.makeText(getApplicationContext(),"Select your language using the action button here ==>",Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 10; i++) {

                int value = rand.nextInt((upper2 - lower2) + 1) + lower2;

                String numstring = Integer.toString(value);
                Log.i("value", numstring);
                String str = "00";
                String str2 = ".jpg";
                String mainstring = str + numstring + str2;
                Log.i("MainString", mainstring);
                String maindata = link + mainstring;
                Item item = new Item();
                item.img = maindata;
                datas.add(item);
                Log.i("maindata", maindata);

            }
        }
        else {

            String string = sharedPreferences.getString("link", "");
            String[] parts = string.split("_");
            String link = parts[0];
            String lower1 = parts[1];
            String upper1 = parts[2];
            Log.i("links", link);
            Log.i("link2", lower1);
            Log.i("links", upper1);
            Integer lower2 = Integer.valueOf(lower1);
            Integer upper2 = Integer.valueOf(upper1);
            Random rand = new Random();
            for (int i = 0; i < 35; i++) {

                int value = rand.nextInt((upper2 - lower2) + 1) + lower2;

                String numstring = Integer.toString(value);
                Log.i("value", numstring);
                String str = "00";
                String str2 = ".jpg";
                String mainstring = str + numstring + str2;
                Log.i("MainString", mainstring);
                String maindata = link + mainstring;
                Item item = new Item();
                item.img = maindata;
                datas.add(item);
                Log.i("maindata", maindata);

            }

        }
        return datas;
        // adapter.notifyDataSetChanged();
    }
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
