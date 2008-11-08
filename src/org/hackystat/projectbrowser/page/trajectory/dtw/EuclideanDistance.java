package org.hackystat.projectbrowser.page.trajectory.dtw;

/**
 * The Euclidean distance implementation.
 *
 * @author Pavel Senin.
 *
 */
public class EuclideanDistance {

  /**
   * Calculates the square of the Euclidean distance between two points.
   *
   * @param point1 The first point.
   * @param point2 The second point.
   * @return The Euclidean distance.
   * @throws DTWException In the case of error.
   */
  private static Double distance2(double[] point1, double[] point2) throws DTWException {
    if (point1.length == point2.length) {
      Double sum = 0D;
      for (int i = 0; i < point1.length; i++) {
        sum = sum + (point2[i] - point1[i]) * (point2[i] - point1[i]);
      }
      return sum;
    }
    else {
      throw new DTWException("Exception in Euclidean distance: array lengths are not equal");
    }
  }

  /**
   * Calculates the Euclidean distance between two points.
   *
   * @param point1 The first point.
   * @param point2 The second point.
   * @return The Euclidean distance.
   * @throws DTWException In the case of error.
   */
  public static double pointDistance(double[] point1, double[] point2) throws DTWException {
    return Math.sqrt(distance2(point1, point2));
  }

  /**
   * Calculates euclidean distance between two time-series of equal length.
   *
   * @param query The timeseries1.
   * @param template The timeseries2.
   * @return The eclidean distance.
   * @throws DTWException if error occures.
   */
  public static double getSeriesDistnace(double[][] query, double[][] template) 
                                                                          throws DTWException {
    if (query.length == template.length) {
      Double res = 0D;
      for (int i = 0; i < query.length; i++) {
        res = res + distance2(query[i], template[i]);
      }
      return Math.sqrt(res);
    }
    else {
      throw new DTWException("Exception in Euclidean distance: array lengths are not equal");
    }
  }

}
