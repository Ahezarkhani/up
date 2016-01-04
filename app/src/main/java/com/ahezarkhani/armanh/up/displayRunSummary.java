package com.ahezarkhani.armanh.up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class displayRunSummary extends AppCompatActivity {

    private ArrayList<String> allCoachingCues;
    private ArrayList<String> coachingCuesList;
    private RunObject thisRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_run_summary);
        if(Globals.runToDisplay ==-1)
            assert(false);
        allCoachingCues = new ArrayList<>();
        for(int num:Globals.runs.get(Globals.runToDisplay).coachCues)
        {
            allCoachingCues.add(Globals.CoachingCues.get(num));
        }

        thisRun = Globals.runs.get(Globals.runToDisplay);


        TextView miles = (TextView)findViewById(R.id.miles_view);
        DecimalFormat df = new DecimalFormat("#.00");
        miles.setText(String.valueOf(df.format(thisRun.getDistance()))+" mi");
    }



    public void goHome(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMap(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void giveTips(View view)
    {
        if(allCoachingCues.size() >0)
        {
            for(String cue: allCoachingCues)
                Toast.makeText(this, cue, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "You have perfect running form.\n 10/10 would recommend.", Toast.LENGTH_LONG).show();

        }

    }
}
