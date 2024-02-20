package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    int hour, minute;
    String homeTown = "Baltimore";
    String pm_am;

    Integer homeTimeZone = 1;
    Integer currentZone = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //load shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        homeTown = sharedPreferences.getString("homeTown", "Baltimore");
        homeTimeZone = sharedPreferences.getInt("spinner", 1);

        // Have settings button go to settings page
        FloatingActionButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });



        // Add items to spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timezones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //listener to change GMT time
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  TextView gmt = findViewById(R.id.current_time_zone_diff);
                                                  //match spinner array to GMT array
                                                    String[] gmtArray = getResources().getStringArray(R.array.gmt);
                                                    gmt.setText(gmtArray[position]);
                                                    currentZone = position;
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                    //do nothing

                                              }
                                          }
                                          );

        //select New York by default
        spinner.setSelection(currentZone);



        //set current time
        TextView originalTime = findViewById(R.id.original);
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)%12;
        if (hour == 0) hour = 12; //12 hour clock
        minute = Calendar.getInstance().get(Calendar.MINUTE);
        pm_am = Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM";
        String output = hour + ":" + String.format("%02d", minute) + " " + pm_am;
        originalTime.setText(output);

        //button to change time
        FloatingActionButton changeTimeButton = findViewById(R.id.edit_time);
        changeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });


        //initialize home time zone
        TextView homeTimeZoneText = findViewById(R.id.home_time_zone);
        String[] timeZones = getResources().getStringArray(R.array.timezones);
        homeTimeZoneText.setText(timeZones[homeTimeZone]);

        //initialize home time zone difference
        TextView homeTimeZoneDiff = findViewById(R.id.home_time_zone_diff);
        String[] gmtArray = getResources().getStringArray(R.array.gmt);
        homeTimeZoneDiff.setText(gmtArray[homeTimeZone]);

        //initialize converted time
        TextView convertedTime = findViewById(R.id.converted);
        //convert time
        int convertedHour = convertTime(currentZone, homeTimeZone, hour, pm_am.equals("PM"));
        int tempHour = convertedHour%12;
        if (tempHour == 0) tempHour = 12;

        String convertedOutput =tempHour + ":" + String.format("%02d", minute) + " " + (convertedHour >= 12 ? "PM" : "AM");
        convertedTime.setText(convertedOutput);


        // convert button
        FloatingActionButton convertButton = findViewById(R.id.convert_button);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(homeTimeZone, currentZone)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Time zones are the same", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }


                //convert time
                TextView convertedTime = findViewById(R.id.converted);
                int convertedHour = convertTime(currentZone, homeTimeZone, hour, pm_am.equals("PM"));
                int tempHour = convertedHour % 12;
                if (tempHour == 0)  tempHour = 12;
                String convertedOutput = tempHour + ":" + String.format("%02d", minute) + " " + (convertedHour >= 12 ? "PM" : "AM");
                convertedTime.setText(convertedOutput);
            }
        });








        }



        public void popTimePicker(View view){
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                    TextView originalTime = findViewById(R.id.original);
                    hour = hourOfDay % 12;
                    if (hour == 0) hour = 12; //12 hour clock
                    MainActivity.this.minute = minute;
                    pm_am = hourOfDay >= 12 ? "PM" : "AM";
                    String output = hour + ":" + String.format("%02d", minute) + " " + pm_am;
                    originalTime.setText(output);
                }


            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        }

        public int convertTime(int currentZone, int homeZone, int hour, boolean pm_am){
            int[] zones = getResources().getIntArray(R.array.zones);
            // equal 0 if 12 else hour
            int hour24 = (hour == 12 ? 0 : hour);
            hour24 += (pm_am ? 12 : 0);
            int diff = zones[homeZone] - zones[currentZone];
            ImageView warning = findViewById(R.id.warning);
            //if time is between 11pm and 7am show warning
            int convertedHour = (hour24 + diff+24) % 24;
            if (convertedHour >= 23 || convertedHour <= 7){
                warning.setVisibility(View.VISIBLE);
            } else {
                warning.setVisibility(View.INVISIBLE);
            }
            return convertedHour;








    }
}






