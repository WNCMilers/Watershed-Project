package com.watershednaturecenter;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

import com.actionbarsherlock.app.SherlockFragment;

public class Distance_Time extends SherlockFragment implements LocationListener
{
	private GoogleMap map;
	private UiSettings mUiSettings;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.distance_time, container, false);
		SupportMapFragment fm = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
		map = fm.getMap();
		map.setMyLocationEnabled(true);
		
		// Getting LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        //Criteria criteria = new Criteria();

        // Getting the name of the best provider
        //String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        //Location location = locationManager.getLastKnownLocation(provider);

        //if(location!=null){
        //    onLocationChanged(location);
        //	LatLng latLng = new LatLng(38.81758,-89.97854);
        //	map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //	map.animateCamera(CameraUpdateFactory.zoomTo(16));
        //}
        //locationManager.requestLocationUpdates(provider, 2000, 0, this);
		return view;
	}
	
	@Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

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

	
	
	
	
	 public void onDestroyView() {
	        super.onDestroyView(); 
	        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	        ft.remove(fragment);
	        ft.commit();
	}
	
};