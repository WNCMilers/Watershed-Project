package com.watershednaturecenter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.watershednaturecenter.Dialogs.RKInstallDialog;

public class MainActivity extends SherlockFragmentActivity
{
	
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	TextView tabCenter;
	TextView tabText;
	
	private static void setDefaultUncaughtExceptionHandler() {
	    try {
	        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

	            @Override
	            public void uncaughtException(Thread t, Throwable e) {
	            	Log.e("log_tag", "Uncaught Exception detected in thread"+e.toString());
	                //Log.e("Uncaught Exception detected in thread {}", t, e);
	            }
	        });
	    } catch (SecurityException e) {
	    	Log.e("log_tag", "Could not set the Default Uncaught Exception Handler"+e.toString());
	        //logger.error("Could not set the Default Uncaught Exception Handler", e);
	    }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setDefaultUncaughtExceptionHandler();
		boolean check = checkPlayServices();
		if(check){
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
				
			mViewPager = new ViewPager(this);
			mViewPager.setId(R.id.pager);
	
			setContentView(mViewPager);
			ActionBar bar = getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
			mTabsAdapter = new TabsAdapter(this, mViewPager);
	
			//mTabsAdapter.addTab(
					//bar.newTab().setText("Tracking"),
					//Distance_Time.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Workout"),
					Workout.class, null);
//			mTabsAdapter.addTab(
//					bar.newTab()
//							.setText("Leaderboard"),
//					Leaderboard.class, null);
//			mTabsAdapter.addTab(
//					bar.newTab()
//							.setText("My Stats"),
//					Stats.class, null);
//			mTabsAdapter.addTab(
//					bar.newTab()
//							.setText("Photos"),
//					Photos.class, null);
			//mTabsAdapter.addTab(
					//bar.newTab()
							//.setText("Events"),
					//Events.class, null);
			//mTabsAdapter.addTab(
					//bar.newTab()
							//.setText("Become a Member"),
							//Membership_Signup_TAB.class, null);
//			mTabsAdapter.addTab(
//					bar.newTab()
//							.setText("Feedback"),
//					Feedback.class, null);
				}
	}
	
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	 
		switch (requestCode) {
		case REQUEST_CODE_RECOVER_PLAY_SERVICES:
			if (resultCode == RESULT_CANCELED) {
		        Toast.makeText(this, "Google Play Services must be installed.",
		            Toast.LENGTH_SHORT).show();
		        finish();
			}
		    return;
		  }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean checkPlayServices() {
	  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	  if (status != ConnectionResult.SUCCESS) {
    	if(status == ConnectionResult.SERVICE_MISSING ||
    	   status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
    	   status == ConnectionResult.SERVICE_DISABLED){
    		showErrorDialog(status);
    	} else {
	      Toast.makeText(this, "This device is not supported.",
	          Toast.LENGTH_LONG).show();
	      finish();
	    }
    	return false;
    	
    	//allow for run on emulator
	    //return true;
	  }else{
		  return true;
	  }
	}
	 
	void showErrorDialog(int code) {
	   Dialog googlePlay = GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES);
	   googlePlay.setCancelable(false);
	   googlePlay.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		
		// Setting Dialog Title
        alertDialog.setTitle("Exit Confirmation");
        
		// Setting Dialog Message
        alertDialog.setMessage("Warning exiting app will cancel any active workout. Continue?");
        
        // On pressing Enable button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
               dialog.dismiss();
               MainActivity.this.finish();
            }
        });
        
        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        
        // Showing Alert Message
        alertDialog.show();	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.login:
	    	//setContentView(R.layout.login);
	    	Intent login = new Intent(this, Login.class);
	    	startActivity(login);
	    	break;
	    case R.id.LRK:
	    	if (IsAppInstalled("com.fitnesskeeper.runkeeper.pro"))
	    	{
	    		Intent RK = new Intent(Intent.ACTION_MAIN);
		    	PackageManager manager = getPackageManager();
		    	RK = manager.getLaunchIntentForPackage("com.fitnesskeeper.runkeeper.pro");
		    	RK.addCategory(Intent.CATEGORY_LAUNCHER);
		    	startActivity(RK);
	    	}
	    	else
	    	{
	    		RKInstallDialog RkDialog = new RKInstallDialog();
	    		RkDialog.show(getSupportFragmentManager(), "RKInstallDialogNotice");
	    	}
	    	break;  
	    case R.id.application_feedback:
	    	Intent feedback = new Intent(this, Feedback.class);
      	   	startActivity(feedback);
      	   	break;
	    case R.id.about:
	    	Intent about = new Intent(this, About.class);
      	   	startActivity(about);
	    	break;
	    case R.id.events:
	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://watershednaturecenter.com/events.php"));
			startActivity(browserIntent);
	    }
	    return true;
	}
	
	private boolean IsAppInstalled(String Package)
	{
		 PackageManager pm = getPackageManager();
		 boolean installed = false;
		 try {
		 pm.getPackageInfo(Package, PackageManager.GET_ACTIVITIES);
		 installed = true;
		 } catch (PackageManager.NameNotFoundException e) {
		 installed = false;
		 }
		 return installed;
	}
	
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener
	{
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo
		{
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args)
			{
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager)
		{
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args)
		{
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position)
		{
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{
		}

		public void onPageSelected(int position)
		{
			mActionBar.setSelectedNavigationItem(position);
		}

		public void onPageScrollStateChanged(int state)
		{
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++)
			{
				if (mTabs.get(i) == tag)
				{
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}
	
	
}