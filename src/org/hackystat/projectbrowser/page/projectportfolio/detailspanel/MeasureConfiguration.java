package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;

/**
 * Configuration for project portfolio measures.
 * @author Shaoxuan Zhang
 *
 */
public class MeasureConfiguration implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = -6799287509742157998L;
  
  /** The threshold of high value. */
  private double higherThreshold;
  /** The threshold of high value. */
  private double lowerThreshold;
  /** If higher value means better. */
  private boolean isHigherTheBetter;
  
  
  /**
   * @param higherThreshold The threshold of high value.
   * @param lowerThreshold The threshold of high value.
   * @param isHigherTheBetter If higher value means better.
   */
  public MeasureConfiguration(double lowerThreshold, double higherThreshold, 
      boolean isHigherTheBetter) {
    this.lowerThreshold = lowerThreshold;
    this.higherThreshold = higherThreshold;
    this.isHigherTheBetter = isHigherTheBetter;
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
   * @param isIncreasingGood the isIncreasingGood to set
   */
  public void setIncreasingGood(boolean isIncreasingGood) {
    this.isHigherTheBetter = isIncreasingGood;
  }

  /**
   * @return the isIncreasingGood
   */
  public boolean isHigherTheBetter() {
    return isHigherTheBetter;
  }
}
