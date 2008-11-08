package org.hackystat.projectbrowser.page.trajectory.dtw;

/**
 * The DTW package-specific exception.
 *
 * @author Pavel Senin.
 *
 */
public class DTWException extends Exception {

  /**
   * Serial.
   */
  private static final long serialVersionUID = 4451593638376379131L;

  /**
   * Constructor.
   *
   * @param description The problem description.
   */
  public DTWException(String description) {
    super(description);
  }

}
