package com.adm.geoadm;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.adm.geoadm.R.color;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;
import com.adm.geoadm.services.NotificationService;

/**
 * 
 * @author cesar
 * 
 */
public class MainActivity extends ActivityMenuLateral {
	ListView RecView;
	TextView cabecera;
	private AdaptadorRecordatorios Radapter;
	ArrayList<Recordatorio> recordatorios;
	TextView aviso;
	ImageView imremind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu_lateral(R.array.lateral_lista_recordatorios, this);
		cabecera = (TextView) findViewById(R.id.cabecera);
		aviso = (TextView) findViewById(R.id.avisonuevo);
		imremind = (ImageView) findViewById(R.id.imremind);
		imremind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Lanzar_nuevo_recordatorio();
			}
		});

		// Initialize Notification Service
		startService(new Intent(this, NotificationService.class));

		RecView = (ListView) findViewById(R.id.lista_recordatorios);
		registerForContextMenu(RecView);

		RecordatoriosDB recDB = new RecordatoriosDB(this);

		// recordatoriotest();

		recordatorios = recDB.listarRecordatorios();
		recDB.close();

		for (Recordatorio reco : recordatorios)
			Log.d("RECORDATORIOS", reco.toString());

		asociarAdapter();
		actualizar_interfaz();

	}

	public void recordatoriotest() {
		RecordatoriosDB recDB = new RecordatoriosDB(this);
		Recordatorio rec = new Recordatorio();
		rec.setActiva(true);
		rec.setNombre("recordatoriotest");
		rec.setDescripcion("textooo");
		rec.setDiasSemana(9);
		recDB.insertar(rec);
	}

	public void actualizar_interfaz() {

		if (recordatorios.size() > 0) {

			cabecera.setText("Tienes " + recordatorios.size()
					+ " recordatorios");
			RecView.setVisibility(View.VISIBLE);
			cabecera.setVisibility(View.VISIBLE);
			aviso.setVisibility(View.GONE);
			imremind.setVisibility(View.GONE);

		} else {

			RecView.setVisibility(View.GONE);
			cabecera.setVisibility(View.GONE);
			aviso.setVisibility(View.VISIBLE);
			imremind.setVisibility(View.VISIBLE);
		}
	}

	public void Lanzar_nuevo_recordatorio() {
		Intent intent = new Intent(this, NuevoRecordatorio.class);
		startActivity(intent);
	}

	public void asociarAdapter() {
		Radapter = new AdaptadorRecordatorios(this, R.layout.elemento_fila,
				recordatorios);
		RecView.setAdapter(Radapter);
		RecView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("posicion", "" + position);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.lista_recordatorios) {

			menu.setHeaderTitle("Acciones");
			menu.add(Menu.NONE, 0, 0, "Borrar");
			menu.add(Menu.NONE, 1, 1, "Modificar");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			Recordatorio r = recordatorios.get(info.position);

			RecordatoriosDB recDB = new RecordatoriosDB(MainActivity.this);
			recDB.borrar(r);
			recordatorios.remove(info.position);

			recDB.close();

			Radapter.notifyDataSetChanged();
			actualizar_interfaz();
		}

		return true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch(item.getItemId()){
	//
	// case R.id.action_nuevo:
	// Intent intent = new Intent(this, NuevoRecordatorio.class);
	// startActivity(intent);
	// return true;
	//
	// }
	//
	// return false;
	// }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Log.d("posicion", "entra");
		switch (i) {
		case 0:
			// intent
			break;
		case 1:
			// intent

		case 2:
			// intent

			break;

		default:
			break;
		}
		mDrawer.closeDrawers();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle item selection
		switch (item.getItemId()) {

		case R.id.action_nuevo:
			Lanzar_nuevo_recordatorio();

			return true;

		case android.R.id.home:
			if (mDrawer.isDrawerOpen(mDrawerOptions)) {
				mDrawer.closeDrawers();
			} else {
				mDrawer.openDrawer(mDrawerOptions);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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
				TextView lunes = (TextView) v.findViewById(R.id.lunes);
				TextView martes = (TextView) v.findViewById(R.id.martes);
				TextView miercoles = (TextView) v.findViewById(R.id.miercoles);
				TextView jueves = (TextView) v.findViewById(R.id.jueves);
				TextView viernes = (TextView) v.findViewById(R.id.viernes);
				TextView sabado = (TextView) v.findViewById(R.id.sabado);
				TextView domingo = (TextView) v.findViewById(R.id.domingo);
				TextView hora = (TextView) v.findViewById(R.id.hora);
				ToggleButton activo = (ToggleButton) v
						.findViewById(R.id.activo);

				if (activo != null) {
					if (rec.isActiva()) {
						activo.setChecked(true);
					} else {
						activo.setChecked(false);
					}

					activo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Recordatorio record = recordatorios.get(position);
							Log.d("Toggle", "cambiado estado de toggle");
							if (v.getId() == R.id.activo) {
								if (((ToggleButton) v).isChecked()) {
									record.setActiva(true);
								} else {
									record.setActiva(false);
								}
								RecordatoriosDB recDB = new RecordatoriosDB(
										MainActivity.this);
								recDB.modificar(record.getId(), record);
								recDB.close();

							}
						}
					});
				}

				ArrayList<Boolean> dias = enterotodias(rec.getDiasSemana());

				if (nombre != null) {
					nombre.setText(rec.getNombre());
				}
				if (texto != null) {
					texto.setText(rec.getDescripcion());
				}
				if (categoria != null) {
					// categoria.setText(rec.getCategoria());
				}
				if (lunes != null) {
					if (dias.get(0)) {
						resaltar_dia(lunes);
					}
				}
				if (martes != null) {
					if (dias.get(1)) {
						resaltar_dia(martes);
					}
				}

				if (miercoles != null) {
					if (dias.get(2)) {
						resaltar_dia(miercoles);
					}
				}
				if (jueves != null) {
					if (dias.get(3)) {
						resaltar_dia(jueves);
					}
				}
				if (viernes != null) {
					if (dias.get(4)) {
						resaltar_dia(viernes);
					}
				}
				if (sabado != null) {
					if (dias.get(5)) {
						resaltar_dia(sabado);
					}
				}
				if (domingo != null) {
					if (dias.get(6)) {
						resaltar_dia(domingo);

					}
				}
				if (hora != null) {
					hora.setText(rec.getHoraInicio() + " a " + rec.getHoraFin());
				}

			}
			return v;
		}
	}

	public void resaltar_dia(TextView dia) {
		dia.setTextColor(getResources().getColor(color.Icazul));
		dia.setTypeface(Typeface.DEFAULT_BOLD);
	}

	public ArrayList<Boolean> enterotodias(int binario) {
		ArrayList<Boolean> semana = new ArrayList<Boolean>();
		String bin = Integer.toBinaryString(binario);

		bin = addceros(bin);
		String[] valores = bin.split("");

		for (int i = 1; i < valores.length; i++) {

			if (valores[i].compareTo("1") == 0) {

				semana.add(true);
			} else {
				semana.add(false);
			}
		}
		return semana;
	}

	public String addceros(String b) {
		String ceros = "";
		for (int i = 0; i < 7 - b.length(); i++) {
			ceros = "0" + ceros;
		}
		return ceros + b;
	}
}
