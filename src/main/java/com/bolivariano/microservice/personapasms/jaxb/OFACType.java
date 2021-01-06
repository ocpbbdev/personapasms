package com.bolivariano.microservice.personapasms.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para OFACType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="OFACType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CONTRATO" type="{}CONTRATOType"/>
 *         &lt;element name="RESULTADOS" type="{}RESULTADOSType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OFACType", propOrder = {
    "contrato",
    "resultados"
})
@XmlRootElement(name="OFAC")
public class OFACType {

    @XmlElement(name = "CONTRATO", required = true)
    protected CONTRATOType contrato;
    @XmlElement(name = "RESULTADOS", required = true)
    protected RESULTADOSType resultados;

    /**
     * Obtiene el valor de la propiedad contrato.
     * 
     * @return
     *     possible object is
     *     {@link CONTRATOType }
     *     
     */
    public CONTRATOType getCONTRATO() {
        return contrato;
    }

    /**
     * Define el valor de la propiedad contrato.
     * 
     * @param value
     *     allowed object is
     *     {@link CONTRATOType }
     *     
     */
    public void setCONTRATO(CONTRATOType value) {
        this.contrato = value;
    }

    /**
     * Obtiene el valor de la propiedad resultados.
     * 
     * @return
     *     possible object is
     *     {@link RESULTADOSType }
     *     
     */
    public RESULTADOSType getRESULTADOS() {
        return resultados;
    }

    /**
     * Define el valor de la propiedad resultados.
     * 
     * @param value
     *     allowed object is
     *     {@link RESULTADOSType }
     *     
     */
    public void setRESULTADOS(RESULTADOSType value) {
        this.resultados = value;
    }

}
