package com.bolivariano.microservice.personapasms.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RESULTADOSType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RESULTADOSType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RESULTADO" type="{}RESULTADOType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="CUENTA" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="DETIENETRAN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESULTADOSType", propOrder = {
    "resultado"
})
public class RESULTADOSType {

    @XmlElement(name = "RESULTADO", required = true)
    protected List<RESULTADOType> resultado;
    @XmlAttribute(name = "CUENTA")
    protected Integer cuenta;
    @XmlAttribute(name = "DETIENETRAN")
    protected Boolean detienetran;

    /**
     * Gets the value of the resultado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRESULTADO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RESULTADOType }
     * 
     * 
     */
    public List<RESULTADOType> getRESULTADO() {
        if (resultado == null) {
            resultado = new ArrayList<RESULTADOType>();
        }
        return this.resultado;
    }

    /**
     * Obtiene el valor de la propiedad cuenta.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCUENTA() {
        return cuenta;
    }

    /**
     * Define el valor de la propiedad cuenta.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCUENTA(Integer value) {
        this.cuenta = value;
    }

    /**
     * Obtiene el valor de la propiedad detienetran.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDETIENETRAN() {
        return detienetran;
    }

    /**
     * Define el valor de la propiedad detienetran.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDETIENETRAN(Boolean value) {
        this.detienetran = value;
    }

}
