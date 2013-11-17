package com.watershednaturecenter;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

public class About extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        final WebView aboutPage = (WebView) findViewById(R.id.about_web_view);
        aboutPage.loadUrl("http://watershednaturecenter.com/History1.php");
        aboutPage.getSettings().setSupportZoom(true);  
        aboutPage.getSettings().setBuiltInZoomControls(true);
     }
	
}
