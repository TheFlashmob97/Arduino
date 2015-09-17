package com.gruppoD.andruino;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class BussolaAndruino implements SensorEventListener {

	private SensorManager gestoreSensoristica;
	private Sensor sensoreMagnetico, sensoreAccelerometro;
	
	private String bussola;
	float[] matrixR, matrixI, matrixValues, valuesAccelerometer, valuesMagneticField;
		
	public BussolaAndruino(MainActivity main) {
		matrixR = new float[9];
	    matrixI = new float[9];
	    matrixValues = new float[3];
	    
	    valuesAccelerometer = new float[3];
	    valuesMagneticField = new float[3];
		
		gestoreSensoristica = (SensorManager) main.getSystemService(Context.SENSOR_SERVICE);
		sensoreAccelerometro = gestoreSensoristica.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensoreMagnetico = gestoreSensoristica.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
        gestoreSensoristica.registerListener(this, sensoreAccelerometro, SensorManager.SENSOR_DELAY_GAME);
        gestoreSensoristica.registerListener(this, sensoreMagnetico, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public String getBussola(){
		return this.bussola;
	}
	//ritorna un vettore di 3 elementi
	public float[] getAccelerometer(){
		return this.valuesAccelerometer;
	}
	
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				for(int i =0; i < 3; i++)
					valuesAccelerometer[i] = event.values[i];
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				for(int i =0; i < 3; i++)
					valuesMagneticField[i] = event.values[i];
				break;
		}
		    
		if(SensorManager.getRotationMatrix(matrixR, matrixI, valuesAccelerometer, valuesMagneticField)){		     
			float azimuthInRadians = SensorManager.getOrientation(matrixR, matrixValues)[0];
			float azimuthInDegress = (float)Math.toDegrees(azimuthInRadians);
			if(azimuthInDegress < 0.0f)
				azimuthInDegress += 360.0f;
        	
			this.bussola = ""+(int)azimuthInDegress;
		}		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy){}
}
