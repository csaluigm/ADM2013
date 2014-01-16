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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class MainActivity extends ActionBarActivity  {
	ListView RecView;
	TextView cabecera;
	private AdaptadorRecordatorios Radapter;
	ArrayList<Recordatorio> recordatorios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cabecera=(TextView)findViewById(R.id.cabecera);
		
		//Initialize Notification Service
		startService(new Intent(this,NotificationService.class));
		
		
		RecView=(ListView)findViewById(R.id.lista_recordatorios);
		RecordatoriosDB recDB = new RecordatoriosDB(this);
		Recordatorio rec=new Recordatorio();
		rec.setActiva(true);
		rec.setNombre("recordatoriotest");
		rec.setDescripcion("textooo");
		rec.setDiasSemana(9);
		
		recDB.insertar(rec);
		
		recordatorios=recDB.listarRecordatorios();
		recDB.close();
		
		cabecera.setText("Tienes "+recordatorios.size()+" recordatorios");
		
		for (Recordatorio reco : recordatorios)
			Log.d("RECORDATORIOS", reco.toString());
				
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
				TextView lunes = (TextView) v.findViewById(R.id.lunes);
				TextView martes = (TextView) v.findViewById(R.id.martes);
				TextView miercoles = (TextView) v.findViewById(R.id.miercoles);
				TextView jueves = (TextView) v.findViewById(R.id.jueves);
				TextView viernes = (TextView) v.findViewById(R.id.viernes);
				TextView sabado = (TextView) v.findViewById(R.id.sabado);
				TextView domingo = (TextView) v.findViewById(R.id.domingo);
				TextView hora = (TextView) v.findViewById(R.id.hora);
				ToggleButton activo=(ToggleButton)v.findViewById(R.id.activo);
				
				
				
				if(activo!=null){
					if(rec.isActiva()){
						activo.setChecked(true);
					}
					else{
						activo.setChecked(false);
					}
					
					activo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId()==R.id.activo){
								if(((ToggleButton)v).isChecked()){
									RecordatoriosDB recDB = new RecordatoriosDB(MainActivity.this);
									//recDB.modificar(recordatorios.get(position).getId(), recordatorios.get(position).setActiva(true));
								}
								
							}
						}
					});
				}
			
				ArrayList<Boolean>dias=enterotodias(rec.getDiasSemana());				
				
				
				if (nombre != null) {
					nombre.setText(rec.getNombre());
				}
				if (texto != null) {
					texto.setText(rec.getDescripcion());
				}
				if (categoria != null) {
					//categoria.setText(rec.getCategoria());
				}
				if (lunes != null) {
					if(dias.get(0)){
						lunes.setTextColor(getResources().getColor(color.Icazul));
					}
				}
				if (martes != null) {
					if(dias.get(1)){
						martes.setTextColor(getResources().getColor(color.Icazul));
					}
				}
		
				if (miercoles != null) {
					if(dias.get(2)){
						miercoles.setTextColor(getResources().getColor(color.Icazul));
					}
				}
				if (jueves != null) {
					if(dias.get(3)){
						jueves.setTextColor(getResources().getColor(color.Icazul));
					}
				}
				if (viernes != null) {
					if(dias.get(4)){
						viernes.setTextColor(getResources().getColor(color.Icazul));
					}
				}
				if (sabado != null) {
					if(dias.get(5)){
						sabado.setTextColor(getResources().getColor(color.Icazul));
					}
				}
				if (domingo != null) {
					if(dias.get(6)){
						domingo.setTextColor(getResources().getColor(color.Icazul));
						
					}
				}
				if (hora != null) {
					hora.setText(rec.getHoraInicio()+" a "+rec.getHoraFin());
				}

			}
			return v;
		}
	}
	public ArrayList<Boolean> enterotodias(int binario){
		ArrayList<Boolean> semana=new ArrayList<Boolean>();
		String bin=Integer.toBinaryString(binario);

		bin=addceros(bin);
		String[]valores=bin.split("");
		
		
		for(int i=1;i<valores.length;i++){
			
			
			if(valores[i].compareTo("1")==0){
				
				semana.add(true);
			}
			else{
				semana.add(false);
			}
		}
		return semana;
	}
	
	public String addceros(String b){
		String ceros="";
		for (int i = 0; i < 7-b.length(); i++) {
			ceros="0"+ceros;
		}
		return ceros+b;
	}
}
