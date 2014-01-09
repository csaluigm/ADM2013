package com.adm.geoadm.fragments;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adm.geoadm.R;
import com.adm.geoadm.db.Recordatorio;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment  extends Fragment implements InfoWindowAdapter, OnInfoWindowClickListener {
	//--- GUI ---
	EditText etAddress;
	Button btGo;
	GoogleMap map=null;
	EditText etRadius;
	int auxRadius;
	Button btPlus;
	Button btMinus;
	Marker marker; 
	Circle circle; 
	LatLng center=null; // iremos guardando la posicion
	boolean pressedUp = false;
	int increment;
	
	/**
	 * Estas funciones las hemos puesto para obtener los datos necesarios 
	 * para crear el recordatorio
	 * Angel y Antonio
	 */
	//---  Properties ---
	/**
	 * Get Latitude
	 * @return Double
	 */
	public double getLatitud() {
		return center.latitude;
	}
	
	/**
	 * Get Longitude
	 * @return Double
	 */
	public double getLongitud() {
		return center.longitude;
	}
	
	/**
	 * Get radius of proximity detector
	 * @return Double
	 */
	public double getRadio() {
		return Double.valueOf(etRadius.getText().toString());
	}
	
	/**
	 * Get name of address
	 * @return String
	 */
	public String getDireccion() {
		return etAddress.getText().toString();
	}
	
	//funcion de recordatorio
	public void setRecordatorioenmapa(Recordatorio rec){
		center= new LatLng(rec.getLatitud(),rec.getLongitud());
		etRadius.setText(String.valueOf(rec.getRadius()));
		etAddress.setText(rec.getDireccion());
		putMarker(center);
		paintCircle(center, (int)rec.getRadius());
	}
	

	//--- Events ----
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_map, null);
		etAddress = (EditText) view.findViewById(R.id.etAddress);
		etRadius = (EditText) view.findViewById(R.id.etRadius);
		   btPlus = (Button) view.findViewById(R.id.btPlus);
		   btMinus = (Button) view.findViewById(R.id.btMinus);
		  if (center==null){
		   etRadius.setEnabled(false);
		   btPlus.setEnabled(false);
		   btMinus.setEnabled(false);
		  }

		if (map == null) {
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fMap)).getMap();
		
			
			
			if (map != null) {
				map.setInfoWindowAdapter(this);
				map.setOnInfoWindowClickListener(this);
				map.getUiSettings().setMyLocationButtonEnabled(true);

			}
		}


		//Metodo sobreescrito para cuando se haga un click en el mapa
		map.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng lat) {
				if(center==null){
					  etRadius.setEnabled(true);
					   btPlus.setEnabled(true);
					   btMinus.setEnabled(true);
				}
				putMarker(lat);
				paintCircle(lat, Integer.parseInt(etRadius.getText().toString()));
			}

		});
		

		etRadius.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(circle!=null){

						paintCircle(center, Integer.parseInt(etRadius.getText().toString()));
					}
				}  
				//do job here owhen Edittext lose focus
			}
		});
		
		etRadius.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				  if (keyCode ==  KeyEvent.KEYCODE_DPAD_CENTER
					        || keyCode ==  KeyEvent.KEYCODE_ENTER) {

					            if (event.getAction() == KeyEvent.ACTION_DOWN) {
	
					            } else if (event.getAction() == KeyEvent.ACTION_UP) {
					            	paintCircle(center, Integer.parseInt(etRadius.getText().toString()));
					            	hidekeyboard(view);
					            } 
					            return true;

					        } else {
					            // it is not an Enter key - let others handle the event
					            return false;
					        }
			}

		});
		etAddress.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				  if (keyCode ==  KeyEvent.KEYCODE_DPAD_CENTER
					        || keyCode ==  KeyEvent.KEYCODE_ENTER) {

					            if (event.getAction() == KeyEvent.ACTION_DOWN) {
	
					            } else if (event.getAction() == KeyEvent.ACTION_UP) {
					            	new MyGeoder().execute();
					            	hidekeyboard(view);
					            } 
					            return true;

					        } else {
					            // it is not an Enter key - let others handle the event
					            return false;
					        }
			}

		});
		
		
	

		btPlus.setOnTouchListener(new View.OnTouchListener() {        
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(pressedUp == false){
						pressedUp = true;
						increment= metersZoom(map.getCameraPosition().zoom);
						new incrementer().execute();
					}
					break;
					//return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					pressedUp = false;
					break;
				}
				return false;
			}
		});

		
		btMinus.setOnTouchListener(new View.OnTouchListener() {        
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(pressedUp == false){
						pressedUp = true;
						increment= - metersZoom(map.getCameraPosition().zoom);
					//	increment=-5;
						new incrementer().execute();
					}
					break;
					//return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					pressedUp = false;
					break;
				}
				return false;
			}
		});

		return view;
	}



	//funcion para ocultar teclado
	public void hidekeyboard(View view){
		 InputMethodManager imm = (InputMethodManager) view.getContext()
			        .getSystemService(Context.INPUT_METHOD_SERVICE);
			     imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	
	//quita el marker que haya y uno nuevo en latlng indicada
	public void putMarker(LatLng center){
		if (marker != null){
			marker.remove();
		}
		this.center=center;
		marker=map.addMarker(new MarkerOptions()
		.position(center));

	}

	//quita el circulo que haya y uno nuevo en latlng indicada
	public void paintCircle(LatLng lat, int radius){
		if (circle!=null){
			circle.remove();
		}
		
		
		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(lat);
		circleOptions.radius(radius); // In meters
		circleOptions.fillColor(0X3A59F44E);
		circleOptions.strokeColor(0X5A59F44E); //borde
		// Get back the mutable Circle
		circle = map.addCircle(circleOptions);
	}

	//Sin relleno
	public void paintCircleWithoutFill(LatLng lat, int radius){
		if (circle!=null){
			circle.remove();
		}

		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(lat);
		circleOptions.radius(radius); // In meters
		circleOptions.strokeColor(0X5A59F44E); //borde
		// Get back the mutable Circle
		circle = map.addCircle(circleOptions);
	}




	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}



	public int metersZoom(float zoom){
		int increment=5;
		
		if (zoom>=16.5 & zoom<17.5){
			increment=5;
		}
		
		else if (zoom>=17.5 & zoom<18.5){
			increment=3;
		}
		else if (zoom>=18.5){
			increment=1;
		}
		else if (zoom>=15.5 & zoom<16.5){
			increment=10;
		}
		else if (zoom>=14.5 & zoom<15.5){
			increment=20;
		}
		else if (zoom>=13.5 & zoom<14.5){
			increment=40;
		}
		else if (zoom>=12.5 & zoom<13.5){
			increment=80;
		}
		else if (zoom>=11.5 & zoom<12.5){
			increment=160;
		}
		else if (zoom>=10.5 & zoom<11.5){
			increment=320;
		}
		else if (zoom>=9.5 & zoom<10.5){
			increment=640;
		}
		else if (zoom>=8.5 & zoom<9.5){
			increment=1280;
		}
		else if (zoom>=7.5 & zoom<8.5){
			increment=2560;
		}
		else if (zoom>=6.5 & zoom<7.5){
			increment=5120;
		}
		else if (zoom>=5.5 & zoom<6.5){
			increment=10240;
		}
		else if (zoom>=4.5 & zoom<5.5){
			increment=20480;
		}
		else if (zoom>=3.5 & zoom<4.5){
			increment=40960;
		}
		else if (zoom>=2.5 & zoom<3.5){
			increment=81920;
		}
		else if (zoom<2.5){
			increment=163840;
		}
		return increment;
	}

	private class incrementer extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			while(pressedUp) {
				auxRadius=Integer.parseInt(etRadius.getText().toString());
				auxRadius= auxRadius + increment;
				if (auxRadius<30){
					auxRadius=30;
				}
				publishProgress(auxRadius);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			}
			return null;
		}
		protected void onProgressUpdate(Integer... progress) {
			etRadius.setText(Integer.toString(auxRadius));
			paintCircleWithoutFill(center,auxRadius);
		}
		protected void onPostExecute(Void result) {
			paintCircle(center, auxRadius);
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private class MyGeoder extends AsyncTask<Void, Void, Void> {

		List<Address> address = null;


		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Geocoder geocoder = new Geocoder(getActivity());
			try {
				if (Geocoder.isPresent()) {
					//for(int i=0;i<10;i++){
						if (address==null){
							address = geocoder.getFromLocationName(etAddress.getText().toString(), 1);
							Thread.sleep(500);				    
					//	}
					}
									  
				}
				
								
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ((address != null) && (address.size() > 0)) {
				//para el inicio, al haber una posicion ya, se habilitan los botones
				if(center==null){
					  etRadius.setEnabled(true);
					   btPlus.setEnabled(true);
					   btMinus.setEnabled(true);
				}
				center= new LatLng(address.get(0).getLatitude(),address.get(0).getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(center,17));
				putMarker(center);
				paintCircle(center, Integer.parseInt(etRadius.getText().toString()));

			}
			else if((address != null) && address.size()==0) {
				Toast.makeText(getActivity(), "No se ha encontrado la ubicacion", Toast.LENGTH_SHORT).show();
			}
			else if (address==null){
			Toast.makeText(getActivity(), "Problema de conexión", Toast.LENGTH_SHORT).show();
			}
		}

	}

}