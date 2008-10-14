package org.hackystat.projectbrowser.page.projectportfolio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Session to hold state for Project Portfolio.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The parameter key of start date. */
  public static final String START_DATE_KEY = "0";
  /** The parameter key of end date. */
  public static final String END_DATE_KEY = "1";
  /** The parameter key of granularity. */
  public static final String GRANULARITY_KEY = "2";
  /** The parameter key of selectedProjects. */
  public static final String SELECTED_PROJECTS_KEY = "3";
  /** 
   * The last parameter key that is required.
   * The telemetry parameters key is optional because not all telemetries have parameter.
   */
  private static final String LAST_REQUIRED_KEY = "3";
  /** The last parameter key. */
  public static final String PARAMETER_ORDER_MESSAGE = "Correct parameter order is : " + 
                        "/startDate/endDate/granularity/projects";

  /** The separator for parameter values. */
  public static final String PARAMETER_VALUE_SEPARATOR = ",";
  /** The separator between project name and its onwer. */
  public static final String PROJECT_NAME_OWNER_SEPARATR = "::";
  
  /** the feedback string. */
  private String feedback = "";
  
  /** The data model to hold state for details panel. */
  private ProjectPortfolioDataModel dataModel;
  
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
  
  /** Error message when parsing page paramters. */
  private String paramErrorMessage = "";

  /**
   * Update the data model.
   */
  public void updateDataModel() {

    
    boolean backgroundProcessEnable = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).
      isBackgroundProcessEnable("projectportfolio");
    this.dataModel.setModel(startDate, endDate, selectedProjects, granularity);

    ProjectBrowserSession.get().logUsage("PORTFOLIO: {invoked} " + 
        ProjectBrowserSession.get().printPageParameters(this.getPageParameters()));
    
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

  /**
   * Initialize the dataModel.
   * Only initialize it when it is null.
   * @return true if the dataModel is initialized, otherwise false.
   */
  public boolean initializeDataModel() {
    if (this.dataModel == null) {
      this.dataModel = new ProjectPortfolioDataModel(
          ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getTelemetryHost(),
          ProjectBrowserSession.get().getEmail(), ProjectBrowserSession.get().getPassword());
      return true;
    }
    return false;
  }

  /**
   * Returns the string that represents startDate in standard formatted.
   * e.g. 2008-08-08T08:08:08+08:00, 
   * the +08:00 in the end means the time zone of this time stamp is +08:00
   * @return a String
   */
  public String getFormattedStartDateString() {
    return Tstamp.makeTimestamp(this.startDate).toString();
  }

  /**
   * Returns the string that represents endDate in standard formatted.
   * e.g. 2008-08-08T08:08:08+08:00, 
   * the +08:00 in the end means the time zone of this time stamp is +08:00
   * @return a String
   */
  public String getFormattedEndDateString() {
    return Tstamp.makeTimestamp(this.endDate).toString();
  }
  
  /**
   * Returns the list of the selected projects in a single String, separated by comma.
   * @return a String.
   */
  public String getSelectedProjectsAsString() {
    StringBuffer projectList = new StringBuffer();
    for (int i = 0; i < this.getSelectedProjects().size(); ++i) {
      Project project = this.getSelectedProjects().get(i);
      if (project == null) {
        continue;
      }
      projectList.append(project.getName());
      projectList.append(PROJECT_NAME_OWNER_SEPARATR);
      projectList.append(project.getOwner());
      if (i < this.getSelectedProjects().size() - 1) {
        projectList.append(PARAMETER_VALUE_SEPARATOR);
      }
    }
    return projectList.toString();
  }
  
  /**
   * Returns a PageParameters instance that represents the content of the input form.
   * @return a PageParameters instance.
   */
  public PageParameters getPageParameters() {
    PageParameters parameters = new PageParameters();

    parameters.put(GRANULARITY_KEY, this.getGranularity());
    parameters.put(START_DATE_KEY, this.getFormattedStartDateString());
    parameters.put(END_DATE_KEY, this.getFormattedEndDateString());
    parameters.put(SELECTED_PROJECTS_KEY, this.getSelectedProjectsAsString());
    
    return parameters;
  }

  /**
   * Load data from URL parameters into this session.
   * @param parameters the URL parameters
   * @return true if all parameters are loaded correctly
   */
  public boolean loadPageParameters(PageParameters parameters) {
    boolean isLoadSucceed = true;
    Logger logger = ProjectBrowserSession.get().getLogger();
    if (!parameters.containsKey(LAST_REQUIRED_KEY)) {
      isLoadSucceed = false;
      String error = "Some parameters are missing, should be " + LAST_REQUIRED_KEY + "\n" +
          PARAMETER_ORDER_MESSAGE;
      logger.warning(error);
      this.paramErrorMessage = error + "\n";
      return false;
    }
    StringBuffer errorMessage = new StringBuffer(1000);
    
    //load granularity
    if (parameters.containsKey(GRANULARITY_KEY)) {
      String granularityString = parameters.getString(GRANULARITY_KEY);
      if (this.getGranularities().contains(granularityString)) {
        this.setGranularity(granularityString);
      }
      else {
        isLoadSucceed = false;
        String error = "Granularity is not supported: " + granularityString;
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("granularity is missing in URL parameters.\n");
    }
    //load dates
    String startDateString = "";
    if (parameters.containsKey(START_DATE_KEY)) {
      startDateString = parameters.getString(START_DATE_KEY);
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("startDate is missing in URL parameters.\n");
    }
    String endDateString = "";
    if (parameters.containsKey(END_DATE_KEY)) {
      endDateString = parameters.getString(END_DATE_KEY);
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("endDate is missing in URL parameters.\n");
    }
    //check last X format first.
    if ("last".equalsIgnoreCase(startDateString)) {
      int time;
      try {
        time = Integer.valueOf(endDateString);
        int interval = 1;
        if ("Month".equalsIgnoreCase(this.granularity)) {
          interval = 30;
        }
        else if ("Week".equalsIgnoreCase(this.granularity)) {
          interval = 7;
        }
        time *= interval;
        XMLGregorianCalendar today = Tstamp.makeTimestamp();
        endDate = Tstamp.incrementDays(today, -interval).toGregorianCalendar().getTimeInMillis();
        startDate = Tstamp.incrementDays(today, -time).toGregorianCalendar().getTimeInMillis();
      }
      catch (NumberFormatException e) {
        String error = endDateString + " cannot be parsed as an integer.";
        logger.warning(error + " > " + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else if (endDateString.length() > 0 && startDateString.length() > 0) {
      try {
        this.endDate = 
          Tstamp.makeTimestamp(endDateString).toGregorianCalendar().getTimeInMillis();
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing end date from URL parameter: " + endDateString;
        logger.warning(error + " > " + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
      try {
        this.startDate = 
          Tstamp.makeTimestamp(startDateString).toGregorianCalendar().getTimeInMillis();
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = 
          "Errors when parsing start date from URL parameter: " + startDateString;
        logger.warning(error + " > " + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    
    //load seletecd project
    if (parameters.containsKey(SELECTED_PROJECTS_KEY)) {
      String[] projectsStringArray = 
        parameters.getString(SELECTED_PROJECTS_KEY).split(PARAMETER_VALUE_SEPARATOR);
      List<Project> projectsList = new ArrayList<Project>();
      for (String projectString : projectsStringArray) {
        int index = projectString.lastIndexOf(PROJECT_NAME_OWNER_SEPARATR);
        String projectName = projectString;
        String projectOwner = null;
        if (index > 0 && index < projectString.length()) {
          projectName = projectString.substring(0, index);
          projectOwner = projectString.substring(index + PROJECT_NAME_OWNER_SEPARATR.length());
          /*
          isLoadSucceed = false;
          String error = "Error URL parameter: project: " + projectString + 
              " >> project name and owner are missing or not formatted correctly.";
          logger.warning(error);
          errorMessage.append(error);
          errorMessage.append('\n');
          continue;
          */
        }
        Project project = this.getProject(projectName, projectOwner);
        if (project == null) {
          isLoadSucceed = false;
          String error = "Error URL parameter: project: " + projectString +
              " >> matching project not found under user: " + 
              ProjectBrowserSession.get().getEmail();
          logger.warning(error);
          errorMessage.append(error);
          errorMessage.append('\n');
        }
        else {
          projectsList.add(project);
        }
      }
      if (!projectsList.isEmpty()) {
        this.setSelectedProjects(projectsList);
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("projects is missing in URL parameters.\n");
    }
    
    if (errorMessage.length() > 0) {
      this.paramErrorMessage = errorMessage.append(PARAMETER_ORDER_MESSAGE).toString();
    }
    return isLoadSucceed;
  }

  /**
   * Returns a Project instance that available to current user and 
   * is matched to the given project name and project owner.
   * @param projectName the given project name.
   * @param projectOwner the given project owner.
   * @return the Project instance. null if no matching project is found,
   * which may means either the project name or project owner is null or there is no Project for
   * this user with the same project name and owner as the given ones.
   */
  public Project getProject(String projectName, String projectOwner) {
    if (projectName == null) {
      return null;
    }
    for (Project project : ProjectBrowserSession.get().getProjectList()) {
      if (projectName.equals(project.getName()) && 
          (projectOwner == null || projectOwner.equals(project.getOwner()))) {
          return project;
      }
    }
    return null;
  }
  
  /**
   * @return the paramErrorMessage
   */
  public String getParamErrorMessage() {
    String temp = this.paramErrorMessage;
    this.clearParamErrorMessage();
    return temp;
  }
  
  /**
   * Clears the paramErrorMessage.
   */
  public void clearParamErrorMessage() {
    this.paramErrorMessage = "";
  }

}
