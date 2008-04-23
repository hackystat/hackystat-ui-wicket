package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputForm;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Session for daily project data page to hold its object.
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The date this user has selected in the ProjectDate form. */
  private long date = ProjectBrowserBasePage.getDateToday().getTime();

  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The project this user has selected. */
  private Project project = null;

  /** The analysis this user has selected. */
  private String analysis = "Coverage";
  
  /** The analysis list. */
  private List<String> analysisList = new ArrayList<String>();

  /** the feedback string. */
  private String feedback = "";

  /** the data model for data panel. */
  private DailyProjectDataModel dataModel = null;
  
  /**
   * Initialize this session, initialize the analysis list.
   */
  public DailyProjectDataSession() {
    //analysisList.add("Build");
    analysisList.add("Coverage");
    analysisList.add("Unit Test");
    //analysisList.add("Complexity");
    //analysisList.add("Coupling");
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
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.date));
  }
  
  /**
   * @param project the project to set
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * @return the project
   */
  public Project getProject() {
    return this.project;
  }
  
  /**
   * Returns the list of projects selected by the user. 
   * @return The list of projects selected by the user. 
   */
  public List<Project> getSelectedProjects() {
    return this.selectedProjects;
  }
  
  /**
   * Sets the set of selected projects.
   * @param projects The projects.
   */
  public void setSelectedProjects(List<Project> projects) {
    this.selectedProjects = projects;
    this.project = (projects.isEmpty()) ? null : projects.get(0);
  }

  /**
   * @param analysis the analysis to set
   */
  public void setAnalysis(String analysis) {
    this.analysis = analysis;
  }

  /**
   * @return the analysis
   */
  public String getAnalysis() {
    return analysis;
  }

  /**
   * @return the analysisList
   */
  public List<String> getAnalysisList() {
    return analysisList;
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
   * @param dataModel the dataModel to set
   */
  public void setDataModel(DailyProjectDataModel dataModel) {
    this.dataModel = dataModel;
  }

  /**
   * @return the dataModel
   */
  public DailyProjectDataModel getDataModel() {
    return dataModel;
  }
}
