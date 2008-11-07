package org.hackystat.projectbrowser.page.todate.detailpanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.loadingprocesspanel.Processable;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.sensorbase.resource.projects.ProjectUtils;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartData;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.logger.HackystatLogger;
import org.hackystat.utilities.tstamp.Tstamp;
import org.hackystat.utilities.uricache.UriCache;

/**
 * Data model to hold state of Project Portfolio.
 * 
 * @author Shaoxuan Zhang
 */
public class ToDateDataModel implements Serializable, Processable {

  /** Support serialization. */
  private static final long serialVersionUID = 5465041655927215391L;
  /** state of data loading process. */
  private volatile boolean inProcess = false;
  /** result of data loading. */
  private volatile boolean complete = false;
  /** message to display when data loading is in process. */
  private String processingMessage = "";

  /** The start date to look for project's data. */
  private long startDate;
  /** host of the telemetry host. */
  private String telemetryHost;
  /** email of the user. */
  private String userEmail;
  /** password of the user. */
  private String password;
  /** The telemetry session. */
  private TelemetrySession telemetrySession;

  /** the granularity this data model focus. */
  private String granularity = "Day";

  /**
   * the time phrase this data model focus. In scale of telemetryGranularity, from current to the
   * past.
   */
  // private int timePhrase = 5;
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  
  /** The measure configurations. */
  private final List<ToDateMeasureConfiguration> measures = 
    new ArrayList<ToDateMeasureConfiguration>();
  private final Map<Project, List<String>> projectData = new HashMap<Project, List<String>>();
  /** The configuration saving capacity. */
  private static Long capacity = 1000L;
  /** The max life of the saved configuration. */
  private static Double maxLife = 300.0;


