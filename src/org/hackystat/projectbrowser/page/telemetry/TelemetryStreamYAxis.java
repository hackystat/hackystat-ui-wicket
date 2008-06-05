package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;

/**
 * Class that represent an Y axis of some Telemetry Streams.
 * @author Shaoxuan Zhang
 *
 */
public class TelemetryStreamYAxis implements Serializable {
  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** the unit name of this axis. */
  private String unitName;
  /** the maximum of this axis. */
  private double maximum;
  /** the minimum of this axis. */
  private double minimum;
  /** the color of this axis. */
  private String color;
  
  /**
   * @param unitName the unit of this axis.
   */
  public TelemetryStreamYAxis(String unitName) {
    this.unitName = unitName;
  }
  /**
   * @return the unitName
   */
  public String getUnitName() {
    return unitName;
  }
  /**
   * @param maximum the maximum to set
   */
  public void setMaximum(double maximum) {
    this.maximum = maximum;
  }
  /**
   * @return the maximum
   */
  public double getMaximum() {
    return maximum;
  }
  /**
   * @param minimum the minimum to set
   */
  public void setMinimum(double minimum) {
    this.minimum = minimum;
    if (this.minimum < 0) {
      this.minimum = 0;
    }
  }
  /**
   * @return the minimum
   */
  public double getMinimum() {
    if (this.minimum < 0) {
      this.minimum = 0;
    }
    return minimum;
  }
  /**
   * @param color the color to set
   */
  public void setColor(String color) {
    this.color = color;
  }
  /**
   * @return the color
   */
  public String getColor() {
    return color;
  }
}
