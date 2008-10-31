package org.hackystat.projectbrowser.page.todate.detailpanel;

import java.io.Serializable;

/**
 * A simple fully serializable to-date meausre configuration for uri cache.
 * @author Shaoxuan Zhang
 */
public class ToDateMeasure implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 6691365752934953536L;
  
  /** The name of the measure. */
  private final String measureName;
  /** If this measure is enabled. */
  private boolean enabled;

  /** The parameters for telemetry chart. */
  private String parameters;
  
  /**
   * Construct this instance from a MeasureConfiguration object.
   * @param measure the MeasureConfiguration object
   */
  public ToDateMeasure(ToDateMeasureConfiguration measure) {
    this.measureName = measure.getName();
    this.setEnabled(measure.isEnabled());
    this.setParameters(measure.getParamtersString());
  }

  /**
   * Compare the with the given PortfolioMeasure.
   * @param obj the given object, should be PortfolioMeasure.
   * @return true if the given object is a PortfolioMeasure instance and
   *  all fills are equals to this instance, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ToDateMeasure)) {
      return false;
    }
    ToDateMeasure m = (ToDateMeasure)obj;
    return (this.getMeasureName().equals(m.getMeasureName())) 
        && (this.isEnabled() == m.isEnabled()) 
        && (this.getParameters().equals(m.getParameters()));
  }
  
  /**
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return (measureName + enabled + parameters).hashCode();
    //return super.hashCode();
  }
  
  /**
   * @return the measureName
   */
  public String getMeasureName() {
    return measureName;
  }

  /**
   * @param enabled the enabled to set
   */
  public final void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @return the enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param parameters the parameters to set
   */
  public final void setParameters(String parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the parameters
   */
  public String getParameters() {
    return parameters;
  }
}
