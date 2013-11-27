package com.adm.geoadm;

public class Recordatorio {
String nombre;
String texto;
String categoria;
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getTexto() {
	return texto;
}
public void setTexto(String texto) {
	this.texto = texto;
}

public String getCategoria() {
	return categoria;
}
public void setCategoria(String categoria) {
	this.categoria = categoria;
}
public Recordatorio(String nombre, String texto, String categoria) {
	super();
	this.nombre = nombre;
	this.texto = texto;
	this.categoria = categoria;
}



}
