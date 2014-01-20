package com.adm.geoadm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordatoriosSQLHelper extends SQLiteOpenHelper {
	public final static String RECORDATORIOS_TABLE = "Recordatorio";
	public final static String CATEGORIAS_TABLE = "Categoria";
	public final static String DB_NAME = "Recordatorios";
	public final static int DB_VERSION = 1;
	
	String createRecordatorios = "CREATE TABLE Recordatorio (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"nombre TEXT, " +
			"descripcion TEXT, " +
			"categoria INTEGER, " +
			"fecha TEXT, " +
			"todoElDia INTEGER, " +
			"horaInicio TEXT, " +
			"horaFin TEXT, " +
			"tipoPeriodicidad INTEGER, " +
			"esPeriodica INTEGER, " +
			"repetir INTEGER, " +
			"diasSemana INTEGER, " +
			"fechaFinPeriodicidad TEXT, " +
			"prioridad INTEGER, " +
			"activa INTEGER, " + 
			"latitud REAL, " +
			"longitud REAL, " +
			"direccion TEXT, " +
			"radius REAL, " +
			"idGeofence TEXT " +
	")";
	
	String createCategorias = "CREATE TABLE Categoria (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"nombre TEXT" +
	")";
	
	public RecordatoriosSQLHelper(Context contexto, String nombre, CursorFactory factory, int version) {
		super(contexto,nombre,factory,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createCategorias);
		db.execSQL(createRecordatorios);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + RECORDATORIOS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORIAS_TABLE);		
	}
}
