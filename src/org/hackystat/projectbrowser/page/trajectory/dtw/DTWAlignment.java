package org.hackystat.projectbrowser.page.trajectory.dtw;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.hackystat.projectbrowser.page.trajectory.dtw.step.AbstractStepFunction;
import org.hackystat.projectbrowser.page.trajectory.dtw.step.SymmetricStepFunction;

/**
 * Envelope for the DTW transform.
 * 
 * @author Pavel Senin.
 * 
 */
public class DTWAlignment {

  private static final String POINT_DL = " ";
  private static final String CR = "\n";
  private static final String SPACE = "     ";
  private double[][] query;
  private double[][] template;
  private double[][] distances;
  private double[][] costMatrix;
  private double postDTWDistance;
  private List<Point> path;

  NumberFormat decFormatter = new DecimalFormat("####0.00");
  NumberFormat warpFormatter = new DecimalFormat("##0.0");
  NumberFormat pathFormatter = new DecimalFormat("##0");
  private double[] template2query;
  private double[] query2template;
  private double[] template2queryCoords;
  private double[] query2templateCoords;
  private AbstractStepFunction stepFunction;

  // private List<Point> alignmentPath;

  /**
   * Set the query time-series.
   * 
   * @param query The query time series.
   * @throws DTWException if error occures.
   */
  public void setQuery(double[][] query) throws DTWException {
    if (null == query) {
      throw new DTWException("Attempt to set null as the query vector.");
    }
    else {
      this.query = new double[query.length][query[0].length];
      for (int i = 0; i < query.length; i++) {
        for (int j = 0; j < query[0].length; j++) {
          this.query[i][j] = query[i][j];
        }
      }
    }
  }

  /**
   * Set the template time-series.
   * 
   * @param template The template time series.
   * @throws DTWException if error occures.
   */
  public void setTemplate(double[][] template) throws DTWException {
    if (null == template) {
      throw new DTWException("Attempt to set null as the query vector.");
    }
    else {
      this.template = new double[template.length][template[0].length];
      for (int i = 0; i < template.length; i++) {
        for (int j = 0; j < template[0].length; j++) {
          this.template[i][j] = template[i][j];
        }
      }
    }
  }

  /**
   * Set the step function.
   * 
   * @param stepPattern the selected step function.
   * @throws DTWException if unable to select a function (invalid name provided).
   */
  public void setStepFunction(String stepPattern) throws DTWException {
    if (SymmetricStepFunction.STEP_PATTERN_P0.equalsIgnoreCase(stepPattern)
        || SymmetricStepFunction.STEP_PATTERN_P05.equalsIgnoreCase(stepPattern)
        || SymmetricStepFunction.STEP_PATTERN_P1.equalsIgnoreCase(stepPattern)
        || SymmetricStepFunction.STEP_PATTERN_P2.equalsIgnoreCase(stepPattern)) {
      this.stepFunction = new SymmetricStepFunction(stepPattern);
    }
    else {
      throw new DTWException("Unable to select the step function: " + stepPattern);
    }
  }

  /**
   * Set the local cost matrix.
   * 
   * @param distances The local cost-matrix.
   * @throws DTWException if error occures.
   */
  public void setDistanceMatrix(double[][] distances) throws DTWException {
    if (null == distances) {
      throw new DTWException("Attempt to set null as the distances matrix.");
    }
    else {
      this.distances = new double[distances.length][distances[0].length];
      for (int i = 0; i < distances.length; i++) {
        for (int j = 0; j < distances[0].length; j++) {
          this.distances[i][j] = distances[i][j];
        }
      }
    }
  }

  /**
   * Set the DTW computed path.
   * 
   * @param path The path.
   */
  private void setPath(List<Point> path) {
    this.path = path;
    // calculate the warping template queries
    this.template2query = new double[template.length];
    for (int i = 0; i < this.template.length; i++) {
      // check for points
      int sum = 0;
      int count = 0;
      for (Point p : path) {
        if (p.x == i) {
          sum = sum + p.y;
          count++;
        }
        if (sum == 0) {
          template2query[i] = 0;
        }
        else {
          template2query[i] = Integer.valueOf(sum).doubleValue()
              / Integer.valueOf(count).doubleValue();
        }

      }
    }
    // get coordinates for this
    this.template2queryCoords = new double[template2query.length];
    for (int i = 0; i < template2query.length; i++) {
      double idx = template2query[i];
      if (Math.floor(idx) == idx) {
        this.template2queryCoords[i] = template[Double.valueOf(template2query[i]).intValue()][0];
      }
      else {
        int idxBelow = Double.valueOf(Math.floor(idx)).intValue();
        int idxAbove = Double.valueOf(Math.ceil(idx)).intValue();
        double vBelow = template[idxBelow][0];
        double vAbove = template[idxAbove][0];
        this.template2queryCoords[i] = vBelow + (vAbove - vBelow) * Math.abs(idx - Math.floor(idx));
      }
    }

    // calculate the warping template queries
    this.query2template = new double[query.length];
    for (int i = 0; i < this.query.length; i++) {
      // check for points
      int sum = 0;
      int count = 0;
      for (Point p : path) {
        if (p.y == i) {
          sum = sum + p.x;
          count++;
        }
        if (sum == 0) {
          query2template[i] = 0;
        }
        else {
          query2template[i] = Integer.valueOf(sum).doubleValue()
              / Integer.valueOf(count).doubleValue();
        }
      }
    }
    // get coordinates for this
    this.query2templateCoords = new double[query2template.length];
    for (int i = 0; i < query2template.length; i++) {
      double idx = query2template[i];
      if (Math.floor(idx) == idx) {
        this.query2templateCoords[i] = query[Double.valueOf(query2template[i]).intValue()][0];
      }
      else {
        int idxBelow = Double.valueOf(Math.floor(idx)).intValue();
        int idxAbove = Double.valueOf(Math.ceil(idx)).intValue();
        double vBelow = query[idxBelow][0];
        double vAbove = query[idxAbove][0];
        this.query2templateCoords[i] = vBelow + (vAbove - vBelow) * Math.abs(idx - Math.floor(idx));
      }
    }

  }

