package org.hackystat.projectbrowser.googlechart;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Chart class that represent a chart image that generated from Google Chart API.
 * @author Shaoxuan Zhang
 */
public class GoogleChart implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** host of google chart service */
  public static final String GOOGLECHART_API_URL = "http://chart.apis.google.com/chart?";
  /** character that separate parameter. */
  public static final String PARAMETER_SEPARATOR = "&";
  /** character that separate data sets. */
  public static final String DATASET_SEPARATOR = "|";
  /** character that separate data items in a data set. */
  public static final String DATAITEM_SEPARATOR = ",";
  /** chart type of this chart. */
  private ChartType chartType;
  /** width in pixel. */
  private int width;
  /** height in pixel. */
  private int height;
  /** data to display. */
  private List<List<Double>> chartData = new ArrayList<List<Double>>();
  /** scale of the data. */
  private List<Double> chartDataScale = null;
  /** colors of the chart. */
  private List<String> colors = new ArrayList<String>();
  /** title. */
  private String title = null;
  /** Axis types. */
  private List<String> axisTypes = new ArrayList<String>();
  /** Axis labels. */
  private List<List<String>> axisLabels = new ArrayList<List<String>>();
  /** Axis max ranges. */
  private List<Double> axisMaxRange = new ArrayList<Double>();
  /** the markers. */
  private List<String> chartMarker = new ArrayList<String>();
  /** the legends. */
  private List<String> chartLegend = new ArrayList<String>();
  /**
   * initialize the chart with indispensable parameter.
   * @param chartType type of this chart.
   * @param width width.
   * @param height height.
   */
  public GoogleChart(ChartType chartType, int width, int height) {
    this.chartType = chartType;
    this.width = width;
    this.height = height;
  }
  
  /**
   * Return the URL that represent this chart from Google Chart API.
   * @return the URL of this chart.
   */
  public String getUrl() {
    String url = GOOGLECHART_API_URL;
    url += "chs=" + this.width + "x" + this.height;
    url += PARAMETER_SEPARATOR + "chd=t:" + getDataAsString(this.chartData);
    if (chartDataScale != null && chartDataScale.size() >= 1) {
      url += PARAMETER_SEPARATOR + "chds=" + toTextEncodingData(this.chartDataScale);
    }
    url += PARAMETER_SEPARATOR + "cht=" + chartType.abbrev();
    if (this.title != null) {
      url += PARAMETER_SEPARATOR + "chtt=" + this.title;
    }
    if (colors != null && colors.size() >= 1) {
      url += PARAMETER_SEPARATOR + "chco=" + toTextEncodingData(this.colors);
    }
    if (this.axisTypes.size() > 0) {
      url += PARAMETER_SEPARATOR + "chxt=" + toTextEncodingData(this.axisTypes);
    }
    if (this.axisLabels.size() > 0) {
      StringBuffer stringBuffer = new StringBuffer();
      for (int i = 0; i < this.axisLabels.size(); ++i) {
        if (this.axisLabels.get(i).size() > 0) {
          stringBuffer.append((stringBuffer.length() > 0) ? '|' : "");
          stringBuffer.append(i);
          stringBuffer.append(':');
          for (String label : this.axisLabels.get(i)) {
            stringBuffer.append('|');
            stringBuffer.append(label);
          }
        }
      }
      url += PARAMETER_SEPARATOR + "chxl=" + stringBuffer.toString();
    }
    if (this.axisMaxRange.size() > 0) {
      List<List<Double>> rangeList = new ArrayList<List<Double>>();
      for (int i = 0; i < this.axisMaxRange.size(); ++i) {
        if (!this.axisMaxRange.get(i).isNaN()) {
          List<Double> range = new ArrayList<Double>();
          range.add(i + 0.0);
          range.add(0.0);
          range.add(this.axisMaxRange.get(i));
          rangeList.add(range);
        }
      }
      url += PARAMETER_SEPARATOR + "chxr=" + this.getDataAsString(rangeList);
    }
    if (this.chartMarker.size() > 0) {
      List<List<String>> markerList = new ArrayList<List<String>>();
      for (int i = 0; i < this.chartMarker.size(); ++i) {
        for (int j = 0; j < this.chartData.get(i).size(); ++j) {
          List<String> marker = new ArrayList<String>();
          marker.add(this.chartMarker.get(i));
          marker.add(this.colors.get(i));
          marker.add(i + "");
          marker.add(j + ".0");
          marker.add("10.0");
          markerList.add(marker);
        }
      }
      url += PARAMETER_SEPARATOR + "chm=" + this.getDataAsString(markerList);
    }
    if (this.chartLegend.size() > 0) {
      StringBuffer stringBuffer = new StringBuffer();
      for (String dataItem : this.chartLegend) {
        int index = dataItem.indexOf('<');
        String data = dataItem.substring(0, index);
        stringBuffer.append(data + DATASET_SEPARATOR);
      }
      String dataString = stringBuffer.toString();
      if (dataString.endsWith(DATASET_SEPARATOR)) {
        dataString = dataString.substring(0, dataString.lastIndexOf(DATASET_SEPARATOR));
      }
      url += PARAMETER_SEPARATOR + "chdl=" + dataString;
    }
    return url;
  }

  /**
   * Return the color list in format of String.
   * @param colorList the color list to encode.
   * @return the result string.
   */
  /*
  private String getColorDataAsString(List<Color> colorList) {
    StringBuffer buffer = new StringBuffer();
    for (Color color : colorList) {
      buffer.append(colorToString(color) + DATAITEM_SEPARATOR);
    }
    String colorString = buffer.toString();
    if (colorString.endsWith(DATAITEM_SEPARATOR)) {
      colorString = colorString.substring(0, colorString.lastIndexOf(DATAITEM_SEPARATOR));
    }
    return colorString;
  }
   */
  
  /**
   * convert a Color object to a String, with format of RRGGBB.
   * @param color the Color to be convert.
   * @return a string that represent the color.
   */
  public static String colorToString(Color color) {
    String colorString = "";
    String red = "000" + Integer.toHexString(color.getRed());
    String green = "00" + Integer.toHexString(color.getGreen());
    String blue = "000" + Integer.toHexString(color.getBlue());
    String alpha = "00" + Integer.toHexString(color.getAlpha());
    colorString += red.substring(red.length() - 2);
    colorString += green.substring(green.length() - 2);
    colorString += blue.substring(blue.length() - 2);
    colorString += alpha.substring(alpha.length() - 2);
    return colorString;
  }
  
  /**
   * return the text encoding data with scaling.
   * @param list the list of data to be encoded.
   * @return String of the encoded data.
   */
  private String getDataAsString(List<? extends List<? extends Object>> list) {
    StringBuffer buffer = new StringBuffer();
    for (List<? extends Object> dataList : list) {
      buffer.append(toTextEncodingData(dataList) + DATASET_SEPARATOR);
    }
    String dataString = buffer.toString();
    if (dataString.endsWith(DATASET_SEPARATOR)) {
      dataString = dataString.substring(0, dataString.lastIndexOf(DATASET_SEPARATOR));
    }
    return dataString;
  }

  /**
   * convert the data list to encoded data string.
   * @param dataList the data list to be converted.
   * @return the string of result.
   */
  private String toTextEncodingData(List<? extends Object> dataList) {
    StringBuffer buffer = new StringBuffer();
    for (Object dataItem : dataList) {
      buffer.append(dataItem + DATAITEM_SEPARATOR);
    }
    String dataString = buffer.toString();
    if (dataString.endsWith(DATAITEM_SEPARATOR)) {
      dataString = dataString.substring(0, dataString.lastIndexOf(DATAITEM_SEPARATOR));
    }
    return dataString;
  }
  
  /**
   * @param width the width to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @param chartType the chartType to set
   */
  public void setChartType(ChartType chartType) {
    this.chartType = chartType;
  }

  /**
   * @return the chartType
   */
  public ChartType getChartType() {
    return chartType;
  }

  /**
   * @param chartData the chartData to set
   */
  public void setChartData(List<List<Double>> chartData) {
    this.chartData = chartData;
  }

  /**
   * @return the chartData
   */
  public List<List<Double>> getChartData() {
    return chartData;
  }

  /**
   * @param chartDataScale the chartDataScale to set
   */
  public void setChartDataScale(List<Double> chartDataScale) {
    this.chartDataScale = chartDataScale;
  }

  /**
   * @return the chartDataScale
   */
  public List<Double> getChartDataScale() {
    return chartDataScale;
  }

  /**
   * @return the color list.
   */
  public List<String> getColors() {
    return this.colors;
  }
  
  /**
   * add the color to color list.
   * @param color String that represent the color in format RRGGBB.
   */
  public void addColor(String color) {
    this.colors.add(color);
  }

  /**
   * add the color to color list.
   * @param color the Color to add.
   */
  public void addColor(Color color) {
    this.colors.add(colorToString(color));
  }
  
  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }
  
  /**
   * add a label with default label values.
   * @param type axis label type, either x, t, y, or r.
   */
  public void addAxisLabel(String type) {
    addAxisLabel(type, new ArrayList<String>());
  }

  /**
   * add a label with given label values.
   * @param type axis label type, either x, t, y, or r.
   * @param labels the given label values.
   */
  public void addAxisLabel(String type, List<String> labels) {
    this.axisTypes.add(type);
    this.axisLabels.add(labels);
    this.axisMaxRange.add(Double.NaN);
  }

  /**
   * add a label with given label range.
   * @param type axis label type, either x, t, y, or r.
   * @param range the given range.
   */
  public void addAxisLabel(String type, Double range) {
    this.axisTypes.add(type);
    this.axisLabels.add(new ArrayList<String>());
    this.axisMaxRange.add(range);
  }
  
  /**
   * Remove the ith axislabel.
   * @param i the index of label to be removed.
   */
  public void removeAxisLabel(int i) {
    this.axisTypes.remove(i);
    this.axisLabels.remove(i);
    this.axisMaxRange.remove(i);
  }
  
  /**
   * @return the chartMarker
   */
  public List<String> getChartMarker() {
    return chartMarker;
  }

  /**
   * @return the chartLegend
   */
  public List<String> getChartLegend() {
    return chartLegend;
  }
  
}
