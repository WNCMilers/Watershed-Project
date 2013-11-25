package com.watershednaturecenter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.app.AlertDialog;
import android.app.Notification.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter.LengthFilter;
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
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.watershednaturecenter.Dialogs.LoginDialog;
import com.watershednaturecenter.Dialogs.SubmitWorkoutDialog;
import com.watershednaturecenter.GPSItems.CoordinateInformation;
import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.MockLocationProvider;
import com.watershednaturecenter.GPSItems.Polygon;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

public class Workout extends SherlockFragment implements LocationListener {
	private static final int GPS_ENABLE = 1;
	private Button Start_StopButton;
	private Button SubmitWorkout;
	private Button RedeemButton;
	private Chronometer WorkoutTimer;
	private long stoppedTime;
	private ProgressDialog progress;
	private TextView lblDist;
	private TextView lblPace;
	private TextView lblTotalWNCMiles;
	private ProgressBar TotalMilesProgressBar;
	ArrayList<PolylineOptions> PolyLines;
	//private PolylineOptions line;
	

	// FOR pushing MOCK LOCATIOn
	MockLocationProvider mock;

	private LocationManager locationManager;
	private HealthGraphApi APIWORKER;
	private WorkoutInfo currentWorkoutInfoWNC;
	private WorkoutInfo currentWorkoutInfoRK;
	private Polygon WNCboundaries;
	private GoogleMap map;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentWorkoutInfoWNC = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutWNC();
		currentWorkoutInfoRK = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutRK(); 
		
		
		View view = inflater.inflate(R.layout.workout, container, false);
		RedeemButton = (Button)view.findViewById(R.id.btnRedeemMembership);
		RedeemButton.setVisibility(View.INVISIBLE);
		
		lblDist = (TextView) view.findViewById(R.id.lblDist);
		lblPace = (TextView) view.findViewById(R.id.lblPace);
		lblTotalWNCMiles = (TextView) view.findViewById(R.id.lblMilesCompleted);
		if (currentWorkoutInfoWNC.TotalWNCMilesForUser >= 25.0 && currentWorkoutInfoWNC.isMembershipRedeemed == false)
		{
			RedeemButton.setVisibility(View.VISIBLE);
		}
		lblTotalWNCMiles.setText(String.format("%.2f mi out of 25.00 mi completed", currentWorkoutInfoWNC.TotalWNCMilesForUser));
		TotalMilesProgressBar = (ProgressBar)view.findViewById(R.id.progressBarMilesCompleted);
		TotalMilesProgressBar.setMax(25000);
		
		
		SupportMapFragment fm = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		map = fm.getMap();
		map.setMyLocationEnabled(true);
		
		
		// runKeeperwebView = (WebView) view.findViewById(R.id.webView);
		APIWORKER = ((WNC_MILERS) getActivity().getApplication())
				.get_APIWORKER();
		WNCboundaries = ((WNC_MILERS) getActivity().getApplication())
				.get_WNCboundaries();

