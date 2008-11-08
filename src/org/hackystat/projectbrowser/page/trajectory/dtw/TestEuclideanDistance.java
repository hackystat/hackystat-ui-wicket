package org.hackystat.projectbrowser.page.trajectory.dtw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the Euclidean distance.
 *
 * @author Pavel Senin.
 *
 */
public class TestEuclideanDistance {

  // 1D points for the test
  private static final double[] testPoint1D1 = { 0.545 };
  private static final double[] testPoint1D2 = { 0.845 };

  // 3D points for the test
  private static final double[] testPoint3D1 = { 0.545, 0.856, 0.856 };
  private static final double[] testPoint3D2 = { 0.845, 0.654, 0.986 };

  // 2d series for the test
  private static final double[][] testSeries1 = { { 0.2, 0.6 }, { 0.3, 0.5 }, { 0.4, 0.4 },
      { 0.5, 0.3 }, { 0.6, 0.2 } };
  private static final double[][] testSeries2 = { { 1.0, 1.8 }, { 1.2, 1.6 }, { 1.4, 1.4 },
      { 1.6, 1.2 }, { 1.8, 1.0 } };

  /**
   * Test the distance between single points.
   *
   * @throws DTWException If error occures.
   */
  @Test
  public void testPointDistance() throws DTWException {
    assertEquals("test 1D distance", EuclideanDistance.pointDistance(testPoint1D1, testPoint1D2),
        Math.abs(testPoint1D2[0] - testPoint1D1[0]), 0.01D);

    double dist = 0D;
    for (int i = 0; i < 3; i++) {
      dist += (testPoint3D1[i] - testPoint3D2[i]) * (testPoint3D1[i] - testPoint3D2[i]);
    }
    dist = Math.sqrt(dist);

    assertEquals("test 1D distance", EuclideanDistance.pointDistance(testPoint3D1, testPoint3D2),
        dist,0.01D);
  }

  /**
   * Test the distance between two series.
   *
   * @throws DTWException If error occures.
   */
  @Test
  public void testSeriesDistance() throws DTWException {

    Double dist = EuclideanDistance.getSeriesDistnace(testSeries1, testSeries2);
     assertEquals("testing distance, ", 3.193743885, dist, 0.01D);
  }

}
