package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel.ProjectDateForm;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data model to hold state of the telemetry chart.
 * @author Shaoxuan Zhang
 */
public class TelemetryChartDataModel implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The start date this user has selected. */
  private long startDate = 0;
  /** The end date this user has selected. */
  private long endDate = 0;
  /** The project this user has selected. */
  //private Project project = null;
  private Map<Project, String> projectCharts = new HashMap<Project, String>();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The URL of google chart. */
  //private String chartUrl = "";
  
  /**
   * @param startDate the start date of this model..
   * @param endDate the end date of this model..
   * @param selectedProjects the project ofs this model.
   * @param telemetryName the telemetry name of this model.
   */
  public void setModel(Date startDate, Date endDate, List<Project> selectedProjects, 
                                 String telemetryName) {
    this.startDate = startDate.getTime();
    this.endDate = endDate.getTime();
    this.selectedProjects = selectedProjects;
    //this.project = selectedProjects.get(0);
    this.telemetryName = telemetryName;
    //this.chartUrl = this.getChartUrl(project);
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
   * @return the project
   */
  /*
  public Project getProject() {
    return project;
  }
  */

  /**
   * @return the telemetryName
   */
  public String getTelemetryName() {
    return telemetryName;
  }

  /**
   * @return the chartUrl
   */
  /*
  public String getChartUrl() {
    return chartUrl;
  }
  */

  /**
   * Returns true if this model does not contain any data. 
   * @return True if no data. 
   */
  public boolean isEmpty() {
    return this.selectedProjects.isEmpty();
  }
  
  /**
   * Return the google chart url that present the telemetry associated with this session.
   * @param project project this chart will present.
   * @return the URL string.
   */
  public String getChartUrl(Project project) {
    TelemetryClient client = ProjectBrowserSession.get().getTelemetryClient();
    TelemetrySession  session = ProjectBrowserSession.get().getTelemetrySession();
    
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, 800, 300);
    if (this.getTelemetryName() != null && project != null) {
      try {
        //retrieve data from hackystat.
        List<TelemetryStream> streams = client.getChart(this.getTelemetryName(), 
                                        project.getOwner(), 
                                        project.getName(), 
                                        session.getGranularity(), 
                                        Tstamp.makeTimestamp(session.getStartDate().getTime()), 
                                        Tstamp.makeTimestamp(session.getEndDate().getTime()), 
                                        session.getParameterAsString()).getTelemetryStream();

        Iterator<String> colorIterator = session.getColors().iterator();
        Iterator<String> markerIterator = session.getMarkers().iterator();
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
        
        
        session.setFeedback(googleChart.getUrl());
        
        return googleChart.getUrl();
      }
      catch (TelemetryClientException e) {
        e.printStackTrace();
      }
    }
    return "";    
  }
  
  /**
   * @return the selectedProjects
   */
  public List<Project> getSelectedProjects() {
    return selectedProjects;
  }

  /**
   * Return the chart for the given project
   * @param project the given project.
   * @return the chart link.
   */
  public String getProjectChart(Project project) {
    String chartLink = this.projectCharts.get(project);
    if (chartLink == null) {
      chartLink = this.getChartUrl(project);
      this.projectCharts.put(project, chartLink);
    }
    return chartLink;
  }
}
