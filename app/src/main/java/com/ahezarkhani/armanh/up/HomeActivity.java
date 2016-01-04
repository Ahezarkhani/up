package com.ahezarkhani.armanh.up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.security.UnresolvedPermission;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ArrayList<String> ArrayListToShow = getDisplayable(Globals.runs);
        ListView cuesListView = (ListView) findViewById(R.id.pastRunsListView);

        populateCuesListView(ArrayListToShow,cuesListView);

        cuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                displaySummary(position);
            }
    });

    }

    private ArrayList<String> getDisplayable(ArrayList<RunObject> runs)
    {
        ArrayList ArrayListToShow = new ArrayList<String>();
        for(RunObject run: runs)
        {
            DecimalFormat df = new DecimalFormat("#.00");
            double runDistance = run.getDistance();
            String runDistString;
            if(runDistance<0.001)
                runDistString = String.valueOf(runDistance);
            else
                runDistString = df.format(runDistance);

            ArrayListToShow.add(run.date + "\t\t\t\t\t" + runDistString + "mi");
        }
        return ArrayListToShow;
    }

    public void populateCuesListView(ArrayList<String> ArrayListToShow, ListView cuesListView)
    {
        ArrayAdapter<String> cuesAdapter =
                new ArrayAdapter<String>(this,R.layout.list_item_layout,ArrayListToShow);
        cuesListView.setAdapter(cuesAdapter);
    }


    /** Called when the user clicks the Send button */
    public void startUp(View view) {
        Intent intent = new Intent(this, UP.class);
        startActivity(intent);
        onPause();
    }

    public void displaySummary(int runToDisplay) {
        Intent intent = new Intent(this, displayRunSummary.class);
        Globals.runToDisplay = runToDisplay;
        startActivity(intent);
        onPause();
    }
}
