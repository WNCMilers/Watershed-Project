package com.watershednaturecenter;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;
import android.location.Criteria;

import org.apache.http.client.methods.HttpPost;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.watershednaturecenter.Dialogs.LoginDialog;
import com.watershednaturecenter.GPSItems.CoordinateInformation;
import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.MockLocationProvider;
import com.watershednaturecenter.GPSItems.Polygon;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

public class Workout extends SherlockFragment implements LocationListener {
	private Button Start_StopButton;
	private Button SubmitWorkout;
	private Chronometer WorkoutTimer;
	private Spinner WorkoutType;
	private ProgressDialog progress;
	private TextView lblDist;
	private TextView lblPace;
	private PolylineOptions line;
	

	// FOR pushing MOCK LOCATIOn
	MockLocationProvider mock;

	private LocationManager locationManager;
	private HealthGraphApi APIWORKER;
	private WorkoutInfo currentWorkoutInfoWNC;
	private WorkoutInfo currentWorkoutInfoRK;
	private Polygon WNCboundaries;
	private GoogleMap map;
	
	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		if (getResources().getConfiguration().orientation == newConfig.ORIENTATION_PORTRAIT)
			map.animateCamera(CameraUpdateFactory.zoomTo(16));
		else
			map.animateCamera(CameraUpdateFactory.zoomTo(17));
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentWorkoutInfoWNC = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutWNC();
		currentWorkoutInfoRK = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutRK(); 
		
		View view = inflater.inflate(R.layout.workout, container, false);
		lblDist = (TextView) view.findViewById(R.id.lblDist);
		lblPace = (TextView) view.findViewById(R.id.lblPace);
		
		SupportMapFragment fm = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		map = fm.getMap();
		map.setMyLocationEnabled(true);
		
		
		// runKeeperwebView = (WebView) view.findViewById(R.id.webView);
		APIWORKER = ((WNC_MILERS) getActivity().getApplication())
				.get_APIWORKER();
		WNCboundaries = ((WNC_MILERS) getActivity().getApplication())
				.get_WNCboundaries();

