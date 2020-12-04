package com.example.yogastudio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.yogastudio.Database.YogaDB;

import java.util.Calendar;
import java.util.Date;

public class SettingPage extends AppCompatActivity {
    TextView btnSave;
    RadioButton rdEasy,rdMedium,rdHard;
    RadioGroup rdGroup;
    YogaDB yogaDB;
    ToggleButton switchAlarm;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        //Init view
        btnSave = (TextView)findViewById(R.id.btnSave);

        rdGroup = (RadioGroup)findViewById(R.id.rGroup);
        rdEasy = (RadioButton) findViewById(R.id.rdEasy);
        rdMedium = (RadioButton) findViewById(R.id.rdMedium);
        rdHard = (RadioButton) findViewById(R.id.rdHard);

        switchAlarm = (ToggleButton)findViewById(R.id.switchAlarm);

        timePicker = (TimePicker)findViewById(R.id.timePicker);

        yogaDB = new YogaDB(this);

        int mode = yogaDB.getSettingMode();
        setRadioButton(mode);

        //Event
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWorkoutMode();
                saveAlarm(switchAlarm.isChecked());
                Toast.makeText(SettingPage.this, "SAVED !!!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void saveAlarm(boolean checked){
        if(checked){
            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent;
            PendingIntent pendingIntent;

            intent = new Intent(SettingPage.this, AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

            //Set timer to alarm
            Calendar calendar = Calendar.getInstance();
            Date today = Calendar.getInstance().getTime();
            calendar.set(today.getYear(), today.getMonth(),today.getDay(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());

            manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

            Log.d("DEBUG","Alarm will wake at : " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
        }

        else {
            //Cancel alarm
            Intent intent = new Intent(SettingPage.this, AlarmNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
        }
    }

    private void saveWorkoutMode(){
        int selectedID = rdGroup.getCheckedRadioButtonId();
        if(selectedID == rdEasy.getId())
            yogaDB.saveSettingMode(0);
        else if (selectedID == rdMedium.getId())
            yogaDB.saveSettingMode(1);
        else if (selectedID == rdHard.getId())
            yogaDB.saveSettingMode(2);
    }

    private void setRadioButton(int mode){
        if(mode == 0)
            rdGroup.check(R.id.rdEasy);
        else if(mode == 1)
            rdGroup.check(R.id.rdMedium);
        else if(mode == 2)
            rdGroup.check(R.id.rdHard);
    }
}
