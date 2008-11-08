package org.hackystat.projectbrowser.page.trajectory.dtw.step;

import java.awt.Point;

import org.hackystat.projectbrowser.page.trajectory.dtw.constraint.AbstractConstraintFunction;

/**
 * Defines the stepfunction for the DTW.
 * 
 * @author Pavel Senin
 */
public abstract class AbstractStepFunction {

  /**
   * Calculates the next step given the cost matrix and constraints.
   * 
   * @param position the current position.
   * @param costMatrix the distance matrix.
   * @param constraints the constraints function.
   * @return The next step.
   */
  public abstract Point doStep(Point position, double[][] costMatrix,
      AbstractConstraintFunction constraints);

  /**
   * Get the local cost matrix using the step function.
   * 
   * @param distanceMatrix The distance matrix to use.
   * @return computed cost matrix.
   */
  public abstract double[][] getCostMatrix(double[][] distanceMatrix);

}