  /**
   * Performs the actual alignment.
   */
  public void doAlignment() {

    // [1.0] using DP build the COST matrix and the best path
    this.costMatrix = this.stepFunction.getCostMatrix(this.distances);

    // [2.0] get directions matrix
    // TODO: directions

    // [3.0] do the optimal alignment tracing path backward
    List<Point> path = new ArrayList<Point>();
    int i = query.length - 1;
    int j = template.length - 1;
    int k = 1;
    double distance = this.costMatrix[i][j];

    // the optimal path
    path.add(new Point(i, j));
    while (i + j > 0) {
      // if we hit the border -> go along the border
      if (i == 0) {
        j--;
      }
      else if (j == 0) {
        i--;
      }
      else {
        // or if we still have some space for search
        double[] options = { this.costMatrix[i - 1][j - 1], this.costMatrix[i - 1][j],
            this.costMatrix[i][j - 1] };
        double min = Math.min(options[0], Math.min(options[1], options[2]));

        if (min == options[0]) {
          i--;
          j--;
        }
        else if (min == options[1]) {
          i--;
        }
        else {
          j--;
        }
      }
      k++;
      distance += this.costMatrix[i][j];
      path.add(new Point(i, j));
    }

    this.postDTWDistance = distance;

    this.setPath(path);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer sb = new StringBuffer(1000);

    sb.append("$template:" + CR);
    for (int i = 0; i < template.length; i++) {
      double[] point = template[i];
      for (int j = 0; j < point.length; j++) {
        String fp = decFormatter.format(point[j]);
        sb.append(SPACE.substring(fp.length()) + fp);
      }
      sb.append(POINT_DL);
    }
    sb.append(CR);

    sb.append("$query:" + CR);
    for (int i = 0; i < query.length; i++) {
      double[] point = query[i];
      for (int j = 0; j < point.length; j++) {
        String fp = decFormatter.format(point[j]);
        sb.append(SPACE.substring(fp.length()) + fp);
      }
      sb.append(POINT_DL);
    }
    sb.append(CR);

    sb.append("$distance matrix:" + CR);
    for (int i = 0; i < distances.length; i++) {
      for (int j = 0; j < distances[i].length; j++) {
        sb.append(decFormatter.format(distances[i][j]) + POINT_DL);
      }
      sb.append(CR);
    }

    sb.append("$cumulative distance matrix:" + CR);
    for (int i = 0; i < costMatrix.length; i++) {
      for (int j = 0; j < costMatrix[i].length; j++) {
        sb.append(decFormatter.format(costMatrix[i][j]) + POINT_DL);
      }
      sb.append(CR);
    }

    sb.append("$indeces:" + CR);
    for (int i = 0; i < path.size(); i++) {
      String fn = pathFormatter.format(path.get(i).x);
      sb.append(SPACE.substring(fn.length()) + fn);
    }
    sb.append(CR);
    for (int i = 0; i < path.size(); i++) {
      String fn = pathFormatter.format(path.get(i).y);
      sb.append(SPACE.substring(fn.length()) + fn);
    }
    sb.append(CR);

    sb.append("$warping query, template=TRUE" + CR);
    for (int i = 0; i < this.template2query.length; i++) {
      String fn = warpFormatter.format(template2query[i]);
      sb.append(SPACE.substring(fn.length()) + fn + " ");
    }
    sb.append(CR);
    sb.append("$warping query, template=TRUE, coords" + CR);
    for (int i = 0; i < this.template2queryCoords.length; i++) {
      String fn = decFormatter.format(template2queryCoords[i]);
      sb.append(SPACE.substring(fn.length()) + fn + " ");
    }
    sb.append(CR);

    sb.append("$warping query, template=FALSE" + CR);
    for (int i = 0; i < this.query2template.length; i++) {
      String fn = warpFormatter.format(query2template[i]);
      sb.append(SPACE.substring(fn.length()) + fn + " ");
    }
    sb.append(CR);
    sb.append("$warping query, template=FALSE, coords" + CR);
    for (int i = 0; i < this.query2templateCoords.length; i++) {
      String fn = decFormatter.format(query2templateCoords[i]);
      sb.append(SPACE.substring(fn.length()) + fn + " ");
    }
    sb.append(CR);

    return sb.toString();
  }

  /**
   * Get the DTW path.
   * 
   * @return The DTW path.
   */
  public List<Point> getPath() {
    return this.path;
  }

  /**
   * Get the warping query.
   * 
   * @return The warping query.
   */
  public double[] getWarpingQuery() {
    return this.query2templateCoords.clone();
  }

  /**
   * Get the post-alignment distance.
   * 
   * @return the post-alignment distance.
   */
  public double getPostDistnace() {
    return this.postDTWDistance;
  }

}
