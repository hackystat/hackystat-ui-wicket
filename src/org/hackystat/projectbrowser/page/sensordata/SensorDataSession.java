package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

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
  
  /** Holds the SensorDataDetails model associated with this session. */
  private SensorDataDetailsModel sensorDataDetailsModel = new SensorDataDetailsModel();
  
  private String sdtName = ""; 
  private String tool = "";
  
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
   * Returns the Project instance selected by the user in this session. 
   * @return The project instance.
   */
  public Project getProject() {
    return ProjectBrowserSession.get().getProjectByNameId(getProjectName());
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

  /**
   * Return the sensor data details model.
   * @return The sensor data details model.
   */
  public SensorDataDetailsModel getSensorDataDetailsModel() {
    return this.sensorDataDetailsModel;
  }
  
  /**
   * Set the sensor data details model.
   * @param model The sensor data details model.
   */
  public void setSensorDataDetailsModel(SensorDataDetailsModel model) {
    this.sensorDataDetailsModel = model;
  }
  
  /**
   * Gets the sdt name. 
   * @return The sdt name.
   */
  public String getSdtName() {
    return this.sdtName;
  }
  
  /**
   * Sets the SDT name.
   * @param sdtName The sdt name.
   */
  public void setSdtName(String sdtName) {
    this.sdtName = sdtName;
  }
  
  /**
   * Gets the tool.
   * @return The tool.
   */
  public String getTool() {
    return this.tool;
  }
  
  /**
   * Sets the tool.
   * @param tool The tool.
   */
  public void setTool(String tool) {
    this.tool = tool;
  }

}