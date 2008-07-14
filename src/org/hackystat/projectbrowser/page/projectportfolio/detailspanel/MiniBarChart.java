package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;

/**
 * A mini bar chart.
 * Use GoogleChart to generate the chart.
 * @author Shaoxuan Zhang
 *
 */
public class MiniBarChart implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1213447209533926430L;
  /** The stream of this chart. */
  protected List<Double> streamData;
  /** The latest value of the stream. */
  private double latestValue;
  /** The width of the chart. */
  public static final int width = 30;
  /** The height of the chart. */
  public static final int height = 15;
  
  /** 
   * Color of the chart. 
   * Default to be black.
   */
  private String chartColor = "000000";
  /** The PageParameters to construct the link. */
  private PageParameters telemetryPageParameters;
  
  
  /**
   * @param stream The stream of this chart.
   */
  public MiniBarChart(TelemetryStream stream) {
    this(stream, "000000");
  }
  
  /**
   * @param stream The stream of this chart.
   * @param color The color of this chart.
   */
  public MiniBarChart(TelemetryStream stream, String color) {
    this.chartColor = color;
    this.streamData = getStreamData(stream);
    for (int i = streamData.size() - 1; i >= 0; --i) {
      this.latestValue = streamData.get(i);
      if (latestValue >= 0) {
        break;
      }
    }
  }

  /**
   * Return a List of Double from the given telemetry stream.
   * @param stream a TelemetryStream
   * @return the list of data of this stream
   */
  protected final List<Double> getStreamData(TelemetryStream stream) {
    List<Double> streamData = new ArrayList<Double>();
    for (TelemetryPoint point : stream.getTelemetryPoint()) {
      if (point.getValue() == null) {
        streamData.add(-1.0);
      }
      else {
        Double value = Double.valueOf(point.getValue());
        if (value.isNaN()) {
          value = -2.0;
        }
        streamData.add(value);
      }
    }
    return streamData;
  }
  
  /**
   * @return a String URL that represents this chart.
   */
  public String getImageUrl() {
    double max = Collections.max(streamData);
    //return empty string when the chart is empty.
    if (max < 0) {
      return "";
    }
    GoogleChart googleChart;
    googleChart = new GoogleChart(ChartType.VERTICAL_SERIES_BAR, width, height);
    googleChart.setBarChartSize((width - streamData.size()) / streamData.size(), 1, 0);
    googleChart.getChartData().add(streamData);
    googleChart.getChartDataScale().
            add(Arrays.asList(new Double[]{ 0.0, max}));
    googleChart.getColors().add(this.chartColor);
    return googleChart.getUrl();
  }

  /**
   * @param color the color to set
   */
  public void setChartColor(String color) {
    this.chartColor = color;
  }

  /**
   * @return the color
   */
  public String getChartColor() {
    return chartColor;
  }

  /**
   * @return the latestValue
   */
  public double getLatestValue() {
    return latestValue;
  }

  /**
   * @return the valueColor
   */
  public String getValueColor() {
    return "000000";
  }

  /**
   * @param telemetryPageParameters the telemetryPageParameters to set
   */
  public void setTelemetryPageParameters(PageParameters telemetryPageParameters) {
    this.telemetryPageParameters = telemetryPageParameters;
  }

  /**
   * @return the telemetryPageParameters
   */
  public PageParameters getTelemetryPageParameters() {
    return telemetryPageParameters;
  }

}
