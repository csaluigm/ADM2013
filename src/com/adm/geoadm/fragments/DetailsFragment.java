package com.adm.geoadm.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adm.geoadm.MainActivity;
import com.adm.geoadm.NuevoRecordatorio;
import com.adm.geoadm.R;
import com.adm.geoadm.R.color;
import com.adm.geoadm.db.Categoria;
import com.adm.geoadm.db.CategoriasDB;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

public class DetailsFragment extends Fragment implements OnClickListener {

	TextView lunes, martes, miercoles, jueves, viernes, sabado, domingo;
	EditText descripcionEdit, horaInicioEdit, horaFinEdit,textonombre, textodescripcion;
	Button agregarButton;
	SeekBar prioridadSeekBar;
	Spinner categoriasSpinner;
	RecordatoriosDB recordatoriosDB;
	Categoria categoriaEscogida;
	CategoriasDB categoriasDB;
	ArrayList<Categoria> categorias;
	Integer horaInicio,horaFin,minutoInicio,minutoFin;
	ArrayAdapter<String> adapter;
	boolean modificar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(((NuevoRecordatorio)getActivity()).getRecId()!=-1){
			modificar=true;
		}
		else{
			modificar=false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_details, null);
		textonombre = (EditText) view
				.findViewById(R.id.fragment_details_nombreEdit);
		textodescripcion = (EditText) view
				.findViewById(R.id.fragment_details_editText_descripcion);
		
		categoriasSpinner = (Spinner) view
				.findViewById(R.id.fragment_details_spinnerCategorias);
		
		horaInicioEdit = (EditText) view
				.findViewById(R.id.fragment_details_horaInicio);
		horaFinEdit = (EditText) view
				.findViewById(R.id.fragment_details_horaFin);
		agregarButton = (Button) view
				.findViewById(R.id.fragment_details_agregar);
		
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
		
		categoriasSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                String nueva=categoriasSpinner.getSelectedItem().toString();
                if(nueva.equals("Nueva...")) dialogoNuevaCategoria();

            }
