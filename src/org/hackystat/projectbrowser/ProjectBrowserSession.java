package org.hackystat.projectbrowser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioSession;
import org.hackystat.projectbrowser.page.projects.ProjectsSession;
import org.hackystat.projectbrowser.page.sensordata.SensorDataSession;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.projectbrowser.page.todate.ToDateSession;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.utilities.logger.HackystatLogger;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a session instance that holds authentication credentials.
 * @author Philip Johnson
 *
 */
public class ProjectBrowserSession extends WebSession {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The email used to connect to the SensorBase. */
  private String email = null;
  /** The password for the SensorBase. */
  private String password = null;
  /** The SensorBase client for this user. */
  // Need to make this class serializable if we want to keep it in the session and not
  // make a new one each request. 
  //private SensorBaseClient client = null; 
  /** The current signinFeedback message to display. */
  private String signinFeedback = "";
  /** The current registerFeedback message to display. */
  private String registerFeedback = "";
  /** If this user has been authenticated against the Sensorbase during this session. */
  private boolean isAuthenticated = false;
  /** The collection of Projects that this user has. */
  //private Map<String, Project> projectMap = null;
  /** The collection of Projects that this user has. */
  private List<Project> projectList = null;
  /** The analysis list. */
  public List<String> analysisList = new ArrayList<String>();

  /** The separator for parameter values. */
  public static final String PARAMETER_VALUE_SEPARATOR = ",";
  /** The separator between project name and its onwer. */
  public static final String PROJECT_NAME_OWNER_SEPARATR = "::";
  
  /** The SensorDataSession that holds page state for SensorData page. */
  private SensorDataSession sensorDataSession = new SensorDataSession();
  /** The DailyProjectDataSession that holds page state for DailyProjectData page. */
  private DailyProjectDataSession dailyProjectDataSession = new DailyProjectDataSession();
  /** The TelemetrySession that holds page state for Telemetry page. */
  private TelemetrySession telemetrySession = new TelemetrySession();
  /** ProjectPortfolioSession that holds page state for Project Portfolio page. */
  private ProjectPortfolioSession projectPortfolioSession = new ProjectPortfolioSession();
  /** The TelemetrySession that holds page state for Telemetry page. */
  private ToDateSession toDateSession = new ToDateSession();
  /** ProjectBrowserSession that holds the page state for the Project page. */
  private ProjectsSession projectsSession = new ProjectsSession();
  /** Trajectory session - holds the page state for the Trajectory page. */
  private TrajectorySession trajectorySession = new TrajectorySession();

  
  /**
   * Provide a constructor that initializes WebSession.
   * @param request The request object.
   */
  public ProjectBrowserSession(Request request) {
    super(request);
    //analysisList.add("Build");
    analysisList.add("Unit Test");
    analysisList.add("Coverage");
    //analysisList.add("Complexity");
    //analysisList.add("Coupling");
  }

  /**
   * Obtain the current session. 
   * @return The current ProjectBrowserSession.
   */
  public static ProjectBrowserSession get() {
    return (ProjectBrowserSession) Session.get();
  }
  
  /**
   * Returns true if the user has been authenticated in this session.
   * @return True if the user has supplied a valid email and password for this sensorbase.
   */
  public boolean isAuthenticated() {
    return this.isAuthenticated;
  }
  
  /**
   * Used by the Signin form to provide the SensorBase authentication credentials to this session. 
   * @param user The user.
   * @param password The password. 
   */
  public void setCredentials(String user, String password) {
    this.email = user;
    this.password = password;
  }
  
  /**
   * Returns the string to be displayed in the SigninFeedback label.
   * @return A signin feedback string. 
   */
  public String getSigninFeedback() {
    return this.signinFeedback;
  }
  
  /**
   * Allows other components to set the feedback string for the signin form.
   * @param signinFeedback The message to be displayed.
   */
  public void setSigninFeedback(String signinFeedback) {
    this.signinFeedback = signinFeedback;
  }
  
  /**
   * Allows other components to set the feedback string for the register form.
   * @param registerFeedback The message to be displayed.
   */
  public void setRegisterFeedback(String registerFeedback) {
    this.registerFeedback = registerFeedback;
  }
  
  /**
   * Returns the string to be displayed in the registerFeedback label.
   * @return A register feedback string. 
   */
  public String getRegisterFeedback() {
    return this.registerFeedback;
  }
  
