package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * Holds all of the models and other state for the SensorDataPage. This enables users to 
 * obtain an analysis in the SensorData page, then go to another page, then come back to this page 
 * and see the page in the same state as when they left it. 
 * @author Philip Johnson
 */
public class SensorDataSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The date for this page.  Represented as a long to avoid findbugs errors. */
  private long date = (new Date()).getTime();
  
  /** The project name associated with this page. */
  private String projectName = null;

  /** Holds the sdtSummaryModel associated with this session. */ 
  private SdtSummaryModel sdtSummaryModel = new SdtSummaryModel();
  
  /**
   * Create a session state instance for this page. 
   */
  public SensorDataSession() {
    // do nothing at the moment. 
  }
  
  /**
   * Gets the date associated with this page. 
   * @return The date for this page. 
   */
  public Date getDate() {
    return new Date(this.date);
  }
  
  /**
   * Sets the date associated with this page. 
   * @param date The date for this page. 
   */
  public void setDate(Date date) {
    this.date = date.getTime();
  }
  
  /**
   * Returns the current date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getDateString() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    return format.format(new Date(this.date));
  }
  
  /**
   * Gets the project name associated with this page. 
   * @return The project name for this page. 
   */
  public String getProjectName() {
    if (this.projectName == null) {
      this.projectName = ProjectBrowserSession.get().getProjectNames().get(0);
    }
    return this.projectName;
  }
  
  /**
   * Sets the projectName for this page. 
   * @param projectName The project name for this page. 
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * Sets the SdtSummaryModel for this page. 
   * @param sdtSummaryModel The sdtSummaryModel.
   */
  public void setSdtSummaryModel(SdtSummaryModel sdtSummaryModel) {
    this.sdtSummaryModel = sdtSummaryModel;
  }

  /**
   * Returns the current SdtSummaryModel for this page. 
   * @return The SdtSummaryModel. 
   */
  public SdtSummaryModel getSdtSummaryModel() {
    return this.sdtSummaryModel;
  }

}
