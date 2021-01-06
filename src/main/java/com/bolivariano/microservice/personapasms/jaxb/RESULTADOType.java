package com.bolivariano.microservice.personapasms.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RESULTADOType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RESULTADOType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="INDICE" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TipoId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumeroID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nombres" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Apellidos_NombreCompania" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESULTADOType", propOrder = {
    "indice",
    "tipoId",
    "numeroID",
    "nombres",
    "apellidosNombreCompania"
})
public class RESULTADOType {

    @XmlElement(name = "INDICE")
    protected int indice;
    @XmlElement(name = "TipoId")
    protected int tipoId;
    @XmlElement(name = "NumeroID")
    protected String numeroID;
    @XmlElement(name = "Nombres", required = true)
    protected String nombres;
    @XmlElement(name = "Apellidos_NombreCompania", required = true)
    protected String apellidosNombreCompania;

    /**
     * Obtiene el valor de la propiedad indice.
     * 
     */
    public int getINDICE() {
        return indice;
    }

    /**
     * Define el valor de la propiedad indice.
     * 
     */
    public void setINDICE(int value) {
        this.indice = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoId.
     * 
     */
    public int getTipoId() {
        return tipoId;
    }

    /**
     * Define el valor de la propiedad tipoId.
     * 
     */
    public void setTipoId(int value) {
        this.tipoId = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroID.
     * 
     */
    public String getNumeroID() {
        return numeroID;
    }

    /**
     * Define el valor de la propiedad numeroID.
     * 
     */
    public void setNumeroID(String value) {
        this.numeroID = value;
    }

    /**
     * Obtiene el valor de la propiedad nombres.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Define el valor de la propiedad nombres.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombres(String value) {
        this.nombres = value;
    }

    /**
     * Obtiene el valor de la propiedad apellidosNombreCompania.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidosNombreCompania() {
        return apellidosNombreCompania;
    }

    /**
     * Define el valor de la propiedad apellidosNombreCompania.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidosNombreCompania(String value) {
        this.apellidosNombreCompania = value;
    }

}