  /**
   * Returns true if this email/password combination is valid for this sensorbase. 
   * @param email The email. 
   * @param password The password.
   * @return True if valid for this sensorbase. 
   */
  public boolean signin(String email, String password) {
    try {
      String host = ((ProjectBrowserApplication)getApplication()).getSensorBaseHost();
      int timeout = 1000 * 60 * 60; 
      //HACK!  This shouldn't be required! I'm doing it because I get a timeout in SensorData
      // when trying to retrieve Hackystat data summaries for a month. 
      System.getProperties().setProperty("sensorbaseclient.timeout", String.valueOf(timeout));
      SensorBaseClient client = new SensorBaseClient(host, email, password);
      // Set timeout to 60 minutes.
      client.setTimeout(timeout);
      client.authenticate();
      this.email = email;
      this.password = password;
      this.isAuthenticated = true;
      return true;
    }
    catch (Exception e) {
      this.isAuthenticated = false;
      return false;
    }
  }
  
  /**
   * Returns a SensorBaseClient instance for this user and session. 
   * @return The SensorBaseClient instance. 
   */
  public SensorBaseClient getSensorBaseClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getSensorBaseHost();
    return new SensorBaseClient(host, this.email, this.password);
  }
  
  /**
   * Returns a TelemetryClient instance for this user and session.
   * @return The TelemetryClient instance. 
   */
  public TelemetryClient getTelemetryClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getTelemetryHost();
    return new TelemetryClient(host, this.email, this.password);
  }
  
  /**
   * Returns a DailyProjectDataClient instance for this user and session.
   * @return The DailyProjectDataClient instance. 
   */
  public DailyProjectDataClient getDailyProjectDataClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getDailyProjectDataHost();
    return new DailyProjectDataClient(host, this.email, this.password);
  }
  
  /**
   * Gets the user's email associated with this session. 
   * @return The user.
   */
  public String getUserEmail() {
    return this.email;
  }
  
  /**
   * Returns the list of project names associated with this user.
   * @return The list of project names. 
   */
  /*
  public List<String> getProjectNames() {
    List<String> projectNames = new ArrayList<String>();
    projectNames.addAll(getProjects().keySet());
    Collections.sort(projectNames);
    return projectNames;
  }
  */
  
  /**
   * Return a map of project names to project instances associated with this user.  
   * If the map has not yet been built, get it from the SensorBase and cache it. 
   * @return The map of Project instances. 
   */
  /*
  public Map<String, Project> getProjects() {
    if (this.projectMap == null) {
      this.projectMap = new HashMap<String, Project>();
      try {
        SensorBaseClient sensorBaseClient = ProjectBrowserSession.get().getSensorBaseClient();
        ProjectIndex projectIndex = sensorBaseClient.getProjectIndex(this.email);
        Set<String> duplicatedProjectNames = new TreeSet<String>();
        for (ProjectRef projectRef : projectIndex.getProjectRef()) {
          Project project = sensorBaseClient.getProject(projectRef);
          Project temp = projectMap.put(project.getName(), project);
          if (temp != null) {
            duplicatedProjectNames.add(project.getName());
            projectMap.put(temp.getName() + " - " + temp.getOwner(), temp);
            projectMap.put(project.getName() + " - " + project.getOwner(), project);
          }
        }
        for (String duplicatedProjectName : duplicatedProjectNames) {
          projectMap.remove(duplicatedProjectName);
        }
      }
      catch (SensorBaseClientException e) {
        Logger logger = ((ProjectBrowserApplication)getApplication()).getLogger();
        logger.warning("Error getting projects for " + this.email + StackTrace.toString(e));
      }
    }
    return this.projectMap;
  }
  */

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
    for (Project project : getProjectList()) {
      if (projectName.equals(project.getName()) && 
          (projectOwner == null || projectOwner.equals(project.getOwner()))) {
          return project;
      }
    }
    return null;
  }
  

  /**
   * Returns a single String represents a list of the projects, separated by comma.
   * @param projects a list of selected projects.
   * @return a String.
   */
  public static String convertProjectListToString(List<Project> projects) {
    StringBuffer projectList = new StringBuffer();
    for (int i = 0; i < projects.size(); ++i) {
      Project project = projects.get(i);
      if (project == null) {
        continue;
      }
      projectList.append(project.getName());
      projectList.append(PROJECT_NAME_OWNER_SEPARATR);
      projectList.append(project.getOwner());
      if (i < projects.size() - 1) {
        projectList.append(PARAMETER_VALUE_SEPARATOR);
      }
    }
    return projectList.toString();
  }

  /**
   * Returns the string that represents the given date in standard formatted.
   * e.g. 2008-08-08T08:08:08+08:00, 
   * the +08:00 in the end means the time zone of this time stamp is +08:00
   * @param date the given date
   * @return a String
   */
  public static String getFormattedDateString(long date) {
    return Tstamp.makeTimestamp(date).toString();
  }
  
  /**
   * Return the project associated with the given id.
   * Id is usually the project name. In case of projects with the same name, the id will become
   * projectName - projectOwner
   * @param projectNameId the given id
   * @return the result project, null if not found.
   */
  /*
  public Project getProjectByNameId(String projectNameId) {
    return this.projectMap.get(projectNameId);
  }
  */
  
  /**
   * Clear project list.
   */
  public void clearProjectList() {
    if (this.projectList != null) {
      this.projectList.clear();
    }
  }
  
  /**
   * Returns the list of Projects associated with this user. 
   * @return The list of Projects. 
   */
  public List<Project> getProjectList() {
    if (this.projectList == null || this.projectList.size() <= 0) {
      projectList = new ArrayList<Project>();
      try {
        SensorBaseClient sensorBaseClient = ProjectBrowserSession.get().getSensorBaseClient();
        ProjectIndex projectIndex = sensorBaseClient.getProjectIndex(this.email);
        for (ProjectRef projectRef : projectIndex.getProjectRef()) {
          projectList.add(sensorBaseClient.getProject(projectRef));
        }
        Collections.sort(projectList, new Comparator<Project>() {
          public int compare(final Project project1, final Project project2) {
            int result = project1.getName().compareTo(project2.getName());
            if (result == 0) {
              result = project1.getOwner().compareTo(project2.getOwner());
            }
            return result;
          }
        });
      }
      catch (SensorBaseClientException e) {
        Logger logger = ((ProjectBrowserApplication)getApplication()).getLogger();
        logger.warning("Error getting projects for " + this.email + ": " + e.getMessage());
      }
    }
    return projectList;
  }


  /**
   * @return the Default project.
   */
  public Project getDefaultProject() {
    for (Project project : this.getProjectList()) {
      if ("Default".equals(project.getName())) {
        return project;
      }
    }
    return null;
  }
  
  /**
   * Returns the SensorDataSession instance. 
   * @return The session state for the sensor data page. 
   */
  public SensorDataSession getSensorDataSession() {
    return this.sensorDataSession;
  }

  /**
   * Returns the DailyProjectDataSession instance. 
   * @return The session state for the daily project data page. 
   */
  public DailyProjectDataSession getDailyProjectDataSession() {
    return this.dailyProjectDataSession;
  }

  /**
   * Returns the TelemetrySession instance. 
   * @return The session state for the telemetry page. 
   */
  public TelemetrySession getTelemetrySession() {
    return this.telemetrySession;
  }

  /**
   * Returns the ToDateSession instance. 
   * @return The session state for the ToDate page. 
   */
  public ToDateSession getToDateSession() {
    return this.toDateSession;
  }
  
  /**
   * Returns the ProjectsDataSession instance. 
   * @return The session state for the projects data page. 
   */
  public ProjectsSession getProjectsSession() {
    return this.projectsSession;
  }
  
  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the projectPortfolioSession
   */
  public ProjectPortfolioSession getProjectPortfolioSession() {
    return projectPortfolioSession;
  }
  
  /**
   * Log the user usage to special file.
   * @param log the log message
   */
  public void logUsage(String log) {
    if (((ProjectBrowserApplication)getApplication()).isLoggingUserUsage()) {
      Logger logger = 
        HackystatLogger.getLogger("org.hackystat.projectbrowser.usage", "projectbrowser");
      logger.info("[" + email + "]" + " " + log);
    }
  }
  
  /**
   * Print the indexed page parameters in format of /parameter0/parameter1/.../.
   * @param parameters the indexed page parameter
   * @return the result string.
   */
  public String printPageParameters(PageParameters parameters) {
    StringBuffer paramString = new StringBuffer();
    int i = 0;
    boolean end = false;
    while (!end) {
      String key = String.valueOf(i);
      if (parameters.containsKey(key)) {
        paramString.append('/');
        paramString.append(parameters.get(key).toString());
        ++i;
      }
      else {
        end = true;
      }
    }
    return paramString.toString();
  }
  
  /**
   * @return the logger that associated to this web application.
   */
  public Logger getLogger() {
    return ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
  }

  /**
   * Returns the TrajectorySession instance. 
   * @return The session state for the trajectory page. 
   */
  public TrajectorySession getTrajectorySession() {
    return this.trajectorySession;
  }
  }
