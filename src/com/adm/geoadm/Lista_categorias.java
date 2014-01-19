package com.adm.geoadm;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		cabecera = (TextView) findViewById(R.id.cabecera);
		CatView = (ListView) findViewById(R.id.lista_categorias);
		registerForContextMenu(CatView);
		CategoriasDB catDB = new CategoriasDB(this);
		categorias = catDB.listarCategorias();
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

			cabecera.setText("Tienes " + categorias.size() + " categorias");
		} else {
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

				TextView nombre_cat = (TextView) v
						.findViewById(R.id.name_category);
				TextView cantidad_cat = (TextView) v
						.findViewById(R.id.count_cat);
				// TextView categoria = (TextView)
				// v.findViewById(R.id.categoria);

				if (nombre_cat != null) {
					nombre_cat.setText("#" + cat.getNombre());
				}
				if (cantidad_cat != null) {
					// TO DO
					cantidad_cat.setText("" + 0);

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
			// abrir dialogo poner nombre
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

			alert.setPositiveButton(
					getResources()
							.getString(R.string.Dialog_categoria_positivo),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (input.getText().toString().length() > 0) {
								// recoger texto

								CategoriasDB catDB = new CategoriasDB(
										Lista_categorias.this);
								Categoria c = new Categoria();
								c.setNombre(input.getText().toString());
								catDB.insertar(c);
								// categorias=catDB.listarCategorias();
								catDB.close();
								Cadapter.add(c);
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
					getResources()
							.getString(R.string.Dialog_categoria_negativo),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert.setCancelable(false);
			alert.show();

			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public int contar_recordatorios_categoria(int cid) {
		int i = 0;
		for (Recordatorio r : recordatorios) {
			if (r.getCategoriaId() == cid)
				i++;
		}
		return i;
	}

	public void borrar_recordatorios_categoria(int cid) {

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.lista_categorias) {

			menu.setHeaderTitle("Acciones");
			menu.add(Menu.NONE, 0, 0, "Borrar");

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			Categoria c = categorias.get(info.position);
			borrar_recordatorios_categoria(c.getId());
			CategoriasDB catDB = new CategoriasDB(Lista_categorias.this);
			catDB.borrar(c);
			categorias.remove(info.position);
			catDB.close();
			Cadapter.notifyDataSetChanged();
			actualizar_interfaz();
			//((MainActivity)getParent()).menu_lateral_categorias();
		}

		return true;

	}
}