package com.watershednaturecenter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends SherlockFragmentActivity
{

	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	TextView tabCenter;
	TextView tabText;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if(checkPlayServices()){
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			super.onCreate(savedInstanceState);
	
			mViewPager = new ViewPager(this);
			mViewPager.setId(R.id.pager);
	
			setContentView(mViewPager);
			ActionBar bar = getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
			mTabsAdapter = new TabsAdapter(this, mViewPager);
	
			mTabsAdapter.addTab(
					bar.newTab().setText("Tracking"),
					Distance_Time.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Challenges"),
					Challenges.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Leaderboard"),
					Leaderboard.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("My Stats"),
					Stats.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Photos"),
					Photos.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Events"),
					Events.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("API Test"),
					Api_Test.class, null);
			mTabsAdapter.addTab(
					bar.newTab()
							.setText("Become a Member"),
					Membership_Signup.class, null);
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
	    //return false;
	    
	    //allow for run on emulator
	    return true;
	  }
	  return true;
	}
	 
	void showErrorDialog(int code) {
		//thinking that this will not work on an emulated device. need to research to find out.
	    GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
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