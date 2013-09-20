package com.watershednaturecenter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.watershednaturecenter.GPSItems.CoordinateInformation;
import com.watershednaturecenter.GPSItems.HealthGraphApi;

public class MapData extends SherlockFragment implements LocationListener
{
	private WebView runKeeperwebView;
	private Button StartButton;
	private Button StopButton;
	private Button PostButton;
	
	
	
	private LocationManager locationManager;
	private HealthGraphApi APIWORKER;
	
	private ArrayList<CoordinateInformation> LocationArray = new ArrayList<CoordinateInformation>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		View view = inflater.inflate(R.layout.mapdata, container, false);
		runKeeperwebView = (WebView) view.findViewById(R.id.webView);
		APIWORKER = new HealthGraphApi(runKeeperwebView);
		APIWORKER.LoginPrompt();
		StartButton = (Button) view.findViewById(R.id.btnStartTracking);
		StartButton.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			 onClickStartTrackingBtn();
		}});
		StopButton = (Button) view.findViewById(R.id.btnStopTracking);
		StopButton.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			 onClickStopTrackingBtn();
		}});
		PostButton = (Button) view.findViewById(R.id.btnPost);
		PostButton.setOnClickListener(new OnClickListener()
		{
		public void onClick(View v)
		{
			 onClickPostBtn();
		}});
		
		
		locationManager = (LocationManager) getSherlockActivity().getSystemService(Context.LOCATION_SERVICE);
		return view;
	}
	
	public void onClickStartTrackingBtn() 
	{
		//reset Coordinate list each time
		LocationArray = new ArrayList<CoordinateInformation>();
		// Getting LocationManager object from System Service LOCATION_SERVICE. Need to mess with parameters to not record quite as many points.
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
		
		Toast.makeText(getSherlockActivity(), "Start Button Clicked", Toast.LENGTH_SHORT).show();
	}
	
	public void onClickStopTrackingBtn() 
	{
		locationManager.removeUpdates(this);
		Toast.makeText(getSherlockActivity(), "Stop Button Clicked", Toast.LENGTH_SHORT).show();
		
	}
	public void onClickPostBtn() 
	{
		if (APIWORKER.GetaccesToken() == null)
				Toast.makeText(getSherlockActivity(), "Not logged in yet", Toast.LENGTH_SHORT).show();
		else
		{
			//BELOW WAS just a test to get number miles walked
			//double miles = APIWORKER.GetWalkingDist();
			//String milesWalkedMessage = String.format("Cool, You have walked %.2f miles so far.", miles); 
			//Toast.makeText(getSherlockActivity(), milesWalkedMessage, Toast.LENGTH_SHORT).show();
			
			//Posts the workout for the coordinates gathered between start and stop
			APIWORKER.PostWorkout(LocationArray);
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