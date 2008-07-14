package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;

/**
 * A mini bar chart with auto coloring.
 * @author Shaoxuan Zhang
 */
public class AutoColorMiniBarChart extends MiniBarChart {

  /** Support serialization. */
  private static final long serialVersionUID = -4913386312346727222L;
  /** RGB String format of red. */
  public static final String red = "ff0000";
  /** RGB String format of green. */
  public static final String green = "00ff00";
  /** RGB String format of yellow. */
  public static final String yellow = "ffff00";
  /** RGB String format of yellow. */
  public static final String black = "000000";
  
  /** The configuration of this chart. */
  private MeasureConfiguration configuration;
  
  /**
   * @param stream The stream of this chart.
   * @param configuration The configuration of this chart..
   */
  public AutoColorMiniBarChart(TelemetryStream stream, MeasureConfiguration configuration) {
    super(stream);
    if (configuration == null) {
      this.configuration = new MeasureConfiguration(40, 90, true);
    }
    else {
      this.configuration = configuration;
    }
    this.setChartColor(getAutoColor());
  }

  /**
   * Return a color string according to the within stream.
   * Green if the stream is monotonic increasing;
   * Red if the stream is monotonic decreasing;
   * Yellow if other wise.
   * @return a string of color.
   */
  private String getAutoColor() { 
    boolean increasing = false;
    boolean decreasing = false;
    for (int i = 1; i < this.streamData.size(); ++i) {
      if (this.streamData.get(i) >= 0) {
        if (this.streamData.get(i - 1) > this.streamData.get(i)) {
          decreasing = true;
        }
        else if (this.streamData.get(i - 1) < this.streamData.get(i)) {
          increasing = true;
        }
      }
    }
    if (!increasing && !decreasing) {
      return green;
    }
    if (increasing && !decreasing) {
      if (configuration.isHigherTheBetter()) {
        return green;
      }
      else {
        return red;
      }
    }
    if (decreasing && !increasing) {
      if (configuration.isHigherTheBetter()) {
        return red;
      }
      else {
        return green;
      }
    }
    return yellow;
  }
  
  /**
   * @return the valueColor
   */
  public String getValueColor() {
    if (this.getLatestValue() < 0) {
      return black;
    }
    if (this.getLatestValue() >= this.configuration.getHigherThreshold()) {
      if (configuration.isHigherTheBetter()) {
        return green;
      }
      else {
        return red;
      }
    }
    if (this.getLatestValue() < this.configuration.getLowerThreshold()) {
      if (configuration.isHigherTheBetter()) {
        return red;
      }
      else {
        return green;
      }
    }
    return yellow;
  }

  /**
   * @param configuration the configuration to set
   */
  public void setConfiguration(MeasureConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * @return the configuration
   */
  public MeasureConfiguration getConfiguration() {
    return configuration;
  }

}
