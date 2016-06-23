//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.23 at 04:58:37 PM EDT 
//


package com.gtnexus.appxpress.platform.module.model.design;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}fieldName"/>
 *         &lt;element ref="{}fieldNumber"/>
 *         &lt;element ref="{}description" minOccurs="0"/>
 *         &lt;element ref="{}fieldPosition"/>
 *         &lt;element ref="{}dataType"/>
 *         &lt;element ref="{}detailedDataType"/>
 *         &lt;element ref="{}indexed"/>
 *         &lt;element ref="{}summaryField"/>
 *         &lt;element ref="{}maxLength"/>
 *         &lt;element ref="{}renderedOnUi"/>
 *         &lt;element ref="{}extendedData" maxOccurs="unbounded"/>
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
    "fieldName",
    "fieldNumber",
    "description",
    "fieldPosition",
    "dataType",
    "detailedDataType",
    "indexed",
    "summaryField",
    "maxLength",
    "renderedOnUi",
    "extendedData"
})
@XmlRootElement(name = "scalarField")
public class ScalarField {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String fieldName;
    @XmlElement(required = true)
    protected BigInteger fieldNumber;
    protected String description;
    @XmlElement(required = true)
    protected BigInteger fieldPosition;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String dataType;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String detailedDataType;
    protected boolean indexed;
    protected boolean summaryField;
    @XmlElement(required = true)
    protected BigInteger maxLength;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String renderedOnUi;
    @XmlElement(required = true)
    protected List<ExtendedData> extendedData;

    /**
     * Gets the value of the fieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the fieldNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFieldNumber() {
        return fieldNumber;
    }

    /**
     * Sets the value of the fieldNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFieldNumber(BigInteger value) {
        this.fieldNumber = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the fieldPosition property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFieldPosition() {
        return fieldPosition;
    }

    /**
     * Sets the value of the fieldPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFieldPosition(BigInteger value) {
        this.fieldPosition = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the detailedDataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailedDataType() {
        return detailedDataType;
    }

    /**
     * Sets the value of the detailedDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailedDataType(String value) {
        this.detailedDataType = value;
    }

    /**
     * Gets the value of the indexed property.
     * 
     */
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Sets the value of the indexed property.
     * 
     */
    public void setIndexed(boolean value) {
        this.indexed = value;
    }

    /**
     * Gets the value of the summaryField property.
     * 
     */
    public boolean isSummaryField() {
        return summaryField;
    }

    /**
     * Sets the value of the summaryField property.
     * 
     */
    public void setSummaryField(boolean value) {
        this.summaryField = value;
    }

    /**
     * Gets the value of the maxLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the value of the maxLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxLength(BigInteger value) {
        this.maxLength = value;
    }

    /**
     * Gets the value of the renderedOnUi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRenderedOnUi() {
        return renderedOnUi;
    }

    /**
     * Sets the value of the renderedOnUi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRenderedOnUi(String value) {
        this.renderedOnUi = value;
    }

    /**
     * Gets the value of the extendedData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extendedData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtendedData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtendedData }
     * 
     * 
     */
    public List<ExtendedData> getExtendedData() {
        if (extendedData == null) {
            extendedData = new ArrayList<ExtendedData>();
        }
        return this.extendedData;
    }

}
