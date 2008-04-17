package org.hackystat.projectbrowser.googlechart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Chart class that represent a chart image that generated from Google Chart API.
 * @author Shaoxuan Zhang
 */
public class GoogleChart {
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
  private List<List<Double>> chartData;
  /** scale of the data. */
  private List<Double> chartDataScale = null;
  /** colors of the chart. */
  private List<Color> colors = null;
  /** title. */
  private String title = null;
  
  /**
   * initialize the chart with indispensable parameter.
   * @param chartType type of this chart.
   * @param width width.
   * @param height height.
   * @param chartData data.
   */
  public GoogleChart(ChartType chartType, int width, int height, List<List<Double>> chartData) {
    this.chartType = chartType;
    this.width = width;
    this.height = height;
    this.chartData = chartData;
  }
  
  /**
   * for trying.
   * @param args for convension.
   */
  public static void main(String[] args) {
    GoogleChart chart = new GoogleChart(ChartType.HORIZONTAL_SERIES_BAR, 300, 1000,
        new ArrayList<List<Double>>());
    List<Double> firstSeries = new ArrayList<Double>();
    List<Double> secondSeries = new ArrayList<Double>();
    for (int i = 1; i <= 40; i++) {
      firstSeries.add(new Double(i));
      secondSeries.add(new Double(i));
    }
    chart.getChartData().add(firstSeries);
    chart.getChartData().add(secondSeries);
    List<Color> colors = new ArrayList<Color>();
    colors.add(Color.GREEN.darker());
    colors.add(Color.RED.brighter());
    chart.setColors(colors);
    System.out.println(chart.getUrl());
  }
  
  /**
   * Return the URL that represent this chart from Google Chart API.
   * @return the URL of this chart.
   */
  public String getUrl() {
    String url = GOOGLECHART_API_URL;
    url += "chs=" + this.width + "x" + this.height;
    url += PARAMETER_SEPARATOR + "chd=" + getDataAsString(this.chartData);
    if (chartDataScale != null && chartDataScale.size() >= 1) {
      url += PARAMETER_SEPARATOR + "chds=" + toTextEncodingData(this.chartDataScale);
    }
    url += PARAMETER_SEPARATOR + "cht=" + chartType.abbrev();
    if (this.title != null) {
      url += PARAMETER_SEPARATOR + "chtt=" + this.title;
    }
    if (colors != null && colors.size() >= 1) {
      url += PARAMETER_SEPARATOR + "chco=" + getColorDataAsString(this.colors);
    }
    return url;
  }

  /**
   * Return the color list in format of String.
   * @param colorList the color list to encode.
   * @return the result string.
   */
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

  /**
   * convert a Color object to a String, with format of RRGGBB.
   * @param color the Color to be convert.
   * @return a string that represent the color.
   */
  private String colorToString(Color color) {
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
  private String getDataAsString(List<List<Double>> list) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("t:");
    for (List<Double> dataList : list) {
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
  private String toTextEncodingData(List<Double> dataList) {
    StringBuffer buffer = new StringBuffer();
    for (Double dataItem : dataList) {
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
   * @param colors the colors to set
   */
  public void setColors(List<Color> colors) {
    this.colors = colors;
  }

  /**
   * @return the colors
   */
  public List<Color> getColors() {
    return colors;
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
}
