package com.watershednaturecenter.GPSItems;

import java.util.ArrayList;

import android.text.format.Time;

public class WorkoutInfo {
	 private String WorkoutType;
	 private String EquipmentUsed;
	 private Time StartTime;
	 private String WorkoutNotes;
	 private double CurDistTraveled;
	 private double CurPace;
	 private Integer RK_ID;
	 
	 public ArrayList<CoordinateInformation> LocationArray = new ArrayList<CoordinateInformation>();
	    
	    
    public WorkoutInfo(){
    	WorkoutType = "Walking";
   	 	EquipmentUsed = "None";
   	 	WorkoutNotes = "";
   	 	StartTime = new Time();
	    }
    
    public WorkoutInfo(String WT,String EU,String WN){
    	WorkoutType = WT;
   	 	EquipmentUsed = EU;
   	 	WorkoutNotes = WN;
   	 	StartTime = new Time();
   	 	StartTime.setToNow();
	    }
	    
	public void SetWorkoutType(String type)
	{
		WorkoutType = type;
	}
	
	public void SetEquipmentUsed(String equip)
	{
		EquipmentUsed = equip;
	}
	
	public void SetWorkoutNotes(String notes)
	{
		WorkoutNotes = notes;
	}
	
	public void SetStartTime(Time time)
	{
		StartTime = time;
	}
	public void SetCurDistTraveled(double dist)
	{
		CurDistTraveled = dist;
	}
	public void SetCurPace(double pace)
	{
		CurPace = pace;
	}
	public void SetRK_ID(Integer ID)
	{
		RK_ID= ID;
	}
	
	public String GetWorkoutType()
	{
		return WorkoutType;
	}
	
	public String GetEquipmentUsed()
	{
		return EquipmentUsed;
	}
	
	public String GetWorkoutNotes()
	{
		return WorkoutNotes;
	}
	
	public Time GetStartTime()
	{
		return StartTime;
	}
	public double GetCurPace()
	{
		return CurPace;
	}
	public double GetCurDistTraveled()
	{
		return CurDistTraveled;
	}
	public Integer getRK_ID()
	{
		return RK_ID;
	}
	
	public void UpdateDistTraveled()
	{
		if (LocationArray.size() > 1)
		{
		double x1 = LocationArray.get(LocationArray.size()-1).GetLatitude();
		double y1 = LocationArray.get(LocationArray.size()-1).GetLongitude();
		double x2 = LocationArray.get(LocationArray.size()-2).GetLatitude();
		double y2 = LocationArray.get(LocationArray.size()-2).GetLongitude();
	    double dLat = Math.toRadians(x2-x1);
	    double dLon = Math.toRadians(y2-y1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2)) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    CurDistTraveled = CurDistTraveled += (3955.64142 *(2 * Math.asin(Math.sqrt(a))));
	    //CurDistTraveled = CurDistTraveled += (6366000 *(2 * Math.asin(Math.sqrt(a))));
		}
		
	}
	
	public void resetWorkoutInfo()
	{
		WorkoutType = "";
		EquipmentUsed = "";
		StartTime = null;
		WorkoutNotes = "";
		CurDistTraveled = 0;
		CurPace = 0;
		LocationArray = new ArrayList<CoordinateInformation>();
	}
	
}
