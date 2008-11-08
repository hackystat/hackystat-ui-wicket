package org.hackystat.projectbrowser.googlechart;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Chart class that represent a chart image that generated from Google Chart API.
 * @author Shaoxuan Zhang
 */
public class GoogleChart implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** host of google chart service. */
  public static final String GOOGLECHART_API_URL = "http://chart.apis.google.com/chart?";
  /** character that separate parameter. */
  public static final String PARAMETER_SEPARATOR = "&";
  /** character that separate data sets. */
  public static final String DATASET_SEPARATOR = "|";
  /** character that separate data items in a data set. */
  public static final String DATAITEM_SEPARATOR = ",";
  /** The maximum size of a google chart. */
  public static final int MAX_SIZE = 300000;
  /** chart type of this chart. */
  private ChartType chartType;
  /** width in pixel. */
  private int width;
  /** height in pixel. */
  private int height;
  /** the background color. */
  private String backgroundColor;
  /** data to display. */
  private List<List<Double>> chartData = new ArrayList<List<Double>>();
  /** scale of the data. */
  private List<List<Double>> chartDataScale = new ArrayList<List<Double>>();
  /** colors of the chart. */
  private List<String> colors = new ArrayList<String>();
  /** title. */
  private String title = null;
  /** Axis types. */
  private List<String> axisTypes = new ArrayList<String>();
  /** Axis labels. */
  private List<List<String>> axisLabels = new ArrayList<List<String>>();
  /** Axis max ranges. */
  private List<List<Double>> axisMaxRange = new ArrayList<List<Double>>();
  /** Axis colors. */
  private List<String> axisColor = new ArrayList<String>();
  /** the markers. */
  private List<String> chartMarker = new ArrayList<String>();
  /** colors of the markers. */
  private List<String> markerColors = new ArrayList<String>();
  /** the legends. */
  private List<String> chartLegend = new ArrayList<String>();
  /** the line styles.*/
  private List<List<Double>> chartLineStyle = new ArrayList<List<Double>>();
  /** the bar chart size. */
  private List<Integer> barChartSize = new ArrayList<Integer>();
  /** collection of markers.*/
  private static final List<String> markers = Arrays.asList(new String[]{"o", "d", "c", "x", "s"});
  /** index of the current available marker. */
  private static int markersIndex = 0;
  
  private static final List<String> jetColors = Arrays.asList(new String[] { "00007F", "0000DC",
      "0039FF", "0096FF", "00F3FF", "50FFAD", "ADFF50", "FFF300", "FF9600", "FF3900", "DC0000",
      "7F0000" });
  private static short currentJetColor = 0;

  
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
   * Return the next available marker.
   * @return a String that represents the marker
   */
  public static String getNextMarker() {
    if (markersIndex >= markers.size()) {
      markersIndex = 0;
    }
    return markers.get(markersIndex++);
  }

  /**
   * Return the URL that represent this chart from Google Chart API.
   * @return the URL of this chart.
   */
  public String getUrl() {
    String url = GOOGLECHART_API_URL;
    url += "chs=" + this.width + "x" + this.height;
    //add data
    url += PARAMETER_SEPARATOR + "chd=t:" + getDataTableAsString(this.chartData);
    //add data scale
    if (chartDataScale != null && !chartDataScale.isEmpty()) {
      String scaleData = getDataTableAsString(this.chartDataScale);
      scaleData = scaleData.replace('|', ',');
      url += PARAMETER_SEPARATOR + "chds=" + scaleData;
    }
    //add chart type
    url += PARAMETER_SEPARATOR + "cht=" + chartType.abbrev();
    //add bar chart size
    if (!this.barChartSize.isEmpty()) {
      url += PARAMETER_SEPARATOR + "chbh=" + getDataListAsString(barChartSize);
    }
    //add title
    if (this.title != null) {
      url += PARAMETER_SEPARATOR + "chtt=" + this.title;
    }
    //add stream color
    if (colors != null && !colors.isEmpty()) {
      url += PARAMETER_SEPARATOR + "chco=" + getDataListAsString(this.colors);
    }
    if (!this.chartLineStyle.isEmpty()) {
      url += PARAMETER_SEPARATOR + "chls=" + getDataTableAsString(chartLineStyle);
    }
    //add axis type
    if (!this.axisTypes.isEmpty()) {
      url += PARAMETER_SEPARATOR + "chxt=" + getDataListAsString(this.axisTypes);
    }
    //add axis label
    if (!this.axisLabels.isEmpty()) {
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
    //add axis range
    if (!this.axisMaxRange.isEmpty()) {
      List<List<String>> axisRange = new ArrayList<List<String>>();
      for (int i = 0; i < this.axisMaxRange.size(); ++i) {
        if (!this.axisMaxRange.get(i).isEmpty()) {
          List<String> range = new ArrayList<String>();
          range.add(String.valueOf(i));
          for (Double value : this.axisMaxRange.get(i)) {
            range.add(value.toString());
          }
          axisRange.add(range);
        }
      }
      url += PARAMETER_SEPARATOR + "chxr=" + this.getDataTableAsString(axisRange);
    }
    //add axis color
    if (!this.axisColor.isEmpty()) {
      List<List<String>> axisStyle = new ArrayList<List<String>>();
      for (int i = 0; i < this.axisColor.size(); ++i) {
        if (this.axisColor.get(i).length() > 0) {
          axisStyle.add(Arrays.asList(new String[]{String.valueOf(i), this.axisColor.get(i)}));
        }
      }
      url += PARAMETER_SEPARATOR + "chxs=" + this.getDataTableAsString(axisStyle);
    }
    //add chart mark
    if (!this.chartMarker.isEmpty()) {
      List<List<String>> markerList = new ArrayList<List<String>>();
      for (int i = 0; i < this.chartMarker.size(); ++i) {
          List<String> marker = new ArrayList<String>();
          marker.add(this.chartMarker.get(i));
          marker.add(this.getMarkerColors().get(i));
          marker.add(i + "");
          marker.add("-1.0");
          marker.add("10.0");
          markerList.add(marker);
      }
      url += PARAMETER_SEPARATOR + "chm=" + this.getDataTableAsString(markerList);
    }
    // add legend
    if (!this.chartLegend.isEmpty()) {
      StringBuffer stringBuffer = new StringBuffer();
      for (String dataItem : this.chartLegend) {
        /*
        int index = dataItem.indexOf('<');
        String data = dataItem.substring(0, index);
        */
        stringBuffer.append(dataItem + DATASET_SEPARATOR);
      }
      String dataString = stringBuffer.toString();
      if (dataString.endsWith(DATASET_SEPARATOR)) {
        dataString = dataString.substring(0, dataString.lastIndexOf(DATASET_SEPARATOR));
      }
      url += PARAMETER_SEPARATOR + "chdl=" + dataString;
    }
    if (backgroundColor != null && backgroundColor.length() > 0) {
      url += PARAMETER_SEPARATOR + "chf=bg,s," + backgroundColor;
    }
    return url;
  }
  
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
  private String getDataTableAsString(List<? extends List<? extends Object>> list) {
    StringBuffer buffer = new StringBuffer();
    for (List<? extends Object> dataList : list) {
      buffer.append(getDataListAsString(dataList) + DATASET_SEPARATOR);
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
  private String getDataListAsString(List<? extends Object> dataList) {
    StringBuffer buffer = new StringBuffer();
    for (Object dataItem : dataList) {
      if (dataItem instanceof Double) {
        dataItem = trimDouble((Double)dataItem);
      }
      buffer.append(dataItem + DATAITEM_SEPARATOR);
    }
    String dataString = buffer.toString();
    if (dataString.endsWith(DATAITEM_SEPARATOR)) {
      dataString = dataString.substring(0, dataString.lastIndexOf(DATAITEM_SEPARATOR));
    }
    return dataString;
  }
  
  /**
   * Trim the double number. 
   * Keep only one number after decimal point.
   * @param dataItem the Double number.
   * @return a string of result.
   */
  private String trimDouble(Double dataItem) {
    String number = dataItem.toString();
    int index = number.indexOf('.');
    if (index > 0 && index + 2 < number.length()) {
      number = number.substring(0, index + 2);
    }
    return number;
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
  public void setChartDataScale(List<List<Double>> chartDataScale) {
    this.chartDataScale = chartDataScale;
  }

  /**
   * @return the chartDataScale
   */
  public List<List<Double>> getChartDataScale() {
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
    addAxisLabel(type, new ArrayList<String>(), "");
  }

  /**
   * add a label with given label values.
   * @param type axis label type, either x, t, y, or r.
   * @param labels the given label values.
   * @param color the axis color.
   */
  public void addAxisLabel(String type, List<String> labels, String color) {
    this.axisTypes.add(type);
    this.axisLabels.add(labels);
    this.axisMaxRange.add(new ArrayList<Double>());
    this.axisColor.add(color);
  }

  /**
   * add a label with given label range.
   * @param type axis label type, either x, t, y, or r.
   * @param range the given range.
   * @param color the axis color.
   */
  public void addAxisRange(String type, List<Double> range, String color) {
    this.axisTypes.add(type);
    this.axisLabels.add(new ArrayList<String>());
    this.axisMaxRange.add(range);
    this.axisColor.add(color);
  }
  
  /**
   * add a line style with the given parameters.
   * @param thickness thickness of the line.
   * @param lineLength length of the line segment.
   * @param blankLength length of the blank segment.
   */
  public void addLineStyle(double thickness, double lineLength, double blankLength) {
    this.chartLineStyle.add(Arrays.asList(new Double[]{thickness, lineLength, blankLength}));
  }
  /**
   * @return true if no Y axis in this chart yet.
   */
  public boolean isYAxisEmpty() {
    for (String axisType : this.axisTypes) {
      if ("y".equals(axisType) || "y".equals(axisType)) {
        return false;
      }
    }
    return true;
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

  /**
   * @return a random color in format of RRGGBB.
   */
  public static String getRandomColor() {
    Long longValue = Math.round(Math.random() * 0xFFFFFF);
    String randomColor = "000000" + Long.toHexString(longValue);
    randomColor = randomColor.substring(randomColor.length() - 6);
    return randomColor;
  }
  /**
   * @return a random marker.
   */
  public static String getRandomMarker() {
    Random random = new Random();
    return markers.get(random.nextInt(markers.size()));
  }
  /**
   * @param markerColors the markerColors to set
   */
  public void setMarkerColors(List<String> markerColors) {
    this.markerColors = markerColors;
  }

  /**
   * @return the markerColors
   */
  public List<String> getMarkerColors() {
    return markerColors;
  }
  /**
   * Set the size of the bar chart.
   * @param barHeight the height or width of the bar.
   * @param groupSpace the space between groups.
   * @param barSpace the space between bars in a group.
   */
  public void setBarChartSize(int barHeight, int groupSpace, int barSpace) {
    this.barChartSize.clear();
    barChartSize.add(barHeight);
    barChartSize.add(groupSpace);
    barChartSize.add(barSpace);
  }

  /**
   * @param backgroundColor the backgroundColor to set
   */
  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
   * @return the backgroundColor
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * @return a random color in format of RRGGBB.
   */
  public static String getNextJetColor() {
    if ((jetColors.size() % 2) > 0) {
      currentJetColor += 2;
    }
    else {
      currentJetColor += 3;
    }
    if (currentJetColor >= jetColors.size()) {
      currentJetColor = 0;
    }
    return jetColors.get(currentJetColor);
  }
  
}
