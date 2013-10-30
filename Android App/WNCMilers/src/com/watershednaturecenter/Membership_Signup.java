package com.watershednaturecenter;

import java.util.regex.Pattern;

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

public class Membership_Signup extends SherlockFragment
{
	// Regular Expression
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{3}-\\d{4}";
    private static final String ZIP_REGEX = "\\d{5}";
    private static final String CITY_REGEX = "^(?:[a-zA-Z]+(?:[.'\\-,])?\\s?)+$";
    
    private MembershipInfo member = new MembershipInfo();
	//private String firstName, lastName, addressLine1, addressLine2, city, state, zipCode, phoneNumber, emailAddress, membershipLevel;
	private Button submitButton;
	private EditText firstNameField, lastNameField, addressLine1Field, addressLine2Field, cityField, zipCodeField, phoneNumberField, emailAddressField;
	private Spinner stateSpinner, membershipLevelSpinner;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.membership_sign_up, container, false);
		
		firstNameField = (EditText) view.findViewById(R.id.firstName);
		lastNameField = (EditText) view.findViewById(R.id.lastName);
		addressLine1Field = (EditText) view.findViewById(R.id.addressLine1);
		addressLine2Field = (EditText) view.findViewById(R.id.addressLine2);
		cityField = (EditText) view.findViewById(R.id.CityTextBox);
		stateSpinner = (Spinner) view.findViewById(R.id.StateSelection);
		zipCodeField = (EditText) view.findViewById(R.id.zipCode);
		phoneNumberField = (EditText) view.findViewById(R.id.PhoneNumber);
		emailAddressField = (EditText) view.findViewById(R.id.EmailAddress);
		membershipLevelSpinner = (Spinner) view.findViewById(R.id.MembershipLevelSelection);
		
		stateSpinner.setSelection(13);
		
		submitButton = (Button) view.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Hides softKeyboard
            	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
            		
              	Toast.makeText(getSherlockActivity(), "Submit Button Clicked", Toast.LENGTH_SHORT).show();
            	getDataFromForm();
            	MySQLConnector MYSQLCOMM = new MySQLConnector(getActivity().getFragmentManager());
            	MYSQLCOMM.RegisterMember(member);
        	}
        });
		return view;
	}
	
	//Function to load data from the textFields/Spinners into local variables
	public void getDataFromForm(){
		member.firstName = firstNameField.getText().toString().trim();
		member.lastName = lastNameField.getText().toString().trim();
		member.addressLine1 = addressLine1Field.getText().toString().trim();
		member.addressLine2 = addressLine2Field.getText().toString().trim();
		member.city = cityField.getText().toString().trim();
		member.state = stateSpinner.getSelectedItem().toString();
		member.zipCode = zipCodeField.getText().toString().trim();
		member.phoneNumber = phoneNumberField.getText().toString().trim();
		member.emailAddress = emailAddressField.getText().toString().trim();
		member.membershipLevel = membershipLevelSpinner.getSelectedItem().toString();
		
		if (!completeFormValidityCheck()){
			Toast.makeText(getSherlockActivity(), "Thanks " + member.firstName + " " + member.lastName, Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean completeFormValidityCheck(){
		boolean errorPresent = false;
		
		if (member.firstName.isEmpty()){
			firstNameField.setError(null);
			firstNameField.setError("First name cannot be blank");
			errorPresent = true;
		}
		if (member.lastName.isEmpty()){
			lastNameField.setError(null);
			lastNameField.setError("Last name cannot be blank");
			errorPresent = true;
		}
		
		if (member.addressLine1.isEmpty()){
			addressLine1Field.setError(null);
			addressLine1Field.setError("Address Line 1 cannot be blank");
			errorPresent = true;
		}
		
		if (member.city.isEmpty()){
			cityField.setError(null);
			cityField.setError("City cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(CITY_REGEX, member.city)){
				cityField.setError(null);
				cityField.setError("Improper City Name Format");
			}
		}
		
		if (member.zipCode.isEmpty()){
			zipCodeField.setError(null);
			zipCodeField.setError("Zip code cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(ZIP_REGEX, member.zipCode)){
				zipCodeField.setError(null);
				zipCodeField.setError("Improper format. Ex: 62025");
			}
		}
		
		if (member.phoneNumber.isEmpty()){
			phoneNumberField.setError(null);
			phoneNumberField.setError("Phone Number cannot be blank");
			errorPresent = true;
		}
		else {
			if(!checkValidity(PHONE_REGEX, member.phoneNumber)){
				phoneNumberField.setError(null);
				phoneNumberField.setError("Improper format. Ex: 555-555-5555");
			}
		}
		
		if (member.emailAddress.isEmpty()){
			emailAddressField.setError(null);
			emailAddressField.setError("Email Address cannot be blank");
			errorPresent = true;
		} else {
			if(!checkValidity(EMAIL_REGEX, member.emailAddress)){
				emailAddressField.setError(null);
				emailAddressField.setError("Improper format. Ex: john@domain.com");
			}
		}
		
		if (errorPresent){
			Toast.makeText(getSherlockActivity(), "Please correct error on form", Toast.LENGTH_SHORT).show();
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
