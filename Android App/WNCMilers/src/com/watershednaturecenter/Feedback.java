package com.watershednaturecenter;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Feedback extends SherlockFragment{

	private String yourName, yourEmail, yourFeedbackType, yourFeedback;
	private EditText nameField, emailAddressField, feedbackMessageField;
	private Spinner feedbackTypeSpinner;
	private Button feedbackSubmitButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.feedback, container, false);
		
		nameField = (EditText) view.findViewById(R.id.yourNameFeedback);
		emailAddressField = (EditText) view.findViewById(R.id.yourEmailFeedback);
		feedbackTypeSpinner = (Spinner) view.findViewById(R.id.feedbackType);
		feedbackMessageField = (EditText) view.findViewById(R.id.feedbackDetails);
		feedbackSubmitButton = (Button) view.findViewById(R.id.feedbackSubmitButton);
		
		feedbackSubmitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	String feedbackDestinationEmailList[] = {"Kharmon@siue.edu", "skatzer@siue.edu", "dmoyer@siue.edu"};  
                
            	//Hides softKeyboard
            	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(feedbackSubmitButton.getWindowToken(), 0);
            		
                getDataFromForm();
                
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
                emailIntent.setType("plain/text");  
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, feedbackDestinationEmailList);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Application Feedback (" + yourFeedbackType + ")" ); 
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, yourFeedback + "\n\n" + yourName + "\n" + yourEmail);
                startActivity(Intent.createChooser(emailIntent, "Send your email using:"));  
                
              	Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
		return view;
	}
	
	//Function to load data from the textFields/Spinners into local variables
	public void getDataFromForm(){
		yourName = nameField.getText().toString().trim();
		yourEmail = emailAddressField.getText().toString().trim();
		yourFeedbackType = feedbackTypeSpinner.getSelectedItem().toString();
		yourFeedback = feedbackMessageField.getText().toString().trim();
	}
	
}
