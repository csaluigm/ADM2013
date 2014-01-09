package com.adm.geoadm.db;

import java.sql.Date;

import com.google.android.gms.location.Geofence;

public class Recordatorio {
	public static final int DIARIA = 1;
	public static final int SEMANAL = 2;
	public static final int MENSUAL = 3;
	public static final int ANUAL = 4;
	
	private static final long EXPIRATION_TIME = Geofence.NEVER_EXPIRE;
	private static final long TRANSITION_TYPE = Geofence.GEOFENCE_TRANSITION_ENTER;
	
	private static long GEOFENCE_COUNTER = 1;
	
	
	//-------  MEMBERS ----------
	int id = -1;
	String nombre = "";
	String descripcion = "";
	int categoria_id = -1;
	Date fecha = new Date(System.currentTimeMillis());
	boolean todoElDia = false;
	String horaInicio = "00:00";
	String horaFin = "00:00";
	boolean esPeriodica = false;
	int tipoPeriodicidad = 0;
	int repetir = 0;
	int diasSemana = 0;
	Date fechaFinPeriodicidad = new Date(System.currentTimeMillis());
	int prioridad = 0;
	boolean activa = false;
	double latitud = 0;
	double longitud = 0;
	String direccion = "";
	double radius = 0;
	String idGeofence = "";
	
	
	//-------  CONSTRUCTOR ------
	public Recordatorio() {}
	
	//-------  METODOS  ---------
	/**
	 * Activate Reminder
	 */
	public void activar() {
		this.activa=true;
	}
	
	/**
	 * Deactivate Reminder
	 */
	public void desactivar() {
		this.activa=false;
	}
	
	/**
	 * Obtain Category associated
	 * @return A Category object
	 */
	public Categoria getCategoria() {
		return null;
	}
	
	/**
	 * Modify Category associated
	 * @param categoria New Category object that will be associated
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria_id = categoria.getId();
	}

	//-------  PROPERTIES  ------
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getCategoriaId() {
		return categoria_id;
	}


	public void setCategoriaId(int categoria_id) {
		this.categoria_id = categoria_id;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public boolean isTodoElDia() {
		return todoElDia;
	}


	public void setTodoElDia(boolean todoElDia) {
		this.todoElDia = todoElDia;
	}


	public String getHoraInicio() {
		return horaInicio;
	}


	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}


	public String getHoraFin() {
		return horaFin;
	}


	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}


	public boolean isEsPeriodica() {
		return esPeriodica;
	}


	public void setEsPeriodica(boolean esPeriodica) {
		this.esPeriodica = esPeriodica;
	}


	public int getTipoPeriodicidad() {
		return tipoPeriodicidad;
	}


	public void setTipoPeriodicidad(int tipoPeriodicidad) {
		this.tipoPeriodicidad = tipoPeriodicidad;
	}


	public int getRepetir() {
		return repetir;
	}


	public void setRepetir(int repetir) {
		this.repetir = repetir;
	}


	public int getDiasSemana() {
		return diasSemana;
	}


	public void setDiasSemana(int diasSemana) {
		this.diasSemana = diasSemana;
	}


	public Date getFechaFinPeriodicidad() {
		return fechaFinPeriodicidad;
	}


	public void setFechaFinPeriodicidad(Date fechaFinPeriodicidad) {
		this.fechaFinPeriodicidad = fechaFinPeriodicidad;
	}


	public int getPrioridad() {
		return prioridad;
	}


	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public double getLatitud() {
		return latitud;
	}


	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}


	public double getLongitud() {
		return longitud;
	}


	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public double getRadius() {
		return radius;
	}


	public void setRadius(double d) {
		this.radius = d;
	}


	public String getIdGeofence() {
		return idGeofence;
	}


	public void setIdGeofence(String idGeofence) {
		this.idGeofence = idGeofence;
	}
	
	
	//------------  METHODS  ------------------
	@Override
	public String toString() {
		String res = "";
		String nl = "\n";
		res += "Id: " + String.valueOf(id) + nl;
		res += "Nombre: " + nombre + nl;
		res += "Descripcion: " + descripcion + nl;
		res += "Latitud: " + String.valueOf(latitud) + nl;
		res += "Longitud: " + String.valueOf(longitud) + nl;
		res += "Radius: " + String.valueOf(radius) + nl;
		res += "Direccion: " + direccion + nl;
		res += "Activa: " + activa + nl;
		return res;
	}
	
}
