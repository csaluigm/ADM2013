package com.adm.geoadm;

import java.sql.Date;

import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;
import com.adm.geoadm.fragments.MapFragment;

import android.content.Intent;
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
	//-------  TABS  ----------------
	MapFragment tabMap;
	DetailsFragment tabDetails;
	//-------------------------------
	
	public static final String KEY_EDIT_RECORDATORIO = "edit_recordatorio";
	public static final String KEY_NOTIFY_RECORDATORIO = "notify_recordatorio";
	
	//-------  TABS ADAPTER ---------
	ViewPager pager;
	MyPageAdapter adapter;
	//-------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_view_pager_action_bar);
		
		//Initialize tabs
		tabMap = new MapFragment();
		tabDetails = new DetailsFragment();
		
		//Distinguish between when user wants edit a Reminder or a Notification is launched
		Intent intent = getIntent();
		if (intent.hasExtra(KEY_NOTIFY_RECORDATORIO)) {
			//This Activity was called for a Notification
			RecordatoriosDB recDB = new RecordatoriosDB(this);
			int id = intent.getIntExtra(KEY_NOTIFY_RECORDATORIO, -1);
			Recordatorio rec = recDB.getRecordatorio(id);
			rec.setActiva(false);
			recDB.modificar(id, rec);
			recDB.close();
			
			//TODO Pasar al DetailsFragment
			tabMap.setRecordatorioenmapa(rec);
		}
		else if (intent.hasExtra(KEY_EDIT_RECORDATORIO)) {
			RecordatoriosDB recDB = new RecordatoriosDB(this);
			int id = intent.getIntExtra(KEY_NOTIFY_RECORDATORIO, -1);
			Recordatorio rec = recDB.getRecordatorio(id);
			recDB.close();
			
			//TODO Pasar al DetailsFragment
			tabMap.setRecordatorioenmapa(rec);
		}
		
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
		tab.setText(getResources().getString(R.string.tab_map));
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText(getResources().getString(R.string.tab_description));
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

//		tab = getSupportActionBar().newTab();
//		tab.setText("tab3");
//		tab.setTabListener(tabListener);
//		getSupportActionBar().addTab(tab);
//
//		tab = getSupportActionBar().newTab();
//		tab.setText("tab4");
//		tab.setTabListener(tabListener);
//		getSupportActionBar().addTab(tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nuevo_recordatorio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.save_recordatorio:
			saveRecordatorio();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void fetchRecordatorio(Recordatorio rec) {
		//Fetch data from map tab
		rec.setLongitud(tabMap.getLongitud());
		rec.setLatitud(tabMap.getLatitud());
		rec.setRadius(tabMap.getRadio());
		rec.setDireccion(tabMap.getDireccion());
		//Fetch data from details tab
		rec.setNombre("Prueba de Recordatorio");
		rec.setDescripcion("Descripcion");		
		rec.setCategoriaId(1);
		rec.setActiva(true);
		//...
	}
	
	private void saveRecordatorio() {
		Intent intent = getIntent();
		if (!intent.hasExtra(KEY_EDIT_RECORDATORIO) && !intent.hasExtra(KEY_NOTIFY_RECORDATORIO)) {
			Recordatorio rec = new Recordatorio();
			fetchRecordatorio(rec);
			RecordatoriosDB recDB = new RecordatoriosDB(this);
			long id = recDB.insertar(rec);
			recDB.close();
		}
		else {
			RecordatoriosDB recDB = new RecordatoriosDB(this);
			int id;
			id = (intent.hasExtra(KEY_EDIT_RECORDATORIO)) ? intent.getIntExtra(KEY_EDIT_RECORDATORIO, -1) : -1;
			id = (intent.hasExtra(KEY_NOTIFY_RECORDATORIO)) ? intent.getIntExtra(KEY_NOTIFY_RECORDATORIO, -1) : -1;
			
			Recordatorio rec = recDB.getRecordatorio(id);
			fetchRecordatorio(rec);
			recDB.modificar(id, rec);
			recDB.close();
		}
		
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
				return tabMap;
			case 1:
				//Cambio esta linea porque creo que la segunda pestaña debe ser
				//el DetailsFragment que son los detalles del Recordatorio
				//Angel y Antonio
				//return new ListViewStringSupportFragment();
				return tabDetails; 
//			case 2:
//				return new ListViewStringSupportFragment();
//			case 3:
//				return new MapFragment();	
				}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			switch(position) {
			case 0:
				return getResources().getString(R.string.tab_map);
			case 1:
				return getResources().getString(R.string.tab_description);
			
			}
			return null;
		}

	}
}