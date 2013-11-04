package com.watershednaturecenter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.watershednaturecenter.Dialogs.LoginDialog;
import com.watershednaturecenter.Dialogs.WNCMileageDialog;
import com.watershednaturecenter.GPSItems.WorkoutInfo;

public class MySQLConnector{
	Context context = WNC_MILERS.getInstance();
	FragmentManager FM;
	WorkoutInfo currentWorkout = ((WNC_MILERS) context).get_CurrentWorkoutWNC();

	public MySQLConnector(FragmentManager fm)
	{
		FM = fm;
	}
	
	public MySQLConnector(){}
	
	public void UpdateMiles()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new UpdateMiles().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new UpdateMiles().execute();
		}
	}
	
	public void GetMiles()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new GetMiles().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new GetMiles().execute();
		}
	}
	
	public void RegisterMember(MembershipInfo member)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new RegisterMember().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, member);
		} else {
			new RegisterMember().execute(member);
		}
	}
	
	
	private static final String url_Update_WNCMileage = "http://www.watershednaturecenter.com/Update_WNCMileage.php";
	private static final String url_Get_WNCMileage = "http://www.watershednaturecenter.com/Get_WNCMileage.php";
	private static final String url_Register_Member = "http://www.watershednaturecenter.com/Register_Member.php";
	
	class UpdateMiles extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//TODO dialog boxes here
		};
		
		@Override
		protected String doInBackground(String... arg0) {
			try
			{
 				double distance = currentWorkout.GetCurDistTraveled();
 				String WorkoutNotes = currentWorkout.GetWorkoutNotes();
				Integer RKlogin = currentWorkout.getRK_ID();
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("RKID", RKlogin.toString()));
				params.add(new BasicNameValuePair("Distance",String.valueOf(distance)));
				params.add(new BasicNameValuePair("WorkoutNotes",WorkoutNotes));
				DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_Update_WNCMileage);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
			}
			catch(Exception e)
			{
				Toast.makeText(context,e.toString() ,Toast.LENGTH_LONG).show();
			}
			GetMiles();
			return null;
		}
		
	}
	
	class GetMiles extends AsyncTask<String,String,String>
	{
		double Mileage;
		Boolean IsRedeemd;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//TODO dialog boxes here
		};
		
		@Override
		protected String doInBackground(String... arg0) {
			try
			{
				httpCalls GetMilesHTTPCall = new httpCalls();
				IsRedeemd = GetMilesHTTPCall.GetCompletedMileage_IsReedemd().isRedeemed;
				Mileage = GetMilesHTTPCall.GetCompletedMileage_IsReedemd().mileage;
			}
			catch(Exception e)
			{
				Integer test = 1;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			
			if (IsRedeemd != null)
			{
				WNCMileageDialog MileageDialog = new WNCMileageDialog(Mileage,IsRedeemd);
				//TODO show Current number miles Dialog
				MileageDialog.show(FM, "Bla");
			}
				
					
		};
		
		private void ConvertToString(InputStream is)
		{
			String Result = null;
			String tempResult = null;
			//convert response to string
		    try{
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                    sb.append(line + "\n");
		            }
		            is.close();
		            tempResult=sb.toString();
		    }catch(Exception e){
		            Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    //parse json data
		    try{
		            JSONArray jArray = new JSONArray(tempResult);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    //Log.i("log_tag","RKID: "+json_data.getString("RKID")+
		                     //       ", Miles: "+json_data.getString("name")+
		                     //       ", RedemDate: "+json_data.getString("")("sex")+
		                     //       ", birthyear: "+json_data.getInt("birthyear")
		                    //);
		                    //Get an output to the screen
		                    Result += "\n\t" + jArray.getJSONObject(i); 
		                    //TODO depending on the name of actuall database fields these will need to be changed!!
		                    Mileage = Double.parseDouble(jArray.getJSONObject(i).getString("Distance"));
	                    	if (jArray.getJSONObject(i).getString("Redemption_Date") == "null") IsRedeemd = false;
	                    	else IsRedeemd = true;	                    		
		            }
		    }catch(JSONException e){
		            Log.e("log_tag", "Error parsing data "+e.toString());
		    }
		}    
	}
	
	class RegisterMember extends AsyncTask<MembershipInfo,String,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//TODO dialog boxes here
		};
		
		@Override
		protected String doInBackground(MembershipInfo... arg0) {
			try
			{
 				//Integer RKlogin = currentWorkout.getRK_ID();
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				//params.add(new BasicNameValuePair("RKID", RKlogin.toString()));
				//TODO: Change these to proper user info stuff
				params.add(new BasicNameValuePair("FirstName", arg0[0].firstName));
				params.add(new BasicNameValuePair("LastName", arg0[0].lastName));
				params.add(new BasicNameValuePair("AddressLine1", arg0[0].addressLine1));
				params.add(new BasicNameValuePair("AddressLine2", arg0[0].addressLine2));
				params.add(new BasicNameValuePair("City", arg0[0].city));
				params.add(new BasicNameValuePair("State", arg0[0].state));
				params.add(new BasicNameValuePair("Zip", arg0[0].zipCode));
				params.add(new BasicNameValuePair("Phone", arg0[0].phoneNumber));
				params.add(new BasicNameValuePair("EmailAddress", arg0[0].emailAddress));
				//params.add(new BasicNameValuePair("membershiplevel", arg0[0].membershipLevel));
				//params.add(new BasicNameValuePair("birthdate", ));
				//params.add(new BasicNameValuePair("end", ));
				
								
				DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_Register_Member);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
			}
			catch(Exception e)
			{
				Toast.makeText(context,e.toString() ,Toast.LENGTH_LONG).show();
			}
			
			return null;
		}
		
	}
}
