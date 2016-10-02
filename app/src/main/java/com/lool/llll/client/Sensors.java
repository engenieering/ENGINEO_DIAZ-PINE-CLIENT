package com.lool.llll.client;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by llll on 02/10/2016.
 */
public abstract class Sensors implements SensorEventListener {
    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    String[] strings=new String[3];
    private float azimuth;
    private float pitch;
    private float roll;


    Context mContext ;

    //constructor
    public Sensors(Context context){
        mContext = context ;
    }

    public void run(){
    SensorManager sManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
    sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
    }



    public float getAzimuth(){
        return azimuth;
    }
    public float getRoll (){
        return roll ;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
        }

        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
            SensorManager.getOrientation(outGravity, values);

            azimuth = values[0] * 57.2957795f;
            pitch =values[1] * 57.2957795f;
            roll = values[2] * 57.2957795f;
            mags = null;
            accels = null;
        //todo
            strings[0]= String.valueOf(azimuth);
            strings[1]= String.valueOf(pitch);
            strings[2]= String.valueOf(roll);
            setTxtView_sensor(strings);
        }

       // Log.d("ssssss","az:"+ String.valueOf(azimuth)+"\tpitch:"+String.valueOf(pitch)+"\troll:"+String.valueOf(roll));
    }
    public abstract void setTxtView_sensor(String[] s);
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
