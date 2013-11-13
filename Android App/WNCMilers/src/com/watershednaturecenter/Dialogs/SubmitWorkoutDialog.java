package com.watershednaturecenter.Dialogs;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.watershednaturecenter.Login;
import com.watershednaturecenter.MainActivity;
import com.watershednaturecenter.MySQLConnector;
import com.watershednaturecenter.R;
import com.watershednaturecenter.WNC_MILERS;
import com.watershednaturecenter.Workout;
import com.watershednaturecenter.GPSItems.HealthGraphApi;
import com.watershednaturecenter.GPSItems.WorkoutInfo;




public class SubmitWorkoutDialog extends DialogFragment {
	private WorkoutInfo currentWorkoutInfoWNC;
	private WorkoutInfo currentWorkoutInfoRK;
	private HealthGraphApi APIWORKER;
	private Button SubmitBtn;
	Button mButton;  
	Spinner WorkoutType;  
	EditText WorkoutNotes;
	 onSubmitListener mListener;
	 Activity Parent;
	 
	 public SubmitWorkoutDialog(Activity parent) {
		Parent = parent;
		SubmitBtn = (Button) parent.findViewById(R.id.SubmitWorkout);
	}
	 
	 
	 interface onSubmitListener {  
	  void setOnSubmitListener(String arg);  
	 }  
	 @Override  
	 public Dialog onCreateDialog(Bundle savedInstanceState) { 
	 currentWorkoutInfoWNC = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutWNC();
	 currentWorkoutInfoRK = ((WNC_MILERS) getActivity().getApplication()).get_CurrentWorkoutRK();
	 APIWORKER = ((WNC_MILERS) getActivity().getApplication()).get_APIWORKER();
	 final Dialog dialog = new Dialog(getActivity());  
	  dialog.setTitle("Details about workout");
	  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	  dialog.setContentView(R.layout.submit_workout_dialog); 
	  dialog.setCanceledOnTouchOutside(false);
	  dialog.show();  
	  mButton = (Button) dialog.findViewById(R.id.button1);  
	  WorkoutType = (Spinner) dialog.findViewById(R.id.WorkoutType); 
	  WorkoutNotes = (EditText) dialog.findViewById(R.id.WorkoutNotesTxt);
	  mButton.setOnClickListener(new OnClickListener() {  
	  
	   @Override  
	   public void onClick(View v) {
		 currentWorkoutInfoRK.SetWorkoutType(WorkoutType.getSelectedItem().toString());
		 currentWorkoutInfoWNC.SetWorkoutType(WorkoutType.getSelectedItem().toString());
		 currentWorkoutInfoRK.SetEquipmentUsed("None");
		 currentWorkoutInfoRK.SetWorkoutNotes(WorkoutNotes.getText().toString());
	 	 APIWORKER.PostWorkout(currentWorkoutInfoRK.LocationArray,currentWorkoutInfoRK);
	 	 if (currentWorkoutInfoWNC.GetCurDistTraveled() > 0.00)
	 	 {
	 		 //only submit to WNC if they actually traveled recordable distance within WNC
	 		 MySQLConnector MYSQLCOMM = new MySQLConnector(getActivity().getSupportFragmentManager());
	 	     MYSQLCOMM.UpdateMiles();
	 	 }
		 SubmitBtn.setEnabled(false);
		 //TODO: Add Check to see if any distance was done inside WNC if so, submit to database.
		 dismiss();
	   }  
	  });
	  return dialog;  
	 }
	}  
	
