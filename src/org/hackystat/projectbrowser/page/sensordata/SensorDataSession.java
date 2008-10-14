package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.Calendar;

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
  //private long date = (new Date()).getTime();
  
  /** The project associated with this page. */
  private Project project = null;

  /** Holds the sdtSummaryModel associated with this session. */ 
  private SdtSummaryModel sdtSummaryModel = new SdtSummaryModel();
  
  /** Holds the SensorDataDetails model associated with this session. */
  private SensorDataDetailsModel sensorDataDetailsModel = new SensorDataDetailsModel();
  /** Holds the IDataProvider for SensorDataDetails. */
  private SensorDataDetailsProvider sensorDataDetailsProvider = new SensorDataDetailsProvider();
  
  private String sdtName = ""; 
  private String tool = "";
  
  /** The month field in the SensorData form, initialized to the current month. */
  private Integer month = Calendar.getInstance().get(Calendar.MONTH);
  /** The year field in the SensorData form, initialized to the current year. */  
  private Integer year = Calendar.getInstance().get(Calendar.YEAR);
  
  private SensorDataTableModel sensorDataTableModel = new SensorDataTableModel();
  
  /**
   * Create a session state instance for this page. 
   */
  public SensorDataSession() {
    // do nothing at the moment. 
  }
  
//  /**
//   * Gets the date associated with this page. 
//   * @return The date for this page. 
//   */
//  public Date getDate() {
//    return new Date(this.date);
//  }
  
//  /**
//   * Sets the date associated with this page. 
//   * @param date The date for this page. 
//   */
//  public void setDate(Date date) {
//    if (date == null) {
//      System.out.println("Calling setDate with: " + date);
//      return;
//    }
//    this.date = date.getTime();
//  }
  
//  /**
//   * Returns the current date in yyyy-MM-dd format.  
//   * @return The date as a simple string. 
//   */
//  public String getDateString() {
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//    return format.format(new Date(this.date));
//  }
  

  /**
   * Gets the project associated with this page. 
   * @return The project for this page. 
   */
  public Project getProject() {
    if (this.project == null) {
      this.setProject(ProjectBrowserSession.get().getDefaultProject());
    }
    return this.project;
  }
  
  /**
   * Sets the projectName for this page. 
   * @param project The project for this page. 
   */
  public void setProject(Project project) {
    this.project = project;
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
   * Return the sensor data details IDataProvider.
   * @return The sensor data details provider.
   */
  public SensorDataDetailsProvider getSensorDataDetailsProvider() {
    return this.sensorDataDetailsProvider;
  }
  
  /**
   * Set the sensor data details IDataProvider.
   * @param model The sensor data details provider
   */
  public void setSensorDataDetailsProvider(SensorDataDetailsProvider model) {
    this.sensorDataDetailsProvider = model;
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
  
  /**
   * Gets the month. 
   * @return The month.
   */
  public Integer getMonth() {
    return this.month;
  }

  /**
   * Gets the year. 
   * @return The year. 
   */
  public Integer getYear() {
    return this.year;
  }
  
  /**
   * Sets the month. 
   * @param month The month. 
   */
  public void setMonth(Integer month) {
    this.month = month;
  }
  
  /**
   * Sets the year. 
   * @param year The year. 
   */
  public void setYear(Integer year) {
    this.year = year; 
  }
  
  /**
   * Gets the SensorDataTableModel. 
   * @return The SensorDataTableModel.
   */
  public SensorDataTableModel getSensorDataTableModel() {
    return this.sensorDataTableModel;
  }
  
  /**
   * Sets the SensorDataTableModel. 
   * @param model The model. 
   */
  public void setSensorDataTableModel(SensorDataTableModel model) {
    this.sensorDataTableModel = model;
  }
  
  
}
