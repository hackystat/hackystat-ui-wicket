package org.hackystat.projectbrowser.page.telemetry.datapanel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputForm;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data model to hold state of the telemetry chart.
 * 
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
  // private Project project = null;
  private Map<Project, String> projectCharts = new HashMap<Project, String>();
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The parameters for this telemetry chart. */
  private List<String> parameters = new ArrayList<String>();
  /** Store the data retrieved from telemetry service. */
  private Map<Project, List<SelectableTelemetryStream>> projectStreamData = 
    new HashMap<Project, List<SelectableTelemetryStream>>();
  /** Chart with all project streams. */
  private String overallChart = null;
  /** Chart with selected project streams. */
  private String selectedChart = null;
  /** the width of this chart. */
  private int width = 1000;
  /** the height of this chart. */
  private int height = 300;
  /** state of data loading process. */
  private boolean inProcess;
  /** result of data loading. */
  private boolean complete;
  /** message to display when data loading is in process.*/
  private String processingMessage = "";
  /** host of the telemetry host. */
  private String telemetryHost;
  /** email of the user. */
  private String email;
  /** password of the user. */
  private String password;
  /** session that holds the state of this page. */
  private TelemetrySession session;
  /** The URL of google chart. */
  // private String chartUrl = "";
  /**
   * @param startDate the start date of this model..
   * @param endDate the end date of this model..
   * @param selectedProjects the project ofs this model.
   * @param telemetryName the telemetry name of this model.
   * @param parameters the list of parameters
   */
  public void setModel(Date startDate, Date endDate, List<Project> selectedProjects,
      String telemetryName, List<IModel> parameters) {
    this.processingMessage = "";
    this.telemetryHost = ((ProjectBrowserApplication)ProjectBrowserSession.get().getApplication()).
                    getTelemetryHost();
    this.email = ProjectBrowserSession.get().getEmail();
    this.password = ProjectBrowserSession.get().getPassword();
    this.session = ProjectBrowserSession.get().getTelemetrySession();
    this.startDate = startDate.getTime();
    this.endDate = endDate.getTime();
    this.selectedProjects = selectedProjects;
    // this.project = selectedProjects.get(0);
    this.telemetryName = telemetryName;
    this.projectCharts.clear();
    this.projectStreamData.clear();
    this.parameters.clear();
    for (IModel model : parameters) {
      this.parameters.add(model.getObject().toString());
    }
    this.overallChart = null;
    this.selectedChart = null;
    // this.chartUrl = this.getChartUrl(project);
  }

  /**
   * Load data from Hackystat service.
   */
  public void loadData() {
    this.inProcess = true;
    this.complete = false;
    this.processingMessage = "Loading data from Hackystat serive.\n";
    try {
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        Project project = this.selectedProjects.get(i);
        this.processingMessage += "Loading data of project " + project.getName() + 
            " (" + (i + 1) + " of " + this.selectedProjects.size() + ").\n";
        List<SelectableTelemetryStream> streamList = new ArrayList<SelectableTelemetryStream>();

        TelemetryClient client = new TelemetryClient(this.telemetryHost, this.email, this.password);
        List<TelemetryStream> streams = client.getChart(this.getTelemetryName(),
            project.getOwner(), project.getName(), session.getGranularity(),
            Tstamp.makeTimestamp(session.getStartDate().getTime()),
            Tstamp.makeTimestamp(session.getEndDate().getTime()), this.getParameterAsString())
            .getTelemetryStream();
        for (TelemetryStream stream : streams) {
          streamList.add(new SelectableTelemetryStream(stream));
        }
        this.projectStreamData.put(project, streamList);
      //this.processingMessage += "Done.\n";
      }
    }
    catch (TelemetryClientException e) {
      this.processingMessage += "Errors when retrieving " + this.telemetryName + 
      " telemetry data: " + e.getMessage() + ". Please try again.\n";
      session.setFeedback("Errors when retrieving " + this.telemetryName + " telemetry data: "
          + e.getMessage() + ". Please try again.");
      this.complete = false;
      this.inProcess = false;
      return;
    }
    this.processingMessage += "All done.\n";
    this.complete = inProcess;
    this.inProcess = false;
  }
  
  /**
   * Cancel the data loading process.
   */
  public void cancelDataLoading() {
    this.inProcess = false;
  }
  /**
   * Returns the start date in yyyy-MM-dd format.
   * 
   * @return The date as a simple string.
   */
  public String getStartDateString() {
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.startDate));
  }

  /**
   * Returns the end date in yyyy-MM-dd format.
   * 
   * @return The date as a simple string.
   */
  public String getEndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.endDate));
  }

  /**
   * @return the project
   */
  /*
   * public Project getProject() { return project; }
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
   * public String getChartUrl() { return chartUrl; }
   */

  /**
   * Returns true if this model does not contain any data.
   * 
   * @return True if no data.
   */
  public boolean isEmpty() {
    return this.selectedProjects.isEmpty();
  }

  /**
   * Return the list of TelemetryStream associated with the given project.
   * 
   * @param project the given project.
   * @return the list of TelemetryStream.
   */
  public List<SelectableTelemetryStream> getTelemetryStream(Project project) {
    List<SelectableTelemetryStream> streamList = this.projectStreamData.get(project);
    if (streamList == null) {
      streamList = new ArrayList<SelectableTelemetryStream>();
    }
    return streamList;
  }

  /**
   * Return the google chart url that present the telemetry associated with this session.
   * 
   * @param project project this chart will present.
   * @return the URL string of the chart.
   */
  public String getChartUrl(Project project) {
    if (this.getTelemetryName() != null && project != null) {
      // retrieve data from hackystat.
      List<SelectableTelemetryStream> streams = this.getTelemetryStream(project);
      List<TelemetryStream> streamList = new ArrayList<TelemetryStream>();
      for (SelectableTelemetryStream stream : streams) {
        streamList.add(stream.getTelemetryStream());
      }
      return getChartUrl(null, streamList);
    }
    return "";
  }

  /**
   * Return the google chart url that present all streams within the given list.
   * 
   * @param namePrecedings precedings to add before stream names.
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getChartUrl(List<String> namePrecedings, List<TelemetryStream> streams) {
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.width, this.height);
    double maximum = 0;
    for (int i = 0; i < streams.size(); ++i) {
      String namePreceding = null;
      if (namePrecedings != null && i < namePrecedings.size()) {
        namePreceding = namePrecedings.get(i);
      }
      double streamMax = this.addStreamToChart(namePreceding, streams.get(i), googleChart);
      if (streamMax > maximum) {
        maximum = streamMax;
      }
    }
    if (!streams.isEmpty()) {
      googleChart.addAxisLabel("x", getDateList(streams.get(0).getTelemetryPoint()));
    }
    if (maximum > 100 || maximum < 50) {
      double newRange;
      if (maximum > 100) {
        newRange = Math.round(maximum / 50) * 50;
      }
      else if (maximum < 25) {
        newRange = Math.round(maximum / 5) * 5;
      }
      else {
        newRange = Math.round(maximum / 10) * 10;
      }
      googleChart.rescaleStream(newRange);
      googleChart.addAxisLabel("y", newRange);
    }
    else {
      googleChart.addAxisLabel("y");
    }

    // ProjectBrowserSession.get().getTelemetrySession().setFeedback(googleChart.getUrl());

    return googleChart.getUrl();
  }

  /**
   * Add the given stream to the google chart. And return the maximum value of the stream.
   * 
   * @param namePreceding string that put before the name of the stream.
   * @param stream the given stream
   * @param googleChart the google chart
   * @return the maximum value of the stream.
   */
  public double addStreamToChart(String namePreceding, TelemetryStream stream,
      GoogleChart googleChart) {
    // prepare useful object.
    Random random = new Random();
    TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
    double maximum = -1;
    List<Double> streamData = new ArrayList<Double>();
    for (TelemetryPoint point : stream.getTelemetryPoint()) {
      if (point.getValue() == null) {
        streamData.add(-1.0);
      }
      else {
        Double value = Double.valueOf(point.getValue());
        if (value.isNaN()) {
          value = 0.0;
        }
        if (value > maximum) {
          maximum = value;
        }
        streamData.add(value);
      }
    }
    // add the chart only if the stream is not empty.
    if (maximum >= 0) {
      googleChart.getChartData().add(streamData);
      Long longValue = Math.round(Math.random() * 0xFFFFFF);
      String randomColor = "000000" + Long.toHexString(longValue);
      randomColor = randomColor.substring(randomColor.length() - 6);
      googleChart.addColor(randomColor);
      if (!session.getMarkers().isEmpty()) {
        int i = random.nextInt(session.getMarkers().size());
        googleChart.getChartMarker().add(session.getMarkers().get(i));
      }
      String name = stream.getName();
      if (namePreceding != null && namePreceding.length() > 0) {
        name = namePreceding + "-" + name;
      }
      googleChart.getChartLegend().add(name);

    }
    return maximum;
  }

  /**
   * Return the date list within the list of points.
   * 
   * @param points the point list.
   * @return the date list.
   */
  public List<String> getDateList(List<TelemetryPoint> points) {
    List<String> dates = new ArrayList<String>();
    for (TelemetryPoint point : points) {
      String month = "0" + point.getTime().getMonth();
      month = month.substring(month.length() - 2);
      String date = "0" + point.getTime().getDay();
      date = date.substring(date.length() - 2);
      String dateString = month + "-" + date;
      dates.add(dateString);
    }
    return dates;
  }

  /**
   * Return the date list inside this model.
   * 
   * @return the date list.
   */
  public List<String> getDateList() {
    if (this.selectedProjects.isEmpty()) {
      return new ArrayList<String>();
    }
    List<SelectableTelemetryStream> streamList = this.getTelemetryStream(this.selectedProjects
        .get(0));
    if (streamList.isEmpty()) {
      return new ArrayList<String>();
    }
    List<String> dateList = getDateList(streamList.get(0).getTelemetryStream().getTelemetryPoint());
    return dateList;
  }

  /**
   * @return the selectedProjects
   */
  public List<Project> getSelectedProjects() {
    return selectedProjects;
  }

  /**
   * Return the chart for the given project
   * 
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

  /**
   * Return the comma-separated list of parameters in String
   * 
   * @return the parameters as String
   */
  public String getParameterAsString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (String model : this.parameters) {
      stringBuffer.append(model);
      stringBuffer.append(',');
    }
    String param = stringBuffer.toString();
    if (param.length() >= 1) {
      param = param.substring(0, param.length() - 1);
    }
    return param;
  }

  /**
   * @return the overallChart
   */
  public String getOverallChart() {
    if (overallChart == null) {
      List<TelemetryStream> streams = new ArrayList<TelemetryStream>();
      List<String> projectNames = new ArrayList<String>();
      for (Project project : this.selectedProjects) {
        List<SelectableTelemetryStream> streamList = this.getTelemetryStream(project);
        for (int i = 0; i < streamList.size(); ++i) {
          streams.add(streamList.get(i).getTelemetryStream());
          projectNames.add(project.getName());
        }
      }
      overallChart = this.getChartUrl(projectNames, streams);
    }
    return overallChart;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return the selectedChart
   */
  public String getSelectedChart() {
    return selectedChart;
  }

  /**
   * @return true if the chart is empty.
   */
  public boolean isChartEmpty() {
    return selectedChart == null || "".equals(selectedChart);
  }

  /**
   * reset the selectedChart.
   * 
   * @return true if the chart is successfully updated.
   */
  public boolean updateSelectedChart() {
    List<TelemetryStream> streams = new ArrayList<TelemetryStream>();
    List<String> projectNames = new ArrayList<String>();
    for (Project project : this.selectedProjects) {
      List<SelectableTelemetryStream> streamList = this.getTelemetryStream(project);
      for (int i = 0; i < streamList.size(); ++i) {
        if (streamList.get(i).isSelected()) {
          streams.add(streamList.get(i).getTelemetryStream());
          projectNames.add(project.getName());
        }
      }
    }
    if (streams.isEmpty()) {
      selectedChart = "";
      return false;
    }
    selectedChart = this.getChartUrl(projectNames, streams);
    return true;
  }

  /**
   * Change all selected flags of streams to the given flag.
   * 
   * @param flag the boolean flag.
   */
  public void changeSelectionForAll(boolean flag) {
    for (List<SelectableTelemetryStream> streamList : this.projectStreamData.values()) {
      for (SelectableTelemetryStream stream : streamList) {
        stream.setSelected(flag);
      }
    }
  }

  /**
   * @return the inProcess
   */
  public boolean isInProcess() {
    return inProcess;
  }

  /**
   * @return the isComplete
   */
  public boolean isComplete() {
    return complete;
  }

  /**
   * @return the message
   */
  public String getProcessingMessage() {
    return processingMessage;
  }
}
