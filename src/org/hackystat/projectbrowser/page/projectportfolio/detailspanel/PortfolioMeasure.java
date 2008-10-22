package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;

/**
 * A simple fully serializable portfolio meausre configuration for uri cache.
 * @author Shaoxuan Zhang
 */
public class PortfolioMeasure implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 6691365752934953536L;
  
  /** The name of the measure. */
  private final String measureName;
  /** If this measure is colorable. */
  private boolean colorable;
  /** If this measure is enabled. */
  private boolean enabled;

  /** If higher value means better. */
  private boolean higherBetter;
  
  /** The threshold of high value. */
  private double higherThreshold;
  /** The threshold of low value. */
  private double lowerThreshold;

  /** The parameters for telemetry chart. */
  private String parameters;
  
  /**
   * Construct this instance from a MeasureConfiguration object.
   * @param measure the MeasureConfiguration object
   */
  public PortfolioMeasure(MeasureConfiguration measure) {
    this.measureName = measure.getName();
    this.setColorable(measure.isColorable());
    this.setEnabled(measure.isEnabled());
    this.setHigherBetter(measure.isHigherBetter());
    this.setHigherThreshold(measure.getHigherThreshold());
    this.setLowerThreshold(measure.getLowerThreshold());
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
    if (!(obj instanceof PortfolioMeasure)) {
      return false;
    }
    PortfolioMeasure m = (PortfolioMeasure)obj;
    return (this.getMeasureName().equals(m.getMeasureName())) 
        && (this.isEnabled() == m.isEnabled()) 
        && (this.isColorable() == m.isColorable())
        && (this.isHigherBetter() == m.isHigherBetter())
        && (this.getLowerThreshold() == m.getLowerThreshold())
        && (this.getHigherThreshold() == m.getHigherThreshold()) 
        && (this.getParameters().equals(m.getParameters()));
  }
  
  /**
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return (measureName + enabled + colorable + higherThreshold + 
            lowerThreshold + higherThreshold + parameters).hashCode();
    //return super.hashCode();
  }
  
  /**
   * @return the measureName
   */
  public String getMeasureName() {
    return measureName;
  }

  /**
   * @param colorable the colorable to set
   */
  public final void setColorable(boolean colorable) {
    this.colorable = colorable;
  }

  /**
   * @return the colorable
   */
  public boolean isColorable() {
    return colorable;
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
   * @param higherBetter the higherBetter to set
   */
  public final void setHigherBetter(boolean higherBetter) {
    this.higherBetter = higherBetter;
  }

  /**
   * @return the higherBetter
   */
  public boolean isHigherBetter() {
    return higherBetter;
  }

  /**
   * @param higherThreshold the higherThreshold to set
   */
  public final void setHigherThreshold(double higherThreshold) {
    this.higherThreshold = higherThreshold;
  }

  /**
   * @return the higherThreshold
   */
  public double getHigherThreshold() {
    return higherThreshold;
  }

  /**
   * @param lowerThreshold the lowerThreshold to set
   */
  public final void setLowerThreshold(double lowerThreshold) {
    this.lowerThreshold = lowerThreshold;
  }

  /**
   * @return the lowerThreshold
   */
  public double getLowerThreshold() {
    return lowerThreshold;
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
