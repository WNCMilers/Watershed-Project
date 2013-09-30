package com.watershednaturecenter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.watershednaturecenter.GPSItems.CoordinateInformation;
import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

public class MapData extends SherlockFragment implements LocationListener
{
	private Button Start_StopButton;
	private Button SubmitWorkout;
	private Chronometer WorkoutTimer;
	private Spinner WorkoutType;
	
	
	private LocationManager locationManager;
	private HealthGraphApi APIWORKER;
	private WorkoutInfo currentWorkoutInfo;
	
	private ArrayList<CoordinateInformation> LocationArray = new ArrayList<CoordinateInformation>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		View view = inflater.inflate(R.layout.mapdata, container, false);
		//runKeeperwebView = (WebView) view.findViewById(R.id.webView);
		APIWORKER = ((WNC_MILERS) getActivity().getApplication()).get_APIWORKER();
		
		WorkoutTimer = (Chronometer) view.findViewById(R.id.WorkoutTimer);
		WorkoutType = (Spinner) view.findViewById(R.id.WorkoutType);
	
		Start_StopButton = (Button) view.findViewById(R.id.Start_StopWorkout);
		Start_StopButton.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			 onClickStart_StopTrackingBtn();
		}});
		SubmitWorkout = (Button) view.findViewById(R.id.SubmitWorkout);
		SubmitWorkout.setEnabled(false);
		SubmitWorkout.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			 onClickSubmitWorkout();
		}});
		
		
		locationManager = (LocationManager) getSherlockActivity().getSystemService(Context.LOCATION_SERVICE);
		return view;
	}
	
	public void onClickStart_StopTrackingBtn() 
	{
		
		if (Start_StopButton.getText().equals("Start Workout"))
		{
			//initialize currentworkoutinfo to selected workout criteria.
			currentWorkoutInfo = new WorkoutInfo(WorkoutType.getSelectedItem().toString(),"None","");
			WorkoutTimer.setBase(SystemClock.elapsedRealtime());
			WorkoutTimer.start();
			//reset Coordinate list each time
			LocationArray = new ArrayList<CoordinateInformation>();
			// Getting LocationManager object from System Service LOCATION_SERVICE. Need to mess with parameters to not record quite as many points.
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
			Toast.makeText(getSherlockActivity(), "Begining Workout", Toast.LENGTH_SHORT).show();
			Start_StopButton.setText("Stop Workout");
			SubmitWorkout.setEnabled(false);
		}
		else
		{
			WorkoutTimer.stop();
			locationManager.removeUpdates(this);
			Toast.makeText(getSherlockActivity(), "Workout Stopped", Toast.LENGTH_SHORT).show();
			Start_StopButton.setText("Start Workout");
			SubmitWorkout.setEnabled(true);
		}
				
		
	}
	
	public void onClickStopTrackingBtn() 
	{
		
		
	}
	public void onClickSubmitWorkout() 
	{
		if (APIWORKER == null)
				Toast.makeText(getSherlockActivity(), "You must login first", Toast.LENGTH_SHORT).show();
		else
		{
			//BELOW WAS just a test to get number miles walked
			//double miles = APIWORKER.GetWalkingDist();
			//String milesWalkedMessage = String.format("Cool, You have walked %.2f miles so far.", miles); 
			//Toast.makeText(getSherlockActivity(), milesWalkedMessage, Toast.LENGTH_SHORT).show();
			
			//Posts the workout for the coordinates gathered between start and stop
			//
			APIWORKER.PostWorkout(LocationArray,currentWorkoutInfo);
			Toast.makeText(getSherlockActivity(), "Workout Posted", Toast.LENGTH_SHORT).show();
		}
			
	}


	
	@Override
	public void onLocationChanged(Location location) {
		CoordinateInformation CurrentLocation = new CoordinateInformation();
		CurrentLocation.SetLatitude(location.getLatitude());
		CurrentLocation.SetLongitude(location.getLongitude());
		CurrentLocation.SetAltitude(location.getAltitude());
		CurrentLocation.SetCurrentDateTime();
		LocationArray.add(CurrentLocation);
		String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
				CurrentLocation.GetLongitude(), CurrentLocation.GetLatitude());
		Toast.makeText(getSherlockActivity(), message,
	            Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}