package org.hackystat.projectbrowser.page.projectportfolio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Session to hold state for Project Portfolio.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The data model to hold state for details panel */
  //private ProjectPortfolioDataModel dataModel = new ProjectPortfolioDataModel();
  /** The start date this user has selected. */
  private long startDate = ProjectBrowserBasePage.getDateBefore(7).getTime();
  /** The end date this user has selected. */
  private long endDate = ProjectBrowserBasePage.getDateBefore(1).getTime();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();

  /**
   * Update the data model.
   */
  public void updateDataModel() {
    //TODO update the data model
    
    Thread thread = new Thread() {
      @Override
      public void run() {
        //dataModel.loadData();
      }
    };
    thread.start();
  }
  
  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate.getTime();
  }

  /**
   * @return the startDate
   */
  public Date getStartDate() {
    return new Date(startDate);
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate.getTime();
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return new Date(endDate);
  }

  /**
   * Returns the start date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getStartDateString() {
    SimpleDateFormat format = 
      new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.startDate));
  }
  
  /**
   * Returns the end date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getEndDateString() {
    SimpleDateFormat format = 
      new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.endDate));
  }

  /**
   * @param selectedProjects the selectedProjects to set
   */
  public void setSelectedProjects(List<Project> selectedProjects) {
    this.selectedProjects = selectedProjects;
  }

  /**
   * @return the selectedProjects
   */
  public List<Project> getSelectedProjects() {
    return selectedProjects;
  }
}
