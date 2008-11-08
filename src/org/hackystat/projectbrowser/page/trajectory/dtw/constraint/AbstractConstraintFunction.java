package org.hackystat.projectbrowser.page.trajectory.dtw.constraint;

import java.awt.geom.Point2D;

/**
 * Defines the constraint function.
 * 
 * @author Pavel Senin.
 * 
 */
public abstract class AbstractConstraintFunction {

  /**
   * Performs the next step given the cost matrix, current position and step function.
   * 
   * @param position the current position.
   * @param costMatrix the cost matrix.
   * @param constraints the constraints set.
   * @return the next step position.
   */
  public abstract Point2D doStep(Point2D position, double[][] costMatrix,
      AbstractConstraintFunction constraints);
}
