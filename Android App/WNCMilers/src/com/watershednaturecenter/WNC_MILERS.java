package com.watershednaturecenter;

import com.watershednaturecenter.GPSItems.HealthGraphApi;

import android.app.Application;

public class WNC_MILERS extends Application{
	private HealthGraphApi APIWORKER;
	
	public HealthGraphApi get_APIWORKER() {
        return APIWORKER;
    }

    public void set_APIWORKER(HealthGraphApi api_variable) {
        this.APIWORKER = api_variable;
    }

}
