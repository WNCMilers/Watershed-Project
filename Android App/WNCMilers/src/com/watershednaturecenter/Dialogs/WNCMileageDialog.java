package com.watershednaturecenter.Dialogs;

import com.watershednaturecenter.Login;
import com.watershednaturecenter.Membership_Signup;
import com.watershednaturecenter.Membership_Signup2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class WNCMileageDialog extends DialogFragment{
	double Mileage = 0;
	boolean IsRedeemed;
	
	public WNCMileageDialog(double miles, boolean IR)
	{
		Mileage = miles;
		IsRedeemed = IR;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title;
        if (Mileage >= 25 && IsRedeemed == true)
        {
        	title = Double.toString(Mileage) + " miles completed at WNC. You already reached 25 miles. Keep up the hard work!!";
        	builder.setMessage(title)
            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }
        else if(Mileage >= 25 && IsRedeemed == false)
        {
        	title = Double.toString(Mileage) + " miles completed at WNC. Congradulations you've reached 25 miles!! would you like to redeem membership?";
        	builder.setMessage(title)
            .setPositiveButton("Redeem Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	dialog.dismiss();
                	Intent i = new Intent(getActivity(), Membership_Signup2.class);
              	   	startActivity(i);
                }
            })
            .setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
        }
        else
        {
        	title = Double.toString(Mileage) + " miles completed at WNC. Keep up the hard work!!";
        	builder.setMessage(title)
            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

