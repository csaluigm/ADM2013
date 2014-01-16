package com.adm.geoadm;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.services.NotificationService;

public class Lista_categorias extends Activity {
	ListView CatView;
	private AdaptadorCategorias Cadapter;
	ArrayList<Categoria> categorias;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_categorias);
		
		//Initialize Notification Service
		startService(new Intent(this,NotificationService.class));
		
		CatView=(ListView)findViewById(R.id.lista_categorias);
		CategoriasDB catDB = new CategoriasDB(this);
		categorias=catDB.listarCategorias();
		catDB.close();
		
		for (Categoria cat : categorias)
			Log.d("CATEGORIAS", cat.toString());
				
		asociarAdapter();
	}
	
	public void asociarAdapter() {
		Cadapter = new AdaptadorCategorias(this, R.layout.fila_categoria,
				categorias);
		CatView.setAdapter(Cadapter);
	}
	
	public class AdaptadorCategorias extends ArrayAdapter<Categoria> {
		private ArrayList<Categoria> items;

		public AdaptadorCategorias(Context context, int textViewResourceId,
				ArrayList<Categoria> items) {
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
				v = vi.inflate(R.layout.fila_categoria, null);

			}
			Categoria cat = items.get(position);
			if (cat != null) {

				TextView nombre_cat = (TextView) v.findViewById(R.id.name_category);
				TextView cantidad_cat = (TextView) v.findViewById(R.id.count_cat);
				//TextView categoria = (TextView) v.findViewById(R.id.categoria);
			

				if (nombre_cat != null) {
					nombre_cat.setText(cat.getNombre());
				}
				if (cantidad_cat != null) {
					// TO DO
					cantidad_cat.setText(""+0);
					
				}

		
	

			}
			return v;
		}
	}
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_categorias, menu);
		return true;
	}

}
