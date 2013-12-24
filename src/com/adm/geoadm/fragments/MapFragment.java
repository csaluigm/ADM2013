package com.adm.geoadm.fragments;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.R.bool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adm.geoadm.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.Projection;
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
	LatLng center; // iremos guardando la posicion
	boolean pressedUp = false;
	
	//--- Events ----
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, null);
		etAddress = (EditText) view.findViewById(R.id.etAddress);
		etRadius = (EditText) view.findViewById(R.id.etRadius);
		
		
		if (map == null) {
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fMap)).getMap();
		

		if (map != null) {
			map.setInfoWindowAdapter(this);
			map.setOnInfoWindowClickListener(this);
		
		}
			}
	
		
		//Metodo sobreescrito para cuando se haga un click en el mapa
		map.setOnMapClickListener(new OnMapClickListener() {
		    public void onMapClick(LatLng lat) {
		    putMarker(lat);
		    paintCircle(lat, Integer.parseInt(etRadius.getText().toString()), 0X3A59F44E);
		    }
			
		});
		
		etRadius.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			public void onFocusChange(View v, boolean hasFocus) {
		            if(!hasFocus){
		            	if(circle!=null){
		            		
		            		paintCircle(center, Integer.parseInt(etRadius.getText().toString()), 0X3A59F44E);
		            	}
		            }
		               //do job here owhen Edittext lose focus 
			}
		});
		
		
		Button btGo = (Button) view.findViewById(R.id.btGo);
		btGo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new MyGeoder().execute();
			}
			
		});
		
		Button btPlus = (Button) view.findViewById(R.id.btPlus);
		btPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btMinus = (Button) view.findViewById(R.id.btMinus);
		btMinus.setOnTouchListener(new View.OnTouchListener() {        
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		            	 if(pressedUp == false){
		                     pressedUp = true;
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
	public void paintCircle(LatLng lat, int radius, int color){
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
	
	
	
	
	private class incrementer extends AsyncTask<Void, Integer, Void> {

	    @Override
	    protected Void doInBackground(Void... arg0) {
	        while(pressedUp) {
	        	    auxRadius=Integer.parseInt(etRadius.getText().toString());
	            	auxRadius= auxRadius + 5;
	            	publishProgress(auxRadius);
	            	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	         // 	etRadius.setText(Integer.toString(auxRadius));
	            
	    }
	    return null;
	}
	    protected void onProgressUpdate(Integer... progress) {
	    	etRadius.setText(Integer.toString(auxRadius));
	    	

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
					address = geocoder.getFromLocationName(etAddress.getText().toString(), 1);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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
				center= new LatLng(address.get(0).getLatitude(),address.get(0).getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(center,17));
			    putMarker(center);
			    paintCircle(center, Integer.parseInt(etRadius.getText().toString()), 0X3A59F44E);
			    
			}
			else {
				Toast.makeText(getActivity(), "Geocoder unavailable", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
}
