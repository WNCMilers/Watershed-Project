package com.watershednaturecenter;

import com.actionbarsherlock.app.SherlockFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Membership_Signup extends SherlockFragment
{
	private String fullName, addressLine1, addressLine2, city, state, zipCode, phoneNumber, emailAddress, membershipLevel;
	private Button submitButton;
	private EditText nameField, addressLine1Field, addressLine2Field, cityField, zipCodeField, phoneNumberField, emailAddressField;
	private Spinner stateSpinner, membershipLevelSpinner;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.membership_sign_up, container, false);
		
		nameField = (EditText) view.findViewById(R.id.FirstNameLastName);
		addressLine1Field = (EditText) view.findViewById(R.id.addressLine1);
		addressLine2Field = (EditText) view.findViewById(R.id.addressLine2);
		cityField = (EditText) view.findViewById(R.id.CityTextBox);
		stateSpinner = (Spinner) view.findViewById(R.id.StateSelection);
		zipCodeField = (EditText) view.findViewById(R.id.zipCode);
		phoneNumberField = (EditText) view.findViewById(R.id.PhoneNumber);
		emailAddressField = (EditText) view.findViewById(R.id.EmailAddress);
		membershipLevelSpinner = (Spinner) view.findViewById(R.id.MembershipLevelSelection);
		
		submitButton = (Button) view.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            	getDataFromForm();
        	}
        });
		return view;
	}
	
	//Function to load data from the textFields/Spinners into local variables
	public void getDataFromForm(){
		fullName = nameField.getText().toString().trim();
		addressLine1 = addressLine1Field.getText().toString().trim();
		addressLine2 = addressLine2Field.getText().toString().trim();
		city = cityField.getText().toString().trim();
		state = stateSpinner.getSelectedItem().toString();
		zipCode = zipCodeField.getText().toString().trim();
		phoneNumber = phoneNumberField.getText().toString().trim();
		emailAddress = emailAddressField.getText().toString().trim();
		membershipLevel = membershipLevelSpinner.getSelectedItem().toString();
		
		if (!checkForBlankRequiredFields()){
			Toast.makeText(getSherlockActivity(), "Thanks " + fullName, Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean checkForBlankRequiredFields(){
		boolean errorPresent = false;
		
		if (fullName.isEmpty()){
			nameField.setError("Name cannot be blank");
			errorPresent = true;
		}
		if (addressLine1.isEmpty()){
			addressLine1Field.setError("Address Line 1 cannot be blank");
			errorPresent = true;
		}
		if (city.isEmpty()){
			cityField.setError("City cannot be blank");
			errorPresent = true;
		}
		if (zipCode.isEmpty()){
			zipCodeField.setError("Zip code cannot be blank");
			errorPresent = true;
		}
		if (phoneNumber.isEmpty()){
			phoneNumberField.setError("Phone Number cannot be blank");
			errorPresent = true;
		}
		if (emailAddress.isEmpty()){
			emailAddressField.setError("Email Address cannot be blank");
			errorPresent = true;
		}
		
		if (errorPresent){
			Toast.makeText(getSherlockActivity(), "Please correct error on form", Toast.LENGTH_SHORT).show();
		}
		return errorPresent;
	}
	
	public void sendRegistrationForm(){
		Toast.makeText(getSherlockActivity(), "Thanks " + fullName, Toast.LENGTH_SHORT).show();
	}
}
