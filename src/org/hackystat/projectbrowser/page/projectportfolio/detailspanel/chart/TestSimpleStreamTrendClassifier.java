package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Test cases for SimpleStreamTrendClassifier
 * @author Shaoxuan Zhang
 *
 */
public class TestSimpleStreamTrendClassifier {
  /** the SimpleStreamTrendClassifier. */
  private SimpleStreamTrendClassifier streamTrendClassifier = new SimpleStreamTrendClassifier();
  
  /**
   * Test with a monotonous increase trend.
   */
  @Test
  public void testIncreaseTrend() {
    List<Double> trend = Arrays.asList(new Double[]{55.0, 65.0, 66.5, 78.8, 79.0, 89.0});
    assertEquals("", StreamTrend.INCREASING, streamTrendClassifier.getStreamTrend(trend));
  }

  /**
   * Test with a monotonous decrease trend.
   */
  @Test
  public void testDecreaseTrend() {
    List<Double> trend = Arrays.asList(new Double[]{89.0, 79.0, 78.8, 66.5, 65.0, 55.0});
    assertEquals("", StreamTrend.DECREASING, streamTrendClassifier.getStreamTrend(trend));
  }

  /**
   * Test with a absolute flat stream.
   */
  @Test
  public void testStableTrend() {
    List<Double> trend = Arrays.asList(new Double[]{65.0, 65.0, 65.0, 65.0, 65.0, 65.0});
    assertEquals("", StreamTrend.STABLE, streamTrendClassifier.getStreamTrend(trend));
  }

  /**
   * Test with an unstable stream.
   */
  @Test
  public void testUnstableTrend() {
    List<Double> trend = Arrays.asList(new Double[]{67.0, 55.0, 66.5, 89.0, 76.0, 78.8});
    assertEquals("", StreamTrend.OTHER, streamTrendClassifier.getStreamTrend(trend));
  }
}
