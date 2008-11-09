package org.hackystat.projectbrowser.page.trajectory.dtw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hackystat.projectbrowser.page.trajectory.dtw.step.SymmetricStepFunction;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the basic DTW recursion.
 *
 * @author Pavel Senin.
 *
 */
public class TestDTWFactory {

  private static final double[][] query = { { 0.00 }, { 0.22 }, { 0.22 }, { 0.28 }, { 0.00 },
      { 0.00 }, { 0.83 }, { 0.93 }, { 1.00 }, { 0.81 }, { 0.81 }, { 0.00 }, { 0.00 }, { 0.26 },
      { 0.22 } };

  private static final double[][] template = { { 0.00 }, { 0.00 }, { 0.50 }, { 0.26 }, { 0.31 },
      { 0.24 }, { 0.30 }, { 0.00 }, { 0.00 }, { 0.30 }, { 0.26 }, { 0.24 }, { 0.33 }, { 0.28 },
      { 0.00 } };

  /**
   * Set up and some visual info.
   */
  @Before
  public void runBefore() {
    assert true;
  }

  /**
   * Tests euclidean distance.
   *
   * @throws DTWException if error occures.
   */
  @Test
  public void testEuclideanDistance() throws DTWException {
    Double dist = EuclideanDistance.getSeriesDistnace(query, template);
    assertEquals("testing distance, ", 1.790140, dist, 0.01D);
  }

  /**
   * Tests euclidean distance.
   *
   * @throws DTWException if error occures.
   */
  @Test
  public void testBasicDTW() throws DTWException {
    DTWAlignment r = DTWFactory.doDTW(query, template, SymmetricStepFunction.STEP_PATTERN_P0);
    assertNotNull("Testing DTW", r);
    // 
    // System.out.println(r);
  }

}