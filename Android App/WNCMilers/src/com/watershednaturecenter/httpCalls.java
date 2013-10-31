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

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.watershednaturecenter.GPSItems.WorkoutInfo;


public class httpCalls {

	private static final String url_Get_WNCMileage = "http://www.watershednaturecenter.com/Get_WNCMileage.php";
	
	class Mileage_Reemed_object
	{
		boolean isRedeemed;
		double mileage;
	}
	Context context = WNC_MILERS.getInstance();
	WorkoutInfo currentWorkout = ((WNC_MILERS) context).get_CurrentWorkoutWNC();
	
	public double GetCompletedMileage() throws NumberFormatException, JSONException
	{
		InputStream is = getMileageHTTP();
		JSONObject response = convertResponseToJSON(is);
		double Dist = Double.parseDouble(response.getString("Distance"));
		currentWorkout.TotalWNCMilesForUser =  Dist;
		return Dist;
	}
	
	public Mileage_Reemed_object GetCompletedMileage_IsReedemd() throws NumberFormatException, JSONException
	{
		Mileage_Reemed_object Mileage_Reedeemd = new Mileage_Reemed_object();
		JSONObject response = convertResponseToJSON(getMileageHTTP());
		getMileageHTTP();
		//TODO depending on the name of actuall database fields these will need to be changed!!
		Mileage_Reedeemd.mileage = Double.parseDouble(response.getString("Distance"));
		currentWorkout.TotalWNCMilesForUser =  Mileage_Reedeemd.mileage;
    	if (response.getString("Redemption_Date") == "null") Mileage_Reedeemd.isRedeemed = false;
    	else Mileage_Reedeemd.isRedeemed = true;
    	return Mileage_Reedeemd;
	}
	
	private InputStream getMileageHTTP()
	{
		Integer RKlogin = currentWorkout.getRK_ID();
		InputStream is = null;
		try
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("RKID", RKlogin.toString()));
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_Get_WNCMileage);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
		}
		catch(Exception e){
		}
		return is;
		
		
	}
	
	private JSONObject convertResponseToJSON(InputStream is)
	{
		String tempResult = null;
		JSONObject json_data = null;
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
    		json_data = jArray.getJSONObject(0);
            
	    }catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    }
	    return json_data;
	}
		
	
}
