package com.bolivariano.microservice.personapasms.bean;

import java.util.Date;

public class CuentaBean {

	private String cedula;
	private String nombre;
	private String nombres;
	private String apellidos;
	private String condicionCedulado;
	private String fechaNacimiento;
	private String nacionalidad;
	private String estadoCivil;
	private String conyuge;
	private String instruccion;
	private String profesion;
	private String fechaFallecimiento;
	private String fechaCedulacion;
	private String domicilio;
	private String calle;
	private String numeroCasa;
	private String sexo;
	private String nombrePadre;
	private String nombreMadre;
	private String lugarNacimiento;
	private String fechaMatrimonio;
	private String homMISNacionalidad;
	private String homMISSexo;
	private String homMISEstCivil;
	private String homMISProfesion;
	private String createdAt ;
	private String identificacion;
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getCondicionCedulado() {
		return condicionCedulado;
	}
	public void setCondicionCedulado(String condicionCedulado) {
		this.condicionCedulado = condicionCedulado;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getConyuge() {
		return conyuge;
	}
	public void setConyuge(String conyuge) {
		this.conyuge = conyuge;
	}
	public String getInstruccion() {
		return instruccion;
	}
	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}
	public String getProfesion() {
		return profesion;
	}
	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}
	public String getFechaFallecimiento() {
		return fechaFallecimiento;
	}
	public void setFechaFallecimiento(String fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
	}
	public String getFechaCedulacion() {
		return fechaCedulacion;
	}
	public void setFechaCedulacion(String fechaCedulacion) {
		this.fechaCedulacion = fechaCedulacion;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumeroCasa() {
		return numeroCasa;
	}
	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getNombrePadre() {
		return nombrePadre;
	}
	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}
	public String getNombreMadre() {
		return nombreMadre;
	}
	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}
	public String getLugarNacimiento() {
		return lugarNacimiento;
	}
	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}
	public String getFechaMatrimonio() {
		return fechaMatrimonio;
	}
	public void setFechaMatrimonio(String fechaMatrimonio) {
		this.fechaMatrimonio = fechaMatrimonio;
	}
	public String getHomMISNacionalidad() {
		return homMISNacionalidad;
	}
	public void setHomMISNacionalidad(String homMISNacionalidad) {
		this.homMISNacionalidad = homMISNacionalidad;
	}
	public String getHomMISSexo() {
		return homMISSexo;
	}
	public void setHomMISSexo(String homMISSexo) {
		this.homMISSexo = homMISSexo;
	}
	public String getHomMISEstCivil() {
		return homMISEstCivil;
	}
	public void setHomMISEstCivil(String homMISEstCivil) {
		this.homMISEstCivil = homMISEstCivil;
	}
	public String getHomMISProfesion() {
		return homMISProfesion;
	}
	public void setHomMISProfesion(String homMISProfesion) {
		this.homMISProfesion = homMISProfesion;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		 Date date = new Date();
		 String minutos = date.getMinutes()+"";
		 String segundos = date.getSeconds()+"";
		 String horas = date.getHours()+"";
		 System.out.println("longitud minutos "+minutos.length());
		 if(horas.length()<2) {
			 this.createdAt = "0"+date.getHours();
		 }else {
			 this.createdAt = date.getHours()+"";
		 }
		 if (minutos.length()<2) {
		this.createdAt =  this.createdAt+"0"+date.getMinutes();
		 }else {
			 
			 this.createdAt =  this.createdAt+""+date.getMinutes();	 
			 
		 }
		 System.out.println("segundos.length()"+segundos.length());
		 if (segundos.length()<2) {
				this.createdAt =this.createdAt+"0"+date.getSeconds();
				 }else {
					 this.createdAt = this.createdAt +""+date.getSeconds();	 
				 }
		 
	}

}
