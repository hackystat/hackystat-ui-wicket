package org.hackystat.projectbrowser.page.loadingprocesspanel;

/**
 * Interface to support the loading process panel.
 * LoadingProcessPanel will be shown when either isInProcess or isComplete is true.
 * LoadingProcessPanel will auto update itself every second when isInProcess is true.
 * LoadingProcessPanel will display content of getProcessingMessage in it.
 * @author Shaoxuan Zhang
 */
public interface Processable {

  /**
   * @return if the process is in progress.
   */
  public boolean isInProcess();

  /**
   * @return if the process is successfully completed.
   */
  public boolean isComplete();

  /**
   * @return the message that reflect the status and/or the result of the process.
   */
  public String getProcessingMessage();
  
}
