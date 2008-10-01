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
   * Constructor.
   * @param measureName the measure name to set.
   */
  public PortfolioMeasure(String measureName) {
    this.measureName = measureName;
  }
  
  /**
   * Construct this instance from a MeasureConfiguration object.
   * @param measure the MeasureConfiguration object
   */
  public PortfolioMeasure(MeasureConfiguration measure) {
    this.measureName = measure.getName();
    this.colorable = measure.isColorable();
    this.enabled = measure.isEnabled();
    this.higherBetter = measure.isHigherBetter();
    this.higherThreshold = measure.getHigherThreshold();
    this.lowerThreshold = measure.getLowerThreshold();
    this.parameters = measure.getParamtersString();
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
  public void setColorable(boolean colorable) {
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
  public void setEnabled(boolean enabled) {
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
  public void setHigherBetter(boolean higherBetter) {
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
  public void setHigherThreshold(double higherThreshold) {
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
  public void setLowerThreshold(double lowerThreshold) {
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
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the parameters
   */
  public String getParameters() {
    return parameters;
  }
}
