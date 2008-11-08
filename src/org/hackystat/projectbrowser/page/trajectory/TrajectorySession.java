package org.hackystat.projectbrowser.page.trajectory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.trajectory.datapanel.TrajectoryChartDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartRef;
import org.hackystat.telemetry.service.resource.chart.jaxb.Type;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Session to hold state for trajectory.
 * 
 * @author Pavel Senin
 * @author Shaoxuan Zhang
 */
public class TrajectorySession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The parameter key of selectedProject1. */
  public static final String SELECTED_PROJECT1_KEY = "project1";
  /** The parameter key of start date of project 1. */
  public static final String START_DATE1_KEY = "startdate1";
  /** The parameter key of end dateof project 1. */
  public static final String END_DATE1_KEY = "enddate1";

  /** The parameter key of selectedProject1. */
  public static final String SELECTED_PROJECT2_KEY = "project2";
  /** The parameter key of start date of project 1. */
  public static final String START_DATE2_KEY = "startdate2";
  /** The parameter key of end dateof project 1. */
  public static final String END_DATE2_KEY = "enddate2";

  /** The parameter key of telemetry. */
  public static final String TELEMETRY_KEY = "telemetry";
  /** The parameter key of granularity. */
  public static final String GRANULARITY_KEY = "granularity";
  /** The parameter key of telemetry parameters. */
  public static final String TELEMETRY_PARAMERTERS_KEY = "param";

  /** The separator for parameter values. */
  public static final String PARAMETER_VALUE_SEPARATOR = ",";
  /** The separator between project name and its onwer. */
  public static final String PROJECT_NAME_OWNER_SEPARATR = "-";

  /** The project #1 this user has selected. */
  private ProjectRecord selectedProject1 = new ProjectRecord();
  // /** The start date this user has selected. */
  // private long project1StartDate = ProjectBrowserBasePage.getDateBefore(7).getTime();
  // /** The end date this user has selected. */
  // private long project1EndDate = ProjectBrowserBasePage.getDateBefore(1).getTime();

  /** The project #2 this user has selected. */
  private ProjectRecord selectedProject2 = new ProjectRecord();
  // /** The start date this user has selected. */
  // private long project2StartDate = ProjectBrowserBasePage.getDateBefore(7).getTime();
  // /** The end date this user has selected. */
  // private long project2EndDate = ProjectBrowserBasePage.getDateBefore(1).getTime();

  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The descriptions for all telemetries. */
  private final Map<String, TelemetryChartDefinition> telemetrys 
                                                  = new HashMap<String, TelemetryChartDefinition>();
  /** The granularity of the chart. Either Day, Week, or Month. */
  private String granularity = "Day";
  /** The available granularities. */
  private final List<String> granularityList = new ArrayList<String>();
  /** the feedback string. */
  private String feedback = "";
  /** The parameters for telemetry chart. */
  private List<IModel> parameters = new ArrayList<IModel>();
  /** The data model to hold state for data panel */
  private TrajectoryChartDataModel dataModel = new TrajectoryChartDataModel();
  /** Error message when parsing page paramters. */
  private String paramErrorMessage = "";

  /** Since I'm reusing the telemetry code, I'm keeping the way to communicate using list. */
  private List<ProjectRecord> projectList = null;

  private String dtwWindowType;

  private String dtwStep;

  private String dtwOpenEndType;

  private Integer dtwWindowSize;

  // Some string constants used while logging
  //
  private static final String SP = " ";
  private static final String CR = "\n";
  private static final String IDNT = "                ";
  private static final String IDNT2 = "                  ";
  private static final String MARK = "[DEBUG] ";
  private static final String ERROR_URL_PARAM = "Error URL parameter: project: ";
  private static final String R_HAND = " > ";

  // making life easy - preselecting the interval dates
  //
  private static final String project1StartString = "2008-01-01";
  private static final String project1EndString = "2008-01-15";

  private static final String project2StartString = "2008-03-01";
  private static final String project2EndString = "2008-03-15";

  /**
   * Constructor - create the session instance.
   */
  public TrajectorySession() {
    granularityList.add("Day");
    granularityList.add("Week");
    granularityList.add("Month");
    try {
      this.selectedProject1.setStartDate(Tstamp.makeTimestamp(project1StartString)
          .toGregorianCalendar().getTimeInMillis());
      this.selectedProject1.setEndDate(Tstamp.makeTimestamp(project1EndString)
          .toGregorianCalendar().getTimeInMillis());
      this.selectedProject2.setStartDate(Tstamp.makeTimestamp(project2StartString)
          .toGregorianCalendar().getTimeInMillis());
      this.selectedProject2.setEndDate(Tstamp.makeTimestamp(project2EndString)
          .toGregorianCalendar().getTimeInMillis());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the list of Projects associated with this user.
   * 
   * @return The list of Projects.
   */
  public List<ProjectRecord> getProjectList() {
    if (this.projectList == null || this.projectList.size() <= 0) {
      projectList = new ArrayList<ProjectRecord>();
      List<Project> projects = ProjectBrowserSession.get().getProjectList();
      for (Project p : projects) {
        ProjectRecord pr = new ProjectRecord(p, 0);
        projectList.add(pr);
      }
    }
    return projectList;
  }

  /**
   * @return the parameters
   */
  public List<IModel> getParameters() {
    return this.parameters;
  }

  /**
   * Returns the list of the parameters in a single String, separated by comma.
   * 
   * @return a String.
   */
  public String getParametersAsString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (IModel model : this.parameters) {
      if (model != null) {
        stringBuffer.append(model.getObject());
        stringBuffer.append(PARAMETER_VALUE_SEPARATOR);
      }
    }
    String param = stringBuffer.toString();
    if (param.endsWith(PARAMETER_VALUE_SEPARATOR)) {
      param = param.substring(0, param.length() - 1);
    }
    return param;
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
   * @param telemetryName the telemetry to set
   */
  public void setTelemetryName(String telemetryName) {
    this.telemetryName = telemetryName;
  }

  /**
   * @return the telemetry
   */
  public String getTelemetryName() {
    return this.telemetryName;
  }

  /**
   * Get the logger.
   * 
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

  /**
   * Return the TelemetryList. Returns empty list if no telemetry streams data collected.
   * 
   * @return the telemetryList
   */
  public List<String> getTelemetryList() {
    Logger logger = getLogger();
    List<String> telemetryList = new ArrayList<String>();
    if (this.getTelemetrys().isEmpty()) {
      TelemetryClient client = ProjectBrowserSession.get().getTelemetryClient();
      try {
        logger.info("Retrieving data for Telemetry chart definitions.");
        for (TelemetryChartRef chartRef : client.getChartIndex().getTelemetryChartRef()) {
          getTelemetrys().put(chartRef.getName(), client.getChartDefinition(chartRef.getName()));
        }
        logger.info("Finished retrieving data for Telemetry chart definitions.");
      }
      catch (TelemetryClientException e) {
        this.feedback = "Exception when retrieving Telemetry chart definition: " + e.getMessage();
        logger.warning("Error when retrieving Telemetry chart definition: " + e.getMessage());
      }
    }
    telemetryList.addAll(this.getTelemetrys().keySet());
    Collections.sort(telemetryList);
    return telemetryList;
  }

  /**
   * Return the list of ParameterDefinition under telemetry type in this session.
   * 
   * @return list of ParameterDefinition.
   */
  public List<ParameterDefinition> getParameterList() {
    Logger logger = getLogger();
    if (this.telemetryName != null) {
      TelemetryChartDefinition teleDef = this.getTelemetrys().get(telemetryName);
      if (teleDef == null) {
        TelemetryClient client = ProjectBrowserSession.get().getTelemetryClient();
        try {
          logger.info("Retrieving chart definition of telemetry: " + this.telemetryName);
          teleDef = client.getChartDefinition(this.telemetryName);
          this.getTelemetrys().put(telemetryName, teleDef);
          return teleDef.getParameterDefinition();
        }
        catch (TelemetryClientException e) {
          this.feedback = "Error when retrieving chart definition of telemetry: "
              + this.telemetryName + ">>" + e.getMessage();
          logger.warning("Error when retrieving chart definition of telemetry: "
              + this.telemetryName + ">>" + e.getMessage());
        }
      }
      else {
        return teleDef.getParameterDefinition();
      }
    }
    return new ArrayList<ParameterDefinition>();
  }

  /**
   * @param startDate the startDate to set
   */
  public void setProject1StartDate(Date startDate) {
    this.selectedProject1.setStartDate(startDate.getTime());
  }

  /**
   * @return the startDate
   */
  public Date getProject1StartDate() {
    return new Date(this.selectedProject1.getStartDate().getTime());
  }

  /**
   * @param endDate the endDate to set
   */
  public void setProject1EndDate(Date endDate) {
    this.selectedProject1.setEndDate(endDate.getTime());
  }

  /**
   * @return the endDate
   */
  public Date getProject1EndDate() {
    return new Date(this.selectedProject1.getEndDate().getTime());
  }

  /**
   * @param startDate the startDate to set
   */
  public void setProject2StartDate(Date startDate) {
    this.selectedProject2.setStartDate(startDate.getTime());
  }

  /**
   * @return the startDate
   */
  public Date getProject2StartDate() {
    return new Date(this.selectedProject2.getStartDate().getTime());
  }

  /**
   * @param endDate the endDate to set
   */
  public void setProject2EndDate(Date endDate) {
    this.selectedProject2.setEndDate(endDate.getTime());
  }

  /**
   * @return the endDate
   */
  public Date getProject2EndDate() {
    return new Date(this.selectedProject2.getEndDate().getTime());
  }

  /**
   * Returns the string that represents startDate in standard formatted. e.g.
   * 2008-08-08T08:08:08+08:00, the +08:00 in the end means the time zone of this time stamp is
   * +08:00
   * 
   * @return a String
   */
  public String getFormattedProject1StartDateString() {
    return Tstamp.makeTimestamp(this.selectedProject1.getStartDate().getTime()).toString();
  }

  /**
   * Returns the string that represents endDate in standard formatted. e.g.
   * 2008-08-08T08:08:08+08:00, the +08:00 in the end means the time zone of this time stamp is
   * +08:00
   * 
   * @return a String
   */
  public String getFormattedProject1EndDateString() {
    return Tstamp.makeTimestamp(this.selectedProject1.getEndDate().getTime()).toString();
  }

  /**
   * Returns the string that represents startDate in standard formatted. e.g.
   * 2008-08-08T08:08:08+08:00, the +08:00 in the end means the time zone of this time stamp is
   * +08:00
   * 
   * @return a String
   */
  public String getFormattedProject2StartDateString() {
    return Tstamp.makeTimestamp(this.selectedProject2.getStartDate().getTime()).toString();
  }

  /**
   * Returns the string that represents endDate in standard formatted. e.g.
   * 2008-08-08T08:08:08+08:00, the +08:00 in the end means the time zone of this time stamp is
   * +08:00
   * 
   * @return a String
   */
  public String getFormattedProject2EndDateString() {
    return Tstamp.makeTimestamp(this.selectedProject2.getEndDate().getTime()).toString();
  }

  /**
   * Set the project1 indent.
   * 
   * @param project1Indent The indent to set.
   */
  public void setProject1Indent(Integer project1Indent) {
    this.selectedProject1.setIndent(project1Indent);
  }

  /**
   * Get the project1 indent.
   * 
   * @return The project1 indent.
   */
  public Integer getProject1Indent() {
    return this.selectedProject1.getIndent();
  }

  /**
   * Set the project2 indent.
   * 
   * @param project2Indent The indent to set.
   */
  public void setProject2Indent(Integer project2Indent) {
    this.selectedProject2.setIndent(project2Indent);
  }

  /**
   * Get the project2 indent.
   * 
   * @return The project2 indent.
   */
  public Integer getProject2Indent() {
    return this.selectedProject2.getIndent();
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
   * @return the granularityList
   */
  public List<String> getGranularityList() {
    return granularityList;
  }

  /**
   * Update the data model.
   */
  public void updateDataModel() {
    getLogger().log(
        Level.FINER,
        MARK + "TrajectorySession: " + "setting TrajectoryChartDataModel with next parameters:" +

        CR + IDNT + "selectedProject1: " + getSelectedProject1().getProject().getName() + CR + IDNT
            + IDNT2 + "startDate: " + getProject1StartDate() + CR + IDNT + IDNT2 + "endDate: "
            + getProject1EndDate() + CR + IDNT + IDNT2 + "indent: " + getProject1Indent() +

            CR + IDNT + "selectedProject2: " + getSelectedProject2().getProject().getName() + CR
            + IDNT + IDNT2 + "startDate: " + getProject2StartDate() + CR + IDNT + IDNT2
            + "endDate: " + getProject2EndDate() + CR + IDNT + IDNT2 + "indent: "
            + getProject2Indent() +

            CR + IDNT + "telemetry: " + telemetryName + CR + IDNT + "granularity: " + granularity
            + CR + IDNT + "parameters: " + CR + parametersToLog(parameters));

    this.dataModel.setModel(getSelectedProject1(), getSelectedProject2(), telemetryName,
        granularity, parameters);
    Thread thread = new Thread() {
      @Override
      public void run() {
        dataModel.loadData();
      }
    };
    thread.start();
  }

  /**
   * Cancel data model's update.
   */
  public void cancelDataUpdate() {
    getLogger().info(
        MARK + "TrajectorySession " + this.hashCode() + " cancelling updateDataModel()");
    dataModel.cancelDataLoading();
  }

  /**
   * Set the data model.
   * 
   * @param dataModel the dataModel to set
   */
  public void setDataModel(TrajectoryChartDataModel dataModel) {
    this.dataModel = dataModel;
  }

  /**
   * Get the data model.
   * 
   * @return the dataModel
   */
  public TrajectoryChartDataModel getDataModel() {
    return dataModel;
  }

  /**
   * Set the project1.
   * 
   * @param rec the selectedProjects to set
   */
  public void setSelectedProject1(ProjectRecord rec) {
    this.selectedProject1 = rec;
  }

  /**
   * Set the project1.
   * 
   * @return the project1.
   */
  public ProjectRecord getSelectedProject1() {
    return selectedProject1;
  }

  /**
   * Set the project2.
   * 
   * @param rec the selectedProjects to set
   */
  public void setSelectedProject2(ProjectRecord rec) {
    this.selectedProject2 = rec;
  }

  /**
   * Set the project2.
   * 
   * @return the project2.
   */
  public ProjectRecord getSelectedProject2() {
    return selectedProject2;
  }

  /**
   * Returns the list of the selected projects in a single String, separated by comma.
   * 
   * @return a String.
   */
  public String getSelectedProject1AsString() {
    if (null == selectedProject1) {
      return "null";
    }
    else {
      StringBuffer projectStr = new StringBuffer();
      projectStr.append(selectedProject1.getProject().getName());
      projectStr.append(PROJECT_NAME_OWNER_SEPARATR);
      projectStr.append(selectedProject1.getProject().getOwner());
      return projectStr.toString();
    }
  }

  /**
   * Returns the list of the selected projects in a single String, separated by comma.
   * 
   * @return a String.
   */
  public String getSelectedProject2AsString() {
    if (null == selectedProject2) {
      return "null";
    }
    else {
      StringBuffer projectStr = new StringBuffer();
      projectStr.append(selectedProject2.getProject().getName());
      projectStr.append(PROJECT_NAME_OWNER_SEPARATR);
      projectStr.append(selectedProject2.getProject().getOwner());
      return projectStr.toString();
    }
  }

  /**
   * @return the telemetrys
   */
  public Map<String, TelemetryChartDefinition> getTelemetrys() {
    return telemetrys;
  }

  /**
   * @return the list of TelemetryChartDefinition.
   */
  public List<TelemetryChartDefinition> getChartDescriptions() {
    List<TelemetryChartDefinition> chartDef = new ArrayList<TelemetryChartDefinition>();
    for (String telemetryName : this.getTelemetryList()) {
      chartDef.add(this.telemetrys.get(telemetryName));
    }
    return chartDef;
  }

  /**
   * Returns a Project instance that is available to current user and is matched to the given
   * project name and project owner.
   * 
   * @param projectName the given project name.
   * @param projectOwner the given project owner.
   * @return the Project instance. null if no matching project is found, which may means either the
   *         project name or project owner is null or there is no Project for this user with the
   *         same project name and owner as the given ones.
   */
  public Project getProject(String projectName, String projectOwner) {
    if (projectName == null || projectOwner == null) {
      return null;
    }
    for (Project project : ProjectBrowserSession.get().getProjectList()) {
      if (projectName.equals(project.getName()) && projectOwner.equals(project.getOwner())) {
        return project;
      }
    }
    return null;
  }

  /**
   * Reports a list of available DTW steps in this implementation.
   * 
   * @return the list of steps to use in the display menu.
   */
  public List<String> getAvailableDTWSteps() {
    List<String> steps = new ArrayList<String>();
    steps.add("symmetric1");
    steps.add("symmetric2");
    steps.add("asymmetric");
    Collections.sort(steps);
    return steps;
  }

  /**
   * Set particular DTW step to use in the analysis.
   * 
   * @param step the step to set.
   */
  public void setDTWStep(String step) {
    this.dtwStep = step;
  }

  /**
   * Get the set step function.
   * 
   * @return the step function set for analysis.
   */
  public String getDTWStep() {
    return this.dtwStep;
  }

  /**
   * Get the list of available implementations of DTW constraints.
   * 
   * @return the list of available DTW constraints functions to use in the form menu.
   */
  public List<String> getAvailableWindowTypes() {
    List<String> windows = new ArrayList<String>();
    windows.add("No Window");
    windows.add("SakoeChiba");
    windows.add("Slanted Band");
    windows.add("Itakura Window");
    // Collections.sort(windows);
    return windows;
  }

  /**
   * Set the specific constraint function.
   * 
   * @param constraint the constraint function to set.
   */
  public void setWindowType(String constraint) {
    this.dtwWindowType = constraint;
  }

  /**
   * Get the set constraint function for the DTW analysis.
   * 
   * @return the set function.
   */
  public String getWindowType() {
    return this.dtwWindowType;
  }

  /**
   * Set the window (band) size for the constraint function.
   * 
   * @param size the window (band) size.
   */
  public void setWindowSize(Integer size) {
    this.dtwWindowSize = size;
  }

  /**
   * Get the window (band) size for constraint function.
   * 
   * @return the window size.
   */
  public Integer getWindowSize() {
    return this.dtwWindowSize;
  }

  /**
   * Returns the list of available DTW open-end alignment implementations.
   * 
   * @return the list of available alignment implementations to use in the form menu.
   */
  public List<String> getAvailableOpenEndTypes() {
    List<String> openEnds = new ArrayList<String>();
    openEnds.add("Open Begin");
    openEnds.add("Open End");
    // Collections.sort(openEnds);
    return openEnds;
  }

  /**
   * Set the specific opend-end alignment implementation.
   * 
   * @param openEnd the open-end alignment implementation.
   */
  public void setOpenEndType(String openEnd) {
    this.dtwOpenEndType = openEnd;
  }

  /**
   * Get the open-end alignment set for DTW analysis.
   * 
   * @return the open-end alignment implementation.
   */
  public String getOpenEndType() {
    return this.dtwOpenEndType;
  }

  /**
   * Load data from URL parameters into this session.
   * 
   * @param parameters the URL parameters
   * @return true if all parameters are loaded correctly
   */
  public boolean loadPageParameters(PageParameters parameters) {
    boolean isLoadSucceed = true;
    boolean isTelemetryLoaded = false;
    StringBuffer errorMessage = new StringBuffer(1000);
    Logger logger = this.getLogger();

    // load selected projects
    //
    if (parameters.containsKey(SELECTED_PROJECT1_KEY)) {
      String projectString = parameters.getString(SELECTED_PROJECT1_KEY);
      String[] projectInfo = projectString.split(PROJECT_NAME_OWNER_SEPARATR, 2);
      if (projectInfo.length < 1) {
        isLoadSucceed = false;
        String error = ERROR_URL_PARAM + projectString
            + " >> project name and owner are missing or not formatted correctly.";
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
      String projectName = projectInfo[0];
      String projectOwner = projectInfo[1];
      Project project = this.getProject(projectName, projectOwner);
      if (project == null) {
        isLoadSucceed = false;
        String error = ERROR_URL_PARAM + projectString
            + " >> matching project not found under user: "
            + ProjectBrowserSession.get().getEmail();
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
      else {
        selectedProject1 = new ProjectRecord(project, 0);
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("projects key is missing in URL parameters.\n");
    }
    // load start date
    if (parameters.containsKey(START_DATE1_KEY)) {
      String startDateString = parameters.getString(START_DATE1_KEY);
      try {
        this.selectedProject1.setStartDate(Tstamp.makeTimestamp(startDateString)
            .toGregorianCalendar().getTimeInMillis());
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing start date from URL parameter: " + startDateString;
        logger.warning(error + R_HAND + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("startDate key is missing in URL parameters.");
    }
    // load end date
    if (parameters.containsKey(END_DATE1_KEY)) {
      String endDateString = parameters.getString(END_DATE1_KEY);
      try {
        this.selectedProject1.setEndDate(Tstamp.makeTimestamp(endDateString).toGregorianCalendar()
            .getTimeInMillis());
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing end date from URL parameter: " + endDateString;
        logger.warning(error + R_HAND + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("endDate key is missing in URL parameters.\n");
    }

    // load selected projects
    //
    if (parameters.containsKey(SELECTED_PROJECT2_KEY)) {
      String projectString = parameters.getString(SELECTED_PROJECT2_KEY);
      String[] projectInfo = projectString.split(PROJECT_NAME_OWNER_SEPARATR, 2);
      if (projectInfo.length < 1) {
        isLoadSucceed = false;
        String error = ERROR_URL_PARAM + projectString
            + " >> project name and owner are missing or not formatted correctly.";
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
      String projectName = projectInfo[0];
      String projectOwner = projectInfo[1];
      Project project = this.getProject(projectName, projectOwner);
      if (project == null) {
        isLoadSucceed = false;
        String error = ERROR_URL_PARAM + projectString
            + " >> matching project not found under user: "
            + ProjectBrowserSession.get().getEmail();
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
      else {
        selectedProject2 = new ProjectRecord(project, 0);
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("projects key is missing in URL parameters.\n");
    }
    // load start date
    if (parameters.containsKey(START_DATE2_KEY)) {
      String startDateString = parameters.getString(START_DATE2_KEY);
      try {
        this.selectedProject2.setStartDate(Tstamp.makeTimestamp(startDateString)
            .toGregorianCalendar().getTimeInMillis());
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing start date from URL parameter: " + startDateString;
        logger.warning(error + R_HAND + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("startDate key is missing in URL parameters.");
    }
    // load end date
    if (parameters.containsKey(END_DATE2_KEY)) {
      String endDateString = parameters.getString(END_DATE2_KEY);
      try {
        this.selectedProject2.setEndDate(Tstamp.makeTimestamp(endDateString).toGregorianCalendar()
            .getTimeInMillis());
      }
      catch (Exception e) {
        isLoadSucceed = false;
        String error = "Errors when parsing end date from URL parameter: " + endDateString;
        logger.warning(error + R_HAND + e.getMessage());
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("endDate key is missing in URL parameters.\n");
    }

    // load telemetry name
    if (parameters.containsKey(TELEMETRY_KEY)) {
      String telemetryString = parameters.getString(TELEMETRY_KEY);
      if (this.getTelemetryList().contains(telemetryString)
          && telemetryString.equalsIgnoreCase("Build")) {
        this.setTelemetryName(telemetryString);
        isTelemetryLoaded = true;
      }
      else {
        isLoadSucceed = false;
        String error = "Telemetry from URL parameter is unknown: " + telemetryString;
        logger.warning(error);
        errorMessage.append(error);
        errorMessage.append('\n');
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("Telemetry key is missing in URL parameters.\n");
    }
    // load granularity
    if (parameters.containsKey(GRANULARITY_KEY)) {
      String granularityString = parameters.getString(GRANULARITY_KEY);
      if (this.granularityList.contains(granularityString)) {
        this.setGranularity(granularity);
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
      errorMessage.append("granularity key is missing in URL parameters.\n");
    }
    // load telemetry parameters
    if (parameters.containsKey(TELEMETRY_PARAMERTERS_KEY)) {
      String paramString = parameters.getString(TELEMETRY_PARAMERTERS_KEY);
      String[] paramStringArray = paramString.split(PARAMETER_VALUE_SEPARATOR);
      this.parameters.clear();
      if (isTelemetryLoaded) {
        List<ParameterDefinition> paramDefList = this.getTelemetrys().get(this.telemetryName)
            .getParameterDefinition();
        if (paramStringArray.length == paramDefList.size()) {
          for (int i = 0; i < paramStringArray.length; ++i) {
            if (isValueMatchType(paramStringArray[i], paramDefList.get(i).getType())) {
              this.parameters.add(new Model(paramStringArray[i]));
            }
            else {
              isLoadSucceed = false;
              String error = "Telemetry parameter: " + paramStringArray[i] + " is not matched to"
                  + " type: " + paramDefList.get(i).getType().getName();
              logger.warning(error);
              errorMessage.append(error);
              errorMessage.append('\n');
            }
          }
        }
        else {
          isLoadSucceed = false;
          String error = "Error in URL parameters: telemetry parameters: "
              + parameters.getString(TELEMETRY_PARAMERTERS_KEY) + "(" + paramStringArray.length
              + ") >> number of parameters not matched to definition within telemetry: "
              + this.telemetryName + "(" + paramDefList.size() + ")";
          logger.warning(error);
          errorMessage.append(error);
          errorMessage.append('\n');
        }
      }
    }
    else {
      isLoadSucceed = false;
      errorMessage.append("param key is missing in URL parameters.\n");
    }
    if (errorMessage.length() > 0) {
      this.paramErrorMessage = errorMessage.toString();
    }
    return isLoadSucceed;
  }

  /**
   * Checks if the given value is of the given type.
   * 
   * @param value the given value.
   * @param type the given type.
   * @return true if the value and type are matched.
   */
  private boolean isValueMatchType(String value, Type type) {
    if ("Enumerated".equals(type.getName())) {
      return type.getValue().contains(value);
    }
    else if ("Boolean".equals(type.getName())) {
      return "true".equals(value) || "false".equals(value);
    }
    else if ("Integer".equals(type.getName())) {
      try {
        Integer.valueOf(value);
        return true;
      }
      catch (NumberFormatException e) {
        return false;
      }
    }
    else if ("Text".equals(type.getName())) {
      return true;
    }
    return false;
  }

  /**
   * Returns a PageParameters instance that represents the content of the input form.
   * 
   * @return a PageParameters instance.
   */
  public PageParameters getPageParameters() {
    PageParameters parameters = new PageParameters();

    parameters.put(SELECTED_PROJECT1_KEY, this.getSelectedProject1AsString());
    parameters.put(SELECTED_PROJECT1_KEY, this.getSelectedProject2AsString());
    parameters.put(START_DATE1_KEY, this.getFormattedProject1StartDateString());
    parameters.put(END_DATE1_KEY, this.getFormattedProject1EndDateString());
    parameters.put(START_DATE2_KEY, this.getFormattedProject2StartDateString());
    parameters.put(END_DATE2_KEY, this.getFormattedProject2EndDateString());
    parameters.put(TELEMETRY_KEY, this.getTelemetryName());
    parameters.put(GRANULARITY_KEY, this.getGranularity());
    parameters.put(TELEMETRY_PARAMERTERS_KEY, this.getParametersAsString());
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
   * Helps to format parameters list.
   * 
   * @param parameters The parameters list.
   * @return formatted parameters log message.
   */
  private String parametersToLog(List<IModel> parameters) {
    StringBuffer sb = new StringBuffer(500);
    for (IModel im : parameters) {
      sb.append(IDNT + IDNT2 + im.getClass() + ": " + im.getObject() + CR);
    }
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer sb = new StringBuffer(1024);
    sb.append("TrajectorySession: " + CR);
    sb.append(IDNT + "project1: " + getSelectedProject1AsString() + CR + IDNT
        + getFormattedProject1StartDateString() + SP + getFormattedProject1EndDateString() + CR);
    sb.append(IDNT + "project2 " + getSelectedProject2AsString() + CR + IDNT
        + getFormattedProject2StartDateString() + SP + getFormattedProject2EndDateString() + CR);
    sb.append(IDNT + "hash code: " + this.hashCode());
    return sb.toString();
  }

}
