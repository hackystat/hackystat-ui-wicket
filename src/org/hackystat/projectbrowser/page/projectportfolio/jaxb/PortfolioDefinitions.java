//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.30 at 11:38:51 AM HST 
//


package org.hackystat.projectbrowser.page.projectportfolio.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Measures"/>
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
    "measures"
})
@XmlRootElement(name = "PortfolioDefinitions")
public class PortfolioDefinitions
    implements Serializable
{

    private final static long serialVersionUID = 6799287509742157998L;
    @XmlElement(name = "Measures", required = true)
    protected Measures measures;

    /**
     * Gets the value of the measures property.
     * 
     * @return
     *     possible object is
     *     {@link Measures }
     *     
     */
    public Measures getMeasures() {
        return measures;
    }

    /**
     * Sets the value of the measures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Measures }
     *     
     */
    public void setMeasures(Measures value) {
        this.measures = value;
    }

    public boolean isSetMeasures() {
        return (this.measures!= null);
    }

}
