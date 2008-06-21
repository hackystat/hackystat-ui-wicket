package org.hackystat.projectbrowser.page.telemetry.datapanel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputForm;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.projectbrowser.page.telemetry.TelemetryStreamYAxis;
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
  /** Chart with selected project streams. */
  private String selectedChart = null;
  /** the width of this chart. */
  private int width = 1000;
  /** the height of this chart. */
  private int height = 300;
  /** state of data loading process. */
  private volatile boolean inProcess = false;
  /** result of data loading. */
  private volatile boolean complete = false;
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
      if (model.getObject() != null) {
        this.parameters.add(model.getObject().toString());
      }
    }
    this.selectedChart = null;
    // this.chartUrl = this.getChartUrl(project);
  }

  /**
   * Load data from Hackystat service.
   */
  public void loadData() {
    this.inProcess = true;
    this.complete = false;
    this.processingMessage = "Retrieving data from Hackystat Telemetry service.\n";
    try {
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        Project project = this.selectedProjects.get(i);
        this.processingMessage += "Retrieving data for project " + project.getName() + 
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
    this.complete = inProcess;
    if (this.complete) {
      this.processingMessage += "All done.\n";
    }
    this.inProcess = false;
  }
  
  /**
   * Cancel the data loading process.
   */
  public void cancelDataLoading() {
    this.processingMessage += "Process Cancelled.\n";
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
   * Return the google chart url that present all streams within the given list.
   * 
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getChartUrl(List<SelectableTelemetryStream> streams) {
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.width, this.height);
    Map<String, TelemetryStreamYAxis> streamAxisMap = new HashMap<String, TelemetryStreamYAxis>();
    
    //combine streams Y axes.
    for (SelectableTelemetryStream stream : streams) {
      if (stream.isEmpty()) {
        continue;
      }
      double streamMax = stream.getMaximum();
      double streamMin = stream.getMinimum();
      String streamUnitName = stream.getUnitName();
      TelemetryStreamYAxis axis = streamAxisMap.get(streamUnitName);
      if (axis == null) {
        axis = newYAxis(streamUnitName, streamMax, streamMin);
        stream.setColor(axis.getColor());
        streamAxisMap.put(streamUnitName, axis);
      }
      else {
        double axisMax = axis.getMaximum();
        double axisMin = axis.getMinimum();
        axis.setMaximum((axisMax > streamMax) ? axisMax : streamMax);
        axis.setMinimum((axisMin < streamMin) ? axisMin : streamMin);
        stream.setColor(axis.getColor());
      }
    }
    //add the y axis and extend the range if it contains only one horizontal line.
    for (TelemetryStreamYAxis axis : streamAxisMap.values()) {
      String axisType = "r";
      if (googleChart.isYAxisEmpty()) {
        axisType = "y";
      }
      List<Double> rangeList = new ArrayList<Double>();
      //extend the range of the axis if it contains only one vertical straight line.
      if (axis.getMaximum() - axis.getMinimum() < 0.1) {
        axis.setMaximum(axis.getMaximum() + 1.0);
        axis.setMinimum(axis.getMinimum() - 1.0);
      }
      rangeList.add(axis.getMinimum());
      rangeList.add(axis.getMaximum());
      //add the axis to the chart.
      googleChart.addAxisRange(axisType, rangeList, axis.getColor());
    }
    //add streams to the chart.
    for (int i = 0; i < streams.size(); ++i) {
      SelectableTelemetryStream stream = streams.get(i);
      if (!stream.isEmpty()) {
        TelemetryStreamYAxis axis = streamAxisMap.get(stream.getUnitName());
        this.addStreamToChart(stream, axis.getMinimum(), axis.getMaximum(), googleChart);
      }
    }
    //add the x axis.
    if (!streams.isEmpty()) {
      googleChart.addAxisLabel("x", 
          getDateList(streams.get(0).getTelemetryStream().getTelemetryPoint()), "");
    }
    return googleChart.getUrl();
  }


  /**
   * Add the given stream to the google chart. And return the maximum value of the stream.
   * 
   * @param stream the given stream.
   * @param maximum the maximum value of the range this stream will be associated to.
   * @param minimum the minimum value of the range this stream will be associated to.
   * @param googleChart the google chart.
   */
  public void addStreamToChart(SelectableTelemetryStream stream,
      double minimum, double maximum, GoogleChart googleChart) {
    // add the chart only if the stream is not empty.
    if (!stream.isEmpty()) {
      //add stream data
      googleChart.getChartData().add(stream.getStreamData());
      //prepare the range data.
      List<Double> range = getRangeList(minimum, maximum);
      //add range
      googleChart.getChartDataScale().add(range);
      //add stream color
      googleChart.addColor(stream.getColor());
      //add line style;
      googleChart.addLineStyle(stream.getThickness(), 
          stream.getLineLength(), stream.getBlankLength());
      /*googleChart.addAxisRange(axisType, range, randomColor);*/
      //add markers
      /*stream.setMarker(GoogleChart.getRandomMarker());*/
      googleChart.getChartMarker().add(stream.getMarker());
      googleChart.getMarkerColors().add(stream.getColor());
    }
  }

  /**
   * create a new TelemetryStreamYAxis instance with given parameters.
   * @param streamUnitName the unit name
   * @param streamMax the maximum value
   * @param streamMin the minimum value
   * @return the new object
   */
  private TelemetryStreamYAxis newYAxis(String streamUnitName, double streamMax, double streamMin) {
    TelemetryStreamYAxis axis = new TelemetryStreamYAxis(streamUnitName);
    axis.setMaximum(streamMax);
    axis.setMinimum(streamMin);
    axis.setColor(GoogleChart.getRandomColor());
    return axis;
  }
  
  /**
   * return a list that contains the range minimum and maximum.
   * The minimum value and maximum value will be normalized accordingly.
   * @param min the minimum value
   * @param max the maximum value
   * @return the list
   */
  private List<Double> getRangeList(double min, double max) {
    double minimum = min;
    double maximum = max;
    List<Double> rangeList = new ArrayList<Double>();
    if (minimum == maximum) {
      minimum -= 0.5;
      minimum = (minimum < 0) ? 0 : minimum;
      maximum += 0.5;
    }
    minimum = Math.floor(minimum);
    maximum = Math.ceil(maximum);
    rangeList.add(minimum);
    rangeList.add(maximum);
    return rangeList;
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
  /*
  public String getProjectChart(Project project) {
    String chartLink = this.projectCharts.get(project);
    if (chartLink == null) {
      chartLink = this.getChartUrl(project);
      this.projectCharts.put(project, chartLink);
    }
    return chartLink;
  }
  */

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
  /*
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
  */
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
   * update the selectedChart.
   * @return true if the chart is successfully updated.
   */
  public boolean updateSelectedChart() {
    List<SelectableTelemetryStream> streams = new ArrayList<SelectableTelemetryStream>();
    boolean dashed = false;
    for (Project project : this.selectedProjects) {
      List<SelectableTelemetryStream> streamList = this.getTelemetryStream(project);
      String projectMarker = null;
      double thickness = 2;
      double lineLength = 1;
      double blankLength = 0;
      boolean isLineStylePicked = false;
      for (SelectableTelemetryStream stream : streamList) {
        if (stream.isSelected()) {
          if (projectMarker == null) {
            projectMarker = GoogleChart.getNextMarker();
          }
          if (!isLineStylePicked) {
            isLineStylePicked = true;
            if (dashed) {
              lineLength = 6;
              blankLength = 3;
            }
            dashed = !dashed;
          }
          stream.setMarker(projectMarker);
          stream.setThickness(thickness);
          stream.setLineLength(lineLength);
          stream.setBlankLength(blankLength);
          streams.add(stream);
        }
        else {
          stream.setColor("");
          stream.setMarker("");
        }
      }
    }
    if (streams.isEmpty()) {
      selectedChart = "";
      return false;
    }
    selectedChart = this.getChartUrl(streams);
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
