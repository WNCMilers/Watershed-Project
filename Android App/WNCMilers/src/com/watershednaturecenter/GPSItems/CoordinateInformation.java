package com.watershednaturecenter.GPSItems;
import java.util.Date;

import android.text.format.Time;



public class CoordinateInformation {
    private double latitude;
    private double longitude;
    private Time CurrentDateTime;
    private double altitude;
    
    
    public CoordinateInformation(){
    	latitude = -0.0;
    	longitude = -0.0;
    	altitude = -0.0;
    	CurrentDateTime = new Time();
    }
    
    public void SetLatitude(double Lat)
    {
    	latitude = Lat;
    }
    
    public void SetLongitude(double Long)
    {
    	longitude = Long;
    }
    
    public void SetAltitude(double alt)
    {
    	altitude = alt;
    }
    
    public void SetCurrentDateTime()
    {
    	CurrentDateTime.setToNow();
    }
    
    public double GetLatitude()
    {
    	return latitude;
    }
    
    public double GetLongitude()
    {
    	return longitude;
    }
    
    public Time GetCurrentDateTime()
    {
    	return CurrentDateTime;
    }
    
    public double GetAltitude()
    {
    	return altitude;
    }
}
