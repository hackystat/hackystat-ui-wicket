package org.hackystat.projectbrowser.page.trajectory.dtw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the DTW utilities.
 * 
 * @author Pavel Senin
 * 
 */
public class TestDTWUtilFactory {

  private static double[] series1 = { 0.08333333333333333, 2.3333333333333335, 4.083333333333333,
      4.166666666666667, 7.633333333, 11.10666667, 14.583333333333334, 15.333333333333334,
      13.583333333333334, 13.583333333333334, 13.5, 11.3, 9.1, 6.916666666666667,
      7.416666666666667, 7.166666666666667, 7.333333333333333, 8.083333333333334, 8.1, 8.12, 8.14,
      8.166666666666666, 6.833333333333333, 9.25, 6.666666666666667, 5.5, 4.4, 3.3, 2.2, 1.1, 0.0 };

  private static double[] series1Normal = { -0.422452038, -0.292359192, -0.191175867, -0.186357614,
      0.014081735, 0.214906543, 0.415924082, 0.459288364, 0.358105039, 0.358105039, 0.353286785,
      0.226084891, 0.098882997, -0.027355246, 0.001554275, -0.012900485, -0.003263978, 0.040100304,
      0.041063955, 0.042220335, 0.043376716, 0.044918557, -0.032173500, 0.107555854, -0.041810007,
      -0.109265557, -0.172866504, -0.236467451, -0.300068398, -0.363669345, -0.427270292 };

  private static double[] series2 = { 0.0, 1.29, 2.5833333333333335, 3.8333333333333335, 5.25,
      4.25, 3.8333333333333335, 4.636666667, 5.443333333, 6.25, 8.75, 8.833333333333334, 8.25,
      7.75, 9.803333333, 11.85666667, 13.916666666666666, 15.166666666666666, 14.0,
      12.333333333333334, 14.416666666666666, 12.24333333, 10.07666667, 7.916666666666667,
      8.166666666666666, 7.833333333333333, 8.166666666666666, 7.416666666666667, 4.944444444,
      2.472222222, 0.0 };
  private static double[] series2Normal = { -0.418689489, -0.346421051, -0.273965873, -0.203938316,
      -0.124573752, -0.180595797, -0.203938316, -0.158933940, -0.113742823, -0.068551707,
      0.071503406, 0.076171910, 0.043492383, 0.015481361, 0.130513294, 0.245545227, 0.360950640,
      0.430978196, 0.365619143, 0.272249068, 0.388961662, 0.267207084, 0.145825986, 0.024818368,
      0.038823880, 0.020149865, 0.038823880, -0.003192654, -0.141691599, -0.280190544, 
      -0.418689489 };

  /**
   * Set up the test environment.
   */
  @Before
  public void before() {
    int size = series1.length;
    double[] xAxis = new double[size];
    for (int i = 0; i < size; i++) {
      xAxis[i] = ((Integer) i).doubleValue();
    }
    try {
      double[][] s1 = DTWUtilFactory.rBind(xAxis, series1);
      double[][] s2 = DTWUtilFactory.rBind(xAxis, series2);
      assertEquals("Testing rbind", s2[2][0], xAxis[2], 0.000001D);
      assertEquals("Testing rbind", s1[3][0], xAxis[3], 0.000001D);
    }
    catch (DTWException e) {
      fail("Was unable to bind valid data.");
      e.printStackTrace();
    }
  }

  /**
   * Test the mean calculation.
   */
  @Test
  public void testMean() {
    assertEquals("Testing mean calculation", 7.389785D, DTWUtilFactory.mean(series1), 0.000001D);
    assertEquals("Testing mean calculation", 7.473656D, DTWUtilFactory.mean(series2), 0.000001D);
  }

  /**
   * Test the variance calculation.
   */
  @Test
  public void testVariance() {
    double[] t1 = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    assertEquals("Testing variance calculation", 9.166667D, DTWUtilFactory.var(t1), 0.00001D);
    assertEquals("Testing variance calculation", 17.29534D, DTWUtilFactory.var(series1), 0.00001D);
    assertEquals("Testing variance calculation", 17.85012D, DTWUtilFactory.var(series2), 0.00001D);
  }

  /**
   * Test the time-series normalization.
   */
  @Test
  public void testNormalize() {
    double[] t1 = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    double[] t1Normal = { -0.49090909, -0.38181818, -0.27272727, -0.16363636, -0.05454545,
        0.05454545, 0.16363636, 0.27272727, 0.38181818, 0.49090909 };
    double[] t1test = DTWUtilFactory.normalize(t1);
    for (int i = 0; i < t1.length; i++) {
      assertEquals("Testing normalization", t1Normal[i], t1test[i], 0.00001D);
    }

    double[] series1test = DTWUtilFactory.normalize(series1);
    for (int i = 0; i < series1.length; i++) {
      assertEquals("Testing normalization", series1Normal[i], series1test[i], 0.00001D);
    }

    double[] series2test = DTWUtilFactory.normalize(series2);
    for (int i = 0; i < series2.length; i++) {
      assertEquals("Testing normalization", series2Normal[i], series2test[i], 0.00001D);
    }
  }

  // public void testGetSymmetricStep(){
  //    
  // }
}
