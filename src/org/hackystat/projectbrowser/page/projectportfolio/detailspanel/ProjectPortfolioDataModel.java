package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.wicket.PageParameters;
import org.hackystat.projectbrowser.page.loadingprocesspanel.Processable;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartData;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data model to hold state of Project Portfolio.
 * 
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioDataModel implements Serializable, Processable {

  /** Support serialization. */
  private static final long serialVersionUID = 5465041655927215391L;
  /** state of data loading process. */
  private volatile boolean inProcess = false;
  /** result of data loading. */
  private volatile boolean complete = false;
  /** message to display when data loading is in process.*/
  private String processingMessage = "";

  /** The start date this user has selected. */
  private long startDate = 0;
  /** The end date this user has selected. */
  private long endDate = 0;
  /** host of the telemetry host. */
  private String telemetryHost;
  /** email of the user. */
  private String email;
  /** password of the user. */
  private String password;
  /** the granularity this data model focus. */
  private String telemetryGranularity = "Week";
  
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The charts in this model. */
  private Map<Project, List<MiniBarChart>> measuresCharts = 
        new HashMap<Project, List<MiniBarChart>>();
  /** The thresholds. */
  private List<MeasureConfiguration> measures = new ArrayList<MeasureConfiguration>();

  /** Measures in Project Portfolio. */
  //private String[] measures = {"Coverage", "CyclomaticComplexity", "Coupling" ,"Churn", 
  //                              "Commit", "CodeIssue", "FileMetric"};
  /** Measures that will be colored. */
  //private String[] coloringMeasures = {"Coverage", "CyclomaticComplexity", "Coupling" ,"Churn"};
  /** Telemetry parameters. */
  //private Map<String, String> parameters = new HashMap<String, String>();
  
  /**
   * Initialize the measure configurations
   */
  public ProjectPortfolioDataModel() {
    measures.add(new MeasureConfiguration("Coverage", true, 40, 90, true));
    measures.add(new MeasureConfiguration("CyclomaticComplexity", true, 10, 20, false));
    measures.add(new MeasureConfiguration("Coupling", true, 15, 25, false));
    measures.add(new MeasureConfiguration("Churn", true, 35, 85, true));
    measures.add(new MeasureConfiguration("Commit", false));
    measures.add(new MeasureConfiguration("CodeIssue", false));
    measures.add(new MeasureConfiguration("FileMetric", false));
  }
  
  /**
   * @param startDate the start date.
   * @param endDate the end date.
   * @param selectedProjects the selected projects.
   * @param telemetryHost the telemetry host
   * @param email the user's email
   * @param password the user's passowrd
   */
  public void setModel(long startDate, long endDate, List<Project> selectedProjects, 
      String telemetryHost, String email, String password) {
    this.telemetryHost = telemetryHost;
    this.email = email;
    this.password = password;
    this.startDate = startDate;
    this.endDate = endDate;
    this.selectedProjects = selectedProjects;
  }

  /**
   * Load data from Hackystat service.
   */
  public void loadData() {

    this.inProcess = true;
    this.complete = false;
    this.processingMessage = "Retrieving data from Hackystat Telemetry service.\n";
    try {
      measuresCharts.clear();
      TelemetryClient telemetryClient = new TelemetryClient(telemetryHost, email, password);
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        //prepare project's information
        Project project = this.selectedProjects.get(i);
        String owner = project.getOwner();
        String projectName = project.getName();

        this.processingMessage += "Retrieve data for project " + projectName + 
        " (" + (i + 1) + " of " + this.selectedProjects.size() + ").\n";
        
        //get charts of this project
        List<MiniBarChart> charts = new ArrayList<MiniBarChart>();
        for (int j = 0; j < measures.size(); ++j) {
          MeasureConfiguration measure = measures.get(j);
          this.processingMessage += "---> Retrieve " + measure.getName() + 
              " (" + (i + 1) + " .. " + (j + 1) + " of " + measures.size() + ").\n";
          
          //set default parameter if no parameter is assigned.
          if (measure.getParameters().isEmpty()) {
            List<String> params = new ArrayList<String>();
            List<ParameterDefinition> paramDefList = getParameterDefinitions(measure.getName());
            for (ParameterDefinition paramDef : paramDefList) {
              params.add(paramDef.getType().getDefault());
            }
            measure.setParameters(params);
          }
          //get data from hackystat
          TelemetryChartData chartData = telemetryClient.getChart(measure.getName(), 
              owner, projectName, telemetryGranularity, Tstamp.makeTimestamp(startDate), 
              Tstamp.makeTimestamp(endDate), measure.getParamtersString());
          
          MiniBarChart chart;
          if (measure.isColorable()) {
            chart = new AutoColorMiniBarChart(chartData.getTelemetryStream().get(0), measure);
          }
          else {
            chart = new MiniBarChart(chartData.getTelemetryStream().get(0));
          }

          chart.setTelemetryPageParameters(getTelemetryPageParameters(measure, project));
          charts.add(chart);
        }
        this.measuresCharts.put(project, charts);
      }
    }
    catch (TelemetryClientException e) {
      String errorMessage = "Errors when retrieving telemetry data: " + e.getMessage() + ". " +
      		"Please try again.";
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
   * Return a PageParameters instance that include necessary information for telemetry.
   * @param measure the telemetry analysis
   * @param project the project
   * @return the PagaParameters object
   */
  private PageParameters getTelemetryPageParameters(MeasureConfiguration measure, Project project) {
    PageParameters parameters = new PageParameters();
    
    parameters.put(TelemetrySession.TELEMETRY_KEY, measure.getName());
    parameters.put(TelemetrySession.START_DATE_KEY, Tstamp.makeTimestamp(startDate).toString());
    parameters.put(TelemetrySession.END_DATE_KEY, Tstamp.makeTimestamp(endDate).toString());
    parameters.put(TelemetrySession.SELECTED_PROJECTS_KEY, 
        project.getName() + TelemetrySession.PROJECT_NAME_OWNER_SEPARATR + project.getOwner());
    parameters.put(TelemetrySession.GRANULARITY_KEY, this.telemetryGranularity);
    parameters.put(TelemetrySession.TELEMETRY_PARAMERTERS_KEY, measure.getParamtersString());
    
    return parameters;
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
   * @return the analysesCharts
   */
  public Map<Project, List<MiniBarChart>> getMeasuresCharts() {
    return measuresCharts;
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
   * @param measure the given measure
   * @return the parameters in a String
   */
  public String getDefaultParametersString(String measure) {
    List<ParameterDefinition> paramDefList = getParameterDefinitions(measure);
      StringBuffer param = new StringBuffer();
      for (int i = 0; i < paramDefList.size(); ++i) {
        ParameterDefinition paramDef = paramDefList.get(i);          
        param.append(paramDef.getType().getDefault());
          if (i < paramDefList.size() - 1) {
            param.append(',');
          }
        }
      return param.toString();
  }

  /**
   * Return the parameter definitions of the given measure.
   * @param measure the given measure
   * @return the parameter definitions.
   */
  public List<ParameterDefinition> getParameterDefinitions(String measure) {
    TelemetryClient telemetryClient = new TelemetryClient(telemetryHost, email, password);
    try {
        return telemetryClient.getChartDefinition(measure).getParameterDefinition();
    }
    catch (TelemetryClientException e) {
      e.printStackTrace();
    }
    return new ArrayList<ParameterDefinition>();
    
  }
}
