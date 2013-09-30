package com.watershednaturecenter.GPSItems;

import android.text.format.Time;

public class WorkoutInfo {
	 private String WorkoutType;
	 private String EquipmentUsed;
	 private Time StartTime;
	 private String WorkoutNotes;
	    
	    
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
}
