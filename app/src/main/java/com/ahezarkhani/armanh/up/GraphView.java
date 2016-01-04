package com.ahezarkhani.armanh.up;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ArmanH on 12/9/2015.
 */
public class GraphView extends View
{

    public RunObject thisRun;
    private double[] distances;
    private Paint distancePaint;
    private Paint velocityPaint;
    private Paint altitudePaint;
    private Paint whitePaint;
    private double[] speeds;
    private Paint badFormPaint;


    public GraphView(Context context,AttributeSet attrs) {
        super(context,attrs);
        thisRun = Globals.runs.get(Globals.runToDisplay);

    }

    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight()-50;//-50 to stay on page (due to getHeight() error)
        Paint lines = new Paint();
        distancePaint = new Paint();
        altitudePaint = new Paint();
        velocityPaint = new Paint();
        whitePaint = new Paint();
        badFormPaint = new Paint();

        lines.setARGB(255, 255, 255, 255);//white
        distancePaint.setARGB(255,133,220,255);//blue
        altitudePaint.setARGB(255,255,69,69);//red
        velocityPaint.setARGB(255, 110, 255, 98);//green
        whitePaint.setARGB(255, 255, 255, 255);//white
        badFormPaint.setARGB(100, 247, 186, 42);//yellow, slightly less opaque

        distancePaint.setStrokeWidth(10);
        altitudePaint.setStrokeWidth(10);
        velocityPaint.setStrokeWidth(10);
        badFormPaint.setStrokeWidth(35);


        //canvas.drawLine(0, 0, 0, height, lines);//y-axis
        //canvas.drawLine(0, height, width, height, lines);//x-axis
        double XunitLength = (double)(width/(thisRun.locationsList.size()-1));

        drawBadFormLines(canvas, height, XunitLength);

        //drawBreakLines(canvas,height,XunitLength);


        double[] scaledAltitudes = scaleAltitudes(height);
        double[] scaledVelocities = scaleVelocities(height);
        double[] scaledDistances = scaleDistances(height);

        drawDistances(canvas,scaledDistances,XunitLength);
        drawAltitudes(canvas, scaledAltitudes, XunitLength);
        drawVelocities(canvas, scaledVelocities, XunitLength);




    }

    private void drawBadFormLines(Canvas canvas, int height, double xunitLength)
    {
        double x;
        for(int i = 0; i<thisRun.locationsList.size();i++)
        {
            x = xunitLength*(double)i;
            if(thisRun.badFormOccurances.get(i)) //badFormOccuredHere
                canvas.drawLine((float)x,(float)0,(float)x,(float)height, badFormPaint);

        }
    }

    private void drawBreakLines(Canvas canvas, int height, double xunitLength)
    {
        double x;
        for(int i = 0; i<thisRun.locationsList.size();i++)
        {
            x = xunitLength*(double)i;
            canvas.drawLine((float)x,(float)0,(float)x,(float)height, whitePaint);

        }
    }

    private void drawVelocities(Canvas canvas, double[] scaledVelocities, double xunitLength) {
        double oldX = 0;//init as origin
        double oldY = scaledVelocities[0];//init as initial distance-->(0)
        double newX,newY;
        for(int i = 1; i<thisRun.locationsList.size();i++)
        {
            System.out.println("888888888"+scaledVelocities[i]);
            newX = xunitLength*(double)i;
            newY = scaledVelocities[i];
            canvas.drawLine((float)oldX,(float)oldY,(float)newX,(float)newY, velocityPaint);
            oldX = newX;
            oldY = newY;
        }

    }

    private void drawAltitudes(Canvas canvas, double[] scaledAltitudes, double xunitLength) {
        double oldX = 0;//init as origin
        double oldY = scaledAltitudes[0];//init as initial distance-->(0)
        double newX,newY;
        for(int i = 1; i<thisRun.locationsList.size();i++)
        {
            System.out.println("888888888"+scaledAltitudes[i]);
            newX = xunitLength*(double)i;
            newY = scaledAltitudes[i];
            canvas.drawLine((float)oldX,(float)oldY,(float)newX,(float)newY, altitudePaint);
            oldX = newX;
            oldY = newY;
        }
    }

    private void drawDistances(Canvas canvas, double[] scaledDistances, double xunitLength)
    {
        double oldX = 0;//init as origin
        double oldY = scaledDistances[0];//init as initial distance-->(0)
        double newX,newY;
        for(int i = 1; i<thisRun.locationsList.size();i++)
        {
            System.out.println("888888888"+scaledDistances[i]);
            newX = xunitLength*(double)i;
            newY = scaledDistances[i];
            canvas.drawLine((float)oldX,(float)oldY,(float)newX,(float)newY, distancePaint);
            oldX = newX;
            oldY = newY;
        }
    }

    private double[] scaleDistances(int height)
    {
        double[] distances =getDistances();
        double[] scaledDistances = new double[distances.length];
        double scaleFactor = height/(max(distances)+.1);
        for(int i = 0; i<distances.length; i++)
        {
            scaledDistances[i] = height-(distances[i]*scaleFactor);
        }

        return scaledDistances;
    }

    private double[] scaleVelocities(int height) {
        double[] velocities = getSpeeds();
        double[] scaledVelocities = new double[velocities.length];
        double scaleFactor = height/(max(velocities)+.1);
        for(int i = 0; i<velocities.length; i++)
        {
            scaledVelocities[i] = height-(velocities[i]*scaleFactor);
        }

        return scaledVelocities;


    }

    private double[] scaleAltitudes(int height)
    {
        double[] altitudes = getAltitudes();
        double[] scaledAltitudes = new double[altitudes.length];
        double scaleFactor = height/(max(altitudes)+.1);
        for(int i = 0; i<altitudes.length; i++)
        {
            scaledAltitudes[i] = height-(altitudes[i]*scaleFactor);
        }

        return scaledAltitudes;
    }

    public double[] getDistances() {
        double[] distances = new double[thisRun.locationsList.size()];
        distances[0] = 0; //no distance when run starts
        double totalDistance = 0.0;
        Location lastLocation = thisRun.locationsList.get(0); //gets initial location to compare
        Location currentLocation;
        for(int i = 1; i<thisRun.locationsList.size();i++)
        {
            currentLocation = thisRun.locationsList.get(i);
            distances[i] = lastLocation.distanceTo(currentLocation);
            lastLocation = currentLocation;
        }

        return distances;
    }


    public double max(double[] array)
    {
        double maxi = array[0];
        for(double num:array)
        {
            if(num>maxi)
                maxi = num;
        }
        return maxi;
    }


    public double[] getSpeeds() {
        double[] speeds = new double[thisRun.locationsList.size()];
        for (int i = 0; i<speeds.length; i++)
        {
            speeds[i] = thisRun.locationsList.get(i).getSpeed();
        }
        return speeds;
    }

    public double[] getAltitudes() {
        double[] altitudes = new double[thisRun.locationsList.size()];
        for (int i = 0; i<altitudes.length; i++)
        {
            altitudes[i] = thisRun.locationsList.get(i).getAltitude();
        }
        return altitudes;
    }
}
