package org.hackystat.projectbrowser.page.trajectory.dtw.step;

import java.awt.Point;

import org.hackystat.projectbrowser.page.trajectory.dtw.constraint.AbstractConstraintFunction;

/**
 * Implements symmetric cost function.
 * 
 * @author Pavel Senin.
 * 
 */
public class AsymmetricStepFunction extends AbstractStepFunction {

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
  public AsymmetricStepFunction(String stepPattern) {
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
      doCostP0(costMatrix, distanceMatrix);
    }
    else if (STEP_PATTERN_P05.equalsIgnoreCase(this.stepPattern)) {
      doCostP05(costMatrix, distanceMatrix);
    }
    else if (STEP_PATTERN_P1.equalsIgnoreCase(this.stepPattern)) {
      doCostP1(costMatrix, distanceMatrix);
    }
    else if (STEP_PATTERN_P2.equalsIgnoreCase(this.stepPattern)) {
      doCostP2(costMatrix, distanceMatrix);
    }

    return costMatrix;
  }

  /**
   * Fills the cost matrix using the step P2.
   * 
   * @param costMatrix the resulting cost matrix.
   * @param distanceMatrix the initial distance matrix.
   */
  private void doCostP2(double[][] costMatrix, double[][] distanceMatrix) {
    int rows = distanceMatrix.length;
    int columns = distanceMatrix[0].length;
    for (int j = 1; j < rows; j++) {
      for (int i = 1; i < columns; i++) {
        double[] options = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
        if ((i - 1) > 0 && (j - 2) > 0) {
          options[0] = costMatrix[i - 2][j - 3] + 2
              * (costMatrix[i - 1][j - 2] + 2 * costMatrix[i][j - 1] + costMatrix[i][j]) / 3;
        }
        if ((i - 1) > 0 && (j - 2) > 0) {
          options[1] = costMatrix[i - 3][j - 2] + costMatrix[i - 2][j - 1] + 2
              * costMatrix[i - 1][j] + costMatrix[i][j];
        }
        options[2] = costMatrix[i - 1][j - 1] + 2 * costMatrix[i][j];
        double minDistance = Math.min(options[0], Math.min(options[1], options[2]));
        costMatrix[i][j] = distanceMatrix[i][j] + minDistance;
      }
    }
  }

  /**
   * Fills the cost matrix using the step P1.
   * 
   * @param costMatrix the resulting cost matrix.
   * @param distanceMatrix the initial distance matrix.
   */
  private void doCostP1(double[][] costMatrix, double[][] distanceMatrix) {
    int rows = distanceMatrix.length;
    int columns = distanceMatrix[0].length;
    for (int j = 1; j < rows; j++) {
      for (int i = 1; i < columns; i++) {
        double[] options = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
        if ((j - 1) > 0) {
          options[0] = costMatrix[i - 1][j - 2] + (costMatrix[i][j - 1] + costMatrix[i][j]) / 2;
        }
        if ((i - 1) > 0) {
          options[1] = costMatrix[i - 2][j - 1] + costMatrix[i - 1][j] + costMatrix[i][j];
        }
        options[2] = costMatrix[i - 1][j - 1] + costMatrix[i][j];
        double minDistance = Math.min(options[0], Math.min(options[1], options[2]));
        costMatrix[i][j] = distanceMatrix[i][j] + minDistance;
      }
    }
  }

  /**
   * Fills the cost matrix using the step P05.
   * 
   * @param costMatrix the resulting cost matrix.
   * @param distanceMatrix the initial distance matrix.
   */
  private void doCostP05(double[][] costMatrix, double[][] distanceMatrix) {
    int rows = distanceMatrix.length;
    int columns = distanceMatrix[0].length;
    for (int j = 1; j < rows; j++) {
      for (int i = 1; i < columns; i++) {
        double[] options = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
            Double.MAX_VALUE, Double.MAX_VALUE };
        if ((j - 2) > 0) {
          options[0] = costMatrix[i - 1][j - 3]
              + (costMatrix[i][j - 2] + costMatrix[i][j - 1] + costMatrix[i][j]) / 3;
        }
        if ((j - 1) > 0) {
          options[1] = costMatrix[i - 1][j - 2] + (costMatrix[i][j - 1] + costMatrix[i][j]) / 2;
        }
        if ((i - 2) > 0) {
          options[2] = costMatrix[i - 3][j - 1] + costMatrix[i - 2][j - 1] + costMatrix[i - 1][j]
              + costMatrix[i][j];
        }
        if ((i - 1) > 0) {
          options[3] = costMatrix[i - 2][j - 1] + costMatrix[i - 1][j] + costMatrix[i][j];
        }
        options[4] = costMatrix[i - 1][j - 1] + costMatrix[i][j];
        double minDistance = Math.min(options[0], Math.min(Math.min(options[1], options[2]), Math
            .min(options[3], options[4])));
        costMatrix[i][j] = distanceMatrix[i][j] + minDistance;
      }
    }
  }

  /**
   * Fills the cost matrix using the step P0.
   * 
   * @param costMatrix the resulting cost matrix.
   * @param distanceMatrix the initial distance matrix.
   */
  private void doCostP0(double[][] costMatrix, double[][] distanceMatrix) {
    int rows = distanceMatrix.length;
    int columns = distanceMatrix[0].length;
    for (int j = 1; j < rows; j++) {
      for (int i = 1; i < columns; i++) {
        double[] options = { costMatrix[i][j - 1], costMatrix[i - 1][j - 1] + 2 * costMatrix[i][j],
            costMatrix[i - 1][j] + costMatrix[i][j] };
        double minDistance = Math.min(options[0], Math.min(options[1], options[2]));
        costMatrix[i][j] = distanceMatrix[i][j] + minDistance;
      }
    }
  }
}
