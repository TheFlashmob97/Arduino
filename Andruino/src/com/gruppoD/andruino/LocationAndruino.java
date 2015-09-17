package com.gruppoD.andruino;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationAndruino implements LocationListener {
	private LocationManager posizione;
	private MainActivity main;
	
	private String longitudine = "0.0", latitudine = "0.0";	
	private boolean isActivated = false;
	
	public LocationAndruino(MainActivity main){
		posizione = (LocationManager) main.getSystemService(Context.LOCATION_SERVICE);
		if(posizione.isProviderEnabled(Context.LOCATION_SERVICE)){
			posizione.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			isActivated = true;
		}
	}
	
	public void onLocationChanged(Location location) {
		this.latitudine = Double.toString(location.getLatitude());
		this.longitudine =  Double.toString(location.getLongitude());		
	}

	public boolean isActivated(){
		return this.isActivated;
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras){}

	public void onProviderEnabled(String provider){
		this.isActivated = true;
		this.main.gpsStatus.setActivated(true);
	}

	public void onProviderDisabled(String provider) {
		this.longitudine = "0.0";
		this.latitudine = "0.0";
		this.isActivated = false;
		this.main.gpsStatus.setActivated(false);
	}

	public String getLongitudine(){
		return this.longitudine;
	}
	
	public String getLatitudine(){
		return this.latitudine;
	}
}