  /**
   * Constructor that initialize the measures.
   * 
   * @param telemetryHost the telemetry host
   * @param email the user's email
   * @param password the user's passowrd
   */
  public ToDateDataModel(String telemetryHost, String email, String password) {
    telemetrySession = ProjectBrowserSession.get().getTelemetrySession();
    this.telemetryHost = telemetryHost;
    this.userEmail = email;
    this.password = password;
    try {
      startDate = Tstamp.makeTimestamp("2008-01-01").toGregorianCalendar().getTimeInMillis();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    this.initializeMeasures();
    this.loadUserConfiguration();
    // List<ParameterDefinition> paramDefList =
    // telemetrySession.getParameterList(measure.getName());
  }

  /**
   * @param selectedProjects the selected projects.
   */
  public void setModel(List<Project> selectedProjects) {
    this.selectedProjects = selectedProjects;
    for (ToDateMeasureConfiguration measure : getEnabledMeasures()) {
      checkMeasureParameters(measure);
    }
    this.projectData.clear();
  }

  /**
   * Initialize the measure configurations.
   */
  private void initializeMeasures() {
    // Load default measures
    measures.clear();
    measures.add(new ToDateMeasureConfiguration("Coverage", this));
    measures.add(new ToDateMeasureConfiguration("CyclomaticComplexity", "Complexity", this));
    measures.add(new ToDateMeasureConfiguration("Coupling", this));
    measures.add(new ToDateMeasureConfiguration("MemberChurn", "Churn", this));
    measures.add(new ToDateMeasureConfiguration("CodeIssue", this));
    measures.add(new ToDateMeasureConfiguration("Commit", this));
    measures.add(new ToDateMeasureConfiguration("Build", this));
    measures.add(new ToDateMeasureConfiguration("UnitTest", "Test", this));
    measures.add(new ToDateMeasureConfiguration("FileMetric", "Size(LOC)", this));
    measures.add(new ToDateMeasureConfiguration("MemberDevTime", "DevTime", this));

    // Load additional user customized measures.

    for (ToDateMeasureConfiguration measure : getEnabledMeasures()) {
      checkMeasureParameters(measure);
    }
  }

  /**
   * Print the to date measure for logging purpose.
   * 
   * @param measure the PortfolioMeasure to log.
   * @return the string
   */
  private String printPortfolioMeasure(ToDateMeasure measure) {
    String s = "/";
    return "<" + measure.getMeasureName() + ": " + s + measure.isEnabled() + s
      + measure.getParameters() + "> ";
  }

  /**
   * Save user's configuration to system's cache.
   * 
   * @return change maked in this saving.
   */
  public String saveUserConfiguration() {
    StringBuffer log = new StringBuffer();
    UriCache userCache = this.getUserConfiguartionCache();
    userCache.put(userEmail + "/todate/startDate", this.startDate);
    for (ToDateMeasureConfiguration measure : this.measures) {
      String uri = userEmail + "/" + measure.getName();
      ToDateMeasure oldMeasure = (ToDateMeasure) userCache.get(uri);
      ToDateMeasure newMeasure = new ToDateMeasure(measure);
      if (oldMeasure == null || !oldMeasure.equals(newMeasure)) {
        userCache.put(uri, newMeasure);
        log.append(printPortfolioMeasure(newMeasure));
      }
    }

    if (log.length() > 0) {
      ProjectBrowserSession.get().logUsage("TODATE CONFIGURATION: {changed} " + log.toString());
    }
    return log.toString();
  }

  /**
   * Load user's configuration from system's cache.
   */
  private void loadUserConfiguration() {
    UriCache userCache = this.getUserConfiguartionCache();
    Long savedDate = (Long)userCache.get(userEmail + "/todate/startDate");
    if (savedDate != null) {
      this.startDate = savedDate;
    }
    for (ToDateMeasureConfiguration measure : this.measures) {
      String uri = userEmail + "/todate/" + measure.getName();
      ToDateMeasure saved = (ToDateMeasure) userCache.get(uri);
      if (saved != null) {
        measure.loadFrom(saved);
      }
    }
  }

  /**
   * Reset user's configuration cache.
   */
  public void resetUserConfiguration() {
    // UriCache userCache = this.getUserConfiguartionCache();
    // userCache.clearAll();
    this.initializeMeasures();
    String msg = this.saveUserConfiguration();
    if (msg.length() > 0) {
      ProjectBrowserSession.get().logUsage("CONFIGURATION: {reset to default} ");
    }
  }

  /**
   * Load data from Hackystat service.
   */
  public void loadData() {

    this.inProcess = true;
    this.complete = false;
    this.processingMessage = "Retrieving data from Hackystat Telemetry service.\n";
    XMLGregorianCalendar today = Tstamp.makeTimestamp();
    try {
      TelemetryClient telemetryClient = new TelemetryClient(telemetryHost, userEmail, password);
      XMLGregorianCalendar startDateTime = Tstamp.makeTimestamp(this.startDate);
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        // prepare project's information
        Project project = this.selectedProjects.get(i);
        String owner = project.getOwner();
        String projectName = project.getName();
        List<String> projectDataList = new ArrayList<String>();

        // prepare start and end time.
        XMLGregorianCalendar startTime = project.getStartTime();
        XMLGregorianCalendar endTime = project.getEndTime();
        if (startDateTime.compare(startTime) == DatatypeConstants.GREATER) {
          startTime = startDateTime;
        }
        if (endTime.compare(today) == DatatypeConstants.GREATER) {
          endTime = today;
        }
        
        this.processingMessage += "Retrieve data for project " + projectName + " (" + (i + 1)
            + " of " + this.selectedProjects.size() + ").\n";
        

        List<ToDateMeasureConfiguration> enableMeasures = getEnabledMeasures();

        for (int j = 0; j < enableMeasures.size(); ++j) {
          ToDateMeasureConfiguration measure = enableMeasures.get(j);

          this.processingMessage += "---> Retrieve " + measure.getName() + "<"
              + measure.getParamtersString() + ">" + " (" + (i + 1) + " .. " + (j + 1) + " of "
              + enableMeasures.size() + ").\n";
          // get data from hackystat
          if (!ProjectUtils.isValidStartTime(project, startTime)) {
            startTime = project.getStartTime();
          }
          String s = "/";
          /*
          System.out.println(
              "retriving telemetry: " + measure.getName() + s + owner + s + projectName + s
                  + granularity + s + startTime + s + endTime + s + measure.getParamtersString());
          */
          getLogger().fine(
              "retriving telemetry: " + measure.getName() + s + owner + s + projectName + s
                  + granularity + s + startTime + s + endTime + s + measure.getParamtersString());
          TelemetryChartData chartData = telemetryClient.getChart(measure.getName(), owner,
              projectName, granularity, startTime, endTime, measure.getParamtersString());
          // Log warning when portfolio definition refers to multi-stream telemetry chart.
          TelemetryStream stream = chartData.getTelemetryStream().get(0);
          String latestValue = getLatestValue(stream);
          if (latestValue == null) {
            latestValue = "N/A";
          }
          else {
            int index = latestValue.indexOf('.');
            if (index > 0 && (index + 2) < latestValue.length()) {
              latestValue = latestValue.substring(0, index + 2);
            }
          }
          projectDataList.add(latestValue);
        }
        this.projectData.put(project, projectDataList);
      }
    }
    catch (TelemetryClientException e) {
      String errorMessage = "Errors when retrieving telemetry data: " + e.getMessage() + ". "
          + "Please try again.";
      this.processingMessage += errorMessage + "\n";

      this.complete = false;
      this.inProcess = false;
      return;
    }
    this.complete = inProcess;
    if (this.complete) {
      this.processingMessage += "All done.\n";
    }
    this.inProcess = false;
  }

