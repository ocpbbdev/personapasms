
package com.bolivariano.microservice.personapasms.consumeWS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumeroID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Nombres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Apellidos_NombreCompania" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Producto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailPredeterminado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisNacionalidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisNacimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisNacionalidadOtra1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisNacionalidadOtra2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisOtraResidencia1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaisOtraResidencia2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idListaBusqueda" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CodActEconomica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TipoEmpresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tipoID",
    "numeroID",
    "nombres",
    "apellidosNombreCompania",
    "producto",
    "usuario",
    "emailPredeterminado",
    "paisNacionalidad",
    "paisNacimiento",
    "paisNacionalidadOtra1",
    "paisNacionalidadOtra2",
    "paisOtraResidencia1",
    "paisOtraResidencia2",
    "fechaNacimiento",
    "idListaBusqueda",
    "codActEconomica",
    "tipoEmpresa"
})
@XmlRootElement(name = "VerificaIDEnteAct")
public class VerificaIDEnteAct {

    @XmlElement(name = "TipoID")
    protected int tipoID;
    @XmlElement(name = "NumeroID")
    protected String numeroID;
    @XmlElement(name = "Nombres")
    protected String nombres;
    @XmlElement(name = "Apellidos_NombreCompania")
    protected String apellidosNombreCompania;
    @XmlElement(name = "Producto")
    protected String producto;
    @XmlElement(name = "Usuario")
    protected String usuario;
    @XmlElement(name = "EmailPredeterminado")
    protected String emailPredeterminado;
    @XmlElement(name = "PaisNacionalidad")
    protected String paisNacionalidad;
    @XmlElement(name = "PaisNacimiento")
    protected String paisNacimiento;
    @XmlElement(name = "PaisNacionalidadOtra1")
    protected String paisNacionalidadOtra1;
    @XmlElement(name = "PaisNacionalidadOtra2")
    protected String paisNacionalidadOtra2;
    @XmlElement(name = "PaisOtraResidencia1")
    protected String paisOtraResidencia1;
    @XmlElement(name = "PaisOtraResidencia2")
    protected String paisOtraResidencia2;
    @XmlElement(name = "FechaNacimiento")
    protected String fechaNacimiento;
    protected int idListaBusqueda;
    @XmlElement(name = "CodActEconomica")
    protected String codActEconomica;
    @XmlElement(name = "TipoEmpresa")
    protected String tipoEmpresa;

    /**
     * Obtiene el valor de la propiedad tipoID.
     * 
     */
    public int getTipoID() {
        return tipoID;
    }

    /**
     * Define el valor de la propiedad tipoID.
     * 
     */
    public void setTipoID(int value) {
        this.tipoID = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroID() {
        return numeroID;
    }

    /**
     * Define el valor de la propiedad numeroID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
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

    /**
     * Obtiene el valor de la propiedad producto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducto() {
        return producto;
    }

    /**
     * Define el valor de la propiedad producto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducto(String value) {
        this.producto = value;
    }

    /**
     * Obtiene el valor de la propiedad usuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define el valor de la propiedad usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Obtiene el valor de la propiedad emailPredeterminado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailPredeterminado() {
        return emailPredeterminado;
    }

    /**
     * Define el valor de la propiedad emailPredeterminado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailPredeterminado(String value) {
        this.emailPredeterminado = value;
    }

    /**
     * Obtiene el valor de la propiedad paisNacionalidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisNacionalidad() {
        return paisNacionalidad;
    }

    /**
     * Define el valor de la propiedad paisNacionalidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisNacionalidad(String value) {
        this.paisNacionalidad = value;
    }

    /**
     * Obtiene el valor de la propiedad paisNacimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisNacimiento() {
        return paisNacimiento;
    }

    /**
     * Define el valor de la propiedad paisNacimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisNacimiento(String value) {
        this.paisNacimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad paisNacionalidadOtra1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisNacionalidadOtra1() {
        return paisNacionalidadOtra1;
    }

    /**
     * Define el valor de la propiedad paisNacionalidadOtra1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisNacionalidadOtra1(String value) {
        this.paisNacionalidadOtra1 = value;
    }

    /**
     * Obtiene el valor de la propiedad paisNacionalidadOtra2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisNacionalidadOtra2() {
        return paisNacionalidadOtra2;
    }

    /**
     * Define el valor de la propiedad paisNacionalidadOtra2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisNacionalidadOtra2(String value) {
        this.paisNacionalidadOtra2 = value;
    }

    /**
     * Obtiene el valor de la propiedad paisOtraResidencia1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisOtraResidencia1() {
        return paisOtraResidencia1;
    }

    /**
     * Define el valor de la propiedad paisOtraResidencia1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisOtraResidencia1(String value) {
        this.paisOtraResidencia1 = value;
    }

    /**
     * Obtiene el valor de la propiedad paisOtraResidencia2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisOtraResidencia2() {
        return paisOtraResidencia2;
    }

    /**
     * Define el valor de la propiedad paisOtraResidencia2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisOtraResidencia2(String value) {
        this.paisOtraResidencia2 = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaNacimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Define el valor de la propiedad fechaNacimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNacimiento(String value) {
        this.fechaNacimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad idListaBusqueda.
     * 
     */
    public int getIdListaBusqueda() {
        return idListaBusqueda;
    }

    /**
     * Define el valor de la propiedad idListaBusqueda.
     * 
     */
    public void setIdListaBusqueda(int value) {
        this.idListaBusqueda = value;
    }

    /**
     * Obtiene el valor de la propiedad codActEconomica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodActEconomica() {
        return codActEconomica;
    }

    /**
     * Define el valor de la propiedad codActEconomica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodActEconomica(String value) {
        this.codActEconomica = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEmpresa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    /**
     * Define el valor de la propiedad tipoEmpresa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEmpresa(String value) {
        this.tipoEmpresa = value;
    }

}
