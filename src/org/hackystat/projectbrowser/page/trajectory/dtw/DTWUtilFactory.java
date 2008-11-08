package org.hackystat.projectbrowser.page.trajectory.dtw;

/**
 * Provide implementation of various computational methods for the DTW.
 * 
 * @author Pavel Senin
 * 
 */
public class DTWUtilFactory {

  /**
   * Binds two linear arrays, producing a 2D array.
   * 
   * @param axis The first component array.
   * @param seriesData The second component array.
   * @return Binded rows.
   * @throws DTWException If error occures.
   */
  public static double[][] rBind(double[] axis, double[] seriesData) throws DTWException {
    if (axis.length == seriesData.length) {
      int len = axis.length;
      double[][] res = new double[len][2];
      for (int i = 0; i < len; i++) {
        res[i][0] = axis[i];
        res[i][1] = seriesData[i];
      }
      return res;
    }
    throw new DTWException("Error while binding rows: uneven arrays provided.");
  }

  /**
   * Compute the mean of the timeseries.
   * 
   * @param series The input timeseries.
   * @return the mean of values.
   */
  public static double mean(double[] series) {
    double res = 0D;
    for (int i = 0; i < series.length; i++) {
      res += series[i];
    }
    return res / ((Integer) series.length).doubleValue();
  }

  /**
   * Compute the variance of the timeseries.
   * 
   * @param series The input timeseries.
   * @return the variance of values.
   */
  public static double var(double[] series) {
    double res = 0D;
    double mean = mean(series);
    for (int i = 0; i < series.length; i++) {
      res += (series[i] - mean) * (series[i] - mean);
    }
    return res / ((Integer) (series.length - 1)).doubleValue();
  }

  /**
   * Normalize timeseries.
   * 
   * @param series The input timeseries.
   * @return the normalized time-series.
   */
  public static double[] normalize(double[] series) {
    double[] res = new double[series.length];
    double mean = mean(series);
    double var = var(series);
    for (int i = 0; i < res.length; i++) {
      res[i] = (series[i] - mean) / var;
    }
    return res;
  }

}
