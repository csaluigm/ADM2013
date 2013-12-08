package com.adm.geoadm.fragments;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapFragment  extends Fragment implements InfoWindowAdapter, OnInfoWindowClickListener {
	//--- GUI ---
	EditText etAddress;
	Button btGo;
	GoogleMap map=null;
	EditText etRadius;
	Button btPlus;
	Button btMinus;
	
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
		
		if (map == null) {
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fMap)).getMap();
		

		if (map != null) {
			map.setInfoWindowAdapter(this);
			map.setOnInfoWindowClickListener(this);
		
		}
			}
		
		
		Button btGo = (Button) view.findViewById(R.id.btGo);
		btGo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new MyGeoder().execute();

			}
		});
		return view;
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
				LatLng lat= new LatLng(address.get(0).getLatitude(),address.get(0).getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,17));
			}
			else {
				Toast.makeText(getActivity(), "Geocoder unavailable", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
}
