package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputForm;
import org.hackystat.projectbrowser.page.telemetry.datapanel.TelemetryChartDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartRef;

/**
 * Session to hold state for telemetry.
 * @author Shaoxuan Zhang
 */
public class TelemetrySession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The start date this user has selected. */
  private long startDate = ProjectBrowserBasePage.getDateBefore(7).getTime();
  /** The end date this user has selected. */
  private long endDate = ProjectBrowserBasePage.getDateBefore(1).getTime();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The descriptions for all telemetries. */
  private final Map<String, TelemetryChartDefinition> telemetrys = 
                            new HashMap<String, TelemetryChartDefinition>();
  /** The granularity of the chart. Either Day, Week, or Month. */
  private String granularity = "Day";
  /** The available granularities. */
  private final List<String> granularityList = new ArrayList<String>();
  /** the feedback string. */
  private String feedback = "";
  /** The parameters for telemetry chart. */
  private List<IModel> parameters = new ArrayList<IModel>();
  /** The data model to hold state for data panel */
  private TelemetryChartDataModel dataModel = new TelemetryChartDataModel();
  /**
   * Create the instance.
   */
  public TelemetrySession() {
    granularityList.add("Day");
    granularityList.add("Week");
    granularityList.add("Month");
  }

  /**
   * @return the parameters
   */
  public List<IModel> getParameters() {
    return this.parameters;
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
   * Return the TelemetryList. Initialize it if it is null.
   * @return the telemetryList
   */
  public List<String> getTelemetryList() {
    Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
    List<String> telemetryList = new ArrayList<String>();
    if (this.getTelemetrys().isEmpty()) {
      TelemetryClient client  = ProjectBrowserSession.get().getTelemetryClient();
      try {
        logger.info("Retrieving data for Telemetry chart definitions.");
        for (TelemetryChartRef chartRef : client.getChartIndex().getTelemetryChartRef()) {
          getTelemetrys().put(chartRef.getName(), client.getChartDefinition(chartRef.getName()));
        }
        logger.info("Finished retrieving data for Telemetry chart definitions.");
      }
      catch (TelemetryClientException e) {
        this.feedback = "Exception when retrieving Telemetry chart definition: " + e.getMessage();
        logger.warning("Error when retrieving Telemetry chart definition: " 
                                      + e.getMessage());
      }
    }
    telemetryList.addAll(this.getTelemetrys().keySet());
    Collections.sort(telemetryList);
    return telemetryList;
  }

  /**
   * Return the list of ParameterDefinition under telemetry type in this session.
   * @return list of ParameterDefinition.
   */
  public List<ParameterDefinition> getParameterList() {
    Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
    if (this.telemetryName != null) {
      TelemetryChartDefinition teleDef = 
        this.getTelemetrys().get(telemetryName);
      if (teleDef == null) {
        TelemetryClient client  = ProjectBrowserSession.get().getTelemetryClient();
        try {
          logger.info("Retrieving chart definition of telemetry: " + this.telemetryName);
          teleDef = client.getChartDefinition(this.telemetryName);
          this.getTelemetrys().put(telemetryName, teleDef);
          return teleDef.getParameterDefinition();
        }
        catch (TelemetryClientException e) {
          this.feedback = "Error when retrieving chart definition of telemetry: " + 
            this.telemetryName + ">>" + e.getMessage();
          logger.warning("Error when retrieving chart definition of telemetry: " + 
              this.telemetryName + ">>" + e.getMessage());
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
   * Returns the start date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getStartDateString() {
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.startDate));
  }
  
  /**
   * Returns the end date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getEndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.endDate));
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
    this.dataModel.
      setModel(getStartDate(), getEndDate(), 
          selectedProjects, telemetryName, granularity, parameters);
    
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
  public void canelDataUpdate() {
    dataModel.cancelDataLoading();
  }
  
  /**
   * @param dataModel the dataModel to set
   */
  public void setDataModel(TelemetryChartDataModel dataModel) {
    this.dataModel = dataModel;
  }

  /**
   * @return the dataModel
   */
  public TelemetryChartDataModel getDataModel() {
    return dataModel;
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

}
