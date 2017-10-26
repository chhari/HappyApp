package com.happy.starter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Main3Activity extends AppCompatActivity {

    TextView Selected;
    SwitchCompat English;
    SwitchCompat Telugu;
    SwitchCompat Hindi;
    SwitchCompat Tamil;
    SwitchCompat Malayalam;
    SwitchCompat Kannada;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sharedPreferences = this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("language", "");

        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                switch (v.getId()){
                    case R.id.Selected:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        break;
                }
                switch (v.getId()){
                    case R.id.Telugu:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        find("Telugu");
                        select("Telugu");
                        break;
                }
                switch (v.getId()){
                    case R.id.Tamil:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        find("Tamil");
                        select("Tamil");

                }
                switch (v.getId()){
                    case R.id.English:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        find("English");
                        select("English");

                }
                switch (v.getId()){
                    case R.id.Hindi:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        find("Hindi");
                        select("Hindi");

                }
                switch (v.getId()){
                    case R.id.Kannada:
                        Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
                        find("Kannada");
                        select("Kannada");

                }

            }
        };


        //on each switch
    //    ((TextView) findViewById(R.id.Selected)).setOnCheckedChangeListener(multiListener);

        ((SwitchCompat) findViewById(R.id.English)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.Telugu)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.Hindi)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.Tamil)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.Kannada)).setOnCheckedChangeListener(multiListener);

    }
    public void select(String str) {
        Selected = (TextView)findViewById(R.id.Selected);
        Selected.setText("Your Selected Language is    " + str);
    }

    public void find(String string ){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("language", string);

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
}
