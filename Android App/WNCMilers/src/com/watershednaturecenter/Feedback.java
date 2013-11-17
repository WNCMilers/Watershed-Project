package com.watershednaturecenter;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Feedback extends Activity{

	private String yourName, yourEmail, yourFeedbackType, yourFeedback, imageFilePath;
	private EditText nameField, emailAddressField, feedbackMessageField, attachmentPath;
	private Spinner feedbackTypeSpinner;
	private Button feedbackSubmitButton;
	private ImageButton attachImageButton;
	private static int PICK_IMAGE = 1;
	private static int SEND_EMAIL = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		nameField = (EditText) findViewById(R.id.yourNameFeedback);
		emailAddressField = (EditText) findViewById(R.id.yourEmailFeedback);
		feedbackTypeSpinner = (Spinner) findViewById(R.id.feedbackType);
		feedbackMessageField = (EditText) findViewById(R.id.feedbackDetails);
		attachImageButton = (ImageButton) findViewById(R.id.attachImageButton1);
		feedbackSubmitButton = (Button) findViewById(R.id.feedbackSubmitButton);
		attachmentPath = (EditText) findViewById(R.id.imageAttachmentPath);
		
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
            	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                	Toast.makeText(Feedback.this, "No Email Clients Installed", Toast.LENGTH_SHORT).show();
                }
                
              	//Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
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
		        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
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
			Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
