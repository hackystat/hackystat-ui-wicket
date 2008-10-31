package org.hackystat.projectbrowser.page.todate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.todate.detailpanel.ToDateDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * Session to hold state for telemetry.
 * 
 * @author Shaoxuan Zhang
 */
public class ToDateSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The parameter key of selectedProjects. */
  public static final String SELECTED_PROJECTS_KEY = "0";

  /** The last parameter key that is required.*/
  private static final String LAST_REQUIRED_KEY = "0";
  /** The last parameter key. */
  public static final String PARAMETER_ORDER_MESSAGE = "Correct parameter order is : "
      + "/<projects>";
  
  /** The separator for parameter values. */
  public static final String PARAMETER_VALUE_SEPARATOR = ",";
  /** The separator between project name and its onwer. */
  public static final String PROJECT_NAME_OWNER_SEPARATR = "::";
  
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();

  /** The data model to hold state for data panel. */
  private ToDateDataModel dataModel;

  /** the feedback string. */
  private String feedback = "";
  /** Error message when parsing page paramters. */
  private String paramErrorMessage = "";

  /**
   * Initialize the dataModel.
   * Only initialize it when it is null.
   * @return true if the dataModel is initialized, otherwise false.
   */
  public boolean initializeDataModel() {
    if (this.dataModel == null) {
      this.dataModel = new ToDateDataModel(
          ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getTelemetryHost(),
          ProjectBrowserSession.get().getEmail(), ProjectBrowserSession.get().getPassword());
  
      Logger logger = HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
      logger.info("ToDateDataModel initialized.");
      return true;
    }
    return false;
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
   * Update the data model. If execute in background process is determined in
   * ProjectBrowserProperties.
   */
  public void updateDataModel() {
    boolean backgroundProcessEnable = ((ProjectBrowserApplication) ProjectBrowserApplication.get())
        .isBackgroundProcessEnable("todate");
    dataModel.setModel(selectedProjects);

    ProjectBrowserSession.get().logUsage(
        "TODATE: {invoked} "
            + ProjectBrowserSession.get().printPageParameters(this.getPageParameters()));

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
   * Cancel data model's update.
   */
  public void cancelDataUpdate() {
    dataModel.cancelDataLoading();
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
   * Returns the list of the selected projects in a single String, separated by comma.
   * 
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
   * Returns a Project instance that available to current user and is matched to the given project
   * name and project owner.
   * 
   * @param projectName the given project name.
   * @param projectOwner the given project owner.
   * @return the Project instance. null if no matching project is found, which may means either the
   *         project name or project owner is null or there is no Project for this user with the
   *         same project name and owner as the given ones.
   */
  public Project getProject(String projectName, String projectOwner) {
    if (projectName == null) {
      return null;
    }
    for (Project project : ProjectBrowserSession.get().getProjectList()) {
      if (projectName.equals(project.getName())
          && (projectOwner == null || projectOwner.equals(project.getOwner()))) {
        return project;
      }
    }
    return null;
  }

  /**
   * Load data from URL parameters into this session.
   * 
   * @param parameters the URL parameters
   * @return true if all parameters are loaded correctly
   */
  public boolean loadPageParameters(PageParameters parameters) {
    boolean isLoadSucceed = true;
    Logger logger = ProjectBrowserSession.get().getLogger();
    if (!parameters.containsKey(LAST_REQUIRED_KEY)) {
      isLoadSucceed = false;
      String error = "Some parameters are missing, should be " + LAST_REQUIRED_KEY + "\n"
          + PARAMETER_ORDER_MESSAGE;
      logger.warning(error);
      this.paramErrorMessage = error + "\n";
      return false;
    }
    StringBuffer errorMessage = new StringBuffer(1000);
    // load seletecd project
    if (parameters.containsKey(SELECTED_PROJECTS_KEY)) {
      String[] projectsStringArray = parameters.getString(SELECTED_PROJECTS_KEY).split(
          PARAMETER_VALUE_SEPARATOR);
      List<Project> projectsList = new ArrayList<Project>();
      for (String projectString : projectsStringArray) {
        int index = projectString.lastIndexOf(PROJECT_NAME_OWNER_SEPARATR);
        String projectName = projectString;
        String projectOwner = null;
        if (index > 0 && index < projectString.length()) {
          projectName = projectString.substring(0, index);
          projectOwner = projectString.substring(index + PROJECT_NAME_OWNER_SEPARATR.length());
          /*
           * isLoadSucceed = false; 
           * String error = "Error URL parameter: project: " + projectString + " >>
           * project name and owner are missing or not formatted correctly."; logger.warning(error);
           * errorMessage.append(error); errorMessage.append('\n'); continue;
           */
        }
        Project project = this.getProject(projectName, projectOwner);
        if (project == null) {
          isLoadSucceed = false;
          String error = "Error URL parameter: project: " + projectString
              + " >> matching project not found under user: "
              + ProjectBrowserSession.get().getEmail();
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
      errorMessage.append("projects key is missing in URL parameters.\n");
    }
    if (errorMessage.length() > 0) {
      this.paramErrorMessage = errorMessage.toString() + PARAMETER_ORDER_MESSAGE;
    }
    return isLoadSucceed;
  }

  /**
   * Returns a PageParameters instance that represents the content of the input form.
   * 
   * @return a PageParameters instance.
   */
  public PageParameters getPageParameters() {
    PageParameters parameters = new PageParameters();

    parameters.put(SELECTED_PROJECTS_KEY, this.getSelectedProjectsAsString());

    return parameters;
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

  /**
   * @return the dataModel
   */
  public ToDateDataModel getDataModel() {
    return dataModel;
  }

}
