package com.bolivariano.microservice.personapasms.exception;

public class CustomException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int codigoError;
	private String mensajeUsuario;
	
	public CustomException(int codigoError, String mensajeUsuario, String mensajeSistema) {
		super(mensajeSistema);
		this.codigoError = codigoError;
		this.mensajeUsuario= mensajeUsuario;
	}

	
	/**
	 * @return the codigoError
	 */
	public int getCodigoError() {
		return codigoError;
	}


	/**
	 * @param codigoError the codigoError to set
	 */
	public void setCodigoError(int codigoError) {
		this.codigoError = codigoError;
	}


	/**
	 * @return the mensajeUsuario
	 */
	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	/**
	 * @param mensajeUsuario the mensajeUsuario to set
	 */
	public void setMensajeUsuario(String mensajeUsuario) {
		this.mensajeUsuario = mensajeUsuario;
	}	
	
	
}
