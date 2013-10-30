package com.watershednaturecenter;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Membership_Signup2 extends Activity
{
	// Regular Expression
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{3}-\\d{4}";
    private static final String ZIP_REGEX = "\\d{5}";
    private static final String CITY_REGEX = "^(?:[a-zA-Z]+(?:[.'\\-,])?\\s?)+$";
    
	private String firstName, lastName, addressLine1, addressLine2, city, state, zipCode, phoneNumber, emailAddress, membershipLevel;
	private Button submitButton;
	private EditText firstNameField, lastNameField, addressLine1Field, addressLine2Field, cityField, zipCodeField, phoneNumberField, emailAddressField;
	private Spinner stateSpinner, membershipLevelSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.membership_sign_up);
		firstNameField = (EditText) findViewById(R.id.firstName);
		lastNameField = (EditText) findViewById(R.id.lastName);
		addressLine1Field = (EditText) findViewById(R.id.addressLine1);
		addressLine2Field = (EditText) findViewById(R.id.addressLine2);
		cityField = (EditText) findViewById(R.id.CityTextBox);
		stateSpinner = (Spinner) findViewById(R.id.StateSelection);
		zipCodeField = (EditText) findViewById(R.id.zipCode);
		phoneNumberField = (EditText) findViewById(R.id.PhoneNumber);
		emailAddressField = (EditText) findViewById(R.id.EmailAddress);
		membershipLevelSpinner = (Spinner) findViewById(R.id.MembershipLevelSelection);
		
		stateSpinner.setSelection(13);
		
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Hides softKeyboard
            	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
            		
              	Toast.makeText(getApplicationContext(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            	getDataFromForm();
        	}
        });
	}
	
	//Function to load data from the textFields/Spinners into local variables
	public void getDataFromForm(){
		firstName = firstNameField.getText().toString().trim();
		lastName = lastNameField.getText().toString().trim();
		addressLine1 = addressLine1Field.getText().toString().trim();
		addressLine2 = addressLine2Field.getText().toString().trim();
		city = cityField.getText().toString().trim();
		state = stateSpinner.getSelectedItem().toString();
		zipCode = zipCodeField.getText().toString().trim();
		phoneNumber = phoneNumberField.getText().toString().trim();
		emailAddress = emailAddressField.getText().toString().trim();
		membershipLevel = membershipLevelSpinner.getSelectedItem().toString();
		
		if (!completeFormValidityCheck()){
			Toast.makeText(this, "Thanks " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean completeFormValidityCheck(){
		boolean errorPresent = false;
		
		if (firstName.isEmpty()){
			firstNameField.setError(null);
			firstNameField.setError("First name cannot be blank");
			errorPresent = true;
		}
		if (lastName.isEmpty()){
			lastNameField.setError(null);
			lastNameField.setError("Last name cannot be blank");
			errorPresent = true;
		}
		
		if (addressLine1.isEmpty()){
			addressLine1Field.setError(null);
			addressLine1Field.setError("Address Line 1 cannot be blank");
			errorPresent = true;
		}
		
		if (city.isEmpty()){
			cityField.setError(null);
			cityField.setError("City cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(CITY_REGEX, city)){
				cityField.setError(null);
				cityField.setError("Improper City Name Format");
			}
		}
		
		if (zipCode.isEmpty()){
			zipCodeField.setError(null);
			zipCodeField.setError("Zip code cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(ZIP_REGEX, zipCode)){
				zipCodeField.setError(null);
				zipCodeField.setError("Improper format. Ex: 62025");
			}
		}
		
		if (phoneNumber.isEmpty()){
			phoneNumberField.setError(null);
			phoneNumberField.setError("Phone Number cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(PHONE_REGEX, phoneNumber)){
				phoneNumberField.setError(null);
				phoneNumberField.setError("Improper format. Ex: 555-555-5555");
			}
		}
		
		if (emailAddress.isEmpty()){
			emailAddressField.setError(null);
			emailAddressField.setError("Email Address cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(EMAIL_REGEX, emailAddress)){
				emailAddressField.setError(null);
				emailAddressField.setError("Improper format. Ex: john@domain.com");
			}
		}
		
		if (errorPresent){
			Toast.makeText(this, "Please correct error on form", Toast.LENGTH_SHORT).show();
		}
		return errorPresent;
	}
	
	public boolean checkValidity(String regex, String text){
		return Pattern.matches(regex, text);
	}
	
	public void sendRegistrationForm(){
		//Toast.makeText(getSherlockActivity(), "Thanks " + fullName, Toast.LENGTH_SHORT).show();
	}
}