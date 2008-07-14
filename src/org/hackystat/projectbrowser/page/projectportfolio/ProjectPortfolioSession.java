package org.hackystat.projectbrowser.page.projectportfolio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
  /** The data model to hold state for details panel */
  private ProjectPortfolioDataModel dataModel = new ProjectPortfolioDataModel();
  /** The start date this user has selected. */
  private long startDate = ProjectBrowserBasePage.getDateBefore(28).getTime();
  /** The end date this user has selected. */
  private long endDate = ProjectBrowserBasePage.getDateBefore(1).getTime();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** the feedback string. */
  private String feedback = "";

  /**
   * Update the data model.
   */
  public void updateDataModel() {

    this.dataModel.setModel(startDate, endDate, selectedProjects);
    
    Thread thread = new Thread() {
      @Override
      public void run() {
        dataModel.loadData();
      }
    };
    thread.start();
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
   * @param dataModel the dataModel to set
   */
  public void setDataModel(ProjectPortfolioDataModel dataModel) {
    this.dataModel = dataModel;
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

}
