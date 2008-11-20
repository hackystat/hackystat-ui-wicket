package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

/**
 * Interface of stream classifier.
 * 
 * @author Shaoxuan Zhang
 *
 */
public interface StreamClassifier {

  /**
   * Parse the given MiniBarChart and produce a StreamCategory result.
   * @param chart the input chart
   * @return StreamCategory enumeration. 
   */
  public abstract StreamCategory getStreamCategory(MiniBarChart chart);
}
