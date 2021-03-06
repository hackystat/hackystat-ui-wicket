//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.30 at 11:38:51 AM HST 
//


package org.hackystat.projectbrowser.page.projectportfolio.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="Measure" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="StreamTrendParameters" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="higherBetter" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="higherThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="lowerThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ParticipationParameters" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="frequencyPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="memberPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="thresoldValue" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DeviationParameters" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="expectationValue" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="moderateDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="unacceptableDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="alias" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="classifierMethod" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *                 &lt;attribute name="merge" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="telemetryParameters" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "measure"
})
@XmlRootElement(name = "Measures")
public class Measures
    implements Serializable
{

    private final static long serialVersionUID = 6799287509742157998L;
    @XmlElement(name = "Measure", required = true)
    protected List<Measures.Measure> measure;

    /**
     * Gets the value of the measure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the measure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Measures.Measure }
     * 
     * 
     */
    public List<Measures.Measure> getMeasure() {
        if (measure == null) {
            measure = new ArrayList<Measures.Measure>();
        }
        return this.measure;
    }

    public boolean isSetMeasure() {
        return ((this.measure!= null)&&(!this.measure.isEmpty()));
    }

    public void unsetMeasure() {
        this.measure = null;
    }


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
     *         &lt;element name="StreamTrendParameters" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="higherBetter" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="higherThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="lowerThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ParticipationParameters" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="frequencyPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="memberPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="thresoldValue" type="{http://www.w3.org/2001/XMLSchema}double" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DeviationParameters" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="expectationValue" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="moderateDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="unacceptableDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="alias" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="classifierMethod" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *       &lt;attribute name="merge" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="telemetryParameters" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "streamTrendParameters",
        "participationParameters",
        "deviationParameters"
    })
    public static class Measure
        implements Serializable
    {

        private final static long serialVersionUID = 6799287509742157998L;
        @XmlElement(name = "StreamTrendParameters")
        protected Measures.Measure.StreamTrendParameters streamTrendParameters;
        @XmlElement(name = "ParticipationParameters")
        protected Measures.Measure.ParticipationParameters participationParameters;
        @XmlElement(name = "DeviationParameters")
        protected Measures.Measure.DeviationParameters deviationParameters;
        @XmlAttribute
        protected String alias;
        @XmlAttribute
        protected String classifierMethod;
        @XmlAttribute
        protected Boolean enabled;
        @XmlAttribute
        protected String merge;
        @XmlAttribute(required = true)
        protected String name;
        @XmlAttribute
        protected String telemetryParameters;

        /**
         * Gets the value of the streamTrendParameters property.
         * 
         * @return
         *     possible object is
         *     {@link Measures.Measure.StreamTrendParameters }
         *     
         */
        public Measures.Measure.StreamTrendParameters getStreamTrendParameters() {
            return streamTrendParameters;
        }

        /**
         * Sets the value of the streamTrendParameters property.
         * 
         * @param value
         *     allowed object is
         *     {@link Measures.Measure.StreamTrendParameters }
         *     
         */
        public void setStreamTrendParameters(Measures.Measure.StreamTrendParameters value) {
            this.streamTrendParameters = value;
        }

        public boolean isSetStreamTrendParameters() {
            return (this.streamTrendParameters!= null);
        }

        /**
         * Gets the value of the participationParameters property.
         * 
         * @return
         *     possible object is
         *     {@link Measures.Measure.ParticipationParameters }
         *     
         */
        public Measures.Measure.ParticipationParameters getParticipationParameters() {
            return participationParameters;
        }

        /**
         * Sets the value of the participationParameters property.
         * 
         * @param value
         *     allowed object is
         *     {@link Measures.Measure.ParticipationParameters }
         *     
         */
        public void setParticipationParameters(Measures.Measure.ParticipationParameters value) {
            this.participationParameters = value;
        }

        public boolean isSetParticipationParameters() {
            return (this.participationParameters!= null);
        }

        /**
         * Gets the value of the deviationParameters property.
         * 
         * @return
         *     possible object is
         *     {@link Measures.Measure.DeviationParameters }
         *     
         */
        public Measures.Measure.DeviationParameters getDeviationParameters() {
            return deviationParameters;
        }

        /**
         * Sets the value of the deviationParameters property.
         * 
         * @param value
         *     allowed object is
         *     {@link Measures.Measure.DeviationParameters }
         *     
         */
        public void setDeviationParameters(Measures.Measure.DeviationParameters value) {
            this.deviationParameters = value;
        }

        public boolean isSetDeviationParameters() {
            return (this.deviationParameters!= null);
        }

        /**
         * Gets the value of the alias property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAlias() {
            return alias;
        }

        /**
         * Sets the value of the alias property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAlias(String value) {
            this.alias = value;
        }

        public boolean isSetAlias() {
            return (this.alias!= null);
        }

        /**
         * Gets the value of the classifierMethod property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClassifierMethod() {
            return classifierMethod;
        }

        /**
         * Sets the value of the classifierMethod property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClassifierMethod(String value) {
            this.classifierMethod = value;
        }

        public boolean isSetClassifierMethod() {
            return (this.classifierMethod!= null);
        }

        /**
         * Gets the value of the enabled property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isEnabled() {
            if (enabled == null) {
                return true;
            } else {
                return enabled;
            }
        }

        /**
         * Sets the value of the enabled property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setEnabled(boolean value) {
            this.enabled = value;
        }

        public boolean isSetEnabled() {
            return (this.enabled!= null);
        }

        public void unsetEnabled() {
            this.enabled = null;
        }

        /**
         * Gets the value of the merge property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMerge() {
            return merge;
        }

        /**
         * Sets the value of the merge property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMerge(String value) {
            this.merge = value;
        }

        public boolean isSetMerge() {
            return (this.merge!= null);
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        public boolean isSetName() {
            return (this.name!= null);
        }

        /**
         * Gets the value of the telemetryParameters property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTelemetryParameters() {
            return telemetryParameters;
        }

        /**
         * Sets the value of the telemetryParameters property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTelemetryParameters(String value) {
            this.telemetryParameters = value;
        }

        public boolean isSetTelemetryParameters() {
            return (this.telemetryParameters!= null);
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="expectationValue" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="moderateDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute name="unacceptableDeviation" type="{http://www.w3.org/2001/XMLSchema}double" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class DeviationParameters
            implements Serializable
        {

            private final static long serialVersionUID = 6799287509742157998L;
            @XmlAttribute
            protected Double expectationValue;
            @XmlAttribute
            protected Double moderateDeviation;
            @XmlAttribute
            protected Boolean scaleWithGranularity;
            @XmlAttribute
            protected Double unacceptableDeviation;

            /**
             * Gets the value of the expectationValue property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getExpectationValue() {
                return expectationValue;
            }

            /**
             * Sets the value of the expectationValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setExpectationValue(double value) {
                this.expectationValue = value;
            }

            public boolean isSetExpectationValue() {
                return (this.expectationValue!= null);
            }

            public void unsetExpectationValue() {
                this.expectationValue = null;
            }

            /**
             * Gets the value of the moderateDeviation property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getModerateDeviation() {
                return moderateDeviation;
            }

            /**
             * Sets the value of the moderateDeviation property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setModerateDeviation(double value) {
                this.moderateDeviation = value;
            }

            public boolean isSetModerateDeviation() {
                return (this.moderateDeviation!= null);
            }

            public void unsetModerateDeviation() {
                this.moderateDeviation = null;
            }

            /**
             * Gets the value of the scaleWithGranularity property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public boolean isScaleWithGranularity() {
                return scaleWithGranularity;
            }

            /**
             * Sets the value of the scaleWithGranularity property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setScaleWithGranularity(boolean value) {
                this.scaleWithGranularity = value;
            }

            public boolean isSetScaleWithGranularity() {
                return (this.scaleWithGranularity!= null);
            }

            public void unsetScaleWithGranularity() {
                this.scaleWithGranularity = null;
            }

            /**
             * Gets the value of the unacceptableDeviation property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getUnacceptableDeviation() {
                return unacceptableDeviation;
            }

            /**
             * Sets the value of the unacceptableDeviation property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setUnacceptableDeviation(double value) {
                this.unacceptableDeviation = value;
            }

            public boolean isSetUnacceptableDeviation() {
                return (this.unacceptableDeviation!= null);
            }

            public void unsetUnacceptableDeviation() {
                this.unacceptableDeviation = null;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="frequencyPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="memberPercentage" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute name="thresoldValue" type="{http://www.w3.org/2001/XMLSchema}double" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class ParticipationParameters
            implements Serializable
        {

            private final static long serialVersionUID = 6799287509742157998L;
            @XmlAttribute
            protected Double frequencyPercentage;
            @XmlAttribute
            protected Double memberPercentage;
            @XmlAttribute
            protected Boolean scaleWithGranularity;
            @XmlAttribute
            protected Double thresoldValue;

            /**
             * Gets the value of the frequencyPercentage property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getFrequencyPercentage() {
                return frequencyPercentage;
            }

            /**
             * Sets the value of the frequencyPercentage property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setFrequencyPercentage(double value) {
                this.frequencyPercentage = value;
            }

            public boolean isSetFrequencyPercentage() {
                return (this.frequencyPercentage!= null);
            }

            public void unsetFrequencyPercentage() {
                this.frequencyPercentage = null;
            }

            /**
             * Gets the value of the memberPercentage property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getMemberPercentage() {
                return memberPercentage;
            }

            /**
             * Sets the value of the memberPercentage property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMemberPercentage(double value) {
                this.memberPercentage = value;
            }

            public boolean isSetMemberPercentage() {
                return (this.memberPercentage!= null);
            }

            public void unsetMemberPercentage() {
                this.memberPercentage = null;
            }

            /**
             * Gets the value of the scaleWithGranularity property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public boolean isScaleWithGranularity() {
                return scaleWithGranularity;
            }

            /**
             * Sets the value of the scaleWithGranularity property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setScaleWithGranularity(boolean value) {
                this.scaleWithGranularity = value;
            }

            public boolean isSetScaleWithGranularity() {
                return (this.scaleWithGranularity!= null);
            }

            public void unsetScaleWithGranularity() {
                this.scaleWithGranularity = null;
            }

            /**
             * Gets the value of the thresoldValue property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getThresoldValue() {
                return thresoldValue;
            }

            /**
             * Sets the value of the thresoldValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setThresoldValue(double value) {
                this.thresoldValue = value;
            }

            public boolean isSetThresoldValue() {
                return (this.thresoldValue!= null);
            }

            public void unsetThresoldValue() {
                this.thresoldValue = null;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="higherBetter" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute name="higherThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="lowerThresold" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="scaleWithGranularity" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class StreamTrendParameters
            implements Serializable
        {

            private final static long serialVersionUID = 6799287509742157998L;
            @XmlAttribute
            protected Boolean higherBetter;
            @XmlAttribute
            protected Double higherThresold;
            @XmlAttribute
            protected Double lowerThresold;
            @XmlAttribute
            protected Boolean scaleWithGranularity;

            /**
             * Gets the value of the higherBetter property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public boolean isHigherBetter() {
                return higherBetter;
            }

            /**
             * Sets the value of the higherBetter property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setHigherBetter(boolean value) {
                this.higherBetter = value;
            }

            public boolean isSetHigherBetter() {
                return (this.higherBetter!= null);
            }

            public void unsetHigherBetter() {
                this.higherBetter = null;
            }

            /**
             * Gets the value of the higherThresold property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getHigherThresold() {
                return higherThresold;
            }

            /**
             * Sets the value of the higherThresold property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setHigherThresold(double value) {
                this.higherThresold = value;
            }

            public boolean isSetHigherThresold() {
                return (this.higherThresold!= null);
            }

            public void unsetHigherThresold() {
                this.higherThresold = null;
            }

            /**
             * Gets the value of the lowerThresold property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public double getLowerThresold() {
                return lowerThresold;
            }

            /**
             * Sets the value of the lowerThresold property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setLowerThresold(double value) {
                this.lowerThresold = value;
            }

            public boolean isSetLowerThresold() {
                return (this.lowerThresold!= null);
            }

            public void unsetLowerThresold() {
                this.lowerThresold = null;
            }

            /**
             * Gets the value of the scaleWithGranularity property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public boolean isScaleWithGranularity() {
                return scaleWithGranularity;
            }

            /**
             * Sets the value of the scaleWithGranularity property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setScaleWithGranularity(boolean value) {
                this.scaleWithGranularity = value;
            }

            public boolean isSetScaleWithGranularity() {
                return (this.scaleWithGranularity!= null);
            }

            public void unsetScaleWithGranularity() {
                this.scaleWithGranularity = null;
            }

        }

    }

}
