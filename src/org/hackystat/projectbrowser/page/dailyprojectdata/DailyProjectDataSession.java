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
 * Session instance for the daily project data page to hold its state.
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
  private String analysis;
  
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
    this.analysis = analysisList.get(0);
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
   * Sets the selected analysis.
   * @param analysis The analysis to set.
   */
  public void setAnalysis(String analysis) {
    this.analysis = analysis;
  }

  /**
   * Gets the selected analysis.
   * @return The analysis.
   */
  public String getAnalysis() {
    return analysis;
  }

  /**
   * Returns the list of possible analyses. 
   * @return The analysisList.
   */
  public List<String> getAnalysisList() {
    return analysisList;
  }

  /**
   * Sets the feedback string. 
   * @param feedback The feedback to set.
   */
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  /**
   * Gets the feedback string, and also clears it.
   * @return The feedback string. 
   */
  public String getFeedback() {
    String returnString = this.feedback;
    this.feedback = "";
    return returnString;
  }

  /**
   * Sets the data model for this page. 
   * @param dataModel The new dataModel.
   */
  public void setDataModel(DailyProjectDataModel dataModel) {
    this.dataModel = dataModel;
  }

  /**
   * Gets the current data model for this page. 
   * @return The dataModel.
   */
  public DailyProjectDataModel getDataModel() {
    return dataModel;
  }
}
