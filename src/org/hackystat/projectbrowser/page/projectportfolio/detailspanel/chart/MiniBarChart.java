package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.
        PortfolioMeasureConfiguration;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.telemetry.service.resource.chart.jaxb.YAxis;

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
  /** The index of the last valid value. */
  private int lastValidIndex = -1;
  /** The latest value of the stream. */
  private double latestValue;
  /** The width of a bar. */
  public static final int BAR_WIDTH = 5;
  /** The max width of the chart. */
  public static final int MAX_WIDTH = 50;
  /** The height of the chart. */
  public static final int CHART_HEIGHT = 15;
  
  /** The PageParameters to construct the chart link to telemetry page. */
  private PageParameters telemetryPageParameters;
  /** The PageParameters to construct the value link to dpd page. */
  //private PageParameters dpdPageParameters;

  /** The configuration of this chart. */
  private final PortfolioMeasureConfiguration configuration;
  
  
  /**
   * @param streams The stream of this chart.
   * @param configuration The configuration of this chart.
   */
  public MiniBarChart(List<TelemetryStream> streams, PortfolioMeasureConfiguration configuration) {
    if (streams.size() > 1) {
      this.streamData = getStreamData(mergeStream(streams, configuration.getMerge()));
    }
    else {
      this.streamData = getStreamData(streams.get(0));
    }
    for (int i = streamData.size() - 1; i >= 0; --i) {
      this.latestValue = streamData.get(i);
      if (latestValue >= 0) {
        this.lastValidIndex = i;
        break;
      }
    }
    this.configuration = configuration;
  }

  /**
   * Merge the streams according to the merge parameter.
   * @param telemetryStreams the input streams.
   * @param merge the method to merge.
   * @return a TelemetryStream.
   */
  private TelemetryStream mergeStream(List<TelemetryStream> telemetryStreams, String merge) {
    //construct the target instance with the first stream.
    TelemetryStream telemetryStream = new TelemetryStream();
    telemetryStream.setYAxis(telemetryStreams.get(0).getYAxis());
    telemetryStream.setName(telemetryStreams.get(0).getName());
    //check y axis, has to be all the same. Otherwise will give out empty stream.
    boolean allMatch = true;
    for (TelemetryStream stream : telemetryStreams) {
      if (!streamsEqual(stream.getYAxis(), telemetryStream.getYAxis())) {
        allMatch = false;
        Logger logger = ProjectPortfolioDataModel.getLogger();
        logger.severe("YAxis: " + stream.getYAxis().getName() + " in stream: " + stream.getName()
            + " is not match to YAxis: " + telemetryStream.getYAxis().getName() + " in stream :"
            + telemetryStream.getName());
      }
    }
    if (allMatch) {
      //combine streams' data.
      List<TelemetryPoint> points = new ArrayList<TelemetryPoint>();
      points.addAll(telemetryStreams.get(0).getTelemetryPoint());
      for (int i = 0; i < points.size(); i++) {
        List<Double> doubleValues = new ArrayList<Double>();
        //get all valid values in the same point.
        for (int j = 0; j < telemetryStreams.size(); ++j) {
          String stringValue = telemetryStreams.get(j).getTelemetryPoint().get(i).getValue();
          if (stringValue != null) {
            doubleValues.add(Double.valueOf(stringValue));
          }
        }
        
        points.get(i).setValue(null);
        //if no valid data, the value of this point will be null
        if (!doubleValues.isEmpty()) {
          if ("sum".equals(merge)) {
            Double value = 0.0;
            for (Double v : doubleValues) {
              value += v;
            }
            points.get(i).setValue(value.toString());
          }
          else if ("avg".equals(merge)) {
            Double value = 0.0;
            for (Double v : doubleValues) {
              value += v;
            }
            value /= telemetryStreams.size();
            points.get(i).setValue(value.toString());
          } 
          else if ("min".equals(merge)) {
            points.get(i).setValue(Collections.min(doubleValues).toString());
          } 
          else if ("max".equals(merge)) {
            points.get(i).setValue(Collections.max(doubleValues).toString());
          }
        }
      }
      telemetryStream.getTelemetryPoint().addAll(points);
    }
    return telemetryStream;
  }

  /**
   * Compare the two given YAxis objects.
   * @param axis1 the first YAxis.
   * @param axis2 the second YAxis.
   * @return true if they are equal.
   */
  private boolean streamsEqual(YAxis axis1, YAxis axis2) {
    return eqauls(axis1.getName(), axis2.getName()) && eqauls(axis1.getUnits(), axis2.getUnits()) &&
           eqauls(axis1.getNumberType(), axis2.getNumberType()) && 
           eqauls(axis1.getLowerBound(), axis2.getLowerBound()) && 
           eqauls(axis1.getUpperBound(), axis2.getUpperBound());
  }

  /**
   * Compare the two given objects. If both objects are null, they are considered equal.
   * @param o1 the first object
   * @param o2 the second object
   * @return true if the two objects are equal, otherwise false.
   */
  private boolean eqauls(Object o1, Object o2) {
    if (o1 == null && o2 == null) {
      return true;
    }
    if (o1 == null || o2 == null) {
      return false;
    }
    return o1.equals(o2);
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
    if (max < 1) {
      max = 1;
    }
    GoogleChart googleChart;
    int chartSize = streamData.size();
    int width = chartSize * BAR_WIDTH;
    width = (width > MAX_WIDTH) ? MAX_WIDTH : width;
    googleChart = new GoogleChart(ChartType.VERTICAL_SERIES_BAR, width, CHART_HEIGHT);
    googleChart.setBarChartSize((width - streamData.size()) / streamData.size(), 1, 0);
    googleChart.getChartData().add(streamData);
    googleChart.getChartDataScale().
            add(Arrays.asList(new Double[]{ 0.0, max}));
    googleChart.getColors().add(this.getChartColor());
    googleChart.setBackgroundColor(this.configuration.getDataModel().getBackgroundColor());
    return googleChart.getUrl();
  }


  /**
   * @return the color
   */
  public String getChartColor() {
    if (configuration.isColorable()) {
      switch (configuration.getStreamTrend(this.streamData)) {
      case STABLE: return configuration.getStableColor();
      case INCREASING: return configuration.getHigherColor();
      case DECREASING: return configuration.getLowerColor();
      default: return configuration.getUnclassifiedTrendColor();
      }
    }
    else {
      return configuration.getDataModel().getFontColor();
    }
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
    if (this.getLatestValue() < 0) {
      return configuration.getDataModel().getNAColor();
    }
    if (configuration.isColorable()) {
      if (this.getLatestValue() >= this.configuration.getHigherThreshold()) {
        return configuration.getHigherColor();
      }
      if (this.getLatestValue() < this.configuration.getLowerThreshold()) {
        return configuration.getLowerColor();
      }
      return configuration.getMiddleColor();
    }
    else {
      return configuration.getDataModel().getFontColor();
    }
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

  /**
   * @return the configuration
   */
  public PortfolioMeasureConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * @return the lastValidIndex
   */
  public int getLastValidIndex() {
    return lastValidIndex;
  }

}