		WorkoutTimer = (Chronometer) view.findViewById(R.id.WorkoutTimer);

		
		Start_StopButton = (Button) view.findViewById(R.id.Start_StopWorkout);
		Start_StopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onClickStart_StopTrackingBtn();
			}
		});
		
		RedeemButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), Membership_Signup.class);
            	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          	   	startActivity(i);
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
		if(view != null) { return view;}
		else return null;
	}

	public void checkGpsEnabled(){
		 String locationProviders = Settings.System.getString(getActivity().getContentResolver(),
				   Settings.System.LOCATION_PROVIDERS_ALLOWED);

		// check if enabled and if not prompt user to enable GPS		   
		if (!locationProviders.contains(LocationManager.GPS_PROVIDER)) {
			showEnableGpsPrompt();
		} 
		else{
			runKeeperPrompt();
		}
	}
	
	//source: http://stackoverflow.com/questions/6880232/disable-check-for-mock-location-prevent-gps-spoofing
	public static boolean isMockSettingsON(Context context) {
	  // returns true if mock location enabled, false if not enabled.
	  if (Settings.Secure.getString(context.getContentResolver(),
	                                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
	     return false;
	  else
	     return true;
	}
	
	public void showEnableGpsPrompt(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		
		// Setting Dialog Title
        alertDialog.setTitle("GPS disabled");
        
		// Setting Dialog Message
        alertDialog.setMessage("WNC Milers needs access to your location. Please enable GPS.");
        
        // On pressing Enable button
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.putExtra("gpsEnableRequest", true);
                getActivity().setIntent(intent);
                getActivity().startActivity(intent);   
                //getActivity().finish();

                dialog.dismiss();
            }
        });
        
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            
            Toast.makeText(getSherlockActivity(), "GPS is required for use of WNC Milers Application", Toast.LENGTH_LONG).show();
            }
        });
        
        // Showing Alert Message
        alertDialog.show();	
	}
	
	public void showDisableMockPrompt(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		
		// Setting Dialog Title
        alertDialog.setTitle("Mock locations enabled");
        
		// Setting Dialog Message
        alertDialog.setMessage("WNC Milers does not permit use of mock locations. Please disable Mock Locations.");
        
        // On pressing Enable button
        alertDialog.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                intent.putExtra("mockDisableRequest", true);
                getActivity().setIntent(intent);
                getActivity().startActivity(intent);   
               // getActivity().finish();

                dialog.dismiss();
            }
        });
        
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            
            Toast.makeText(getSherlockActivity(), "Mock locations must be disabled for use of WNC Milers Application", Toast.LENGTH_LONG).show();
            }
        });
        
        // Showing Alert Message
        alertDialog.show();	
	}
	
	@Override
	public void  onResume() {		
		super.onResume();
		
		boolean gpsEnableRequest = this.getActivity().getIntent().getBooleanExtra("gpsEnableRequest", false);
		boolean mockDisableRequest = this.getActivity().getIntent().getBooleanExtra("mockDisableRequest", false);
		
		if(gpsEnableRequest){
			 String locationProviders = Settings.System.getString(getActivity().getContentResolver(),
					   Settings.System.LOCATION_PROVIDERS_ALLOWED);
			
			if(locationProviders.contains(LocationManager.GPS_PROVIDER)){
				runKeeperPrompt();
			}else{
				Toast.makeText(getSherlockActivity(), "GPS is required for use of WNC Milers Application", Toast.LENGTH_LONG).show();
			}
		}
		
		if(mockDisableRequest){
	    	if (Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")){
	    		Toast.makeText(getSherlockActivity(), "Mock locations must be disabled for use of WNC Milers Application", Toast.LENGTH_LONG).show();
	    	}
	    	else{
	    		checkGpsEnabled();
	    	}
		}
	}
	
	public void onClickStart_StopTrackingBtn() {
		checkGpsEnabled();
		
		//TODO: enable this block of code to prevent cheaters and delete checkGpsEnabled above.
//		if(isMockSettingsON(getActivity())){
//			showDisableMockPrompt();
//		}else{
//			checkGpsEnabled();
//		}
	}
	
	public void runKeeperPrompt(){
		if (Start_StopButton.getText().equals("New Workout")){
			if (!LoginStatus())
			{
				DisplayLoginPopup();
			}
			else
			{
				// reset Workouts Each Time
				currentWorkoutInfoWNC.resetWorkoutInfo();
				currentWorkoutInfoRK.resetWorkoutInfo();
				map.clear();
				PolyLines =  new ArrayList<PolylineOptions>();
				
				//TODO need to make function for overall Workout Initialization
				
				Criteria gpsCriteria = getGpsCriteria();
				
				
				
				// Getting LocationManager object from System Service
				// LOCATION_SERVICE. Need to mess with parameters to not record
				// quite as many points.
				
				//TODO: uncomment this to enable Real GPS updates. TODO make able to easily turn on/off mock locations.
				locationManager.requestLocationUpdates(locationManager.getBestProvider(gpsCriteria, true),2000,1,this);
				
				// Push Locations
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
				//try {
					//new PushLocations().execute(1);
				//} catch (Exception e) {
					//e.printStackTrace();
				//}
				Start_StopButton.setText("Pause Workout");
				SubmitWorkout.setEnabled(false);

				progress = new ProgressDialog(getActivity());
				progress.setTitle("Starting Workout");
				progress.setMessage("Wait for GPS fix...");
				progress.show();
			}
			
			
		}
		else if (Start_StopButton.getText().equals("Pause Workout")){
			WorkoutTimer.stop();
			stoppedTime = WorkoutTimer.getBase() - SystemClock.elapsedRealtime();
			//locationManager.removeUpdates(this);
			currentWorkoutInfoRK.wasWorkoutPaused = true;
			currentWorkoutInfoWNC.wasWorkoutPaused = true;
			Start_StopButton.setText("Resume Workout");
			SubmitWorkout.setEnabled(true);
		}
		else {
			WorkoutTimer.start();
			WorkoutTimer.setBase(SystemClock.elapsedRealtime() + stoppedTime);
//			//locationManager.removeUpdates(this);
			Start_StopButton.setText("Pause Workout");
			SubmitWorkout.setEnabled(false);
		}
		
	}
	
	public void DisplayLoginPopup()
	{
		LoginDialog LD = new LoginDialog();
		LD.show(getSherlockActivity().getSupportFragmentManager(),"LoginDialogNotice");
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
			Start_StopButton.setText("New Workout");
			SubmitWorkoutDialog SD = new SubmitWorkoutDialog(this.getSherlockActivity());
			SD.setTargetFragment(this, 0);
			FragmentManager FM = getSherlockActivity().getSupportFragmentManager();
			SD.show(FM,"SubmitWorkoutDialogNotice");
	};

	@Override
	public void onLocationChanged(Location location) {
		if(Start_StopButton.getText().equals("Pause Workout") || Start_StopButton.getText().equals("Resume Workout")){
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
			
			
	
	        
			//First check to see if workout is paused
			if (Start_StopButton.getText().equals("Resume Workout") == false)
			{
				//Check to see if user is in WNC
				if (WNCboundaries.contains(CurrentLocation.GetLatitude(), CurrentLocation.GetLongitude()))
				{
					if (currentWorkoutInfoWNC.JustLeftWatershed == true)
					{
						//if user leaves water shed and reenters
						//adds current location to array twice so that distance is not calcualted from last point in watershed.
						//prevents user from entering watershed, driving to other side, multiple times to get 25 miles.
						currentWorkoutInfoWNC.LocationArray.add(CurrentLocation);
						currentWorkoutInfoWNC.JustLeftWatershed = false;
					}
					
					if(currentWorkoutInfoWNC.wasWorkoutPaused){
						//WorkoutTimer.start();	
						currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
						currentWorkoutInfoWNC.LocationArray.add(CurrentLocation);
						currentWorkoutInfoWNC.wasWorkoutPaused = false;
						currentWorkoutInfoWNC.wasWorkoutPaused = false;
					}
					
					//add to both Run keeper location array, and WNCMilers Location Array
					currentWorkoutInfoWNC.LocationArray.add(CurrentLocation);
					currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
					Double Traveled = currentWorkoutInfoWNC.UpdateDistTraveled();
					currentWorkoutInfoRK.UpdateDistTraveled();
					if (currentWorkoutInfoWNC.TotalWNCMilesForUser+Traveled >= 25.0  && currentWorkoutInfoWNC.isMembershipRedeemed == false)
					{
						RedeemButton.setVisibility(View.VISIBLE);
					}
					lblTotalWNCMiles.setText(String.format("%.2f mi out of 25.00 mi completed", currentWorkoutInfoWNC.TotalWNCMilesForUser+Traveled));
					TotalMilesProgressBar.setProgress((int)((currentWorkoutInfoWNC.TotalWNCMilesForUser+Traveled)*1000));
					}
					else//Just add into Run Keeper Location Array
					{
						if(currentWorkoutInfoWNC.LocationArray.size() > 0)
						{
							currentWorkoutInfoWNC.JustLeftWatershed = true;
						}
						
						if(currentWorkoutInfoWNC.wasWorkoutPaused){
							//WorkoutTimer.start();	
							currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
							currentWorkoutInfoWNC.wasWorkoutPaused = false;
						}
						currentWorkoutInfoRK.LocationArray.add(CurrentLocation);
						currentWorkoutInfoRK.UpdateDistTraveled();
					}
			
			
				LatLng PreviousLocation;
				if (currentWorkoutInfoRK.LocationArray.size() >1)
					PreviousLocation = new LatLng(currentWorkoutInfoRK.LocationArray.get(currentWorkoutInfoRK.LocationArray.size()-2).GetLatitude(),currentWorkoutInfoRK.LocationArray.get(currentWorkoutInfoRK.LocationArray.size()-2).GetLongitude());
				else
					PreviousLocation = latLng;
	        
	
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
			
			Drawline(PreviousLocation, latLng, hours, minutes);
			}
		}else{
			if(!Start_StopButton.getText().equals("New Workout")){
				currentWorkoutInfoWNC.wasWorkoutPaused = false;
			}
		}
			
	}
	
	
	private void Drawline(LatLng PreviousLocation, LatLng CurLoc, int hours, int minutes)
	{
		PolylineOptions pline = new PolylineOptions();
		
		pline.width(5);
		if (hours ==0)
		{
			if (minutes < 5)pline.color(Color.rgb(255, 0, 0));
			
			else if(minutes < 6)pline.color(Color.rgb(255, 50, 0));
			else if(minutes < 7)pline.color(Color.rgb(255, 101, 0));
			else if(minutes < 8)pline.color(Color.rgb(255, 153, 0));
			else if(minutes < 9)pline.color(Color.rgb(255, 204, 0));
			else if(minutes < 10)pline.color(Color.rgb(255, 255, 0));
			else if(minutes < 11)pline.color(Color.rgb(204, 255, 0));
			else if(minutes < 12)pline.color(Color.rgb(153, 255, 0));
			else if(minutes < 13)pline.color(Color.rgb(101, 255, 0));
			else if(minutes < 14)pline.color(Color.rgb(50, 255, 0));
			else pline.color(Color.rgb(0, 255, 0));
		}
		else pline.color(Color.rgb(0, 255, 0));
		//else if(pace < 72000)line.color(Color.parseColor("#00FF00"));
		
			
		
		pline.add(PreviousLocation,CurLoc);
		PolyLines.add(pline);
        map.addPolyline(PolyLines.get(PolyLines.size()-1));
	}

	@Override
	public void onProviderDisabled(String provider) {
		String locationProviders = Settings.System.getString(getActivity().getContentResolver(),
				   Settings.System.LOCATION_PROVIDERS_ALLOWED);

		// check if enabled and if not prompt user to enable GPS		   
		if (!locationProviders.contains(LocationManager.GPS_PROVIDER)) {
			if(Start_StopButton.getText().equals("Pause Workout")){
				Start_StopButton.performClick();
			}
			showEnableGpsPrompt();
		}

	}

	@Override
	public void onProviderEnabled(String provider) {
		 
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
			mock.pushLocation(38.815912000, -89.977662000);sleep();
			mock.pushLocation(38.815915000, -89.977555000);sleep();
			mock.pushLocation(38.815922000, -89.977462000);sleep();
			mock.pushLocation(38.815915000, -89.977363000);sleep();
			mock.pushLocation(38.815914000, -89.977260000);sleep();
			mock.pushLocation(38.815907000, -89.977154000);sleep();
			mock.pushLocation(38.815903000, -89.977060000);sleep();
			mock.pushLocation(38.815901000, -89.976947000);sleep();
			mock.pushLocation(38.815894000, -89.976855000);sleep();
			mock.pushLocation(38.815891000, -89.976741000);sleep();
			mock.pushLocation(38.815887000, -89.976644000);sleep();
			mock.pushLocation(38.815884000, -89.976523000);sleep();
			mock.pushLocation(38.815875000, -89.976392000);sleep();
			mock.pushLocation(38.815885000, -89.976312000);sleep();
			mock.pushLocation(38.815929000, -89.976240000);sleep();
			mock.pushLocation(38.815977000, -89.976173000);sleep();
			mock.pushLocation(38.816008000, -89.976078000);sleep();
			mock.pushLocation(38.816059000, -89.976002000);sleep();
			mock.pushLocation(38.816108000, -89.975915000);sleep();
			mock.pushLocation(38.816165000, -89.975848000);sleep();
			mock.pushLocation(38.816248000, -89.975813000);sleep();
			mock.pushLocation(38.816320000, -89.975779000);sleep();
			mock.pushLocation(38.816392000, -89.975743000);sleep();
			mock.pushLocation(38.816483000, -89.975734000);sleep();
			mock.pushLocation(38.816593000, -89.975726000);sleep();
			mock.pushLocation(38.816676000, -89.975715000);sleep();
			mock.pushLocation(38.816764000, -89.975683000);sleep();
			mock.pushLocation(38.816861000, -89.975654000);sleep();
			mock.pushLocation(38.816958000, -89.975633000);sleep();
			mock.pushLocation(38.817055000, -89.975633000);sleep();
			mock.pushLocation(38.817133000, -89.975629000);sleep();
			mock.pushLocation(38.817233000, -89.975629000);sleep();
			mock.pushLocation(38.817322000, -89.975624000);sleep();
			mock.pushLocation(38.817245000, -89.975473000);sleep();
			mock.pushLocation(38.817161000, -89.975398000);sleep();
			mock.pushLocation(38.817053000, -89.975227000);sleep();
			mock.pushLocation(38.816998000, -89.975071000);sleep();
			mock.pushLocation(38.817090000, -89.974905000);sleep();
			mock.pushLocation(38.817138000, -89.974738000);sleep();
			mock.pushLocation(38.816973000, -89.974341000);sleep();
			mock.pushLocation(38.817170000, -89.973960000);sleep();
			mock.pushLocation(38.817430000, -89.973630000);sleep();
			mock.pushLocation(38.817617000, -89.973365000);sleep();
			mock.pushLocation(38.817900000, -89.973000000);sleep();
			mock.pushLocation(38.818169000, -89.972705000);sleep();
			mock.pushLocation(38.817980000, -89.973110000);sleep();
			mock.pushLocation(38.817680000, -89.973505000);sleep();
			mock.pushLocation(38.817510000, -89.973730000);sleep();
			mock.pushLocation(38.817270000, -89.974040000);sleep();
			mock.pushLocation(38.817036000, -89.974341000);sleep();
			mock.pushLocation(38.817170000, -89.974690000);sleep();
			mock.pushLocation(38.817134000, -89.974902000);sleep();
			mock.pushLocation(38.817038000, -89.975076000);sleep();
			mock.pushLocation(38.817082000, -89.975194000);sleep();
			mock.pushLocation(38.817184000, -89.975347000);sleep();
			mock.pushLocation(38.817282000, -89.975441000);sleep();
			mock.pushLocation(38.817356000, -89.975616000);sleep();
			mock.pushLocation(38.817424000, -89.975607000);sleep();
			mock.pushLocation(38.817491000, -89.975598000);sleep();
			mock.pushLocation(38.817564000, -89.975599000);sleep();
			mock.pushLocation(38.817649000, -89.975595000);sleep();
			mock.pushLocation(38.817739000, -89.975601000);sleep();
			mock.pushLocation(38.817781000, -89.975607000);sleep();
			/*
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
			mock.pushLocation(38.815991,-89.979849);*/
		}

		public void sleep() {
			try {
				Thread.sleep(1000);
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
		TotalMilesProgressBar.setProgress((int)((currentWorkoutInfoWNC.TotalWNCMilesForUser)*1000));
	}

	@Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = (getFragmentManager().findFragmentById(R.id.map));  
        if (f != null && f.isResumed()) 
            getFragmentManager().beginTransaction().remove(f).commit();
    }

}