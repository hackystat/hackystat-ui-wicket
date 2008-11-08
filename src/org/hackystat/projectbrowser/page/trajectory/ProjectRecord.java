package org.hackystat.projectbrowser.page.trajectory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The container for the project and some auxiliary information.
 *
 * @author Pavel Senin.
 *
 */
public class ProjectRecord implements Serializable {

  /**
   * Serial.
   */
  private static final long serialVersionUID = 1L;

  // the project record
  private Project project;

  // the selected start date
  private Date startDate;
  // the selected end date
  private Date endDate;
  // the shift for the data displayed
  private Integer indent = 0;

  private static final String CR = "\n";
  private static final SimpleDateFormat labelDateFormat = 
                                                        new SimpleDateFormat("MM/dd/yy", Locale.US);

  /**
   * Constructor.
   *
   * @param project The project.
   * @param selectedStartDate The start date for the interval.
   * @param selectedEndDate The end date for the interval.
   * @param indent The shift.
   */
  public ProjectRecord(Project project, Date selectedStartDate, Date selectedEndDate,
      Integer indent) {
    this.project = project;
    this.startDate = new Date(selectedStartDate.getTime());
    this.endDate = new Date(selectedStartDate.getTime());
    this.indent = indent;
  }

  /**
   * Constructor.
   *
   * @param project The project.
   * @param indent The shift in days.
   */
  public ProjectRecord(Project project, Integer indent) {
    this.project = project;
    this.startDate = new Date();
    this.endDate = new Date();
    this.indent = indent;
  }

  /**
   * Constructor.
   */
  public ProjectRecord() {
    super();
    this.project = null;
    this.startDate = new Date(ProjectBrowserBasePage.getDateBefore(7).getTime());
    this.endDate = new Date(ProjectBrowserBasePage.getDateBefore(1).getTime());
    this.indent = 0;
  }

  /**
   * Returns the project.
   *
   * @return The project.
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * Prints the debug log message.
   *
   * @return The debug message.
   */
  public synchronized String toLabelMessage() {
    StringBuffer sb = new StringBuffer(1024);
    sb.append("project: " + this.project.getName() + CR);
    sb.append("owner " + this.project.getOwner() + CR);
    sb.append("life:"
        + labelDateFormat.format(new Date(Tstamp.makeTimestamp(this.project.getStartTime())
            .getTime()))
        + "-"
        + labelDateFormat
            .format(new Date(Tstamp.makeTimestamp(this.project.getEndTime()).getTime())) + CR);
    sb.append("interval:" + labelDateFormat.format(this.startDate) + "-"
        + labelDateFormat.format(this.endDate) + CR);
    sb.append("shift " + this.indent + CR);
    return sb.toString();
  }



  /**
   * Set the indent
   *
   * @param indent The indent to set.
   */
  public void setIndent(Integer indent) {
    this.indent = indent;
  }

  /**
   * Get the indent.
   *
   * @return The indent for the project interval.
   */
  public Integer getIndent() {
    return this.indent;
  }

  /**
   * Set the start date for this record.
   *
   * @param timeInMillis The time to set.
   */
  public void setStartDate(long timeInMillis) {
    this.startDate = new Date(timeInMillis);
  }

  /**
   * Get the start date for this record.
   *
   * @return The start date for this project.
   */
  public Date getStartDate() {
    Date d = new Date(this.startDate.getTime());
    return d;
  }

  /**
   * Set the end date for this record.
   *
   * @param timeInMillis The time to set.
   */
  public void setEndDate(long timeInMillis) {
    this.endDate = new Date(timeInMillis);
  }


  /**
   * Get the end date for this record.
   *
   * @return The end date for this project.
   */
  public Date getEndDate() {
    Date d = new Date(this.endDate.getTime());
    return d;
  }

  /**
   * Set the project.
   *
   * @param project The project to set.
   */
  public void setProject(Project project) {
    this.project = project;
  }

}
