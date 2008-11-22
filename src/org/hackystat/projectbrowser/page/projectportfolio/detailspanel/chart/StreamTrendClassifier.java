package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import java.io.Serializable;
import java.util.List;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure.
        StreamTrendParameters;;
/**
 * Classify stream trend into 4 classes: increasing, decreasing, stable and unstable(other).
 * Monotonous increase line will be considered as increasing.
 * Monotonous decrease line will be considered as decreasing.
 * Exactly flat horizontal line will be considered as stable.
 * Others will be considered as unstable(other).
 * Note: a flat line with a little difference in the middle will be consider as unstable(other).
 * @author Shaoxuan Zhang
 *
 */
public class StreamTrendClassifier implements Serializable, StreamClassifier {

  /** Support serialization. */
  private static final long serialVersionUID = 5078158281421083678L;
  /** Name of this classifier. */
  public static final String name = "StreamTrend";
  /** Acceptable error in percentage. */
  private double e = 0.1;
  /** The minimal error in absolute value. */
  private static final double MINI_ERROR = 0.1;

  /** If higher value means better. */
  private boolean higherBetter;
  
  /** The threshold of high value. */
  private double higherThreshold;
  /** The threshold of low value. */
  private double lowerThreshold;
  
  /**
   * @param lowerThreshold the {@link lowerThreshold} to set.
   * @param higherThreshold the {@link higherThreshold} to set.
   * @param higherBetter the {@link higherBetter} to set.
   */
  public StreamTrendClassifier(double lowerThreshold, double higherThreshold,
      boolean higherBetter) {
    this.higherBetter = higherBetter;
    this.higherThreshold = higherThreshold;
    this.lowerThreshold = lowerThreshold;
  }
  
  /**
   * Parse the given MiniBarChart and produce a StreamCategory result.
   * @param chart the input chart
   * @return StreamCategory enumeration. 
   */
  public PortfolioCategory getStreamCategory(MiniBarChart chart) {
    List<Double> stream = chart.streamData;
    for (int i = 0; i < stream.size(); i++) {
      if (stream.get(i).isNaN() || stream.get(i) < 0) {
        stream.remove(i);
        i--;
      }
    }
    int size = stream.size();
    if (size == 1) {
      return PortfolioCategory.GOOD;
    }
    // the first valid value
    double firstValue = stream.get(0);
    // the last valid value
    double lastValue = stream.get(size - 1);
    // the acceptable error
    double error = (firstValue + lastValue) / 2 * e;
    if (error < MINI_ERROR) {
      error = MINI_ERROR;
    }

    //if it is stable trend with acceptable vibration, it will be stable.
    int increasePoint = 0;
    int decreasePoint = 0;
    for (int i = 1; i < size; ++i) {
      if (!isEqual(stream.get(i), stream.get(i - 1), error)) {
        if (stream.get(i) < stream.get(i - 1)) {
          decreasePoint++;
        }
        else if (stream.get(i) > stream.get(i - 1)) {
          increasePoint++;
        }
      }
    }
    if (isEqual(lastValue, firstValue, error) && decreasePoint == 0 && increasePoint == 0) {
      return PortfolioCategory.GOOD;
    }
    if (lastValue > firstValue && decreasePoint == 0) {
      return getHighCategory();
    }
    if (lastValue < firstValue && increasePoint == 0) {
      return getLowCategory();
    }
    return PortfolioCategory.AVERAGE;
  }
  
  /**
   * Return the category of higher values.
   * @return the PortfolioCategory.
   */
  private PortfolioCategory getHighCategory() {
    if (this.higherBetter) {
      return PortfolioCategory.GOOD;
    }
    return PortfolioCategory.POOR;
  }

  /**
   * Return the category of lower values.
   * @return the PortfolioCategory.
   */
  private PortfolioCategory getLowCategory() {
    if (this.higherBetter) {
      return PortfolioCategory.POOR;
    }
    return PortfolioCategory.GOOD;
  }
  
  /**
   * Tests if the two given value are equal.
   * @param a the first value.
   * @param b the second value.
   * @param error the acceptable error.
   * @return true if their difference is within acceptable error, otherwise false.
   */
  private boolean isEqual(double a, double b, double error) {
    if (Math.abs(a - b) > error) {
      return false;
    }
    return true;
  }

  /**
   * Return the panel for users to configure this stream trend classifer.
   * User can customize higher, lower thresholds and if higher is better.
   * @param id The Wicket component id.
   * @return a Panel
   */
  public Panel getConfigurationPanel(String id) {
    return new StreamTrendClassifierConfigurationPanel(id, this);
  }

  /**
   * Return the category of the given value.
   * If value is higher than {@link higherThreshold}, {@link getHighCategory()} will be returned.
   * If value is higher than {@link lowerThreshold}, {@link getLowCategory()} will be returned.
   * Otherwise, will return AVERAGE.
   * @param value the given value.
   * @return a {@link PortfolioCategory} result
   */
  public PortfolioCategory getValueCategory(double value) {
    if (value < 0) {
      return PortfolioCategory.NA;
    }
    if (value >= this.higherThreshold) {
      return getHighCategory();
    }
    if (value < this.lowerThreshold) {
      return getLowCategory();
    }
    return PortfolioCategory.AVERAGE;
    
  }

  /**
   * @param higherBetter the higherBetter to set
   */
  public void setHigherBetter(boolean higherBetter) {
    this.higherBetter = higherBetter;
  }

  /**
   * @return the higherBetter
   */
  public boolean isHigherBetter() {
    return higherBetter;
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
   * @return the name of this classifier.
   */
  public String getName() {
    return name;
  }

  /**
   * Save classifier's setting into the given {@link Measure} instance.
   * @param measure the given {@link Measure} instance
   */
  public void saveSetting(Measure measure) {
    StreamTrendParameters param = new StreamTrendParameters();
    param.setHigherBetter(higherBetter);
    param.setLowerThresold(lowerThreshold);
    param.setHigherThresold(higherThreshold);
    measure.setStreamTrendParameters(param);
  }
}
