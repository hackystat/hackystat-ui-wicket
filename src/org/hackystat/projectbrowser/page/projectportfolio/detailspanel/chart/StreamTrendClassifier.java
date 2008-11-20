package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import java.io.Serializable;
import java.util.List;

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
  /** Acceptable error in percentage. */
  private double e;
  /** The minimal error in absolute value. */
  private static final double MINI_ERROR = 0.1;

  /** 
   * Default constructor using e = 10%.
   */
  public StreamTrendClassifier() {
    this(0.1);
  }
  
  /**
   * @param e Acceptable error in percentage.
   */
  public StreamTrendClassifier(double e) {
    this.e = e;
  }
  
  /**
   * Parse the given MiniBarChart and produce a StreamCategory result.
   * @param chart the input chart
   * @return StreamCategory enumeration. 
   */
  public StreamCategory getStreamCategory(MiniBarChart chart) {
    List<Double> stream = chart.streamData;
    for (int i = 0; i < stream.size(); i++) {
      if (stream.get(i).isNaN() || stream.get(i) < 0) {
        stream.remove(i);
        i--;
      }
    }
    int size = stream.size();
    if (size == 1) {
      return StreamCategory.STABLE;
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
      return StreamCategory.STABLE;
    }
    if (lastValue > firstValue && decreasePoint == 0) {
      return StreamCategory.INCREASING;
    }
    if (lastValue < firstValue && increasePoint == 0) {
      return StreamCategory.DECREASING;
    }
    return StreamCategory.OTHER;
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
}
