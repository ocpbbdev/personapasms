package com.bolivariano.microservice.personapasms.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gizlocorp.filetransfer.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OFAC_QNAME = new QName("", "OFAC");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gizlocorp.filetransfer.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OFACType }
     * 
     */
    public OFACType createOFACType() {
        return new OFACType();
    }

    /**
     * Create an instance of {@link CONTRATOType }
     * 
     */
    public CONTRATOType createCONTRATOType() {
        return new CONTRATOType();
    }

    /**
     * Create an instance of {@link RESULTADOType }
     * 
     */
    public RESULTADOType createRESULTADOType() {
        return new RESULTADOType();
    }

    /**
     * Create an instance of {@link RESULTADOSType }
     * 
     */
    public RESULTADOSType createRESULTADOSType() {
        return new RESULTADOSType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OFACType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "OFAC")
    public JAXBElement<OFACType> createOFAC(OFACType value) {
        return new JAXBElement<OFACType>(_OFAC_QNAME, OFACType.class, null, value);
    }

}
