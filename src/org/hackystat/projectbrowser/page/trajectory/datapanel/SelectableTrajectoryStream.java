package org.hackystat.projectbrowser.page.trajectory.datapanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hackystat.projectbrowser.page.trajectory.dtw.DTWUtilFactory;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;

/**
 * Group a selected flag with a TelemetryStream, so that this instance can be flaged as selected or
 * not.
 * 
 * @author Shaoxuan, Pavel Senin.
 * 
 */
public class SelectableTrajectoryStream implements Serializable {
  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** Determine this stream is selected or not. */
  private boolean selected = false;
  /** The TelemetryStream of this stream. */
  private TelemetryStream telemetryStream;
  /** The color associated with this stream. */
  private String color = "";
  /** the marker of this stream. */
  private String marker = "";
  /** thickness of the line. */
  private double thickness = 2;
  /** length of the line segment. */
  private double lineLength = 1;
  /** length of the blank segment. */
  private double blankLength = 0;
  /** the maximum of this stream. */
  private double maximum;
  /** the minimum of this stream. */
  private double minimum;
  private Integer indent;

  /**
   * Constructor.
   * 
   * @param telemetryStream the TelemetryStream of this instance.
   */
  public SelectableTrajectoryStream(TelemetryStream telemetryStream) {
    this.telemetryStream = telemetryStream;
    // initial the maximum and minimum value.
    List<Double> streamData = this.getStreamData();
    maximum = -1;
    minimum = 99999999;
    for (Double value : streamData) {
      if (value > maximum) {
        maximum = value;
      }
      if (value >= 0 && value < minimum) {
        minimum = value;
      }
    }
  }

  /**
   * Constructor.
   * 
   * @param stream The telemetry stream.
   * @param indent The indent value.
   */
  public SelectableTrajectoryStream(TelemetryStream stream, Integer indent) {
    this.telemetryStream = stream;
    // initial the maximum and minimum value.
    List<Double> streamData = this.getStreamData();
    maximum = -1;
    minimum = 99999999;
    for (Double value : streamData) {
      if (value > maximum) {
        maximum = value;
      }
      if (value >= 0 && value < minimum) {
        minimum = value;
      }
    }
    this.indent = indent;
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * @return the selected
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * @param telemetryStream the telemetryStream to set
   */
  public void setTelemetryStream(TelemetryStream telemetryStream) {
    this.telemetryStream = telemetryStream;
  }

  /**
   * @return the telemetryStream
   */
  public TelemetryStream getTelemetryStream() {
    return telemetryStream;
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

  /**
   * Returns a background-color attribute with the value of color.
   * 
   * @return The background-color key-value pair.
   */
  public String getBackgroundColorValue() {
    return "background-color:#" + getColor();
  }

  /**
   * @param marker the marker to set
   */
  public void setMarker(String marker) {
    this.marker = marker;
  }

  /**
   * @return the marker
   */
  public String getMarker() {
    return marker;
  }

  /**
   * @return the isEmpty
   */
  public boolean isEmpty() {
    return this.maximum < 0;
  }

  /**
   * @return the maximum
   */
  public double getMaximum() {
    return maximum;
  }

  /**
   * @return the minimum
   */
  public double getMinimum() {
    return minimum;
  }

  /**
   * @param thickness the thickness to set
   */
  public void setThickness(double thickness) {
    this.thickness = thickness;
  }

  /**
   * @return the thickness
   */
  public double getThickness() {
    return thickness;
  }

  /**
   * @param lineLength the lineLength to set
   */
  public void setLineLength(double lineLength) {
    this.lineLength = lineLength;
  }

  /**
   * @return the lineLength
   */
  public double getLineLength() {
    return lineLength;
  }

  /**
   * @param blankLength the blankLength to set
   */
  public void setBlankLength(double blankLength) {
    this.blankLength = blankLength;
  }

  /**
   * @return the blankLength
   */
  public double getBlankLength() {
    return blankLength;
  }

  /**
   * Get the stream data.
   * 
   * @param maxStreamLength The expected length of the list to plot. N/A points will be added to
   *        reach the length specified.
   * @return the list of data of this stream
   */
  public final List<Double> getStreamData(Integer maxStreamLength) {
    List<Double> streamData = new ArrayList<Double>();
    for (int i = 0; i < this.indent; i++) {
      streamData.add(-1.0);
    }
    for (TelemetryPoint point : this.getTelemetryStream().getTelemetryPoint()) {
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
    // extend the list
    while (streamData.size() < maxStreamLength) {
      streamData.add(-2.0);
    }
    return streamData;
  }

  /**
   * Get the stream data.
   * 
   * @return the list of data of this stream
   */
  public final ArrayList<Double> getNormalizedStreamData() {
    double[] streamData = new double[this.indent
        + this.getTelemetryStream().getTelemetryPoint().size()];
    for (int i = 0; i < this.indent; i++) {
      streamData[i] = 0.0D;
    }
    int currElement = this.indent;
    for (TelemetryPoint point : this.getTelemetryStream().getTelemetryPoint()) {
      if (point.getValue() == null) {
        streamData[currElement] = 0.0D;
      }
      else {
        Double value = Double.valueOf(point.getValue());
        if (value.isNaN()) {
          value = 0.0D;
        }
        streamData[currElement] = value;
      }
      currElement++;
    }
    // normalize
    streamData = DTWUtilFactory.normalize(streamData);
    ArrayList<Double> res = new ArrayList<Double>();
    for (int i = 0; i < streamData.length; i++) {
      res.add(streamData[i]);
    }
    return res;
  }

  /**
   * Return a image url that shows only one marker. Using google chart to generate this image. there
   * is an example output: http://chart.apis.google.com/chart?
   * chs=20x20&cht=ls&chd=t:-1,1.0,-1&chds=0.9,1.1&chm=c,FF0000,0,-1,20.0
   * 
   * @return the image url
   */
  public String getMarkerImageUrl() {
    if (!this.isSelected() || this.isEmpty() || this.marker.length() <= 0) {
      return "";
    }
    String imageUrl = "http://chart.apis.google.com/chart?"
        + "chs=45x15&cht=ls&chd=t:1.0,1.0,1.0&chds=0.9,1.1&" + "chm=" + marker + "," + color
        + ",0,1,10.0&" + "chls=" + thickness + "," + lineLength + "," + blankLength + "&" + "chco="
        + color;
    return imageUrl;
  }

  /**
   * Return the Units of this stream.
   * 
   * @return String of the units.
   */
  public String getUnitName() {
    return this.telemetryStream.getYAxis().getUnits();
  }

  /**
   * Get the stream data.
   * 
   * @return the list of data of this stream
   */
  public final List<Double> getStreamData() {
    List<Double> streamData = new ArrayList<Double>();
    for (TelemetryPoint point : this.getTelemetryStream().getTelemetryPoint()) {
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
   * Set the indent.
   * 
   * @param indent The indent value.
   */
  public void setIndent(Integer indent) {
    this.indent = indent;
  }

  /**
   * Get the indent.
   * 
   * @return the indent value.
   */
  public Integer getIndent() {
    return this.indent;
  }
}
