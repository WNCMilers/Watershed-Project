package com.watershednaturecenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.watershednaturecenter.GPSItems.HealthGraphApi;

public class Login extends Activity
{
	private HealthGraphApi APIWORKER;
	private WebView runKeeperwebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	    runKeeperwebView = (WebView) findViewById(R.id.loginWebView);
	    
	    //Initialize Application Variable HealthGraphAPI
		((WNC_MILERS) this.getApplication()).set_APIWORKER(new HealthGraphApi(runKeeperwebView, this));
		APIWORKER = ((WNC_MILERS) this.getApplication()).get_APIWORKER();
		APIWORKER.LoginPrompt();
	}
	
}
