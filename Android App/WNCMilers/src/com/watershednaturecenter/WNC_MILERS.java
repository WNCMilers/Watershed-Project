package com.watershednaturecenter;

import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.Polygon;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

import android.app.Application;

public class WNC_MILERS extends Application{
	private HealthGraphApi APIWORKER;
	private WorkoutInfo CurrentWorkoutWNC;
	private WorkoutInfo CurrentWorkoutRK;
	double [] lat = {38.816288,38.815678,38.815686,38.818244,38.819690,38.818696,38.816288};
	double [] Lon = {-89.981675,-89.979873,-89.975753,-89.975163,-89.978596,-89.982533,-89.981675};
	private Polygon WNCBoundaries = new Polygon(lat,Lon, 6);
	
	
	public HealthGraphApi get_APIWORKER() {
        return APIWORKER;
    }

    public void set_APIWORKER(HealthGraphApi api_variable) {
        this.APIWORKER = api_variable;
    }
    
    public WorkoutInfo get_CurrentWorkoutWNC() {
        return CurrentWorkoutWNC;
    }
    public WorkoutInfo get_CurrentWorkoutRK() {
        return CurrentWorkoutRK;
    }

    public void set_CurrentWorkouts(WorkoutInfo WNC,WorkoutInfo RK) {
        this.CurrentWorkoutWNC = WNC;
        this.CurrentWorkoutRK = RK;
    }
    
    public Polygon get_WNCboundaries() {
        return WNCBoundaries;
    }

}
