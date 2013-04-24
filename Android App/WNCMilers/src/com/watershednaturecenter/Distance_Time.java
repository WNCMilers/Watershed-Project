package com.watershednaturecenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class Distance_Time extends SherlockFragment
{
	private GoogleMap map;
	private UiSettings mUiSettings;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.distance_time, container, false);
		//map.setMyLocationButtonEnabled(true);
		
		 if (checkReady()) {
			 map.setMyLocationEnabled(true);
	        }
		return view;
	}
	
	
	
	
	 public void onDestroyView() {
	        super.onDestroyView(); 
	        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	        ft.remove(fragment);
	        ft.commit();
	}
	
	private boolean checkReady() {
        if (map == null) {
            return false;
        }
        return true;
    }
	
	public void setMyLocationLayerEnabled(View v) {
        if (!checkReady()) {
            return;
        }
        // Enables/disables the my location layer (i.e., the dot/chevron on the map). If enabled, it
        // will also cause the my location button to show (if it is enabled); if disabled, the my
        // location button will never show.
        map.setMyLocationEnabled(((CheckBox) v).isChecked());
	}
	
};