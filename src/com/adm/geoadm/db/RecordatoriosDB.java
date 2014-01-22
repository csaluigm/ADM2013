package com.adm.geoadm.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecordatoriosDB {
	//-----  MEMBERS  -------------
	RecordatoriosSQLHelper helper;
	SQLiteDatabase db;
		
	//-----  CONSTRUCTORS  --------
	public RecordatoriosDB(Context context) {
		helper = new RecordatoriosSQLHelper(context, RecordatoriosSQLHelper.DB_NAME, 
				null, RecordatoriosSQLHelper.DB_VERSION);
		db = helper.getWritableDatabase();
	}
	
	//------  METHODS  ------------
	private Recordatorio getARecordatorio(Cursor c) {
		Recordatorio rec = new Recordatorio();
		int i=0;
		rec.setId(c.getInt(i)); i++;
		rec.setNombre(c.getString(i)); i++;
		rec.setDescripcion(c.getString(i)); i++;
		rec.setCategoriaId(c.getInt(i)); i++;
		rec.setFecha(Date.valueOf(c.getString(i))); i++;
		rec.setTodoElDia(c.getInt(i)==1); i++;
		rec.setHoraInicio(c.getString(i)); i++;
		rec.setHoraFin(c.getString(i)); i++;
		rec.setTipoPeriodicidad(c.getInt(i)); i++;
		rec.setEsPeriodica(c.getInt(i)==1); i++;
		rec.setRepetir(c.getInt(i)); i++;
		rec.setDiasSemana(c.getInt(i)); i++;
		rec.setFechaFinPeriodicidad(Date.valueOf(c.getString(i))); i++;
		rec.setPrioridad(c.getInt(i)); i++;
		rec.setActiva(c.getInt(i)==1); i++;
		rec.setLatitud(c.getDouble(i)); i++;
		rec.setLongitud(c.getDouble(i)); i++;
		rec.setDireccion(c.getString(i)); i++;
		rec.setRadius(c.getDouble(i)); i++;
		rec.setIdGeofence(c.getString(i)); i++;
		return rec;
	}
	
	private ContentValues getContentValue(Recordatorio rec) {
		ContentValues cv = new ContentValues();
		cv.put("nombre", rec.getNombre());
		cv.put("descripcion", rec.getDescripcion());
		cv.put("categoria", rec.getCategoriaId());
		cv.put("fecha", rec.getFecha().toString());
		cv.put("todoElDia", (rec.isTodoElDia()) ? 1 : 0);
		cv.put("horaInicio", rec.getHoraInicio());
		cv.put("horaFin", rec.getHoraFin());
		cv.put("tipoPeriodicidad", rec.getTipoPeriodicidad());
		cv.put("esPeriodica", (rec.isEsPeriodica()) ? 1 : 0);
		cv.put("repetir", rec.getRepetir());
		cv.put("diasSemana", rec.getDiasSemana());
		cv.put("fechaFinPeriodicidad", rec.getFechaFinPeriodicidad().toString());
		cv.put("prioridad", rec.getPrioridad());
		cv.put("activa", (rec.isActiva()) ? 1 : 0);
		cv.put("latitud", rec.getLatitud());
		cv.put("longitud", rec.longitud);
		cv.put("direccion", rec.getDireccion());
		cv.put("radius", rec.getRadius());
		cv.put("idGeofence", rec.getIdGeofence());
		return cv;
	}
	
	/**
	 * List all Reminders of Database
	 * @return ArrayList object with an array of all Reminders
	 */
	public ArrayList<Recordatorio> listarRecordatorios() {
		ArrayList<Recordatorio> recordatorios = new ArrayList<Recordatorio>();
		
		Cursor c = db.query(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null, null, null, null, null, null);
		
		while (c.moveToNext()) {
			Recordatorio rec = getARecordatorio(c);
			recordatorios.add(rec);
		}
		
		c.close();
		return recordatorios;
	}
	
	/**
	 * List all active Reminders of Database
	 * @return ArrayList object with an array of all Reminders
	 */
	public ArrayList<Recordatorio> listarRecordatoriosActivos() {
		ArrayList<Recordatorio> recordatorios = new ArrayList<Recordatorio>();
		
		Cursor c = db.query(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null,
				"activa=1", null, null, null, null);
		
		
		Calendar today = Calendar.getInstance();
		Log.d("RECSDB","Calendar: " + today.toString());
		
		while (c.moveToNext()) {
			Recordatorio rec = getARecordatorio(c);
			Log.d("RECSDB","Recordatorio: " + rec.toString());
			
			switch(today.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				
				if ((rec.getDiasSemana()&1)==1) {
					if (estaEntreLasHoras(today, rec)) {						
						recordatorios.add(rec);
					}
				}					
				break;
			case Calendar.SATURDAY:
				if ((rec.getDiasSemana()&2)==2) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			case Calendar.FRIDAY:
				if ((rec.getDiasSemana()&4)==4) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			case Calendar.THURSDAY:
				if ((rec.getDiasSemana()&8)==8) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			case Calendar.WEDNESDAY:
				if ((rec.getDiasSemana()&16)==16) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			case Calendar.TUESDAY:
				if ((rec.getDiasSemana()&32)==32) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			case Calendar.MONDAY:
				if ((rec.getDiasSemana()&64)==64) {
					if (estaEntreLasHoras(today, rec))
						recordatorios.add(rec);
				}					
				break;
			}			
			
		}
		c.close();
			
		return recordatorios;
	}
	
	
	private boolean estaEntreLasHoras(Calendar fecha, Recordatorio rec) {
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		
		String[] horaInicioStr = rec.getHoraInicio().split(":");
		String[] horaFinStr = rec.getHoraFin().split(":");
		
		int[] horaInicio = new int[]{Integer.valueOf(horaInicioStr[0]), Integer.valueOf(horaInicioStr[1])};
		int[] horaFin = new int[]{Integer.valueOf(horaFinStr[0]), Integer.valueOf(horaFinStr[1])};
		
		
		if ((hora>horaInicio[0] || (hora==horaInicio[0] && minuto>=horaInicio[1])) && (hora<horaFin[0] || (hora==horaInicio[0] && minuto<=horaInicio[1]))) {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * List  Reminders by categoryId
	 * @return ArrayList object with an array of all Reminders
	 */
	public ArrayList<Recordatorio> listarRecordatoriosByCat(int catid) {
		ArrayList<Recordatorio> recordatorios = new ArrayList<Recordatorio>();
		
		Cursor c = db.query(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null,
				"categoria="+catid, null, null, null, null);
		
		while (c.moveToNext()) {
			Recordatorio rec = getARecordatorio(c);
			recordatorios.add(rec);
		}
		
		c.close();
		return recordatorios;
	}
	
	/**
	 * Obtain a Reminder with the id given
	 * @param id Id of Reminder 
	 * @return A Reminder object associated with id given or null if object wasnt founded
	 */
	public Recordatorio getRecordatorio(int id) {
		Recordatorio rec = null;
		
		Cursor c = db.query(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null,
				"id=?",
				new String[]{String.valueOf(id)},
				null, null, null);
		while (c.moveToNext()) {
			rec = getARecordatorio(c);
		}
		
		return rec;
	}
	
	/**
	 * Return a Recordatorio given by its geofence Id
	 * @param geofenceId Geofence Id for search a Recordatorio
	 * @return A Reminder object associated with Geofence Id given or null if no exists
	 */
	public Recordatorio findByGeofenceId(String geofenceId) {
		Recordatorio rec = null;
		
		Cursor c = db.query(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null, 
				"idGeofence=?",
				new String[]{geofenceId},
				null, null, null
		);
		
		while(c.moveToNext()) {
			rec = getARecordatorio(c);
		}
		
		return rec;
	}
	
	/**
	 * Insert a new Reminder into Database
	 * @param rec Reminder object to insert
	 * @return Returns a id (possitive number) if object was inserted succesfully or -1 if a error occurred
	 */
	public long insertar(Recordatorio rec) {
		ContentValues cv = getContentValue(rec);
		return db.insert(RecordatoriosSQLHelper.RECORDATORIOS_TABLE,
				null,
				cv);
	}

	
	
	/**
	 * Remove a Reminder object from database
	 * @param recordatorio Reminder object to be deleted
	 * @return Return true if Reminder could be deleted, false otherwise
	 */
	public boolean borrar(Recordatorio recordatorio) {
		int count = db.delete(RecordatoriosSQLHelper.RECORDATORIOS_TABLE, "id=?", new String[]{String.valueOf(recordatorio.getId())});
		return count==1;
	}
	
	/**
	 * Update a Reminder object depends on a id given
	 * @param id Id of Reminder object to update
	 * @param recordatorio New Reminder object that replace old Reminder in id given
	 * @return Return true if Reminder could be updated, false otherwise
	 */
	public boolean modificar(int id, Recordatorio recordatorio) {
		ContentValues cv = getContentValue(recordatorio);
		int count = db.update(RecordatoriosSQLHelper.RECORDATORIOS_TABLE, cv, "id=?", new String[]{String.valueOf(id)});
		return count==1;
	}
	
	/**
	 * Remove all Recordatorios
	 */
	public void borrarTodos() {
		db.delete(RecordatoriosSQLHelper.RECORDATORIOS_TABLE, null, null);
	}
	/**
	 * Remove by category Id
	 */
	public void borrarByCatId(int cid) {
		db.delete(RecordatoriosSQLHelper.RECORDATORIOS_TABLE, "categoria=?", new String[] {""+cid} );
	}
	
	/**
	 * Close connection with database
	 */
	public void close() {
		db.close();
		helper.close();
	}
}
