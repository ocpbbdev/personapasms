package com.bolivariano.microservice.personapasms.bean;

import com.bolivariano.mensajebolivariano.MensajeSalida;

public class HomologacionRegistroCivil extends MensajeSalida{
	public String codHomGenero;
	public String codHomEstadoCivil;
	public String codHomNacionalidad;
	public String codHomProfesion;
	public String nombreProfesion;
	
	public String getNombreProfesion() {
		return nombreProfesion;
	}
	public void setNombreProfesion(String nombreProfesion) {
		this.nombreProfesion = nombreProfesion;
	}
	public String getCodHomGenero() {
		return codHomGenero;
	}
	public void setCodHomGenero(String codHomGenero) {
		this.codHomGenero = codHomGenero;
	}
	public String getCodHomEstadoCivil() {
		return codHomEstadoCivil;
	}
	public void setCodHomEstadoCivil(String codHomEstadoCivil) {
		this.codHomEstadoCivil = codHomEstadoCivil;
	}
	public String getCodHomNacionalidad() {
		return codHomNacionalidad;
	}
	public void setCodHomNacionalidad(String codHomNacionalidad) {
		this.codHomNacionalidad = codHomNacionalidad;
	}
	public String getCodHomProfesion() {
		return codHomProfesion;
	}
	public void setCodHomProfesion(String codHomProfesion) {
		this.codHomProfesion = codHomProfesion;
	}
}