		WorkoutTimer = (Chronometer) view.findViewById(R.id.WorkoutTimer);
		WorkoutType = (Spinner) view.findViewById(R.id.WorkoutType);

		
		Start_StopButton = (Button) view.findViewById(R.id.Start_StopWorkout);
		Start_StopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onClickStart_StopTrackingBtn();
			}
		});
		SubmitWorkout = (Button) view.findViewById(R.id.SubmitWorkout);
		SubmitWorkout.setEnabled(false);
		SubmitWorkout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onClickSubmitWorkout();
			}
		});

		locationManager = (LocationManager) getSherlockActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		try{
			LatLng lastLoc = new LatLng(lastknownLocation.getLatitude(),lastknownLocation.getLongitude());
			// Showing the current location in Google Map
	        map.moveCamera(CameraUpdateFactory.newLatLng(lastLoc));

	        // Zoom in the Google Map
	        map.animateCamera(CameraUpdateFactory.zoomTo(17));
	        
		}
		catch(Exception e)
		{
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(38.817480, -89.978670)));
			// Zoom in the Google Map
	        map.animateCamera(CameraUpdateFactory.zoomTo(17));
		}
		
				
		
		initializelayout();
		return view;
	}

	
	public void onClickStart_StopTrackingBtn() {

		
		if (Start_StopButton.getText().equals("Start Workout")) {
			if (!LoginStatus())
			{
				DisplayLoginPopup();
			}
			else
			{
				// reset Workouts Each Time
				currentWorkoutInfoWNC.resetWorkoutInfo();
				currentWorkoutInfoRK.resetWorkoutInfo();
				line = new PolylineOptions();
				currentWorkoutInfoRK.SetWorkoutType(WorkoutType.getSelectedItem().toString());
				currentWorkoutInfoRK.SetEquipmentUsed("None");
				//TODO need to make function for overall Workout Initialization
				
				Criteria gpsCriteria = getGpsCriteria();
				
				
				
				// Getting LocationManager object from System Service
				// LOCATION_SERVICE. Need to mess with parameters to not record
				// quite as many points.
				
				//TODO: uncomment this to enable Real GPS updates. TODO make able to easily turn on/off mock locations.
				//locationManager.requestLocationUpdates(locationManager.getBestProvider(gpsCriteria, true),3000,3,this);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
				// Push Locations
				try {
					new PushLocations().execute(1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Toast.makeText(getSherlockActivity(), "Begining Workout",
						Toast.LENGTH_SHORT).show();
				Start_StopButton.setText("Stop Workout");
				SubmitWorkout.setEnabled(false);

				progress = new ProgressDialog(getActivity());
				progress.setTitle("Starting Workout");
				progress.setMessage("Wait for GPS fix...");
				progress.show();
			}
			
			
		} else {
			WorkoutTimer.stop();
			locationManager.removeUpdates(this);
			Toast.makeText(getSherlockActivity(), "Workout Stopped",
					Toast.LENGTH_SHORT).show();
			Start_StopButton.setText("Start Workout");
			SubmitWorkout.setEnabled(true);
		}

	}
	
	
	public void DisplayLoginPopup()
	{
		LoginDialog LD = new LoginDialog();
		LD.show(getSherlockActivity().getFragmentManager(),"LoginDialogNotice");
	}
	
	public boolean LoginStatus()
	{
		if (APIWORKER == null)return false;
		else if(APIWORKER.GetaccesToken() == null)return false;
		else return true;
	}
	
	private Criteria getGpsCriteria()
	{
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); //Used when we can get a GPS fix
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		return criteria;
	}

	public void onClickStopTrackingBtn() {

	}

	public void onClickSubmitWorkout() {
		
			// BELOW WAS just a test to get number miles walked
			// double miles = APIWORKER.GetWalkingDist();
			// String milesWalkedMessage =
			// String.format("Cool, You have walked %.2f miles so far.", miles);
			// Toast.makeText(getSherlockActivity(), milesWalkedMessage,
			// Toast.LENGTH_SHORT).show();

			// Posts the workout for the coordinates gathered between start and
			// stop
			//
			APIWORKER.PostWorkout(currentWorkoutInfoRK.LocationArray,currentWorkoutInfoRK);
			MySQLConnector MYSQLCOMM = new MySQLConnector(getActivity().getFragmentManager());
			MYSQLCOMM.UpdateMiles();
			//TODO: Add Check to see if any distance was done inside WNC if so, submit to database.
			SubmitWorkout.setEnabled(false);
	};

	@Override
	public void onLocationChanged(Location location) {

		if (progress.isShowing()) {
			WorkoutTimer.setBase(SystemClock.elapsedRealtime());
			WorkoutTimer.start();
			progress.dismiss();
			
			
			Time t = new Time();
			t.setToNow();
			currentWorkoutInfoRK.SetStartTime(t);
			currentWorkoutInfoWNC.SetStartTime(t);
		}
		
		// Creating a LatLng object for the current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        
        
        
     // Showing the current location in Google Map
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
		CoordinateInformation CurrentLocation = new CoordinateInformation();
		CurrentLocation.SetLatitude(latLng.latitude);
		CurrentLocation.SetLongitude(latLng.longitude);
		CurrentLocation.SetAltitude(location.getAltitude());
		CurrentLocation.SetCurrentDateTime();
		
		

        
		
		//Check to see if user is in WNC
		if (WNCboundaries.contains(CurrentLocation.GetLatitude(), CurrentLocation.GetLongitude()))
		{
			//add to both Run keeper location array, and WNCMilers Location Array
			currentWorkoutInfoWNC.LocationArray.add(CurrentLocation);
			currentWorkoutInfoWNC.UpdateDistTraveled();
			currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
			currentWorkoutInfoRK.UpdateDistTraveled();
		}
		else//Just add into Run Keeper Location Array
		{
			currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
			currentWorkoutInfoRK.UpdateDistTraveled();
		}
		
		LatLng PreviousLocation = new LatLng(currentWorkoutInfoRK.LocationArray.get(currentWorkoutInfoRK.LocationArray.size()-1).GetLatitude(),currentWorkoutInfoRK.LocationArray.get(currentWorkoutInfoRK.LocationArray.size()-1).GetLongitude());
        
        

		//String message =
		//String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
		//CurrentLocation.GetLongitude(), CurrentLocation.GetLatitude());
		double Distance = currentWorkoutInfoRK.GetCurDistTraveled();
		lblDist.setText(String.format("%.2f", Distance) + " mi.");
		
		long timeElapsed = SystemClock.elapsedRealtime() - WorkoutTimer.getBase();
		long pace = 0;
		if (Distance != 0.0)
		{
			pace = (long) (timeElapsed/Distance);
		}
		int hours = (int) (pace / 3600000);
		int minutes = (int) (pace - hours * 3600000) / 60000;
		int seconds = (int) (pace - hours * 3600000 - minutes * 60000) / 1000;
		lblPace.setText(Integer.toString(hours)+ ":"+Integer.toString(minutes)+ ":"+Integer.toString(seconds));
		//Toast.makeText(getSherlockActivity(), message, Toast.LENGTH_SHORT)
				//.show();
		
		Drawline(PreviousLocation, latLng, pace);
		
	}
	
	private void Drawline(LatLng PreviousLocation, LatLng CurLoc, long pace)
	{
		line.width(5);
		if (pace > 360000)line.color(Color.parseColor("#FF0000 "));
		else if(pace > 42000)line.color(Color.parseColor("#FF5500"));
		else if(pace > 48000)line.color(Color.parseColor("#FFBB00"));
		else if(pace > 54000)line.color(Color.parseColor("#FFFF00"));
		else if(pace > 60000)line.color(Color.parseColor("#BBFF00"));
		else if(pace > 66000)line.color(Color.parseColor("#55FF00"));
		else if(pace > 72000)line.color(Color.parseColor("#00FF00"));
			
        
        line.add(PreviousLocation,CurLoc);
        map.addPolyline(line);
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

	// FOR MOCK LOCATIONS
	private class PushLocations extends AsyncTask<Integer, Integer, Double> {
		@Override
		protected Double doInBackground(Integer... params) {
			PushLocations();
			return null;
		}

		public void PushLocations() {
			mock = new MockLocationProvider(LocationManager.GPS_PROVIDER,
					getActivity());
			// MOCK LOCATION
			sleep();
			mock.pushLocation(38.815899,-89.978631);
			sleep();
			mock.pushLocation(38.815915,-89.97751);
			sleep();
			mock.pushLocation(38.815882,-89.976335);
			sleep();
			mock.pushLocation(38.816275,-89.975825);
			sleep();
			mock.pushLocation(38.816726,-89.975707);
			sleep();
			mock.pushLocation(38.817291,-89.975637);
			sleep();
			mock.pushLocation(38.817909,-89.975648);
			sleep();
			mock.pushLocation(38.818173,-89.976211);
			sleep();
			mock.pushLocation(38.818444,-89.976812);
			sleep();
			mock.pushLocation(38.818825,-89.977596);
			sleep();
			mock.pushLocation(38.819163,-89.978255);
			sleep();
			mock.pushLocation(38.819171,-89.97906);
			sleep();
			mock.pushLocation(38.818979,-89.979891);
			sleep();
			mock.pushLocation(38.818737,-89.980761);
			sleep();
			mock.pushLocation(38.818419,-89.981576);
			sleep();
			mock.pushLocation(38.818097,-89.98186);
			sleep();
			mock.pushLocation(38.817566,-89.981646);
			sleep();
			mock.pushLocation(38.817048,-89.981463);
			sleep();
			mock.pushLocation(38.816609,-89.980954);
			sleep();
			mock.pushLocation(38.815991,-89.979849);
		}

		public void sleep() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void initializelayout()
	{
		
		Start_StopButton.getBackground().setColorFilter(Color.parseColor("#CCFF66"), PorterDuff.Mode.MULTIPLY);
		SubmitWorkout.getBackground().setColorFilter(Color.parseColor("#E65050"), PorterDuff.Mode.MULTIPLY);
	}
	

}