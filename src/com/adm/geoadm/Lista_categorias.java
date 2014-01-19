package com.adm.geoadm;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

public class Lista_categorias extends ActionBarActivity {
	ListView CatView;
	private AdaptadorCategorias Cadapter;
	ArrayList<Categoria> categorias;
	ArrayList<Recordatorio> recordatorios;
	private TextView cabecera;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_categorias);
		
		
		cabecera = (TextView) findViewById(R.id.cabecera);
		CatView=(ListView)findViewById(R.id.lista_categorias);
		CategoriasDB catDB = new CategoriasDB(this);
		categorias=catDB.listarCategorias();
		catDB.close();
		
		
		for (Categoria cat : categorias)
			Log.d("CATEGORIAS", cat.toString());
				
		asociarAdapter();
		actualizar_interfaz();
		RecordatoriosDB recDB = new RecordatoriosDB(this);
		recordatorios = recDB.listarRecordatorios();
		recDB.close();
		
	}
	
	public void asociarAdapter() {
		Cadapter = new AdaptadorCategorias(this, R.layout.fila_categoria,
				categorias);
		CatView.setAdapter(Cadapter);
	}
	

	public void actualizar_interfaz() {

		if (categorias.size() > 0) {

			cabecera.setText("Tienes " + categorias.size()
					+ "Categorias");
		}
		else{
			cabecera.setText("No tienes ninguna categoría para clasificar tus recordatorios");
		}
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
					nombre_cat.setText("#"+cat.getNombre());
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.action_nuevo:
			//abrir dialogo poner nombre
			// pedir el numero de participantes
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getResources().getString(
					R.string.Dialog_categoria_title));
			alert.setMessage(getResources().getString(
					R.string.Dialog_categoria_message));

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			input.requestFocus();
			alert.setView(input);

			alert.setPositiveButton(getResources()
					.getString(R.string.Dialog_categoria_positivo),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (input.getText().toString().length() > 0) {
								//recoger texto
								
								CategoriasDB catDB = new CategoriasDB(Lista_categorias.this);
								Categoria c=new Categoria();
								c.setNombre(input.getText().toString());
								catDB.insertar(c);								
								categorias=catDB.listarCategorias();
								catDB.close();
								Cadapter.notifyDataSetChanged();
								actualizar_interfaz();
									} 

								
							
							// ocultar teclado
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(input.getWindowToken(),
									0);
						}
					});

			alert.setNegativeButton(
					getResources().getString(R.string.Dialog_categoria_negativo),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert.setCancelable(false);
			alert.show();
			
			
			

			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public int contar_recordatorios_categoria(int cid){
		int i=0;
		for (Recordatorio r : recordatorios){
			if(r.getCategoriaId()==cid)i++;
		}		
		return i;
	}
	
	public void borrar_recordatorios_categoria(int cid){
		
	}
	

}