/**
 * Se abre un Dialog similar al de nueva Categoria para aï¿½adir una nueva.
 */
            private void dialogoNuevaCategoria() {
				// TODO Auto-generated method stub
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

    			alert.setTitle(getResources().getString(
    					R.string.Dialog_categoria_title));
    			alert.setMessage(getResources().getString(
    					R.string.Dialog_categoria_message));

    			// Set an EditText view to get user input
    			final EditText input = new EditText(getActivity());
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

    								CategoriasDB catDB = new CategoriasDB(getActivity().getApplicationContext());
    								Categoria c = new Categoria();
    								c.setNombre("#"+input.getText().toString());
    								catDB.insertar(c);
    								catDB.close();
    								rellenarSpinner();
    								categoriasSpinner.setSelection(categoriasSpinner.getCount()-2);
    								
    							}
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

				
			}

			@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
		
		
		if(modificar){
			rellenar_info_rec();
		}

		return view;
	}
	
	public void rellenar_info_rec(){
	recordatoriosDB = new RecordatoriosDB(getActivity().getApplicationContext());
	Recordatorio rmod=recordatoriosDB.getRecordatorio(((NuevoRecordatorio)getActivity()).getRecId());
	textonombre.setText(rmod.getNombre());
	textodescripcion.setText(rmod.getDescripcion());
	horaInicioEdit.setText(rmod.getHoraInicio());
	horaFinEdit.setText(rmod.getHoraFin());
	horaInicio=Integer.parseInt(rmod.getHoraInicio().split(":")[0]);
	minutoInicio=Integer.parseInt(rmod.getHoraInicio().split(":")[1]);
	horaFin=Integer.parseInt(rmod.getHoraFin().split(":")[0]);
	minutoFin=Integer.parseInt(rmod.getHoraFin().split(":")[1]);
	
	agregarButton.setText("Modificar");	
	categoriasSpinner.setSelection(rmod.getCategoriaId());
	rellenar_dias(rmod.getDiasSemana());
	}
	public void rellenar_dias(int valor){
	ArrayList<Boolean> dias = enterotodias(valor);
	
		if (dias.get(0)) {
			resaltar_dia(lunes);
		}
	

		if (dias.get(1)) {
			resaltar_dia(martes);
		}
	

	
		if (dias.get(2)) {
			resaltar_dia(miercoles);
		}
	
	
		if (dias.get(3)) {
			resaltar_dia(jueves);
		}
	
	
		if (dias.get(4)) {
			resaltar_dia(viernes);
		}
	
	
		if (dias.get(5)) {
			resaltar_dia(sabado);
		}
	
	
		if (dias.get(6)) {
			resaltar_dia(domingo);
		
		}
	}
	
	/**
	 * Cambia el color y pone en negrita el TexView que le pasemos
	 * 
	 * @param dia el textview con el dia de la semana para resaltar
	 */
	public void resaltar_dia(TextView dia,int color) {
		dia.setTextColor(color);
		dia.setTypeface(Typeface.DEFAULT_BOLD);
	}

	/**
	 * Transforma un entero en un vector de 7 bools donde cada bool representa
	 * una unidad del entero convertido a binario.true si es 1 false si es 0
	 * 
	 * @param binario entero que queremos convertir a binario y despues a vector de bools
	 * @return vector de bools
	 */
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

	/**
	 * añade ceros por la izquierda para rellenar un numero binario en forma de
	 * string
	 * 
	 * @param b String al que queremos añadir ceros por la izquierda
	 * @return el String con los ceros añadidos
	 */
	public String addceros(String b) {
		String ceros = "";
		for (int i = 0; i < 7 - b.length(); i++) {
			ceros = "0" + ceros;
		}
		return ceros + b;
	}
	
	private void rellenarSpinner() {
		
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<String> nombresCategorias = new ArrayList<String>();
		nombresCategorias.add("Sin categoría");
		categorias = categoriasDB.listarCategorias();

		for (int i = 0; i < categorias.size(); i++) {
			nombresCategorias.add(categorias.get(i).getNombre());
		}
		nombresCategorias.add("Nueva...");
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				nombresCategorias);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriasSpinner.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

/**
 * Si marca uno de los días, lo pinta en negrita y azul.
 * Si es horaInicio, horaFin, lanza un TimePicker para que seleccione la hora.
 */


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
			resaltar_dia(dia);
		
		}
		if(argId == horaInicioEdit.getId()){
			Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                	minutoInicio=selectedMinute;
                	horaInicio=selectedHour;
                	horaInicioEdit.setText( horaInicio.toString().format("%02d", horaInicio) + ":" + minutoInicio.toString().format("%02d",minutoInicio));
                   
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
                	minutoFin=selectedMinute;
                    horaFin=selectedHour;
                    horaFinEdit.setText( horaFin.toString().format("%02d", horaFin) + ":" + minutoFin.toString().format("%02d",minutoFin));
                  
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
		}
	}

	private void resaltar_dia(TextView dia) {
		if (dia.getTypeface() == Typeface.DEFAULT_BOLD) {
			dia.setTextColor(getResources().getColor(color.Black));
			dia.setTypeface(Typeface.DEFAULT);
		} else {
			dia.setTextColor(getResources().getColor(color.Icazul));
			dia.setTypeface(Typeface.DEFAULT_BOLD);
		}
		
	}

