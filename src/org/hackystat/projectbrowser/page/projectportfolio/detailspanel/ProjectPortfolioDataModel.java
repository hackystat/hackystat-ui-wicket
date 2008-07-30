package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
  /** Measures in Project Portfolio. */
  private String[] measures = {"Coverage", "CyclomaticComplexity", "Coupling" ,"Churn", 
                                "Commit", "CodeIssue", "FileMetric"};
  /** Measures that will be colored. */
  private String[] coloringMeasures = {"Coverage", "CyclomaticComplexity", "Coupling" ,"Churn"};
  /** The charts in this model. */
  private Map<Project, List<MiniBarChart>> measuresCharts = 
        new HashMap<Project, List<MiniBarChart>>();
  /** Telemetry parameters. */
  private Map<String, String> parameters = new HashMap<String, String>();
  /** The thresholds. */
  private Map<String, MeasureConfiguration> configurations = 
    new HashMap<String, MeasureConfiguration>();
  
  /**
   * Initialize the measure configurations
   */
  public ProjectPortfolioDataModel() {
    configurations.put("Coverage" , new MeasureConfiguration(40, 90, true));
    configurations.put("CyclomaticComplexity" , new MeasureConfiguration(10, 20, false));
    configurations.put("Coupling" , new MeasureConfiguration(15, 25, false));
    configurations.put("Churn" , new MeasureConfiguration(35, 85, true));
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
      List<String> colorMeasures = Arrays.asList(this.coloringMeasures);
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        //prepare project's information
        Project project = this.selectedProjects.get(i);
        String owner = project.getOwner();
        String projectName = project.getName();

        this.processingMessage += "Retrieve data for project " + projectName + 
        " (" + (i + 1) + " of " + this.selectedProjects.size() + ").\n";
        
        //get charts of this project
        List<MiniBarChart> charts = new ArrayList<MiniBarChart>();
        for (int j = 0; j < measures.length; ++j) {
          String measure = measures[j];
          this.processingMessage += "---> Retrieve " + measure + 
              " (" + (j + 1) + " of " + measures.length + ").\n";
          TelemetryChartData chartData = telemetryClient.getChart(measure, owner, projectName, 
              telemetryGranularity, Tstamp.makeTimestamp(startDate), Tstamp.makeTimestamp(endDate),
              getParameters().get(measure));
          MiniBarChart chart;
          if (colorMeasures.contains(measure)) {
            chart = new AutoColorMiniBarChart(chartData.getTelemetryStream().get(0),
                this.configurations.get(measure));
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
  private PageParameters getTelemetryPageParameters(String measure, Project project) {
    PageParameters parameters = new PageParameters();
    
    parameters.put(TelemetrySession.TELEMETRY_KEY, measure);
    parameters.put(TelemetrySession.START_DATE_KEY, Tstamp.makeTimestamp(startDate).toString());
    parameters.put(TelemetrySession.END_DATE_KEY, Tstamp.makeTimestamp(endDate).toString());
    parameters.put(TelemetrySession.SELECTED_PROJECTS_KEY, 
        project.getName() + TelemetrySession.PROJECT_NAME_OWNER_SEPARATR + project.getOwner());
    parameters.put(TelemetrySession.GRANULARITY_KEY, this.telemetryGranularity);
    parameters.put(TelemetrySession.TELEMETRY_PARAMERTERS_KEY, this.getParameters().get(measure));
    
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
   * @return the parameters
   */
  public Map<String, String> getParameters() {
    if (parameters.isEmpty()) {
      TelemetryClient telemetryClient = new TelemetryClient(telemetryHost, email, password);
      try {
        for (String analysis : this.measures) {
          List<ParameterDefinition> paramDefList =  
          telemetryClient.getChartDefinition(analysis).getParameterDefinition();
          StringBuffer param = new StringBuffer();
            for (int i = 0; i < paramDefList.size(); ++i) {
              ParameterDefinition paramDef = paramDefList.get(i);
              param.append(paramDef.getType().getDefault());
              if (i < paramDefList.size() - 1) {
                param.append(',');
              }
            }
            parameters.put(analysis, param.toString());
        }
      }
      catch (TelemetryClientException e) {
        e.printStackTrace();
      }
    }
    return parameters;
  }

}
