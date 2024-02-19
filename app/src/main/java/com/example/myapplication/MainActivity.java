package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.Calendar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    int hour, minute;
    String pm_am;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                    //do nothing

                                              }
                                          }
                                          );




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





    }






