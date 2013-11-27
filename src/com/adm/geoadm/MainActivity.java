package com.adm.geoadm;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	ListView RecView;
	private AdaptadorRecordatorios Radapter;
	ArrayList<Recordatorio> recordatorios;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RecView=(ListView)findViewById(R.id.lista_recordatorios);
		recordatorios=new ArrayList<Recordatorio>();
		
		
		recordatorios.add(new Recordatorio("rec1", "acuerdate","hogar"));
		recordatorios.add(new Recordatorio("rec2", "acuerdatexxxxx","hogar"));
		
		asociarAdapter();
		
	}
	
	public void asociarAdapter() {
		Radapter = new AdaptadorRecordatorios(this, R.layout.elemento_fila,
				recordatorios);
		RecView.setAdapter(Radapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()){
	        
	        case R.id.action_nuevo:
	        	Intent intent = new Intent(this, NuevoRecordatorio.class);
	        	startActivity(intent);
	            return true;
	      
	    }

	    return false;
	}
	
	public class AdaptadorRecordatorios extends ArrayAdapter<Recordatorio> {
		private ArrayList<Recordatorio> items;

		public AdaptadorRecordatorios(Context context, int textViewResourceId,
				ArrayList<Recordatorio> items) {
			super(context, textViewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
			
			View v = convertView;
			if (v == null) {
				Log.d("indice", "indice " + position);
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.elemento_fila, null);

			}
			Recordatorio rec = items.get(position);
			if (rec != null) {

				TextView nombre = (TextView) v.findViewById(R.id.nombre);
				TextView texto = (TextView) v.findViewById(R.id.texto);
				TextView categoria = (TextView) v.findViewById(R.id.categoria);
			

				if (nombre != null) {
					nombre.setText(rec.getNombre());
				}
				if (texto != null) {
					texto.setText(rec.getTexto());
				}
				if (categoria != null) {
					categoria.setText(rec.getCategoria());
				}
		
	

			}
			return v;
		}
	}
	
}
