package com.watershednaturecenter.Dialogs;



import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import com.watershednaturecenter.MySQLConnector;
import com.watershednaturecenter.R;
import com.watershednaturecenter.WNC_MILERS;
import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.WorkoutInfo;




public class SubmitWorkoutDialog extends DialogFragment{
	private WorkoutInfo currentWorkoutInfoWNC;
	private WorkoutInfo currentWorkoutInfoRK;
	private HealthGraphApi APIWORKER;
	Button mButton;  
	Spinner WorkoutType;  
	 onSubmitListener mListener;  
	 String text = "";  
	  
	 interface onSubmitListener {  
	  void setOnSubmitListener(String arg);  
	 }  
	  
	 @Override  
	 public Dialog onCreateDialog(Bundle savedInstanceState) { 
	 currentWorkoutInfoWNC = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutWNC();
	 currentWorkoutInfoRK = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutRK();
	 APIWORKER = ((WNC_MILERS) getActivity().getApplication()).get_APIWORKER();
	  final Dialog dialog = new Dialog(getActivity());  
	  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
	  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	  dialog.setContentView(R.layout.submit_workout_dialog);  
	  dialog.getWindow().setBackgroundDrawable(  
	    new ColorDrawable(Color.TRANSPARENT));  
	  dialog.show();  
	  mButton = (Button) dialog.findViewById(R.id.button1);  
	  WorkoutType = (Spinner) dialog.findViewById(R.id.WorkoutType);  
	  mButton.setOnClickListener(new OnClickListener() {  
	  
	   @Override  
	   public void onClick(View v) {
		 currentWorkoutInfoRK.SetWorkoutType(WorkoutType.getSelectedItem().toString());
		 currentWorkoutInfoRK.SetEquipmentUsed("None");
	 	 APIWORKER.PostWorkout(currentWorkoutInfoRK.LocationArray,currentWorkoutInfoRK);
		 MySQLConnector MYSQLCOMM = new MySQLConnector(getActivity().getFragmentManager());
		 MYSQLCOMM.UpdateMiles();
		 //TODO: Add Check to see if any distance was done inside WNC if so, submit to database.
	    dismiss();  
	   }  
	  });  
	  return dialog;  
	 }  
	}  
	
