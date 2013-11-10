package com.watershednaturecenter;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Feedback extends SherlockFragment{

	private String yourName, yourEmail, yourFeedbackType, yourFeedback, imageFilePath;
	private EditText nameField, emailAddressField, feedbackMessageField, attachmentPath;
	private Spinner feedbackTypeSpinner;
	private Button feedbackSubmitButton;
	private ImageButton attachImageButton;
	private static int PICK_IMAGE = 1;
	private static int SEND_EMAIL = 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.feedback, container, false);
		
		nameField = (EditText) view.findViewById(R.id.yourNameFeedback);
		emailAddressField = (EditText) view.findViewById(R.id.yourEmailFeedback);
		feedbackTypeSpinner = (Spinner) view.findViewById(R.id.feedbackType);
		feedbackMessageField = (EditText) view.findViewById(R.id.feedbackDetails);
		attachImageButton = (ImageButton) view.findViewById(R.id.attachImageButton1);
		feedbackSubmitButton = (Button) view.findViewById(R.id.feedbackSubmitButton);
		attachmentPath = (EditText) view.findViewById(R.id.imageAttachmentPath);
		
		attachImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	 Intent imagePicker = new Intent();
            	 imagePicker.setType("image/*");
            	 imagePicker.setAction(Intent.ACTION_GET_CONTENT);
            	 startActivityForResult(Intent.createChooser(imagePicker, "Select Picture"), PICK_IMAGE);
            }
        });
		
		feedbackSubmitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	String feedbackDestinationEmailList[] = {"wncdevelopers@gmail.com"};  
                
            	//Hides softKeyboard
            	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(feedbackSubmitButton.getWindowToken(), 0);
            		
                getDataFromForm();
                
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
                emailIntent.setType("plain/text");  
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, feedbackDestinationEmailList);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Application Feedback (" + yourFeedbackType + ")" ); 
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, yourFeedback + "\n\n" + yourName + "\n" + yourEmail);
                
                Uri lastsavedUri = Uri.parse(new File("file://" + imageFilePath).toString());
                emailIntent.putExtra(Intent.EXTRA_STREAM, lastsavedUri);
                try{
                	startActivityForResult(Intent.createChooser(emailIntent, "Send your email using:"), SEND_EMAIL);  
                }
                catch (Exception e){
                	Toast.makeText(getSherlockActivity(), "No Email Clients Installed", Toast.LENGTH_SHORT).show();
                }
                
              	//Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
	        Uri _uri = data.getData();

	        if (_uri != null) {
		        //User had pick an image.
		        Cursor cursor = getActivity().getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
		        cursor.moveToFirst();
	
		        //Link to the image
		        imageFilePath = cursor.getString(0);
		        cursor.close();
	        }
	        attachmentPath.setHint("Image Attached");
	    }
		if(requestCode == SEND_EMAIL){
			nameField.setText(null);
			emailAddressField.setText(null);
			feedbackMessageField.setText(null);
			imageFilePath = null;
			attachmentPath.setHint("Attach Screenshot");
			feedbackTypeSpinner.setSelection(0);
			Toast.makeText(getSherlockActivity(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
