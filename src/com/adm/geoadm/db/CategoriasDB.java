package com.adm.geoadm.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoriasDB {
	//-----  MEMBERS  -------------
	RecordatoriosSQLHelper helper;
	SQLiteDatabase db;
	
	//-----  CONSTRUCTORS  --------
	public CategoriasDB(Context context) {
		helper = new RecordatoriosSQLHelper(context, RecordatoriosSQLHelper.DB_NAME,
										null, RecordatoriosSQLHelper.DB_VERSION);
		db = helper.getWritableDatabase();
		
	}
	
	//-----  METHODS  -------------
	private Categoria getACategory(Cursor c) {
		Categoria cat = new Categoria();
		cat.setId(c.getInt(0));
		cat.setNombre(c.getString(1));
		cat.setColor(c.getInt(2));
		return cat;
	}
	
	/**
	 * List all Categories of Database
	 * @return ArrayList object with an array of all Categories
	 */
	public ArrayList<Categoria> listarCategorias() {
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		
		Cursor c = db.query(RecordatoriosSQLHelper.CATEGORIAS_TABLE,
				null, null, null, null, null, null);
		
		while (c.moveToNext()) {
			Categoria cat = getACategory(c);
			categorias.add(cat);
		}
		
		c.close();
		return categorias;
	}
	
	/**
	 * Obtain a Category with the id given
	 * @param id Id of Category 
	 * @return A Category object associated with id given or null if object wasnt founded
	 */
	public Categoria getCategoria(int id) {
		Categoria cat = null;
		
		Cursor c = db.query(RecordatoriosSQLHelper.CATEGORIAS_TABLE,
				null,
				"id=?",
				new String[]{String.valueOf(id)},
				null, null, null);
		while (c.moveToNext()) {
			cat = getACategory(c);
		}
		
		return cat;
	}
	
	////////////////////////////////////
	
	/**
	 * Obtain a Category with the id given
	 * @param id Id of Category 
	 * @return A Category object associated with id given or null if object wasnt founded
	 */
	public Categoria getCategoriaPorString(String nombre) {
		Categoria cat = null;
		String[] args = new String[] {""+nombre};
		Cursor c = db.query(RecordatoriosSQLHelper.CATEGORIAS_TABLE,null, "nombre=?", args, null, null, null);
		
//		Cursor c = db.query(RecordatoriosSQLHelper.CATEGORIAS_TABLE,null,"nombre=?",new String[]{nombre},	null, null, null);
		
		
		while (c.moveToNext()) {
			cat = getACategory(c);
		}
		
		return cat;
	}
	
	
	///////////////////////////////
	
	
	/**
	 * Insert a new Categoria into Database
	 * @param categoria Category object to insert
	 * @return Returns a id (possitive number) if object was inserted succesfully or -1 if a error occurred
	 */
	public long insertar(Categoria categoria) {
		ContentValues cv = new ContentValues();
		cv.put("nombre", categoria.getNombre());
		cv.put("color", categoria.getColor());
		
		return db.insert(RecordatoriosSQLHelper.CATEGORIAS_TABLE,
				null,
				cv);
	}
	
	/**
	 * Remove a Category object from database
	 * @param categoria Category object to be deleted
	 * @return Return true if deletion of Category could be done, false otherwise
	 */
	public boolean borrar(Categoria categoria) {
		int count = db.delete(RecordatoriosSQLHelper.CATEGORIAS_TABLE, "id=?", new String[]{String.valueOf(categoria.getId())});
		return count==1;
	}
	
	/**
	 * Update a Category object depends on a id given
	 * @param id Id of Category object to update
	 * @param categoria New Category object that replace old Category in id given
	 * @return Return true if updated could be done, false otherwise
	 */
	public boolean modificar(int id, Categoria categoria) {
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("nombre", categoria.getNombre());
		cv.put("color", categoria.getColor());
		int count = db.update(RecordatoriosSQLHelper.CATEGORIAS_TABLE, cv, "id=?", new String[]{String.valueOf(id)});
		return count==1;
	}
	
	/**
	 * Remove all Categories
	 */
	public void borrarTodos() {
		db.delete(RecordatoriosSQLHelper.CATEGORIAS_TABLE, null, null);
	}
	
	/**
	 * Close connection with database
	 */
	public void close() {
		db.close();
		helper.close();
	}
	
}
