package com.adm.geoadm.fragments;

import java.sql.Date;
import java.util.ArrayList;

import com.adm.geoadm.R;
import com.adm.geoadm.R.color;
import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

import android.graphics.Typeface;
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
	int diasSemana=0;

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

		// rellenarSpinner();

		return view;
	}

	private void rellenarSpinner() {
		// TODO Auto-generated method stub
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<String> nombresCategorias = new ArrayList<String>();

		categoriasDB = new CategoriasDB(getActivity().getApplicationContext());
		categorias = categoriasDB.listarCategorias();

		for (int i = 0; i < categorias.size(); i++) {
			nombresCategorias.add(categorias.get(i).getNombre());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), android.R.layout.simple_spinner_item,
				nombresCategorias);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriasSpinner.setAdapter(adapter);
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
				int valor = obtenerValorDia(dia);
				diasSemana = diasSemana - valor;
			} else {

				dia.setTextColor(getResources().getColor(color.Icazul));
				dia.setTypeface(Typeface.DEFAULT_BOLD);
				int valor = obtenerValorDia(dia);
				diasSemana = diasSemana + valor;
			}
		}

	}

	private int obtenerValorDia(TextView dia) {
		// TODO Auto-generated method stub
		int valor = 0;
		if (dia.getId() == lunes.getId())
			valor = 64;
		else if (dia.getId() == martes.getId())
			valor = 32;
		else if (dia.getId() == miercoles.getId())
			valor = 16;
		else if (dia.getId() == jueves.getId())
			valor = 8;
		else if (dia.getId() == viernes.getId())
			valor = 4;
		else if (dia.getId() == sabado.getId())
			valor = 2;
		else if (dia.getId() == domingo.getId())
			valor = 1;
		return valor;
	}

	private void agregarRecordatorio() {
		// TODO Auto-generated method stub

		recordatoriosDB = new RecordatoriosDB(getActivity()
				.getApplicationContext());
		categoriaEscogida = (Categoria) categoriasSpinner.getSelectedItem();

		Recordatorio recordatorio = new Recordatorio();
		
		recordatorio.setNombre("" + textonombre.getText());
		recordatorio.setDescripcion("" + textodescripcion.getText());
		// recordatorio.setCategoria(categoriaEscogida);
		recordatorio.setHoraInicio(horaInicioEdit.getText().toString());
		recordatorio.setHoraFin(horaFinEdit.getText().toString());
		recordatorio.setDiasSemana(diasSemana);
		recordatorio.activar();

		// CON RESPECTO AL MAPFRAGMENT
		// recordatorio.setDireccion(direccion);
		// recordatorio.setLatitud(latitud);
		// recordatorio.setLongitud(longitud);
		// recordatorio.setRadius(d);
		// recordatorio.setIdGeofence(idGeofence);

		recordatoriosDB.insertar(recordatorio);
	}
	}

