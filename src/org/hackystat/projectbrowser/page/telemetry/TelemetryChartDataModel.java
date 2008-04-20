package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel.ProjectDateForm;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Data model to hold state of the telemetry chart.
 * @author Shaoxuan Zhang
 */
public class TelemetryChartDataModel implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The start date this user has selected. */
  private long startDate = 0;
  /** The end date this user has selected. */
  private long endDate = 0;
  /** The project this user has selected. */
  private Project project = null;
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The URL of google chart. */
  private String chartUrl = "";
  
  /**
   * @param startDate the start date of this model..
   * @param endDate the end date of this model..
   * @param project the project of this model.
   * @param telemetryName the telemetry name of this model.
   * @param chartUrl the chart url of this model.
   */
  public void setModel(Date startDate, Date endDate, Project project, 
                                 String telemetryName, String chartUrl) {
    this.startDate = startDate.getTime();
    this.endDate = endDate.getTime();
    this.project = project;
    this.telemetryName = telemetryName;
    this.chartUrl = chartUrl;
  }
  /**
   * Returns the start date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getStartDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.startDate));
  }
  
  /**
   * Returns the end date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getEndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.endDate));
  }

  /**
   * @return the project
   */
  public Project getProject() {
    return project;
  }

  /**
   * @return the telemetryName
   */
  public String getTelemetryName() {
    return telemetryName;
  }

  /**
   * @return the chartUrl
   */
  public String getChartUrl() {
    return chartUrl;
  }

  /**
   * Returns true if this model does not contain any data. 
   * @return True if no data. 
   */
  public boolean isEmpty() {
    return this.chartUrl == null || chartUrl.length() <= 0;
  }
}
