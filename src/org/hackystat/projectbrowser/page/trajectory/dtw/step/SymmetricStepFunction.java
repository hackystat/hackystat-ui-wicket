package org.hackystat.projectbrowser.page.trajectory.dtw.step;

import java.awt.Point;

import org.hackystat.projectbrowser.page.trajectory.dtw.constraint.AbstractConstraintFunction;

/**
 * Implements symmetric cost function.
 * 
 * @author Pavel Senin.
 * 
 */
public class SymmetricStepFunction extends AbstractStepFunction {

  private String stepPattern;

  /** Step pattern p0 according to Sakoe-Chiba. */
  public static final String STEP_PATTERN_P0 = "P0";

  /** Step pattern p05 according to Sakoe-Chiba. */
  public static final String STEP_PATTERN_P05 = "P05";

  /** Step pattern p1 according to Sakoe-Chiba. */
  public static final String STEP_PATTERN_P1 = "P1";

  /** Step pattern p2 according to Sakoe-Chiba. */
  public static final String STEP_PATTERN_P2 = "P2";

  /**
   * Constructor.
   * 
   * @param stepPattern specifies the step pattern.
   */
  public SymmetricStepFunction(String stepPattern) {
    this.stepPattern = stepPattern;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Point doStep(Point position, double[][] distanceMatrix,
      AbstractConstraintFunction constraints) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double[][] getCostMatrix(double[][] distanceMatrix) {

    int rows = distanceMatrix.length;
    int columns = distanceMatrix[0].length;

    double[][] costMatrix = new double[rows][columns];

    // [2.1] starting point
    costMatrix[0][0] = distanceMatrix[0][0];

    // [2.2.1] first column
    for (int i = 1; i < rows; i++) {
      costMatrix[i][0] = distanceMatrix[i][0] + costMatrix[i - 1][0];
    }

    // [2.2.2] first row
    for (int j = 1; j < columns; j++) {
      costMatrix[0][j] = distanceMatrix[0][j] + costMatrix[0][j - 1];
    }

    if (STEP_PATTERN_P0.equalsIgnoreCase(this.stepPattern)) {
      // fill the cost matrix here
      for (int j = 1; j < rows; j++) {
        for (int i = 1; i < columns; i++) {
          double[] options = { 2 * costMatrix[i - 1][j - 1], costMatrix[i - 1][j], 
                               costMatrix[i][j - 1] };
          double minDistance = Math.min(options[0], Math.min(options[1], options[2]));
          costMatrix[i][j] = distanceMatrix[i][j] + minDistance;
        }
      }

    }
    else if (STEP_PATTERN_P05.equalsIgnoreCase(this.stepPattern)) {
      assert true;
    }
    else if (STEP_PATTERN_P1.equalsIgnoreCase(this.stepPattern)) {
      assert true;
    }
    else if (STEP_PATTERN_P2.equalsIgnoreCase(this.stepPattern)) {
      assert true;
    }

    return costMatrix;
  }
}
