package com.adm.geoadm.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adm.geoadm.NuevoRecordatorio;
import com.adm.geoadm.R;
import com.adm.geoadm.R.color;
import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

public class DetailsFragment extends Fragment implements OnClickListener {

	TextView textonombre, textodescripcion, textocategoria, textoprioridad,
			lunes, martes, miercoles, jueves, viernes, sabado, domingo;
	EditText descripcionEdit, horaInicioEdit, horaFinEdit;
	Button agregarButton;
	SeekBar prioridadSeekBar;
	Spinner categoriasSpinner;
	RecordatoriosDB recordatoriosDB;
	Categoria categoriaEscogida;
	CategoriasDB categoriasDB;
	ArrayList<Categoria> categorias;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_details, null);
		textonombre = (TextView) view
				.findViewById(R.id.fragment_details_nombreEdit);
		textodescripcion = (TextView) view
				.findViewById(R.id.fragment_details_editText_descripcion);
		categoriasSpinner = (Spinner) view
				.findViewById(R.id.fragment_details_spinnerCategorias);
		descripcionEdit = (EditText) view
				.findViewById(R.id.fragment_details_nombreEdit);
		horaInicioEdit = (EditText) view
				.findViewById(R.id.fragment_details_horaInicio);
		horaFinEdit = (EditText) view
				.findViewById(R.id.fragment_details_horaFin);
		agregarButton = (Button) view
				.findViewById(R.id.fragment_details_agregar);
		descripcionEdit = (EditText) view
				.findViewById(R.id.fragment_details_nombreEdit);
		lunes = (TextView) view.findViewById(R.id.fd_lunes);
		martes = (TextView) view.findViewById(R.id.fd_martes);
		miercoles = (TextView) view.findViewById(R.id.fd_miercoles);
		jueves = (TextView) view.findViewById(R.id.fd_jueves);
		viernes = (TextView) view.findViewById(R.id.fd_viernes);
		sabado = (TextView) view.findViewById(R.id.fd_sabado);
		domingo = (TextView) view.findViewById(R.id.fd_domingo);

		lunes.setOnClickListener(this);
		martes.setOnClickListener(this);
		miercoles.setOnClickListener(this);
		jueves.setOnClickListener(this);
		viernes.setOnClickListener(this);
		sabado.setOnClickListener(this);
		domingo.setOnClickListener(this);
		agregarButton.setOnClickListener(this);
		horaFinEdit.setOnClickListener(this);
		horaInicioEdit.setOnClickListener(this);
		

		
		categoriasDB = new CategoriasDB(getActivity().getApplicationContext());
				
		
		
		rellenarSpinner();

		return view;
	}

	private void rellenarSpinner() {
		// TODO Auto-generated method stub
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<String> nombresCategorias = new ArrayList<String>();

//		categorias = pruebaDemoCategorias();
		
		
		
		categorias = categoriasDB.listarCategorias();

		for (int i = 0; i < categorias.size(); i++) {
			nombresCategorias.add(categorias.get(i).getNombre());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				nombresCategorias);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriasSpinner.setAdapter(adapter);
	}

	private ArrayList<Categoria> pruebaDemoCategorias() {
		// TODO Auto-generated method stub
		ArrayList<Categoria> categorias2 = new ArrayList<Categoria>();
		
		for(int i =0;i<10;i++){
			Categoria cat = new Categoria();
			cat.setId(i);
			cat.setNombre("Categoria"+i);
			categorias2.add(cat);
			categoriasDB.insertar(cat);
		}
		return categorias2;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int argId = arg0.getId();
		if (argId == agregarButton.getId()) {
			agregarRecordatorio();
		}
		if (argId == lunes.getId() || argId == martes.getId()
				|| argId == miercoles.getId() || argId == jueves.getId()
				|| argId == viernes.getId() || argId == sabado.getId()
				|| argId == domingo.getId()) {

			TextView dia = (TextView) arg0;
			if (dia.getTypeface() == Typeface.DEFAULT_BOLD) {
				dia.setTextColor(getResources().getColor(color.Black));
				dia.setTypeface(Typeface.DEFAULT);
			} else {

				dia.setTextColor(getResources().getColor(color.Icazul));
				dia.setTypeface(Typeface.DEFAULT_BOLD);
			}
		}
		if(argId == horaInicioEdit.getId()){
			Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    horaInicioEdit.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
		}
		if(argId == horaFinEdit.getId()){
			Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    horaFinEdit.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
		}
	}

	private int obtenerValorDias() {
		// TODO Auto-generated method stub
		int valor = 0;
		if(lunes.getTypeface() == Typeface.DEFAULT_BOLD) valor+=64;
		if(martes.getTypeface() == Typeface.DEFAULT_BOLD) valor+=32;
		if(miercoles.getTypeface() == Typeface.DEFAULT_BOLD) valor+=16;
		if(jueves.getTypeface() == Typeface.DEFAULT_BOLD) valor+=8;
		if(viernes.getTypeface() == Typeface.DEFAULT_BOLD) valor+=4;
		if(sabado.getTypeface() == Typeface.DEFAULT_BOLD) valor+=2;
		if(domingo.getTypeface() == Typeface.DEFAULT_BOLD) valor+=1;
		return valor;
	}

	private void agregarRecordatorio() {
		// TODO Auto-generated method stub

		recordatoriosDB = new RecordatoriosDB(getActivity().getApplicationContext());
		
		Recordatorio recordatorio = new Recordatorio();
		int diasSemana = obtenerValorDias();
		Categoria categoriaInsertar = new Categoria();
		String nomCategoria = (String) categoriasSpinner.getSelectedItem();
		categoriaInsertar = categoriasDB.getCategoriaPorString(nomCategoria);
		
		recordatorio.setNombre("" + textonombre.getText());
		recordatorio.setDescripcion("" + textodescripcion.getText());
		recordatorio.setCategoria(categoriaInsertar);
		recordatorio.setCategoriaId(categoriaInsertar.getId());
		recordatorio.setHoraInicio(horaInicioEdit.getText().toString());
		recordatorio.setHoraFin(horaFinEdit.getText().toString());
		recordatorio.setDiasSemana(diasSemana);
		recordatorio.activar();

		// CON RESPECTO AL MAPFRAGMENT
		
		MapFragment mf=((NuevoRecordatorio)getActivity()).getTabMap();
		
		recordatorio.setLatitud(mf.getLatitud());
		recordatorio.setLongitud(mf.getLongitud());
		recordatorio.setRadius(mf.getRadio());
		recordatorio.setDireccion(mf.getDireccion()); 
//		 recordatorio.setIdGeofence(idGeofence);
		 
//		Toast.makeText(getActivity().getApplicationContext(),"catID seleccionada:"+categoriaInsertar.getId()+"\ncatNombre seleccionada:"+categoriaInsertar.getNombre(),Toast.LENGTH_LONG).show();
//		 Toast.makeText(getActivity().getApplicationContext(),"punt:"+diasSemana,Toast.LENGTH_LONG).show();

		recordatoriosDB.insertar(recordatorio);
	}
	}

