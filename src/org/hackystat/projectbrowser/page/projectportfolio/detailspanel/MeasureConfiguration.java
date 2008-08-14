package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for project portfolio measures.
 * @author Shaoxuan Zhang
 *
 */
public class MeasureConfiguration implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = -6799287509742157998L;

  /** RGB String format of red. */
  public static final String red = "ff0000";
  /** RGB String format of green. */
  public static final String green = "00ff00";
  /** RGB String format of yellow. */
  public static final String yellow = "ffff00";
  
  /** The name of the measure. */
  private String measureName;
  /** If this measure is colorable. */
  private boolean colorable;
  /** If this measure is enabled. */
  private boolean enabled = true;
  
  /** The threshold of high value. */
  private double higherThreshold;
  /** The threshold of high value. */
  private double lowerThreshold;
  /** If higher value means better. */
  private boolean isHigherTheBetter;
  /** The color for higher value and increasing trend. */
  private String higherColor;
  /** The color for middle value and unstable trend. */
  private String middleColor;
  /** The color for lower value and decreasing trend. */
  private String lowerColor;
  
  /** This meausre's parameter list. */
  private List<String> parameters = new ArrayList<String>();
  
  /**
   * Constructor with default value;
   * @param name The name of the measure.
   * @param colorable If this measure is colorable.
   */
  public MeasureConfiguration(String name, boolean colorable) {
    this(name, colorable, 40, 90, true, green, yellow, red);
  }

  /**
   * @param name The name of the measure.
   * @param colorable If this measure is colorable.
   * @param higherThreshold The threshold of high value.
   * @param lowerThreshold The threshold of high value.
   * @param isHigherTheBetter If higher value means better.
   */
  public MeasureConfiguration(String name, boolean colorable, double lowerThreshold, 
      double higherThreshold, boolean isHigherTheBetter) {
    this(name, colorable, lowerThreshold, higherThreshold, isHigherTheBetter, green, yellow, red);
    if (!this.isHigherTheBetter) {
      this.higherColor = red;
      this.lowerColor = green;
    }
  
  }
  /**
   * Full parameter constructor.
   * @param name The name of the measure.
   * @param colorable If this measure is colorable.
   * @param higherThreshold The threshold of high value.
   * @param lowerThreshold The threshold of high value.
   * @param isHigherTheBetter If higher value means better.
   * @param higherColor The color for higher value and increasing trend.
   * @param middleColor The color for middle value and unstable trend.
   * @param lowerColor The color for lower value and decreasing trend.
   */
  public MeasureConfiguration(String name, boolean colorable, double lowerThreshold, 
      double higherThreshold, boolean isHigherTheBetter, 
      String higherColor, String middleColor, String lowerColor) {
    this.measureName = name;
    this.colorable = colorable;
    this.lowerThreshold = lowerThreshold;
    this.higherThreshold = higherThreshold;
    this.isHigherTheBetter = isHigherTheBetter;
    this.higherColor = higherColor;
    this.middleColor = middleColor;
    this.lowerColor = lowerColor;
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
   * @param isHigherTheBetter the isIncreasingGood to set
   */
  public void setHigherTheBetter(boolean isHigherTheBetter) {
    this.isHigherTheBetter = isHigherTheBetter;
  }

  /**
   * @return the isIncreasingGood
   */
  public boolean isHigherTheBetter() {
    return isHigherTheBetter;
  }

  /**
   * @param measureName the measureName to set
   */
  public void setMeasureName(String measureName) {
    this.measureName = measureName;
  }

  /**
   * @return the measureName
   */
  public String getName() {
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
   * @param enabled the enable to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @return the enable
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param higherColor the higherColor to set
   */
  public void setHigherColor(String higherColor) {
    this.higherColor = higherColor;
  }

  /**
   * @return the higherColor
   */
  public String getHigherColor() {
    return higherColor;
  }

  /**
   * @param middleColor the middleColor to set
   */
  public void setMiddleColor(String middleColor) {
    this.middleColor = middleColor;
  }

  /**
   * @return the middleColor
   */
  public String getMiddleColor() {
    return middleColor;
  }

  /**
   * @param lowerColor the lowerColor to set
   */
  public void setLowerColor(String lowerColor) {
    this.lowerColor = lowerColor;
  }

  /**
   * @return the lowerColor
   */
  public String getLowerColor() {
    return lowerColor;
  }
  
  /** 
   * Return the parameters in a single String.
   * @return parameters separated by ',' in a single String.
   */
  public String getParamtersString() {
    if (this.parameters.isEmpty()) {
      return "";
    }
    StringBuffer param = new StringBuffer();
      for (int i = 0; i < this.parameters.size(); ++i) {
        param.append(this.parameters.get(i));
        if (i < this.parameters.size() - 1) {
          param.append(',');
        }
      }
    return param.toString();
  }

  /**
   * @param parameters the parameters to set
   */
  public void setParameters(List<String> parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the parameters
   */
  public List<String> getParameters() {
    return parameters;
  }
}
