package com.adm.geoadm.db;

public class Categoria {
	//-----  MEMBERS -----------
	int id;
	String nombre;
	
	//-----  CONSTRUCTOR -------
	/**
	 * Default constructor
	 */
	public Categoria() {
		this.id = 0;
		this.nombre = "";
	}
	
	/**
	 * Constructor with parameters
	 * @param id Id of Database
	 * @param nombre Name of Category
	 */
	public Categoria (int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	
	//-----  PROPERTIES  -------
	/**
	 * Get Id
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * Set Id
	 * @param id Id of Database
 	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Get Name of Category
	 * @return String with Name of Category
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Set Name of Category
	 * @param nombre String with new name of category
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
