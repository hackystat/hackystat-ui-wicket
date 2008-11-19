package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import java.io.Serializable;
import java.util.List;

/**
 * A simple stream trend classifier.
 * Classify stream trend into 4 classes: increasing, decreasing, stable and unstable(other).
 * Monotonous increase line will be considered as increasing.
 * Monotonous decrease line will be considered as decreasing.
 * Exactly flat horizontal line will be considered as stable.
 * Others will be considered as unstable(other).
 * Note: a flat line with a little difference in the middle will be consider as unstable(other).
 * @author Shaoxuan Zhang
 *
 */
public class SimpleStreamClassifier implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 2787331259250397902L;

  /**
   * Parse the given MiniBarChart and produce a StreamCategory result.
   * @param chart the input chart
   * @return StreamCategory enumeration. 
   */
  public StreamCategory getStreamCategory(MiniBarChart chart) {
    boolean increased = false;
    boolean decreased = false;
    List<Double> stream = chart.streamData;
    double oldValue = stream.get(0);
    for (int i = 1; i < stream.size(); ++i) {
      Double newValue = stream.get(i);
      if (!newValue.isNaN() && newValue >= 0) {
        if (oldValue > newValue) {
          decreased = true;
        }
        else if (oldValue < newValue) {
          increased = true;
        }
        oldValue = newValue;
      }
    }
    if (!increased && !decreased) {
      return StreamCategory.STABLE;
    }
    if (increased && !decreased) {
      return StreamCategory.INCREASING;
    }
    if (decreased && !increased) {
      return StreamCategory.DECREASING;
    }
    return StreamCategory.OTHER;
  }
}
