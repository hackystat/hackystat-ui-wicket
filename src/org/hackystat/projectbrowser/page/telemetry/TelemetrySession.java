package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel.ProjectDateForm;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartRef;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.tstamp.Tstamp;

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
  /** The project this user has selected. */
  private Project project = null;
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The granularity of the chart. Either Day, Week, or Month. */
  private String granularity = "Day";
  /** The available granularities. */
  private final List<String> granularityList = new ArrayList<String>();
  /** The analysis list. */
  private List<String> telemetryList = null;
  /** the feedback string. */
  private String feedback = "";
  /** The parameters for telemetry chart. */
  private List<IModel> parameters = new ArrayList<IModel>();
  /** The data model to hold state for data panel */
  private TelemetryChartDataModel dataModel = new TelemetryChartDataModel();
  /** Default color collection.*/
  private List<String> colors = new ArrayList<String>();
  /** collection of markers.*/
  private List<String> markers = new ArrayList<String>();
  /**
   * Create the instance.
   */
  public TelemetrySession() {
    granularityList.add("Day");
    granularityList.add("Week");
    granularityList.add("Month");
    colors.add("76A4FB");
    colors.add("FF0000");
    colors.add("80C65A");
    colors.add("224499");
    colors.add("990066");
    colors.add("FF9900");
    colors.add("FFCC33");
    markers.add("o");
    markers.add("d");
    markers.add("c");
    markers.add("x");
    markers.add("s");
  }

  /**
   * @return the parameters
   */
  public List<IModel> getParameters() {
    return this.parameters;
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
    return project;
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
    if (this.telemetryList == null || this.telemetryList.size() <= 0) {
      telemetryList = new ArrayList<String>();
      TelemetryClient client  = ProjectBrowserSession.get().getTelemetryClient();
      try {
        for (TelemetryChartRef chartRef : client.getChartIndex().getTelemetryChartRef()) {
          telemetryList.add(chartRef.getName());
        }
      }
      catch (TelemetryClientException e) {
        this.feedback = "Exception when retrieving Telemetry chart definition: " + e.getMessage();
      }
      Collections.sort(this.telemetryList);
    }
    return telemetryList;
  }

  /**
   * Return the list of ParameterDefinition under telemetry type in this session.
   * @return list of ParameterDefinition.
   */
  public List<ParameterDefinition> getParameterList() {
    if (this.telemetryName != null) {
      TelemetryClient client  = ProjectBrowserSession.get().getTelemetryClient();
      try {
        return client.getChartDefinition(this.telemetryName).getParameterDefinition();
      }
      catch (TelemetryClientException e) {
        this.feedback = "Exception when retrieving Telemetry chart definition: " + e.getMessage();
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
    SimpleDateFormat format = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.startDate));
  }
  
  /**
   * Returns the end date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getEndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.ENGLISH);
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
   * Return the comma-separated list of parameters in String
   * @return the parameters as String
   */
  public String getParameterAsString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (IModel model : this.parameters) {
      stringBuffer.append(model.getObject());
      stringBuffer.append(',');
    }
    String param = stringBuffer.toString();
    if (param.length() >= 1) {
      param = param.substring(0, param.length() - 1);
    }
    return param;
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
    this.dataModel.setModel(getStartDate(), getEndDate(), project, telemetryName, getChartUrl());
  }
  
  /**
   * Return the google chart url that present the telemetry associated with this session.
   * @return the URL string.
   */
  public String getChartUrl() {
    TelemetryClient client = ProjectBrowserSession.get().getTelemetryClient();
    
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, 800, 300);
    if (this.getTelemetryName() != null && this.getProject() != null) {
      try {
        //retrieve data from hackystat.
        List<TelemetryStream> streams = client.getChart(this.getTelemetryName(), 
                                        this.getProject().getOwner(), 
                                        this.getProject().getName(), 
                                        this.getGranularity(), 
                                        Tstamp.makeTimestamp(this.getStartDate().getTime()), 
                                        Tstamp.makeTimestamp(this.getEndDate().getTime()), 
                                        this.getParameterAsString()).getTelemetryStream();

        Iterator<String> colorIterator = this.colors.iterator();
        Iterator<String> markerIterator = this.markers.iterator();
        for (TelemetryStream stream : streams) {
          List<Double> streamData = new ArrayList<Double>();
          for (TelemetryPoint point : stream.getTelemetryPoint()) {
            if (point.getValue() == null) {
              streamData.add(-1.0);
              /*
              if (streamData.isEmpty() || isCumulativeFalse()) {
                streamData.add(-1.0);
              }
              else {
                streamData.add(streamData.get(streamData.size() - 1));
              }
            */
            }
            else {
              Double value = Double.valueOf(point.getValue());
              if (value.isNaN()) {
                value = 0.0;
              }
              streamData.add(value);
            }
          }
          googleChart.getChartData().add(streamData);
          googleChart.addColor(colorIterator.next());
          googleChart.getChartMarker().add(markerIterator.next());
          googleChart.getChartLegend().add(stream.getName());
        }
        List<String> dates = new ArrayList<String>();
        for (TelemetryPoint point : streams.get(0).getTelemetryPoint()) {
          String month = "0" + point.getTime().getMonth();
          month = month.substring(month.length() - 2);
          String date = "0" + point.getTime().getDay();
          date = date.substring(date.length() - 2);
          String dateString = month + "-" + date;
          dates.add(dateString);
        }
        googleChart.addAxisLabel("x", dates);
        googleChart.addAxisLabel("y");
        
        
        this.feedback = googleChart.getUrl();
        
        return googleChart.getUrl();
      }
      catch (TelemetryClientException e) {
        e.printStackTrace();
      }
    }
    return "";    
  }

  /**
   * @return true if session associated telemetry has cumulative parameter and it is false.
   */
  /*
  private boolean isCumulativeFalse() {
    List<ParameterDefinition> paramDefList = this.getParameterList();
    for (int i = 0; i < paramDefList.size(); ++i) {
      if ("cumulative".equals(paramDefList.get(i).getName())) {
        String value = this.parameters.get(i).getObject().toString();
        Boolean result = !Boolean.parseBoolean(value);
        return result.booleanValue();
      }
    }
    return false;
  }
  */
  
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
}
