package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends MainActivity{

    String homeTown;
    Integer homeZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        //get data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //EditText for home town
        EditText homeTownForm = findViewById(R.id.home_town);
        homeTown = sharedPreferences.getString("homeTown", "Baltimore");
        homeTownForm.setText(homeTown);

        // Add items to spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timezones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //listener to change GMT time
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  TextView gmt = findViewById(R.id.home_time_gmt);
                                                  //match spinner array to GMT array
                                                  String[] gmtArray = getResources().getStringArray(R.array.gmt);
                                                  gmt.setText(gmtArray[position]);
                                                  homeZone= position;
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                  //do nothing

                                              }
                                          }
        );

        //get SharedPreference integer for spinner
        spinner.setSelection(sharedPreferences.getInt("spinner", 1));


        //save button
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from EditText
                EditText homeTown = findViewById(R.id.home_town);
                String homeTownString = homeTown.getText().toString();

                //save data to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("homeTown", homeTownString);
                myEdit.putInt("spinner", homeZone);
                myEdit.apply();

                //go back to main activity
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
