package com.watershednaturecenter.GPSItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//extra imports



public class HealthGraphApi {
	private WebView RunKeeperWebView;
	private String authCode;
	private String accessToken;
	private final static String CLIENT_ID = "370386b2c7bc4dad9456ee168d812be0";
    private final static String CLIENT_SECRET = "a0be7566339c4869bf101389a1dd45fa";
    private final static String CALLBACK_URL = "http://www.watershednaturecenter.uri/callback";
    
    @SuppressLint("SetJavaScriptEnabled")
	public HealthGraphApi(WebView webView)
	{
    	CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        RunKeeperWebView = webView;
		//This is important. JavaScript is disabled by default. Enable it.
        webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
	}
    
    
    public void SetAuthorizationCode() {
        
        RunKeeperWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(CALLBACK_URL)) {
                    authCode = Uri.parse(url).getQueryParameter("code");
                    RunKeeperWebView.setVisibility(View.GONE);
                    SetAccessToken();
                    return true;
                }
 
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
 
    private void SetAccessToken() {
        String accessTokenUrl = "https://runkeeper.com/apps/token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s";
        final String finalUrl = String.format(accessTokenUrl, authCode, CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(finalUrl);
                    HttpResponse response = client.execute(post);
                    String jsonString = EntityUtils.toString(response.getEntity());
                    final JSONObject json = new JSONObject(jsonString);
                    accessToken = json.getString("access_token");
 
                } catch (Exception e) {
                    e.printStackTrace();
                }
 
            }
 
    private void getTotalDistance(String accessToken) {        
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://api.runkeeper.com/records");
            
            get.addHeader("Authorization", "Bearer " + accessToken);
            get.addHeader("Accept", "*/*");
            
            HttpResponse response = client.execute(get);
            
            String jsonString = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(jsonString);
            findTotalWalkingDistance(jsonArray);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private double findTotalWalkingDistance(JSONArray arrayOfRecords) {
    	double totalWalkingDistanceMiles = 0;
        try {
            //Each record has activity_type and array of statistics. Traverse to  activity_type = Walking
            for (int ii = 0; ii < arrayOfRecords.length(); ii++) {
                JSONObject statObject = (JSONObject) arrayOfRecords.get(ii);
                if ("Walking".equalsIgnoreCase(statObject.getString("activity_type"))) {
                    //Each activity_type has array of stats, navigate to "Overall" statistic to find the total distance walked.
                    JSONArray walkingStats = statObject.getJSONArray("stats");
                    for (int jj = 0; jj < walkingStats.length(); jj++) {
                        JSONObject iWalkingStat = (JSONObject) walkingStats.get(jj);
                        if ("Overall".equalsIgnoreCase(iWalkingStat.getString("stat_type"))) {
                            long totalWalkingDistanceMeters = iWalkingStat.getLong("value");
                            totalWalkingDistanceMiles = totalWalkingDistanceMeters * 0.00062137;
                            
                        }
                    }
                }
            }
        } catch (JSONException e) {           
            e.printStackTrace();
            return -1.0;
        }
        return totalWalkingDistanceMiles;
    }
    
    
    
    
    
    public void LoginPrompt()
    {
    	RunKeeperWebView.setVisibility(View.VISIBLE);
    	RunKeeperWebView.requestFocus(View.FOCUS_DOWN);
    	String authorizationUrl = "https://runkeeper.com/apps/authorize?response_type=code&client_id=%s&redirect_uri=%s";
        authorizationUrl = String.format(authorizationUrl, CLIENT_ID, CALLBACK_URL);
    	SetAuthorizationCode();
        RunKeeperWebView.loadUrl(authorizationUrl);
    }
    
    public String GetaccesToken()
    {
		return accessToken;
    }
    
    public double GetWalkingDist()
    {
    	double Walkingdist = 0;
    	try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://api.runkeeper.com/records");
            
            get.addHeader("Authorization", "Bearer " + accessToken);
            get.addHeader("Accept", "*/*");
            
            HttpResponse response = client.execute(get);
            
            String jsonString = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(jsonString);
            Walkingdist =  findTotalWalkingDistance(jsonArray);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return Walkingdist;
    }
    
    
    public void PostWorkout(ArrayList<CoordinateInformation> LocationArray)
    {
    	try{
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://api.runkeeper.com/fitnessActivities");
    	post.addHeader("Authorization", "Bearer " + accessToken);
    	post.addHeader("Accept", "*/*");
    	post.addHeader("Content-Type", "application/vnd.com.runkeeper.NewFitnessActivity+json");
    	
    	
    	JSONObject j = new JSONObject();
    	j.put("type", "Walking");
    	j.put("equipment", "None");
    	j.put("start_time", "Sat, 1 Jan 2011 00:00:00");
    	j.put("notes", "None");
    	j.put("path", BuildPathParm(LocationArray));
    	StringEntity se = new StringEntity( j.toString());
        post.setEntity(se);
        HttpResponse response = client.execute(post);
    	}
    	catch(Exception e)
    	{
    		//Catch Exception here
    	}
    	
    	
    	
    }
    
    
    //Loops through the Gps Coordinates Gathered and inserts them into JSONObject to be posted
    private JSONArray BuildPathParm(ArrayList<CoordinateInformation> LocationArray)
    {
    	JSONArray ja = new JSONArray();
    	int count = 0;
    	try{
    		for (final CoordinateInformation Info : LocationArray) {
    			JSONObject temp = new JSONObject();
    			int diff = (int) TimeUnit.MILLISECONDS.toSeconds(Info.GetCurrentDateTime().toMillis(true)-LocationArray.get(0).GetCurrentDateTime().toMillis(true));
    			temp.put("timestamp",diff);
    			temp.put("altitude", 0);
    			temp.put("longitude", Info.GetLongitude());
    			temp.put("latitude", Info.GetLatitude());
    			if (count == 0)
    			{
    				temp.put("type", "start");
    			}
    			else if (count < LocationArray.size())
    			{
    				temp.put("type", "gps");
    			}
    			else
    			{
    				temp.put("type", "end");
    			}
    			count++;
    			ja.put(temp);
    		}
    	}
    	catch(Exception e)
    	{
    		//Catch Exception here
    	}
		return ja;
    }
    
}