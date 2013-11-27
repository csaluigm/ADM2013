package com.adm.geoadm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NuevoRecordatorio extends ActionBarActivity {

	ViewPager pager;
	MyPageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_view_pager_action_bar);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		adapter = new MyPageAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.vpFragment);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				getSupportActionBar().setSelectedNavigationItem(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabListener tabListener = new TabListener() {
			
			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(arg0.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
		Tab tab = getSupportActionBar().newTab();
		tab.setText("tab1");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText("tab2");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText("tab3");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText("tab4");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0) {
			case 0:
				return new ListViewStringSupportFragment();
			case 1:
				return new ListViewStringSupportFragment();
			case 2:
				return new ListViewStringSupportFragment();
			case 3:
				return new ListViewStringSupportFragment();			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			switch(position) {
			case 0:
				return "tab1";
			case 1:
				return "tab2";
			case 2:
				return "tab3";
			case 3:
				return "tab4";
			}
			return null;
		}

	}
}