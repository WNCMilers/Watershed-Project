package com.watershednaturecenter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.watershednaturecenter.GPSItems.WorkoutInfo;

class MySQLConnector{
	Context context = WNC_MILERS.getInstance();
	WorkoutInfo currentWorkout = ((WNC_MILERS) context).get_CurrentWorkoutWNC();
	//String RKlogin = ((WNC_MILERS) context).get_Rklogin();

	public void UpdateMiles()
	{
		new UpdateMiles().execute();
	}
	
	
	private static final String url_Update_WNCMileage = "http://www.##ADDRESS HERE##.com/Update_WNCMileage.php";
	
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
				Integer RKlogin = currentWorkout.getRK_ID();
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("RKID", RKlogin.toString()));
				params.add(new BasicNameValuePair("Distance",String.valueOf(distance)));
				DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_Update_WNCMileage);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
			}
			catch(Exception e)
			{
				Integer test = 1;
			}
			
			
			return null;
		}
		
	}
	
	
}
