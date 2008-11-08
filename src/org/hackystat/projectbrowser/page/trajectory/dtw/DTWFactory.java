package org.hackystat.projectbrowser.page.trajectory.dtw;

/**
 * Does DTW alignment.
 * 
 * @author Pavel Senin.
 * 
 */
public class DTWFactory {

  /**
   * Performs the alignment of two timeseries.
   * 
   * @param query the query series.
   * @param template the template series.
   * @param stepPattern the step pattern to use.
   * @return the dtw alignment of the series.
   * 
   * @throws DTWException if error occures.
   */
  public static DTWAlignment doDTW(double[][] query, double[][] template, String stepPattern)
      throws DTWException {

    // get a new record and memorize series
    DTWAlignment res = new DTWAlignment();
    res.setQuery(query);
    res.setTemplate(template);
    res.setStepFunction(stepPattern);

    // [1.0] get the matrix of the global point-to-point distances.
    //
    double[][] localCostMatrix = new double[query.length][template.length];
    for (int i = 0; i < query.length; i++) {
      for (int j = 0; j < template.length; j++) {
        localCostMatrix[i][j] = EuclideanDistance.pointDistance(query[i], template[j]);
      }
    }
    res.setDistanceMatrix(localCostMatrix);

    res.doAlignment();

    return res;
  }

}
