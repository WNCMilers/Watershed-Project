package com.watershednaturecenter;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;;

public class Membership_Signup extends Activity
{
	// Regular Expression
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{3}-\\d{4}";
    private static final String ZIP_REGEX = "\\d{5}";
    private static final String CITY_REGEX = "^(?:[a-zA-Z]+(?:[.'\\-,])?\\s?)+$";
    
    private MembershipInfo member = new MembershipInfo();
	//private String firstName, lastName, addressLine1, addressLine2, city, state, zipCode, phoneNumber, emailAddress, membershipLevel;
	private Button submitButton;
	private EditText firstNameField, lastNameField, addressLine1Field, addressLine2Field, cityField, 
						zipCodeField, phoneNumberField, emailAddressField;
	private Spinner designationSpinner, stateSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.membership_sign_up);
        
		designationSpinner = (Spinner) findViewById(R.id.DesignationSelection);
		firstNameField = (EditText) findViewById(R.id.firstName);
		lastNameField = (EditText) findViewById(R.id.lastName);
		addressLine1Field = (EditText) findViewById(R.id.addressLine1);
		addressLine2Field = (EditText) findViewById(R.id.addressLine2);
		cityField = (EditText) findViewById(R.id.CityTextBox);
		stateSpinner = (Spinner) findViewById(R.id.StateSelection);
		zipCodeField = (EditText) findViewById(R.id.zipCode);
		phoneNumberField = (EditText) findViewById(R.id.PhoneNumber);
		emailAddressField = (EditText) findViewById(R.id.EmailAddress);
		//membershipLevelSpinner = (Spinner) view.findViewById(R.id.MembershipLevelSelection);
				
		stateSpinner.setSelection(13);
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Hides softKeyboard
            	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
            		
              	//Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            	if (getDataFromForm()){
            	    MySQLConnector MYSQLCOMM = new MySQLConnector();
	            	MYSQLCOMM.RegisterMember(member, Membership_Signup.this);
            	}
        	}
        });
	}
	
	//Function to load data from the textFields/Spinners into local variables
	public boolean getDataFromForm(){
		if (!completeFormValidityCheck()){
			
			member.designation = designationSpinner.getSelectedItem().toString();
			member.firstName = firstNameField.getText().toString().trim();
			member.lastName = lastNameField.getText().toString().trim();
			member.addressLine1 = addressLine1Field.getText().toString().trim();
			member.addressLine2 = addressLine2Field.getText().toString().trim();
			member.city = cityField.getText().toString().trim();
			member.state = stateSpinner.getSelectedItem().toString();
			member.zipCode = zipCodeField.getText().toString().trim();
			member.phoneNumber = phoneNumberField.getText().toString().trim();
			member.emailAddress = emailAddressField.getText().toString().trim();
			member.membershipLevel = "WNC Miler";
			//member.membershipLevel = membershipLevelSpinner.getSelectedItem().toString();
			
			return true;
		}else{
			return false;
		}
	}
	
	public boolean completeFormValidityCheck(){
		boolean errorPresent = false;
		
		if (firstNameField.getText().toString().trim().isEmpty()){
			firstNameField.setError(null);
			firstNameField.setError("First name cannot be blank");
			errorPresent = true;
		}
		if (lastNameField.getText().toString().trim().isEmpty()){
			lastNameField.setError(null);
			lastNameField.setError("Last name cannot be blank");
			errorPresent = true;
		}
		
		if (addressLine1Field.getText().toString().trim().isEmpty()){
			addressLine1Field.setError(null);
			addressLine1Field.setError("Address Line 1 cannot be blank");
			errorPresent = true;
		}
		
		if (cityField.getText().toString().trim().isEmpty()){
			cityField.setError(null);
			cityField.setError("City cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(CITY_REGEX, cityField.getText().toString().trim())){
				cityField.setError(null);
				cityField.setError("Improper City Name Format");
				errorPresent = true;
			}
		}
		
		if (zipCodeField.getText().toString().trim().isEmpty()){
			zipCodeField.setError(null);
			zipCodeField.setError("Zip code cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(ZIP_REGEX, zipCodeField.getText().toString().trim())){
				zipCodeField.setError(null);
				zipCodeField.setError("Improper format. Ex: 62025");
				errorPresent = true;
			}
		}
		
		if (phoneNumberField.getText().toString().trim().isEmpty()){
			phoneNumberField.setError(null);
			phoneNumberField.setError("Phone Number cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(PHONE_REGEX, phoneNumberField.getText().toString().trim())){
				phoneNumberField.setError(null);
				phoneNumberField.setError("Improper format. Ex: 555-555-5555");
				errorPresent = true;
			}
		}
		
		if (emailAddressField.getText().toString().trim().isEmpty()){
			emailAddressField.setError(null);
			emailAddressField.setError("Email Address cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(EMAIL_REGEX, emailAddressField.getText().toString().trim())){
				emailAddressField.setError(null);
				emailAddressField.setError("Improper format. Ex: john@domain.com");
				errorPresent = true;
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
}
