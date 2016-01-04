package com.ahezarkhani.armanh.up;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UP extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private static final String TOASTDIRECTIONS = "PLEASE FASTEN DEVICE LIKE SO";
    Location lastLocation;




    public final static String EXTRA_MESSAGE = "com.ahezarkhani.armanh.up.MESSAGE";
    private SensorManager AccelManager;
    private SensorManager GyroManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private double initialTimeStamp;
    private final double NS2S = .000000001;
    private boolean initialTrue = true;
    private int peakCounter = 0;
    private boolean yPositivityChanger = true; //true = positive
    private boolean xPositivityChanger = true; //true = positive
    private boolean zPositivityChanger = true; //true = positive
    private boolean[] positivityChangerList = {xPositivityChanger,yPositivityChanger,
                                                zPositivityChanger};
    private boolean accelXPositivityChanger = true; //true = positive
    private ArrayList<ArrayList> currentGyroDataX;
    private ArrayList<ArrayList> currentGyroDataY;
    private ArrayList<ArrayList> currentGyroDataZ;
    private ArrayList<ArrayList> gyroDataList;
    private ArrayList<Double> xIntegralList;
    private ArrayList<Double> yIntegralList;
    private ArrayList<Double> zIntegralList;
    private ArrayList<ArrayList> gyroIntegralList;
    private int counter = 0;
    private double interimTimeStamp = 0.0;
    private double[] gravity = {0,0,0};
    private boolean accelInitialized = false;


    private final int ROTATIONALMOE = 15; //degrees


    private String accelPositivity = "";

    private TextToSpeech tts;
    private final String ROTATIONERROR = "your hips are rotating too much";
    private int rotationErrorCounter = 0;
    private final int rotationErrorCounterMax = 5;
    private double mLastX;
    private double mLastY;
    private double mLastZ;
    private double NOISE = .6;
    private int accelZeroCounter = 0;
    private ArrayList<Integer> allCoachingCues;
    private final int ROTATINGHIPS = 0;
    private double currentTime;
    private boolean inCountDownTime = true;
    private boolean didNotSay10 = true;
    private boolean didNotSay9 = true;
    private boolean didNotSay8 = true;
    private boolean didNotSay7 = true;
    private boolean didNotSay6 = true;
    private boolean didNotSay5 = true;
    private boolean didNotSay4 = true;
    private boolean didNotSay3 = true;
    private boolean didNotSay2 = true;
    private boolean didNotSay1 = true;

    private RunObject thisRun;
    Calendar c = Calendar.getInstance();



    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private boolean lastLocationSwitch = true;

    private boolean badFormOccured = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        Toast.makeText(this, TOASTDIRECTIONS, Toast.LENGTH_SHORT).show();
        AccelManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        GyroManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = AccelManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope = GyroManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        AccelManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        GyroManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        currentGyroDataX = new ArrayList<ArrayList>();
        currentGyroDataY = new ArrayList<ArrayList>();
        currentGyroDataZ = new ArrayList<ArrayList>();
        gyroDataList = new ArrayList<ArrayList>();
        gyroDataList.add(currentGyroDataX);
        gyroDataList.add(currentGyroDataY);
        gyroDataList.add(currentGyroDataZ);

        xIntegralList = new ArrayList<Double>();
        yIntegralList = new ArrayList<Double>();
        zIntegralList = new ArrayList<Double>();
        gyroIntegralList = new ArrayList<ArrayList>();
        gyroIntegralList.add(xIntegralList);
        gyroIntegralList.add(yIntegralList);
        gyroIntegralList.add(zIntegralList);
        tts = new TextToSpeech(this,this);

        allCoachingCues = new ArrayList<Integer>();
        thisRun = new RunObject(getDate(), getTime());


        buildGoogleApiClient();
        mGoogleApiClient.connect();
        createLocationRequest();
    }



    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {

        if (initialTrue)  //Sets Initial Timestamp for starting activity
        {
            speakCoach("Begin run");
            initialTimeStamp = (double) event.timestamp;
            initialTrue = false;
        }


        if(inCountDownTime)
        {
            currentTime = (event.timestamp - initialTimeStamp) * NS2S;
            if(currentTime>10)
            {
                inCountDownTime = false;
                initialTrue = true;
            }
            if(currentTime>10 && didNotSay1)
            {
                speakCoach("1");
                didNotSay1 = false;
            }
            else if(currentTime>9 && didNotSay2)
            {
                speakCoach("2");
                didNotSay2 = false;
            }
            else if(currentTime>8 && didNotSay3){
                speakCoach("3");
                didNotSay3 = false;
            }
            else if(currentTime>7 && didNotSay4){
                speakCoach("4");
                didNotSay4 = false;
            }
            else if(currentTime>6 && didNotSay5){
                speakCoach("5");
                didNotSay5 = false;
            }
            else if(currentTime>5 && didNotSay6){
                speakCoach("6");
                didNotSay6 = false;
            }
            else if(currentTime>4 && didNotSay7){
                speakCoach("7");
                didNotSay7 = false;
            }
            else if(currentTime>3 && didNotSay8){
                speakCoach("8");
                didNotSay8 = false;
            }
            else if(currentTime>2 && didNotSay9){
                speakCoach("9");
                didNotSay9 = false;
            }
            else if(currentTime>1 && didNotSay10)
            {
                speakCoach("in 10");
                didNotSay10 = false;
            }
        }

        else {
            Sensor sensor = event.sensor;
            if (sensor == mGyroscope) //Gyro Data
            {
                calculateRotation(event.values, event.timestamp, currentGyroDataX, 0);//x-axis
                calculateRotation(event.values, event.timestamp, currentGyroDataY, 1);//y-axis
                calculateRotation(event.values, event.timestamp, currentGyroDataZ, 2);//z-axis
            } else if (sensor == mAccelerometer)  //Acelerometer Data
            {
                computeAccelerometerData(event);
            }
        }
    }



    private void computeAccelerometerData(SensorEvent event) {
        //lines 159-204 from
        //https://jayeshcp.wordpress.com/2013/05/01/how-to-use-accelerometer-sensor-in-android/
        //
        // event object contains values of acceleration, read those
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        final double alpha = 0.8; // constant for our filter below6i


        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * y;

        // Remove the gravity contribution with the high-pass filter.
        x = x - gravity[0];
        y = y - gravity[1];
        z = z - gravity[2];

    }

    private void calculateRotation(float[] values, long timestamp,ArrayList<ArrayList> currentGyroData,int axisIndex)
    {
        ArrayList<Double> currentGyroEvent = new ArrayList<Double>();
        currentGyroEvent.add((double)values[0]);
        currentGyroEvent.add((double)values[1]);
        currentGyroEvent.add((double)values[2]);
        currentGyroEvent.add((double) timestamp);

        if(values[axisIndex] >0)
        {
            if(!positivityChangerList[axisIndex])
            {
                positivityChangerList[axisIndex] =true;
                if(gyroDataList.get(axisIndex).size()>0)
                {
                    double integral = integrate(gyroDataList.get(axisIndex),axisIndex);
                    if(Math.abs(integral)>ROTATIONALMOE) {
                        rotationErrorCounter++;
                        gyroIntegralList.get(axisIndex).add(integral);
                    }
                }
                gyroDataList.get(axisIndex).clear();
            }

            gyroDataList.get(axisIndex).add(currentGyroEvent);//[x,y,z,timestamp]
        }
        else
        {
            if(positivityChangerList[axisIndex])
            {
                positivityChangerList[axisIndex] =false;
                if(gyroDataList.get(axisIndex).size()>0)
                {
                    double integral = integrate(gyroDataList.get(axisIndex),axisIndex);
                    if(Math.abs(integral)>ROTATIONALMOE) {
                        rotationErrorCounter++;
                        gyroIntegralList.get(axisIndex).add(integral);
                    }
                }
                gyroDataList.get(axisIndex).clear();
            }
            currentGyroDataX.add(currentGyroEvent); //[x,y,z,timestamp]
        }



        if(rotationErrorCounter>=rotationErrorCounterMax) {

            speakCoach(ROTATIONERROR);
            rotationErrorCounter = 0;
            badFormOccured = true;
            if(!thisRun.coachCues.contains(0))
                thisRun.coachCues.add(0);
        }
    }

    private void speakCoach(String coachingStatement)
    {
        tts.speak(coachingStatement, TextToSpeech.QUEUE_ADD, null);
    }


    public double integrate(ArrayList<ArrayList> currentGyroData, int axis) //ArrayList = [x,y,z,timestamp]
    {
        double integral = 0;
        double time0 = (double) currentGyroData.get(0).get(3);
        double val0 = (double) currentGyroData.get(0).get(axis);
        double time1,val1;
        for(int i = 1; i<currentGyroData.size();i++)
        {
            val1 = (double) currentGyroData.get(i).get(axis);
            time1 = (double) currentGyroData.get(i).get(3);
            double dValue=((val1+val0)/2.0)*2;
            double dT = (time1-time0)*NS2S*2;
            interimTimeStamp+=dT;
            integral+=dValue*dT;
            val0 = val1;//reset val0
            time0 = time1;//reset time0
        }
        return integral*10;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        int n = 2;
    }


    @Override
    public void onInit(int status)
    {
        //Must be Overridden
    }

    public void endRun(View view)
    {
        AccelManager.unregisterListener(this);
        GyroManager.unregisterListener(this);
        stopLocationUpdates();
        Globals.runToDisplay = Globals.runs.size();
        Globals.runs.add(thisRun);
        Intent intent = new Intent(this, displayRunSummary.class);
        startActivity(intent);
    }

    public String getDate() {
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = 1+c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return month+"/"+day+"/"+year;
    }

    public String getTime() {
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        return hour+":"+minute;
    }


    /**
     * method from http://developer.android.com/training/location/retrieve-current.html
     */
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * method from http://developer.android.com/training/location/retrieve-current.html
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng latlng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            thisRun.runCoordinates.add(latlng);
            thisRun.locationsList.add(mLastLocation);
            thisRun.badFormOccurances.add(badFormOccured);
            badFormOccured = false; //reset the value
        }

        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        //Must be Overridden
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Must be Overridden

    }

    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateList(location);
        if(lastLocationSwitch) {
            lastLocation = location;
            lastLocationSwitch = false;
        }
    }

    private void updateList(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        thisRun.runCoordinates.add(latlng);
        thisRun.locationsList.add(location);
        thisRun.badFormOccurances.add(badFormOccured);
        badFormOccured = false; //reset
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    @Override
    public void onBackPressed()
    {
        AccelManager.unregisterListener(this);
        GyroManager.unregisterListener(this);
        stopLocationUpdates();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}