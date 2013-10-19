package com.watershednaturecenter;

import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

import android.app.Application;

public class WNC_MILERS extends Application{
	private HealthGraphApi APIWORKER;
	private WorkoutInfo CurrentWorkout;
	
	
	
	public HealthGraphApi get_APIWORKER() {
        return APIWORKER;
    }

    public void set_APIWORKER(HealthGraphApi api_variable) {
        this.APIWORKER = api_variable;
    }
    
    public WorkoutInfo get_CurrentWorkout() {
        return CurrentWorkout;
    }

    public void set_CurrentWorkout(WorkoutInfo workout) {
        this.CurrentWorkout = workout;
    }

}
