package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.contextsensitive.ContextSensitiveMenu;
import org.hackystat.projectbrowser.page.contextsensitive.ContextSensitivePanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.build.BuildDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.commit.CommitDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.complexity.ComplexityDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coupling.CouplingDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coverage.CoverageDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.devtime.DevTimeDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.filemetric.FileMetricDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.unittest.UnitTestDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Session instance for the daily project data page to hold its state.
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The parameter key of dpd analysis. */
  public static final String ANALYSIS_KEY = "0";
  /** The parameter key of date. */
  public static final String DATE_KEY = "1";
  /** The parameter key of selectedProjects. */
  public static final String SELECTED_PROJECTS_KEY = "2";
  /**
   * The last parameter key that is required. 
   */
  private static final String LAST_REQUIRED_KEY = "2";

  /** The parameter instruction message. */
  public static final String PARAMETER_ORDER_MESSAGE = "Correct parameter order is : "
      + "/<analysis>/<date>/<projects>";

  /** Error message when parsing page paramters. */
  private String paramErrorMessage = "";
  
  /** The date this user has selected in the ProjectDate form. */
  private long date = ProjectBrowserBasePage.getDateYesterday().getTime();

  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  
  /** The analysis this user has selected. Defaults to Build. */
  private String analysis = "Coverage";
  
  /** The list of analysis choices. */
  private static final List<String> analysisList = 
    Arrays.asList("Build", "Commit", "Coupling", "Coverage", "Complexity", "DevTime", "FileMetric", 
        "UnitTest");

  /** the feedback string. */
  private String feedback = "";

  /** The context sensitive panel.  We keep a pointer to this in the session for Ajax updating. */
  private ContextSensitivePanel csPanel; 
  
  /** Holds the state of the context-sensitive menus in the context sensitive panel. */
  private Map<String, ContextSensitiveMenu> csMenus = new HashMap<String, ContextSensitiveMenu>();
  
  /** The Coverage data model. */
  private CoverageDataModel coverageDataModel = new CoverageDataModel();
  
  /** The Unit Test analysis data model. */
  private UnitTestDataModel unitTestDataModel = new UnitTestDataModel();
  
  /** The coupling data model. */
  private CouplingDataModel couplingDataModel = new CouplingDataModel();

  /** The complexity data model. */
  private ComplexityDataModel complexityDataModel = new ComplexityDataModel();

  /** The build data model. */
  private BuildDataModel buildDataModel = new BuildDataModel();

  /** The DevTime data model. */
  private DevTimeDataModel devTimeDataModel = new DevTimeDataModel();

  /** The FileMetric data model. */
  private FileMetricDataModel fileMetricDataModel = new FileMetricDataModel();
  
  /** The Commit data model. */
  private CommitDataModel commitDataModel = new CommitDataModel();


  /**
   * Initialize this session, including the list of context-sensitive menus.
   */
  public DailyProjectDataSession() {
    // Initialize the context sensitive menus.  
    // Since the default analysis is Coverage, the Values and Coverage Type menus are visible.
    csMenus.put("Values", new ContextSensitiveMenu("Values", "Count", 
        Arrays.asList("Count", "Percentage"), true));
    csMenus.put("Coverage Type", new ContextSensitiveMenu("Coverage Type", "Method", 
        Arrays.asList("Block", "Class", "Conditional", "Element", "Line", "Method", "Statement"), 
        true));
    csMenus.put("Coupling Type", new ContextSensitiveMenu("Coupling Type", "Afferent+Efferent", 
        Arrays.asList("Afferent", "Efferent", "Afferent+Efferent"), 
        false));
  }

  /**
   * Return the associated DPD analysis with the given telemetry.
   * @param telemetryName the name of the given telemetry
   * @return the associated DPD, null if not found a match one.
   */
  public static String getAssociatedDpdAnalysis(final String telemetryName) {
    if (telemetryName == null || telemetryName.length() <= 0) {
      return null;
    }
    for (String analysis : analysisList) {
      if (telemetryName.contains(analysis)) {
        return analysis;
      }
    }
    return null;
  }
  /**
   * Returns a PageParameters instance that represents the content of the input form.
   * 
   * @return a PageParameters instance.
   */
  public PageParameters getPageParameters() {
    PageParameters parameters = new PageParameters();

    parameters.put(ANALYSIS_KEY, this.getAnalysis());
    parameters.put(DATE_KEY, ProjectBrowserSession.getFormattedDateString(this.date));
    parameters.put(SELECTED_PROJECTS_KEY, 
        ProjectBrowserSession.convertProjectListToString(this.getSelectedProjects()));

    return parameters;
  }
  
  /**
   * Load data from URL parameters into this session.
   * @param parameters the URL parameters
   * @return true if all parameters are loaded correctly
   */
  public boolean loadPageParameters(PageParameters parameters) {
    boolean isLoadSucceed = true;
    //boolean isDpdLoaded = false;
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

    // load dpd analysis name
    if (parameters.containsKey(ANALYSIS_KEY)) {
      String analysisString = parameters.getString(ANALYSIS_KEY);
      if (this.getAnalysisList().contains(analysisString)) {
        this.setAnalysis(analysisString);
        //isDpdLoaded = true;
      }
      else {
        isLoadSucceed = false;
        String error = "Analysis from URL parameter is unknown: " + analysisString;
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("Analysis key is missing in URL parameters.\n");
    }
    
    //load dates
    if (parameters.containsKey(DATE_KEY)) {
      String startDateString = parameters.getString(DATE_KEY);
      try {
        this.date = Tstamp.makeTimestamp(startDateString).toGregorianCalendar()
            .getTimeInMillis();
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing date from URL parameter: " + startDateString;
        logger.warning(error + " > " + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("date key is missing in URL parameters.\n");
    }
    
    //load seletecd project
    if (parameters.containsKey(SELECTED_PROJECTS_KEY)) {
      String[] projectsStringArray = 
        parameters.getString(SELECTED_PROJECTS_KEY).split(
            ProjectBrowserSession.PARAMETER_VALUE_SEPARATOR);
      List<Project> projectsList = new ArrayList<Project>();
      for (String projectString : projectsStringArray) {
        int index = projectString.lastIndexOf(ProjectBrowserSession.PROJECT_NAME_OWNER_SEPARATR);
        String projectName = projectString;
        String projectOwner = null;
        if (index > 0 && index < projectString.length()) {
          projectName = projectString.substring(0, index);
          projectOwner = projectString.substring(
              index + ProjectBrowserSession.PROJECT_NAME_OWNER_SEPARATR.length());
        }
        Project project = ProjectBrowserSession.get().getProject(projectName, projectOwner);
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
    SimpleDateFormat format = 
      new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.date));
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
   * Gets all context sensitive menus.
   * @return The context sensitive menus.
   */
  public List<ContextSensitiveMenu> getContextSensitiveMenus() {
    return new ArrayList<ContextSensitiveMenu>(this.csMenus.values());
  }
  
  /**
   * Gets the context sensitive menu with the passed name, or null if not found.
   * @param name The name of the context sensitive menu.
   * @return The menu instance, or null if not found.
   */
  public ContextSensitiveMenu getContextSensitiveMenu(String name) {
    return this.csMenus.get(name);
  }
  
  /**
   * Get the context sensitive panel holding the context sensitive menus.
   * @return The context sensitive panel.
   */
  public ContextSensitivePanel getContextSensitivePanel() { 
    return this.csPanel;
  }
  
  /**
   * Sets the panel containing the context sensitive menus.
   * @param panel The panel.
   */
  public void setContextSensitivePanel(ContextSensitivePanel panel) {
    this.csPanel = panel;
  }
  
  /**
   * Get the Coverage data model. 
   * @return The Coverage data model associated with this session.
   */
  public CoverageDataModel getCoverageDataModel() {
    return this.coverageDataModel;
  }
  
  /**
   * Gets the Unit test model associated with this session.
   * @return The Unit Test model. 
   */
  public UnitTestDataModel getUnitTestDataModel() {
    return this.unitTestDataModel;
  }
  
  
  /**
   * Gets the Coupling model associated with this session.
   * @return The Coupling model. 
   */
  public CouplingDataModel getCouplingDataModel() {
    return this.couplingDataModel;
  }
  
  /**
   * Gets the Complexity model associated with this session.
   * @return The Complexity model. 
   */
  public ComplexityDataModel getComplexityDataModel() {
    return this.complexityDataModel;
  }
  
  /**
   * Gets the Build model associated with this session.
   * @return The Build model. 
   */
  public BuildDataModel getBuildDataModel() {
    return this.buildDataModel;
  }
  
  /**
   * Gets the DevTime model associated with this session.
   * @return The DevTime model. 
   */
  public DevTimeDataModel getDevTimeDataModel() {
    return this.devTimeDataModel;
  }
  
  /**
   * Gets the FileMetric model associated with this session.
   * @return The FileMetric model. 
   */
  public FileMetricDataModel getFileMetricDataModel() {
    return this.fileMetricDataModel;
  }
  
  /**
   * Gets the Commit model associated with this session.
   * @return The Commit model. 
   */
  public CommitDataModel getCommitDataModel() {
    return this.commitDataModel;
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
   * Sets all analysis data models to their empty state. 
   */
  public void clearDataModels() {
    this.coverageDataModel.clear();
    this.unitTestDataModel.clear();
    this.couplingDataModel.clear();
    this.complexityDataModel.clear();
    this.buildDataModel.clear();
    this.devTimeDataModel.clear();
    this.fileMetricDataModel.clear();
    this.commitDataModel.clear();
  }
}