  /**
   * Return the latest valid value of the given stream.
   * @param stream the telemetry stream.
   * @return the latest value, null if no valid value in the stream.
   */
  private String getLatestValue(TelemetryStream stream) {
    for (int i = stream.getTelemetryPoint().size() - 1; i >= 0; --i) {
      String value = stream.getTelemetryPoint().get(i).getValue();
      if (value != null) {
        return value;
      }
    }
    return null;
  }
  /**
   * Check the parameter in the given measure configuration. Set the parameter to default if it is
   * incorrect.
   * 
   * @param measure the given MeasureConfiguration.
   */
  private void checkMeasureParameters(ToDateMeasureConfiguration measure) {
    List<ParameterDefinition> paramDefList = telemetrySession.getParameterList(measure.getName());
    if (measure.getParameters().size() != paramDefList.size()) {
      measure.getParameters().clear();
      for (ParameterDefinition paramDef : paramDefList) {
        if ("cumulative".equals(paramDef.getName())) {
          measure.getParameters().add(new Model("true"));
        }
        else {
          measure.getParameters().add(new Model(paramDef.getType().getDefault()));
        }
      }
    }
    //TODO check data type here.
  }

  /**
   * Cancel the data loading process.
   */
  public void cancelDataLoading() {
    this.processingMessage += "Process Cancelled.\n";
    this.inProcess = false;
  }

  /**
   * @return the inProcess
   */
  public boolean isInProcess() {
    return inProcess;
  }

  /**
   * @return the complete
   */
  public boolean isComplete() {
    return complete;
  }

  /**
   * @return the processingMessage
   */
  public String getProcessingMessage() {
    return processingMessage;
  }

  /**
   * @return the selectedProjects
   */
  public List<Project> getSelectedProjects() {
    return selectedProjects;
  }

  /**
   * Returns true if this model does not contain any data.
   * 
   * @return True if no data.
   */
  public boolean isEmpty() {
    return this.selectedProjects.isEmpty();
  }

  /**
   * Return the default parameters of the given measure.
   * 
   * @param measure the given measure
   * @return the parameters in a String
   */
  /*
   * public String getDefaultParametersString(String measure) { List<ParameterDefinition>
   * paramDefList = getParameterDefinitions(measure); StringBuffer param = new StringBuffer(); for
   * (int i = 0; i < paramDefList.size(); ++i) { ParameterDefinition paramDef = paramDefList.get(i);
   * param.append(paramDef.getType().getDefault()); if (i < paramDefList.size() - 1) {
   * param.append(','); } } return param.toString(); }
   */

  /**
   * @return the measures
   */
  public List<ToDateMeasureConfiguration> getMeasures() {
    return measures;
  }

  /**
   * @return the enabled measures
   */
  public final List<ToDateMeasureConfiguration> getEnabledMeasures() {
    List<ToDateMeasureConfiguration> enableMeasures = new ArrayList<ToDateMeasureConfiguration>();
    for (ToDateMeasureConfiguration measure : measures) {
      if (measure.isEnabled()) {
        enableMeasures.add(measure);
      }
    }
    return enableMeasures;
  }

  /**
   * Return the names of enabled measures. If alias available for the measure, alias will be
   * returned. Otherwise, name in measure's definition will be returned.
   * 
   * @return the names of the enabled measures.
   */
  public List<String> getEnabledMeasuresName() {
    List<String> names = new ArrayList<String>();
    for (ToDateMeasureConfiguration measure : measures) {
      if (measure.isEnabled()) {
        names.add(measure.getDisplayName());
      }
    }
    return names;
  }

  /**
   * @return the userConfiguartionCache
   */
  private UriCache getUserConfiguartionCache() {
    return new UriCache(userEmail, "todate", maxLife, capacity);
  }

  /**
   * @return the logger that associated to this web application.
   */
  private static Logger getLogger() {
    return HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
  }

  /**
   * @return the projectData
   */
  public Map<Project, List<String>> getProjectData() {
    return projectData;
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
}
