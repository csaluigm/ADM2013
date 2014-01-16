package com.adm.geoadm.fragments;

import java.sql.Date;
import java.util.ArrayList;

import com.adm.geoadm.R;
import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsFragment extends Fragment  implements OnClickListener{

	
	TextView textonombre,textodescripcion,textocategoria, textodiario,textorepetir,textoprioridad;
	EditText descripcionEdit, fechaEdit, horainicioEdit, horafinEdit, prioridadEdit;
	Button agregarButton;
	SeekBar prioridadSeekBar;
	Spinner categoriasSpinner;
	CheckBox todoeldia, repetir;
	DatePicker fechaText;
	RecordatoriosDB recordatoriosDB;
	Categoria categoriaEscogida;
	CategoriasDB categoriasDB;
	Date fecha;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_details, null);
		
		textonombre = (TextView) view.findViewById(R.id.fragment_details_nombreEdit);
		textodescripcion= (TextView) view.findViewById(R.id.fragment_details_editText_descripcion);
		textonombre = (TextView) view.findViewById(R.id.fragment_details_nombreEdit);
		textonombre = (TextView) view.findViewById(R.id.fragment_details_nombreEdit);
		categoriasSpinner = (Spinner) view.findViewById(R.id.fragment_details_spinnerCategorias);
		agregarButton = (Button) view.findViewById (R.id.fragment_details_agregar);
		descripcionEdit = (EditText) view.findViewById(R.id.fragment_details_nombreEdit);
		todoeldia = (CheckBox) view.findViewById(R.id.todoeldia);
		repetir = (CheckBox) view.findViewById(R.id.fragment_details_repetir);
		prioridadSeekBar = (SeekBar) view.findViewById(R.id.fragment_details_seekbar);
		horainicioEdit = (EditText) view.findViewById(R.id.fragment_details_horaInicio);
		horafinEdit = (EditText) view.findViewById(R.id.fragment_details_horaFin);
		fechaEdit = (EditText) view.findViewById(R.id.fragment_details_datePicker);
		
		
		agregarButton.setOnClickListener(this);
		rellenarSpinner();
		
		
		
		
		
		return view;
	}

	private void rellenarSpinner() {
		// TODO Auto-generated method stub
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<String> nombresCategorias = new ArrayList<String>();
		
		categoriasDB = new CategoriasDB(getActivity().getApplicationContext());
		categorias = categoriasDB.listarCategorias();
		
		for(int i =0 ;i<categorias.size();i++){
			nombresCategorias.add(categorias.get(i).getNombre());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,nombresCategorias);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriasSpinner.setAdapter(adapter);
		}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId()==agregarButton.getId()){
			agregarRecordatorio();
		}
		
	}

	private void agregarRecordatorio() {
		// TODO Auto-generated method stub
		
		recordatoriosDB = new RecordatoriosDB(getActivity().getApplicationContext());
		categoriaEscogida = (Categoria) categoriasSpinner.getSelectedItem();
		int diasSemana = getDiasSemana();
		
		DatePicker datePicker = new DatePicker(getActivity().getApplicationContext());
		fecha.setDate(datePicker.getDayOfMonth());
		fecha.setMonth(datePicker.getMonth());
		fecha.setYear(datePicker.getYear());
		
		
		Recordatorio recordatorio = new Recordatorio();
		
		recordatorio.setNombre(""+textonombre.getText());
		recordatorio.setDescripcion(""+textodescripcion.getText());
		recordatorio.setCategoria(categoriaEscogida);
		recordatorio.setFecha(fecha);
		recordatorio.setTodoElDia(todoeldia.isSelected());
		recordatorio.setHoraInicio(horainicioEdit.getText().toString());
		recordatorio.setHoraFin(horafinEdit.getText().toString());
		recordatorio.setDiasSemana(diasSemana);
		recordatorio.setPrioridad(prioridadSeekBar.getProgress());
		
		//CON RESPECTO AL MAPFRAGMENT
//		recordatorio.setDireccion(direccion);
//		recordatorio.setLatitud(latitud);
//		recordatorio.setLongitud(longitud);
//		recordatorio.setRadius(d);
//		recordatorio.setIdGeofence(idGeofence);
		
		
		recordatoriosDB.insertar(recordatorio);
		Toast.makeText(getActivity().getApplicationContext(), ""+recordatorio.getNombre()
				+recordatorio.getDescripcion()+recordatorio.getDiasSemana(),Toast.LENGTH_LONG).show();
	}

	private int getDiasSemana() {
		// TODO Auto-generated method stub
		
		return 0;
	}
	
	
}
