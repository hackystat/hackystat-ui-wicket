package org.hackystat.projectbrowser.page.projectportfolio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Session to hold state for Project Portfolio.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** the feedback string. */
  private String feedback = "";
  
  /** The data model to hold state for details panel */
  private final ProjectPortfolioDataModel dataModel = new ProjectPortfolioDataModel();
  
  /** The start date this user has selected. */
  private long startDate = ProjectBrowserBasePage.getDateBefore(28).getTime();
  /** The end date this user has selected. */
  private long endDate = ProjectBrowserBasePage.getDateBefore(1).getTime();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** the granularity this data model focus. */
  private String granularity = "Week";
  /** The available granularities. */
  private final String[] granularities = {"Day", "Week", "Month"};

  /**
   * Update the data model.
   */
  public void updateDataModel() {

    boolean backgroundProcessEnable = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).
      isBackgroundProcessEnable("projectportfolio");
    
    this.dataModel.setModel(startDate, endDate, selectedProjects, granularity,
        ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getTelemetryHost(),
        ProjectBrowserSession.get().getEmail(),
        ProjectBrowserSession.get().getPassword());

    if (backgroundProcessEnable) {
      Thread thread = new Thread() {
        @Override
        public void run() {
          dataModel.loadData();
        }
      };
      thread.start();
    }
    else {
      dataModel.loadData();
    }
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

  /**
   * @return the dataModel
   */
  public ProjectPortfolioDataModel getDataModel() {
    return dataModel;
  }
  
  /**
   * @param feedback the feedback to set
   */
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  /**
   * @return the feedback
   */
  public String getFeedback() {
    String returnString = this.feedback;
    this.feedback = "";
    return returnString;
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
   * @param granularity the granularity to set
   */
  public void setGranularity(String granularity) {
    this.granularity = granularity;
  }

  /**
   * @return the granularity
   */
  public String getGranularity() {
    return granularity;
  }

  /**
   * @return the granularities
   */
  public List<String> getGranularities() {
    return Arrays.asList(this.granularities);
  }
  
}
