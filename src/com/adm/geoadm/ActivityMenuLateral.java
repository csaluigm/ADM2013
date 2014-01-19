package com.adm.geoadm;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityMenuLateral extends ActionBarActivity implements
AdapterView.OnItemClickListener {
	 protected ActionBarDrawerToggle mDrawerToggle;
	protected DrawerLayout mDrawer;
	protected ListView mDrawerOptions;
	  protected String[] navMenuTitles;
	//private Object mTitle;
	protected CharSequence mDrawerTitle;
	public ArrayAdapter<String>tlateraladapter;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
					
	}
	
	/**
	 * Inicializa el Navigation Drawer
	 * @param stringArrayId Id del array de strings que queremos poner en el navigation drawer
	 * @param contexto activity actual para que se pueda modificar el action bar dinámicamente
	 */
	public void menu_lateral(ArrayList<String> titulos,Context contexto) {
		context=contexto;
		mDrawerTitle = getTitle();
		mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		 // load slide menu items
        navMenuTitles = titulos.toArray(new String[titulos.size()]);
		tlateraladapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1, navMenuTitles);
		
        mDrawerOptions.setAdapter(tlateraladapter);
		mDrawerOptions.setOnItemClickListener(this);
	    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.drawable.ic_navigation_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                ActivityCompat.invalidateOptionsMenu((Activity)context);
            }
 
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                ActivityCompat.invalidateOptionsMenu((Activity)context);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void setTitle(CharSequence title) {
       
        getSupportActionBar().setTitle(mDrawerTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	
}
