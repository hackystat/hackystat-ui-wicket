package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;

/**
 * A mini bar chart with auto coloring.
 * @author Shaoxuan Zhang
 */
public class AutoColorMiniBarChart extends MiniBarChart {

  /** Support serialization. */
  private static final long serialVersionUID = -4913386312346727222L;
  
  /** The configuration of this chart. */
  private MeasureConfiguration configuration;
  
  /**
   * @param stream The stream of this chart.
   * @param configuration The configuration of this chart..
   */
  public AutoColorMiniBarChart(TelemetryStream stream, MeasureConfiguration configuration) {
    super(stream);
    this.configuration = configuration;
  }

  /**
   * @return the color
   */
  public String getChartColor() {
    return getAutoColor();
  }

  /**
   * Return a color string according to the within stream.
   * Green if the stream is monotonic increasing;
   * Red if the stream is monotonic decreasing;
   * Yellow if other wise.
   * @return a string of color.
   */
  private String getAutoColor() { 
    boolean increased = false;
    boolean decreased = false;
    for (int i = 1; i < this.streamData.size(); ++i) {
      if (this.streamData.get(i) >= 0) {
        if (this.streamData.get(i - 1) > this.streamData.get(i)) {
          decreased = true;
        }
        else if (this.streamData.get(i - 1) < this.streamData.get(i)) {
          increased = true;
        }
      }
    }
    if (!increased && !decreased) {
      return configuration.getStableColor();
    }
    if (increased && !decreased) {
      return configuration.getHigherColor();
    }
    if (decreased && !increased) {
      return configuration.getLowerColor();
    }
    return configuration.getMiddleColor();
  }
  
  /**
   * @return the valueColor
   */
  @Override
  public String getValueColor() {
    if (this.getLatestValue() < 0) {
      return black;
    }
    if (this.getLatestValue() >= this.configuration.getHigherThreshold()) {
      return configuration.getHigherColor();
    }
    if (this.getLatestValue() < this.configuration.getLowerThreshold()) {
      return configuration.getLowerColor();
    }
    return configuration.getMiddleColor();
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
