package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Test cases for EnhancedStreamTrendClassifier.
 * @author Shaoxuan Zhang
 *
 */
public class TestEnhancedStreamTrendClassifier {
  /** the EnhancedStreamTrendClassifier. */
  private StreamTrendClassifier streamTrendClassifier = new StreamTrendClassifier();
  
  /**
   * Test with a increasing stream with acceptable decreasing point.<p>
   * example chart: 
   * <img src="http://chart.apis.google.com/chart?chs=500x200&chd=t:55.0,67.0,66.5,78.8,76.0,89.0&
   * chds=0.0,100.0&cht=lc&chls=2.0,1.0,0.0&chxt=y,x&chxr=0,0.0,100.0&chm=c,000000,0,-1.0,10.0"/>
   */
  @Test
  public void testIncreaseTrend() {
    List<Double> trend = Arrays.asList(new Double[]{55.0, 67.0, 66.5, 78.8, 76.0, 89.0});
    MiniBarChart chart = new MiniBarChart(null, null);
    chart.streamData = trend;
    assertEquals("", StreamCategory.INCREASING, streamTrendClassifier.getStreamCategory(chart));
  }

  /**
   * Test with a decreasing stream with acceptable increasing point. <p>
   * example chart: 
   * <img src="http://chart.apis.google.com/chart?chs=500x200&chd=t:89.0,76.0,78.8,66.5,67.0,55.0&
   * chds=0.0,100.0&cht=lc&chls=2.0,1.0,0.0&chxt=y,x&chxr=0,0.0,100.0&chm=c,000000,0,-1.0,10.0"/>
   */
  @Test
  public void testDecreaseTrend() {
    List<Double> trend = Arrays.asList(new Double[]{89.0, 76.0, 78.8, 66.5, 67.0, 55.0});
    MiniBarChart chart = new MiniBarChart(null, null);
    chart.streamData = trend;
    assertEquals("", StreamCategory.DECREASING, streamTrendClassifier.getStreamCategory(chart));
  }

  /**
   * Test with a stable stream with acceptable vibration point.<p>
   * example chart: 
   * <img src="http://chart.apis.google.com/chart?chs=500x200&chd=t:65.0,67.0,66.5,68.8,66.0,69.0&
   * chds=0.0,100.0&cht=lc&chls=2.0,1.0,0.0&chxt=y,x&chxr=0,0.0,100.0&chm=c,000000,0,-1.0,10.0"/>
   */
  @Test
  public void testStableTrend() {
    List<Double> trend = Arrays.asList(new Double[]{65.0, 67.0, 66.5, 68.8, 66.0, 69.0});
    MiniBarChart chart = new MiniBarChart(null, null);
    chart.streamData = trend;
    assertEquals("", StreamCategory.STABLE, streamTrendClassifier.getStreamCategory(chart));
  }

  /**
   * Test with an unstable stream.<p>
   * example chart: 
   * <img src="http://chart.apis.google.com/chart?chs=500x200&chd=t:67.0,55.0,66.5,89.0,76.0,78.8&
   * chds=0.0,100.0&cht=lc&chls=2.0,1.0,0.0&chxt=y,x&chxr=0,0.0,100.0&chm=c,000000,0,-1.0,10.0"/>
   */
  @Test
  public void testUnstableTrend() {
    List<Double> trend = Arrays.asList(new Double[]{67.0, 55.0, 66.5, 89.0, 76.0, 78.8});
    MiniBarChart chart = new MiniBarChart(null, null);
    chart.streamData = trend;
    assertEquals("", StreamCategory.OTHER, streamTrendClassifier.getStreamCategory(chart));
  }
}
