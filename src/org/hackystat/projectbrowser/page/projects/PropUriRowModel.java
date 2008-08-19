package org.hackystat.projectbrowser.page.projects;

import java.io.Serializable;
import org.apache.wicket.IClusterable;

/**
 * Provides a model for the summary of projects and their info.
 * 
 * @author Philip Johnson.
 * @author Randy Cox.
 */
public class PropUriRowModel implements Serializable, IClusterable {

  /** For serialization */
  private static final long serialVersionUID = 1L;
  /** Property Label. */
  public String propertyLabel;
  /** Property value. */
  public String propertyValue;
  /** URI Pattern 1. */
  public String uriPattern1;
  /** URI Pattern 2. */
  public String uriPattern2;
  /** URI Pattern 3. */
  public String uriPattern3;

  /**
   * Return property label.
   * 
   * @return the propertyLabel
   */
  public String getPropertyLabel() {
    return propertyLabel;
  }

  /**
   * Set property label.
   * 
   * @param propertyLabel the propertyLabel to set
   */
  public void setPropertyLabel(String propertyLabel) {
    this.propertyLabel = propertyLabel;
  }

  /**
   * Get property value.
   * 
   * @return the propertyValue
   */
  public String getPropertyValue() {
    return propertyValue;
  }

  /**
   * Set property value.
   * 
   * @param propertyValue the propertyValue to set
   */
  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

  /**
   * Get uri pattern.
   * 
   * @return the uriPattern1
   */
  public String getUriPattern1() {
    return uriPattern1;
  }

  /**
   * Set uri pattern.
   * 
   * @param uriPattern1 the uriPattern1 to set
   */
  public void setUriPattern1(String uriPattern1) {
    this.uriPattern1 = uriPattern1;
  }

  /**
   * Get uri pattern.
   * 
   * @return the uriPattern2
   */
  public String getUriPattern2() {
    return uriPattern2;
  }

  /**
   * Set uri pattern.
   * 
   * @param uriPattern2 the uriPattern2 to set
   */
  public void setUriPattern2(String uriPattern2) {
    this.uriPattern2 = uriPattern2;
  }

  /**
   * Get uri pattern.
   * 
   * @return the uriPattern3
   */
  public String getUriPattern3() {
    return uriPattern3;
  }

  /**
   * Set uri pattern.
   * 
   * @param uriPattern3 the uriPattern3 to set
   */
  public void setUriPattern3(String uriPattern3) {
    this.uriPattern3 = uriPattern3;
  }

  /**
   * Provide data as a string.
   * 
   * @return data in string format.
   */
  public String getString() {
    StringBuffer result = new StringBuffer();
    result.append("PropUriRow = ");
    result.append(this.getPropertyLabel());
    result.append(this.getPropertyValue());
    result.append(this.getUriPattern1());
    result.append(this.getUriPattern2());
    result.append(this.getUriPattern3());
    return result.toString();
  }
};
