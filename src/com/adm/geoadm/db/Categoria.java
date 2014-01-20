package com.adm.geoadm.db;

public class Categoria {
	//-----  MEMBERS -----------
	int id;
	String nombre;
	int color;
	
	//-----  CONSTRUCTOR -------
	/**
	 * Default constructor
	 */
	public Categoria() {
		this.id = 0;
		this.nombre = "";
		this.color=-16737281;//Azul holo
		
	}
	
	/**
	 * Constructor with parameters
	 * @param id Id of Database
	 * @param nombre Name of Category
	 */
	public Categoria (int id, String nombre,int color) {
		this.id = id;
		this.nombre = nombre;
		this.color=color;
	}
	
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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