/**
 * Recorre todos los dias para ver los que están marcados.
 * De estar en negrita, suma su correspondiente valor para tener los dias de la semana en un entero. 
 * Más tarde este entero se traducirá en binario para poder saber los días escogidos.
 * 
 */
	private int obtenerValorDias() {
	
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
	
	/**
	 * Cuando se selecciona agregar recordatorio desde el botón, se crea un recordatorio nuevo y se rellenan los campos.
	 * Crea una instancia del mapFragment para recibir los datos relativos al mapa del otro fragment.
	 * Cuando inserta el recordatorio en la DB, lanza la activity principal.
	 */
	private void agregarRecordatorio() {
	
		recordatoriosDB = new RecordatoriosDB(getActivity()
				.getApplicationContext());

		Recordatorio recordatorio = new Recordatorio();
		int diasSemana = obtenerValorDias();


		recordatorio.setNombre("" + textonombre.getText());
		if (recordatorio.getNombre() == "") {
			Toast.makeText(getActivity().getApplicationContext(),
					R.string.warning_not_name, Toast.LENGTH_LONG).show();
		} else {
			recordatorio.setDescripcion("" + textodescripcion.getText());
			String nomCategoria = (String) categoriasSpinner.getSelectedItem();

			// control de la categoria
			if (hay_categoria(nomCategoria)) {
				Categoria categoriaInsertar = categoriasDB
						.getCategoriaPorString(nomCategoria);
				int idCat = categoriaInsertar.getId();
				recordatorio.setCategoria(categoriaInsertar);
				recordatorio.setCategoriaId(idCat);
			} else {
				recordatorio.setCategoriaId(-1);
			}
			

			if (horaInicioEdit.getText().length() == 0
					|| horaFinEdit.getText().length() == 0) {
				Toast.makeText(getActivity().getApplicationContext(),
						R.string.warning_not_hour, Toast.LENGTH_LONG).show();
			} else {

				if (!comprobarHoraFinPosterior()) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.warning_hour_not_posterior,
							Toast.LENGTH_LONG).show();
				} else {
					recordatorio.setHoraInicio(horaInicioEdit.getText()
							.toString());
					recordatorio.setHoraFin(horaFinEdit.getText().toString());

					if (diasSemana == 0) {
						Toast.makeText(getActivity().getApplicationContext(),
								R.string.warning_not_day, Toast.LENGTH_LONG)
								.show();
					} else {
						recordatorio.setDiasSemana(diasSemana);
						recordatorio.activar();

						// CON RESPECTO AL MAPFRAGMENT

						MapFragment mf = ((NuevoRecordatorio) getActivity())
								.getTabMap();

						if (mf.getLatitud() == -200) {
							Toast.makeText(
									getActivity().getApplicationContext(),
									R.string.warning_not_location,
									Toast.LENGTH_LONG).show();
						} else {
							recordatorio.setLatitud(mf.getLatitud());
							recordatorio.setLongitud(mf.getLongitud());
							recordatorio.setRadius(mf.getRadio());
							recordatorio.setDireccion(mf.getDireccion());
							if(modificar){
							recordatoriosDB.modificar(((NuevoRecordatorio)getActivity()).getRecId(), recordatorio);
							}
							else{
								recordatoriosDB.insertar(recordatorio);	
							}
							
							recordatoriosDB.close();
							getActivity().finish();
//							Intent intent = new Intent(getActivity(),
//									MainActivity.class);
//							startActivity(intent);
						}

					}
				}
			}
		}
	}
	
	
	/**
	 * contrasta si se ha elegido una categoría en el spinner ya que hay una opción sin categoría
	 * @param nomCategoria el string del elemento seleccionado en el spinner
	 * @return true si coincide con el String acordado para decir que no hay categoría
	 */
	private boolean hay_categoria(String nomCategoria) {
		
		if (nomCategoria.compareTo("Sin categoría")==0){
			return false;
		}
		else {
			return true;
		}		
	}
/**
	 * Comprueba si es coherente las horas marcadas
	 * @return true si es coherente
	 * 		   false si no
	 */
	private boolean comprobarHoraFinPosterior() {
		if(horaFin<=horaInicio)
			if(minutoFin<=minutoInicio)
				return false;
		
		return true;
	}

	
	
	
	
	}



