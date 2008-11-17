package org.hackystat.projectbrowser.page.trajectory.datapanel;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.Processable;
import org.hackystat.projectbrowser.page.trajectory.ProjectRecord;
import org.hackystat.projectbrowser.page.trajectory.dtw.DTWAlignment;
import org.hackystat.projectbrowser.page.trajectory.dtw.DTWException;
import org.hackystat.projectbrowser.page.trajectory.dtw.DTWFactory;
import org.hackystat.projectbrowser.page.trajectory.dtw.EuclideanDistance;
import org.hackystat.projectbrowser.page.trajectory.dtw.step.SymmetricStepFunction;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.logger.HackystatLogger;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data model to hold state of the telemetry chart.
 * 
 * @author Shaoxuan Zhang, Pavel Senin
 */
public class TrajectoryChartDataModel implements Serializable, Processable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The granularity of the chart. Either Day, Week, or Month. */
  private String granularity = "Day";

  /** The project this user has selected. */
  private Map<Project, String> projectCharts = new HashMap<Project, String>();

  /** The projects this user has selected. */
  private ProjectRecord selectedProject1 = new ProjectRecord();
  // /** The start date this user has selected. */
  // private long project1StartDate = 0;
  // /** The end date this user has selected. */
  // private long project1EndDate = 0;
  /** The total length of the timeseries within the project1. */
  private int selectedProject1StreamLength;

  /** The projects this user has selected. */
  private ProjectRecord selectedProject2 = new ProjectRecord();
  // /** The start date this user has selected. */
  // private long project2StartDate = 0;
  // /** The end date this user has selected. */
  // private long project2EndDate = 0;
  /** The total length of the timeseries within the project2. */
  private int selectedProject2StreamLength;

  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The parameters for this telemetry chart. */
  private List<String> parameters = new ArrayList<String>();
  /** Store the data retrieved from telemetry service. */
  private Map<Project, List<SelectableTrajectoryStream>> projectStreamData = 
    new HashMap<Project, List<SelectableTrajectoryStream>>();
  /** Chart with selected project streams. */
  private String selectedChart = null;
  /** the width of this chart. */
  private int width = 800;
  /** the height of this chart. */
  private int height = 300;
  /** state of data loading process. */
  private volatile boolean inProcess = false;
  /** result of data loading. */
  private volatile boolean complete = false;
  /** message to display when data loading is in process. */
  private String processingMessage = "";
  /** host of the telemetry host. */
  private String telemetryHost;
  /** email of the user. */
  private String email;
  /** password of the user. */
  private String password;
  private int maxStreamLength;

  /** Chart with selected project streams. */
  private String normalizedTSChart = null;

  /** Chart with selected project streams. */
  private String dtwChart = null;

  private static final String CR = "\n";
  private static final String IDNT = "                ";
  private static final String IDNT2 = "                  ";
  private static final String MARK = "[DEBUG] ";

  private String dtwStatistics = "dtw Placeholder";
  private String paramErrorMessage = " TEST ";

  /**
   * Set all the parameters for the current chart.
   * 
   * @param selectedProject1 The trajectory project1.
   * @param selectedProject2 The trajectory project2.
   * @param telemetryName The selected telemetry stream name.
   * @param granularity The seleceted granularity.
   * @param parameters Additional telemetry parameters.
   * 
   */
  public void setModel(ProjectRecord selectedProject1, ProjectRecord selectedProject2,
      String telemetryName, String granularity, List<IModel> parameters) {

    // okay, going to get some updates here
    this.inProcess = true;
    this.complete = false;

    // but log first what we got from that form
    getLogger().log(
        Level.FINER,
        MARK + "TrajectoryChartModelParameters: setModel()" + CR + IDNT + "selectedProject1: "
            + selectedProject1.getProject().getName() + CR + IDNT + IDNT2 + "startDate: "
            + selectedProject1.getStartDate() + CR + IDNT + IDNT2 + "endDate: "
            + selectedProject1.getEndDate() + CR + IDNT + IDNT2 + "indent: "
            + selectedProject1.getIndent() + CR + IDNT + "selectedProject2: "
            + selectedProject2.getProject().getName() + CR + IDNT + IDNT2 + "startDate: "
            + selectedProject2.getStartDate() + CR + IDNT + IDNT2 + "endDate: "
            + selectedProject2.getEndDate() + CR + IDNT + IDNT2 + "indent: "
            + selectedProject2.getIndent() + CR + IDNT + "telemetry: " + telemetryName + CR + IDNT
            + "granularity: " + granularity + CR + IDNT + "parameters: " + CR
            + parametersToLog(parameters));

    // clear the process message and get sensorbase config from session
    this.processingMessage = "";
    this.telemetryHost = ((ProjectBrowserApplication) ProjectBrowserSession.get().getApplication())
        .getTelemetryHost();
    this.email = ProjectBrowserSession.get().getEmail();
    this.password = ProjectBrowserSession.get().getPassword();

    // reset and set parameters
    this.selectedProject1 = selectedProject1;
    this.selectedProject2 = selectedProject2;
    this.granularity = granularity;
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
    this.normalizedTSChart = null;
    this.dtwChart = null;
  }

  /**
   * Load data from Hackystat service.
   */
  public void loadData() {
    Logger logger = HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
    this.inProcess = true;
    this.complete = false;
    logger.log(Level.FINER, MARK + "TrajectoryChartDataModel: loadData() INPROCESS: "
        + isInProcess() + ", COMPLETE: " + isComplete());
    this.processingMessage = "Retrieving telemetry chart <" + getTelemetryName()
        + "> from Hackystat Telemetry service.\n";
    try {
      TelemetryClient client = new TelemetryClient(this.telemetryHost, this.email, this.password);

      // Loading data for the project #1
      //
      List<SelectableTrajectoryStream> streamList = new ArrayList<SelectableTrajectoryStream>();
      Project project = selectedProject1.getProject();

      this.processingMessage += "Retrieving data for project: " + project.getName() + ".\n";
      logger.log(Level.FINER, MARK + "Retrieving chart <" + getTelemetryName()
          + "> data from the telemetry: " + CR + IDNT + "project: "
          + selectedProject1.getProject().getName() + CR + IDNT + "start: "
          + Tstamp.makeTimestamp(selectedProject1.getStartDate().getTime()) + CR + IDNT + "end: "
          + Tstamp.makeTimestamp(selectedProject1.getEndDate().getTime()) + CR + IDNT
          + "parameters: " + this.getParameterAsString());

      List<TelemetryStream> streams = client.getChart(this.getTelemetryName(), project.getOwner(),
          project.getName(), granularity,
          Tstamp.makeTimestamp(selectedProject1.getStartDate().getTime()),
          Tstamp.makeTimestamp(selectedProject1.getEndDate().getTime()),
          this.getParameterAsString()).getTelemetryStream();

      for (TelemetryStream stream : streams) {
        streamList.add(new SelectableTrajectoryStream(stream, selectedProject1.getIndent()));
      }
      this.projectStreamData.put(project, streamList);

      if (streams.isEmpty()) {
        selectedProject1StreamLength = -1;
      }
      else {
        selectedProject1StreamLength = streams.get(0).getTelemetryPoint().size();
      }
      logger.log(Level.FINER, MARK + "Finished retrieving chart <" + getTelemetryName()
          + "> for project: " + project.getName() + " streams retrieved: " + streams.size()
          + " length: " + selectedProject1StreamLength);

      // Loading data for the project #2
      //
      streamList = new ArrayList<SelectableTrajectoryStream>();
      project = selectedProject2.getProject();

      this.processingMessage += "Retrieving data for project: " + project.getName() + ".\n";
      logger.log(Level.FINER, MARK + "Retrieving chart <" + getTelemetryName()
          + "> data from the telemetry: " + CR + IDNT + "project: "
          + selectedProject2.getProject().getName() + CR + IDNT + "start: "
          + Tstamp.makeTimestamp(selectedProject2.getStartDate().getTime()) + CR + IDNT + "end: "
          + Tstamp.makeTimestamp(selectedProject2.getEndDate().getTime()) + CR + IDNT
          + "parameters: " + this.getParameterAsString());

      streams = client.getChart(this.getTelemetryName(), project.getOwner(), project.getName(),
          granularity, Tstamp.makeTimestamp(selectedProject2.getStartDate().getTime()),
          Tstamp.makeTimestamp(selectedProject2.getEndDate().getTime()),
          this.getParameterAsString()).getTelemetryStream();

      for (TelemetryStream stream : streams) {
        streamList.add(new SelectableTrajectoryStream(stream));
      }
      this.projectStreamData.put(project, streamList);

      if (streams.isEmpty()) {
        selectedProject2StreamLength = -1;
      }
      else {
        selectedProject2StreamLength = streams.get(0).getTelemetryPoint().size();
      }
      logger.log(Level.FINER, MARK + "Finished retrieving chart <" + getTelemetryName()
          + "> for project: " + project.getName() + " streams retrieved: " + streams.size()
          + " length: " + selectedProject2StreamLength);

      // save the maximal stream length
      //
      maxStreamLength = Math.max(selectedProject1StreamLength + this.selectedProject1.getIndent(),
          selectedProject2StreamLength + this.selectedProject2.getIndent());
      logger.log(Level.FINER, MARK + "the longest stream is " + maxStreamLength + " points.");

    }
    catch (TelemetryClientException e) {
      String errorMessage = "Errors when retrieving " + getTelemetryName() + " telemetry data: "
          + e.getMessage() + ". Please try again.";
      this.processingMessage += errorMessage + "\n";

      logger.warning(errorMessage);
      this.complete = false;
      this.inProcess = false;
      return;
    }

    this.inProcess = false;
    this.complete = true;
    this.processingMessage += "All done.\n";
    logger.log(Level.FINER, "[DEBUG] TrajectoryChartDataModel: loadData() INPROCESS: "
        + isInProcess() + ", COMPLETE: " + isComplete());

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
  public String getProject1StartDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT,
        Locale.ENGLISH);
    return format.format(new Date(this.selectedProject1.getStartDate().getTime()));
  }

  /**
   * Returns the end date in yyyy-MM-dd format.
   * 
   * @return The date as a simple string.
   */
  public String getProject1EndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT,
        Locale.ENGLISH);
    return format.format(new Date(this.selectedProject1.getEndDate().getTime()));
  }

  /**
   * Returns the start date in yyyy-MM-dd format.
   * 
   * @return The date as a simple string.
   */
  public String getProject2StartDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT,
        Locale.ENGLISH);
    return format.format(new Date(this.selectedProject2.getStartDate().getTime()));
  }

  /**
   * Returns the end date in yyyy-MM-dd format.
   * 
   * @return The date as a simple string.
   */
  public String getProject2EndDateString() {
    SimpleDateFormat format = new SimpleDateFormat(ProjectBrowserBasePage.DATA_FORMAT,
        Locale.ENGLISH);
    return format.format(new Date(this.selectedProject2.getEndDate().getTime()));
  }

  /**
   * @return the telemetryName
   */
  public String getTelemetryName() {
    return telemetryName;
  }

  /**
   * Returns true if this model does not contain any data.
   * 
   * @return True if no data.
   */
  public boolean isEmpty() {
    return (null == this.selectedProject1) && (null == this.selectedProject2);
  }

  /**
   * Return the list of TelemetryStream associated with the given project.
   * 
   * @param project the given project.
   * @return the list of TelemetryStream.
   */
  public List<SelectableTrajectoryStream> getTrajectoryStream(Project project) {
    List<SelectableTrajectoryStream> streamList = this.projectStreamData.get(project);
    if (streamList == null) {
      streamList = new ArrayList<SelectableTrajectoryStream>();
    }
    return streamList;
  }

  /**
   * @return the selectedChart
   */
  public String getSelectedChart() {
    getLogger()
        .log(Level.FINER, MARK + "TrajectoryChartDataModel: getting selected TelemetryChart");
    return selectedChart;
  }

  /**
   * @return true if the chart is empty.
   */
  public boolean isChartEmpty() {
    return selectedChart == null || "".equals(selectedChart);
  }

  /**
   * @return the selectedChart
   */
  public String getNormalizedTSChart() {
    getLogger().log(Level.FINER, MARK + "TrajectoryChartDataModel, getting Normalized TS chart");
    return normalizedTSChart;
  }

  /**
   * @return true if the chart is empty.
   */
  public boolean isNormalizedTSChartEmpty() {
    return normalizedTSChart == null || "".equals(normalizedTSChart);
  }

  /**
   * @return the selectedChart
   */
  public String getDTWChart() {
    getLogger().log(Level.FINER, MARK + "TrajectoryChartDataModel, getting DTW chart");
    return dtwChart;
  }

  /**
   * @return true if the chart is empty.
   */
  public boolean isDTWChartEmpty() {
    return dtwChart == null || "".equals(dtwChart);
  }

  /**
   * update the selectedChart.
   * 
   * @return true if the chart is successfully updated.
   */
  public boolean updateSelectedChart() {
    getLogger().log(Level.FINER, MARK + "TelemetryChart: updating");
    // Since we have only two projects here let's plot them
    //
    List<SelectableTrajectoryStream> streams = new ArrayList<SelectableTrajectoryStream>();
    boolean dashed = false;
    ArrayList<ProjectRecord> rec = new ArrayList<ProjectRecord>();
    rec.add(selectedProject1);
    rec.add(selectedProject2);
    for (ProjectRecord projectRec : rec) {
      Project project = projectRec.getProject();
      getLogger()
          .log(Level.FINER, MARK + "TelemetryChart: processing project " + project.getName());
      List<SelectableTrajectoryStream> streamList = this.getTrajectoryStream(project);
      getLogger().log(Level.FINER,
          MARK + "TelemetryChart: found " + streamList.size() + " stream(s)."); // NOPMD
      String projectMarker = null;
      double thickness = 2;
      double lineLength = 1;
      double blankLength = 0;
      boolean isLineStylePicked = false;
      for (SelectableTrajectoryStream stream : streamList) {
        getLogger().log(
            Level.FINER,
            MARK + "TelemetryChart: processing stream " + stream.getTelemetryStream().getName()
                + ", selected: " + stream.isSelected() + ", length: "
                + stream.getStreamData().size());
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
          stream.setIndent(projectRec.getIndent());
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
    selectedChart = this.getTelemetryChartURL(streams);
    getLogger().log(Level.FINER, MARK + "TelemetryChart URL: " + selectedChart);
    return true;
  }

  /**
   * Extract the time series from the selectedChart, normalize those and chart them.
   * 
   * @return true if the chart is successfully updated.
   */
  public boolean updateNormalizedTSChart() {
    getLogger().log(Level.FINER, MARK + "Normalized TS Chart: updating.");
    boolean dashed = false;
    // Since we have only two projects here let's plot them
    //
    // create the list of streams we are about to plot
    List<SelectableTrajectoryStream> streams = new ArrayList<SelectableTrajectoryStream>();
    //
    // make sure we do know which projects we are working on
    ArrayList<ProjectRecord> rec = new ArrayList<ProjectRecord>();
    rec.add(selectedProject1);
    rec.add(selectedProject2);
    //
    // now, iterate over this projects and make chart happen
    for (ProjectRecord projectRec : rec) {
      Project project = projectRec.getProject();
      getLogger().log(Level.FINER,
          MARK + "Normalized TS Chart: processing project " + project.getName());
      List<SelectableTrajectoryStream> streamList = this.getTrajectoryStream(project);
      getLogger().log(Level.FINER,
          MARK + "Normalized TS Chart: found " + streamList.size() + " stream(s).");
      String projectMarker = null;
      double thickness = 2;
      double lineLength = 1;
      double blankLength = 0;
      boolean isLineStylePicked = false;
      for (SelectableTrajectoryStream stream : streamList) {
        getLogger().log(
            Level.FINER,
            MARK + "Normalized TS Chart: processing stream "
                + stream.getTelemetryStream().getName() + ", selected: " + stream.isSelected()
                + ", length: " + stream.getStreamData().size());
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
          stream.setIndent(projectRec.getIndent());
          streams.add(stream);
        }
        else {
          stream.setColor("");
          stream.setMarker("");
        }
      }
    }
    if (streams.isEmpty()) {
      normalizedTSChart = "";
      return false;
    }
    normalizedTSChart = this.getNormalizedTSChartURL(streams);
    getLogger().log(Level.FINER, MARK + "Normalized TS Chart URL: " + normalizedTSChart);
    return true;
  }

  /**
   * Extract the time series from the selectedChart, normalize those and chart them.
   * 
   * @return true if the chart is successfully updated.
   */
  public boolean updateDTWChart() {
    getLogger().log(Level.FINER, MARK + "DTW Chart: updating.");
    boolean dashed = false;
    // Since we have only two projects here let's plot them
    //
    // create the list of streams we are about to plot
    List<SelectableTrajectoryStream> streams = new ArrayList<SelectableTrajectoryStream>();
    //
    // make sure we do know which projects we are working on
    ArrayList<ProjectRecord> rec = new ArrayList<ProjectRecord>();
    rec.add(selectedProject1);
    rec.add(selectedProject2);
    //
    // now, iterate over this projects and make chart happen
    for (ProjectRecord projectRec : rec) {
      Project project = projectRec.getProject();
      getLogger().log(Level.FINER, MARK + "DTW Chart: processing project " + project.getName());
      List<SelectableTrajectoryStream> streamList = this.getTrajectoryStream(project);
      getLogger().log(Level.FINER, MARK + "DTW Chart: found " + streamList.size() + " stream(s).");
      String projectMarker = null;
      double thickness = 2;
      double lineLength = 1;
      double blankLength = 0;
      boolean isLineStylePicked = false;
      for (SelectableTrajectoryStream stream : streamList) {
        getLogger().log(
            Level.FINER,
            MARK + "DTW Chart: processing stream " + stream.getTelemetryStream().getName()
                + ", selected: " + stream.isSelected() + ", length: "
                + stream.getStreamData().size());
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
          stream.setIndent(projectRec.getIndent());
          streams.add(stream);
        }
        else {
          stream.setColor("");
          stream.setMarker("");
        }
      }
    }
    if (streams.isEmpty()) {
      dtwChart = "";
      return false;
    }
    dtwChart = this.getDTWChartURL(streams);
    getLogger().log(Level.FINER, MARK + "DTW chart URL:" + dtwChart);
    return true;
  }

  /**
   * Get the DTW statistics.
   * 
   * @return DTW statistics.
   */
  public String getDTWStatistics() {
    return this.dtwStatistics;
  }

  /**
   * Return the google chart url that present all streams within the given list.
   * 
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getTelemetryChartURL(List<SelectableTrajectoryStream> streams) {
    getLogger().log(Level.FINER,
        MARK + "TelemetryChart: getting url for " + streams.size() + " stream(s).");
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.width, this.height);
    TrajectoryStreamYAxis streamAxis = null;

    // make up the Y axis for all streams
    for (SelectableTrajectoryStream stream : streams) {
      if (stream.isEmpty()) {
        continue;
      }
      getLogger().log(
          Level.FINER,
          MARK + "TelemetryChart: processing stream " + stream.getTelemetryStream().getName()
              + ", length " + stream.getTelemetryStream().getTelemetryPoint().size());
      double streamMax = stream.getMaximum();
      double streamMin = stream.getMinimum();
      String streamUnitName = stream.getUnitName();
      if (streamAxis == null) {
        streamAxis = newYAxis(streamUnitName, streamMax, streamMin);
      }
      else {
        double axisMax = streamAxis.getMaximum();
        double axisMin = streamAxis.getMinimum();
        streamAxis.setMaximum((axisMax > streamMax) ? axisMax : streamMax);
        streamAxis.setMinimum((axisMin < streamMin) ? axisMin : streamMin);
      }
      stream.setColor(GoogleChart.getNextJetColor());
    }
    // add the y axis and extend the range if it contains only one horizontal line.
    String axisType = "r";
    if (googleChart.isYAxisEmpty()) {
      axisType = "y";
    }
    List<Double> rangeList = new ArrayList<Double>();
    // extend the range of the axis if it contains only one vertical straight line.
    if (streamAxis.getMaximum() - streamAxis.getMinimum() < 0.1) {
      streamAxis.setMaximum(streamAxis.getMaximum() + 1.0);
      streamAxis.setMinimum(streamAxis.getMinimum() - 1.0);
    }
    rangeList.add(streamAxis.getMinimum());
    rangeList.add(streamAxis.getMaximum());
    // add the axis to the chart.
    googleChart.addAxisRange(axisType, rangeList, streamAxis.getColor());

    // add streams to the chart.
    for (int i = 0; i < streams.size(); ++i) {
      SelectableTrajectoryStream stream = streams.get(i);
      if (!stream.isEmpty()) {
        this.addStreamToChart(stream, streamAxis.getMinimum(), streamAxis.getMaximum(),
            maxStreamLength, googleChart);
      }
    }
    // add the x axis, X axis is a list of markers
    if (!streams.isEmpty()) {
      googleChart.addAxisLabel("x", getMarkersList(), "");
    }
    return googleChart.getUrl();
  }

  /**
   * Return the google chart url that present normalized streams.
   * 
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getNormalizedTSChartURL(List<SelectableTrajectoryStream> streams) {
    getLogger().log(Level.FINER,
        MARK + "Normalized TS: getting url for " + streams.size() + " stream(s).");
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.width, this.height);
    TrajectoryStreamYAxis streamAxis = null;

    // okay here we are expecting to see two streams in the input array - get them normalized.
    double streamMin = Double.MAX_VALUE;
    double streamMax = Double.MIN_VALUE;
    for (SelectableTrajectoryStream stream : streams) {
      if (!stream.isEmpty()) {
        List<Double> normalizedStreamData = stream.getNormalizedStreamData();
        if (streamMin > Collections.min(normalizedStreamData)) {
          streamMin = Collections.min(normalizedStreamData);
        }
        if (streamMax < Collections.max(normalizedStreamData)) {
          streamMax = Collections.max(normalizedStreamData);
        }
      }
    }
    getLogger().log(Level.FINER,
        MARK + "Normalized TS: min: " + streamMin + ", maximum: " + streamMax);

    List<ArrayList<Double>> normalizedStreams = new ArrayList<ArrayList<Double>>();

    // make up the Y axis for all streams
    for (SelectableTrajectoryStream stream : streams) {
      if (!stream.isEmpty()) {
        String streamUnitName = stream.getUnitName();
        streamAxis = newYAxis(streamUnitName, streamMax, streamMin);
        stream.setColor(GoogleChart.getNextJetColor());
      }
    }

    // add the y axis and extend the range if it contains only one horizontal line.
    String axisType = "r";
    if (googleChart.isYAxisEmpty()) {
      axisType = "y";
    }

    List<Double> rangeList = new ArrayList<Double>();
    rangeList.add(streamAxis.getMinimum());
    rangeList.add(streamAxis.getMaximum());

    // add the axis to the chart.
    googleChart.addAxisRange(axisType, rangeList, streamAxis.getColor());

    // add streams to the chart.
    for (int i = 0; i < streams.size(); ++i) {
      SelectableTrajectoryStream stream = streams.get(i);
      if (!stream.isEmpty()) {
        ArrayList<Double> s = this.addStreamToNormalizedChart(stream, streamAxis.getMinimum(),
            streamAxis.getMaximum(), maxStreamLength, googleChart);
        normalizedStreams.add(s);
      }
    }
    // add the x axis, X axis is a list of markers
    if (!streams.isEmpty()) {
      googleChart.addAxisLabel("x", getMarkersList(), "");
      try {
        NumberFormat decFormatter = new DecimalFormat("00.00");
        googleChart.setTitle("Normalized streamData|Euclidean distance="
            + decFormatter.format(this.getEuclideanDistance(normalizedStreams.get(0),
                normalizedStreams.get(1))));
      }
      catch (DTWException e) {
        e.printStackTrace();
      }
    }
    return googleChart.getUrl();
  }

  /**
   * Calculates the Euclidean distance.
   * 
   * @param s1 time series 1.
   * @param s2 time series 2.
   * @return the Euclidean distance.
   * @throws DTWException if error occures.
   */
  private Double getEuclideanDistance(List<Double> s1, List<Double> s2) throws DTWException {
    double[][] stream1 = new double[s1.size()][2];
    int i = 0;
    for (Double d : s1) {
      stream1[i][0] = Integer.valueOf(i).doubleValue();
      stream1[i][1] = d.doubleValue();
      i++;
    }
    double[][] stream2 = new double[s2.size()][2];
    i = 0;
    for (Double d : s2) {
      stream2[i][0] = Integer.valueOf(i).doubleValue();
      stream2[i][1] = d.doubleValue();
      i++;
    }
    return EuclideanDistance.getSeriesDistnace(stream1, stream2);
  }

  /**
   * Return the google chart url that present normalized streams.
   * 
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getDTWChartURL(List<SelectableTrajectoryStream> streams) {
    getLogger().log(Level.FINER,
        MARK + "DTW Chart: getting url for " + streams.size() + " stream(s).");
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.width, this.height);
    TrajectoryStreamYAxis streamAxis = null;

    // okay here we are expecting to see two streams in the input array - get them normalized.
    double streamMin = Double.MAX_VALUE;
    double streamMax = Double.MIN_VALUE;
    for (SelectableTrajectoryStream stream : streams) {
      if (!stream.isEmpty()) {
        List<Double> normalizedStreamData = stream.getNormalizedStreamData();
        if (streamMin > Collections.min(normalizedStreamData)) {
          streamMin = Collections.min(normalizedStreamData);
        }
        if (streamMax < Collections.max(normalizedStreamData)) {
          streamMax = Collections.max(normalizedStreamData);
        }
      }
    }

    getLogger().log(Level.FINER,
        MARK + "Normalized TS: min: " + streamMin + ", maximum: " + streamMax);

    // make up the Y axis for all streams
    for (SelectableTrajectoryStream stream : streams) {
      if (stream.isEmpty()) {
        continue;
      }
      getLogger().log(
          Level.FINER,
          MARK + "DTW Chart: processing stream " + stream.getTelemetryStream().getName()
              + ", length " + stream.getTelemetryStream().getTelemetryPoint().size());
      String streamUnitName = stream.getUnitName();
      streamAxis = newYAxis(streamUnitName, streamMax, streamMin);
      stream.setColor(GoogleChart.getNextJetColor());
    }

    // add the y axis and extend the range if it contains only one horizontal line.
    String axisType = "r";
    if (googleChart.isYAxisEmpty()) {
      axisType = "y";
    }

    List<Double> rangeList = new ArrayList<Double>();
    rangeList.add(streamAxis.getMinimum());
    rangeList.add(streamAxis.getMaximum());

    // add the axis to the chart.
    googleChart.addAxisRange(axisType, rangeList, streamAxis.getColor());

    // run DTW in here
    //
    // extract time series as the list of double values
    List<List<Double>> rawSeries = this.getRawSeriesForDTW(streams);
    List<List<Double>> dtwSeries = this.getDTWSeries(rawSeries);

    // add streams to the chart.
    // add streams to the chart.
    for (int i = 0; i < streams.size(); ++i) {
      SelectableTrajectoryStream stream = streams.get(i);
      if (!stream.isEmpty()) {
        this.addStreamToDTWChart(stream, dtwSeries.get(i), streamAxis.getMinimum(), streamAxis
            .getMaximum(), maxStreamLength, googleChart);
      }
    }

    // add the x axis, X axis is a list of markers
    if (!streams.isEmpty()) {
      googleChart.addAxisLabel("x", getMarkersList(), "");
      try {
        NumberFormat decFormatter = new DecimalFormat("00.00");
        googleChart.setTitle("Normalized streamData|Euclidean distance="
            + decFormatter.format(this.getEuclideanDistance(dtwSeries.get(0), dtwSeries.get(1))));
      }
      catch (DTWException e) {
        e.printStackTrace();
      }
    }
    return googleChart.getUrl();
  }

  /**
   * Performs DTW alignment.
   * 
   * @param rawSeries the series to align.
   * @return aligned series.
   */
  private List<List<Double>> getDTWSeries(List<List<Double>> rawSeries) {

    List<List<Double>> res = new ArrayList<List<Double>>();

    List<Double> query = rawSeries.get(0);
    double[][] queryD = new double[query.size()][1];
    int i = 0;
    for (Double n : query) {
      queryD[i][0] = n.doubleValue();
      i++;
    }

    List<Double> template = rawSeries.get(1);
    double[][] templateD = new double[template.size()][1];
    i = 0;
    for (Double n : template) {
      templateD[i][0] = n.doubleValue();
      i++;
    }

    try {
      DTWAlignment r = DTWFactory.doDTW(queryD, templateD, SymmetricStepFunction.STEP_PATTERN_P0);
      // getLogger().log(Level.FINER, MARK + "DTW Record: " + CR + r.toString());
      res.add(template);

      double[] rawWarpingQuery = r.getWarpingQuery();
      List<Double> warpingQuery = new ArrayList<Double>();
      for (int k = 0; k < rawWarpingQuery.length; k++) {
        warpingQuery.add(Double.valueOf(rawWarpingQuery[k]));
      }

      res.add(warpingQuery);
      return res;
    }
    catch (DTWException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Prepares series for the alignment - extracts data from streams.
   * 
   * @param streams input streams
   * @return extracted data.
   */
  private List<List<Double>> getRawSeriesForDTW(List<SelectableTrajectoryStream> streams) {
    getLogger().log(Level.FINER, MARK + "DTW Chart: getting raw series.");
    List<List<Double>> res = new ArrayList<List<Double>>();
    // add streams to the chart.
    for (int i = 0; i < streams.size(); ++i) {
      SelectableTrajectoryStream stream = streams.get(i);
      if (!stream.isEmpty()) {
        res.add(stream.getNormalizedStreamData());
      }
    }
    if (!res.isEmpty()) {
      StringBuffer sb = new StringBuffer(1000);
      NumberFormat decFormatter = new DecimalFormat("####0.00");
      for (List<Double> l : res) {
        for (Double d : l) {
          sb.append(decFormatter.format(d));
          sb.append(", ");
        }
        sb.append('|');
      }
      getLogger().log(Level.FINER, MARK + "DTW Chart: RAW series: " + sb.toString());
      return res;
    }
    return null;
  }

  /**
   * Add the given stream to the google chart. And return the maximum value of the stream.
   * 
   * @param stream the given stream.
   * @param maximum the maximum value of the range this stream will be associated to.
   * @param minimum the minimum value of the range this stream will be associated to.
   * @param maxStreamLength the plotted stream length on X axis.
   * @param googleChart the google chart.
   */
  public void addStreamToChart(SelectableTrajectoryStream stream, double minimum, double maximum,
      Integer maxStreamLength, GoogleChart googleChart) {
    // add the chart only if the stream is not empty.
    if (!stream.isEmpty()) {
      // add stream data
      googleChart.getChartData().add(stream.getStreamData(maxStreamLength));
      // prepare the range data.
      List<Double> range = getRangeList(minimum, maximum);
      // add range
      googleChart.getChartDataScale().add(range);
      // add stream color
      googleChart.addColor(stream.getColor());
      // add line style;
      googleChart.addLineStyle(stream.getThickness(), stream.getLineLength(), stream
          .getBlankLength());
      /* googleChart.addAxisRange(axisType, range, randomColor); */
      // add markers
      /* stream.setMarker(GoogleChart.getRandomMarker()); */
      googleChart.getChartMarker().add(stream.getMarker());
      googleChart.getMarkerColors().add(stream.getColor());
    }
  }

  /**
   * Add the given stream to the google chart and return the normalized data.
   * 
   * @param stream the given stream.
   * @param maximum the maximum value of the range this stream will be associated to.
   * @param minimum the minimum value of the range this stream will be associated to.
   * @param maxStreamLength the plotted stream length on X axis.
   * @param googleChart the google chart.
   * 
   * @return normalized data extracted from the stream.
   */
  public ArrayList<Double> addStreamToNormalizedChart(SelectableTrajectoryStream stream,
      double minimum, double maximum, Integer maxStreamLength, GoogleChart googleChart) {
    // add the chart only if the stream is not empty.
    if (!stream.isEmpty()) {
      // add stream data
      ArrayList<Double> res = stream.getNormalizedStreamData();
      getLogger().log(Level.FINER, "TrajectoryChart: adding series: " + res);
      googleChart.getChartData().add(res);
      // prepare the range data.
      List<Double> range = getRangeList(minimum, maximum);
      // add range
      googleChart.getChartDataScale().add(range);
      // add stream color
      googleChart.addColor(stream.getColor());
      // add line style;
      googleChart.addLineStyle(stream.getThickness(), stream.getLineLength(), stream
          .getBlankLength());
      /* googleChart.addAxisRange(axisType, range, randomColor); */
      // add markers
      /* stream.setMarker(GoogleChart.getRandomMarker()); */
      googleChart.getChartMarker().add(stream.getMarker());
      googleChart.getMarkerColors().add(stream.getColor());
      return res;
    }
    return new ArrayList<Double>();
  }

  /**
   * Add the given stream to the google chart. And return the maximum value of the stream.
   * 
   * @param stream the given stream.
   * @param list the chart data.
   * @param maximum the maximum value of the range this stream will be associated to.
   * @param minimum the minimum value of the range this stream will be associated to.
   * @param maxStreamLength the plotted stream length on X axis.
   * @param googleChart the google chart.
   */
  public void addStreamToDTWChart(SelectableTrajectoryStream stream, List<Double> list,
      double minimum, double maximum, Integer maxStreamLength, GoogleChart googleChart) {
    // add the chart only if the stream is not empty.
    if (!stream.isEmpty()) {
      // add stream data
      googleChart.getChartData().add(list);
      // prepare the range data.
      List<Double> range = getRangeList(minimum, maximum);
      // add range
      googleChart.getChartDataScale().add(range);
      // add stream color
      googleChart.addColor(stream.getColor());
      // add line style;
      googleChart.addLineStyle(stream.getThickness(), stream.getLineLength(), stream
          .getBlankLength());
      /* googleChart.addAxisRange(axisType, range, randomColor); */
      // add markers
      /* stream.setMarker(GoogleChart.getRandomMarker()); */
      googleChart.getChartMarker().add(stream.getMarker());
      googleChart.getMarkerColors().add(stream.getColor());
    }
  }

  /**
   * create a new TelemetryStreamYAxis instance with given parameters.
   * 
   * @param streamUnitName the unit name
   * @param streamMax the maximum value
   * @param streamMin the minimum value
   * @return the new object
   */
  private TrajectoryStreamYAxis newYAxis(String streamUnitName, double streamMax, 
      double streamMin) {
    TrajectoryStreamYAxis axis = new TrajectoryStreamYAxis(streamUnitName);
    axis.setMaximum(streamMax);
    axis.setMinimum(streamMin);
    axis.setColor("00007F");
    return axis;
  }

  /**
   * return a list that contains the range minimum and maximum. The minimum value and maximum value
   * will be normalized accordingly.
   * 
   * @param min the minimum value
   * @param max the maximum value
   * @return the list
   */
  private List<Double> getRangeList(double min, double max) {
    double minimum = min;
    double maximum = max;
    List<Double> rangeList = new ArrayList<Double>();
    // if (minimum == maximum) {
    // minimum -= 0.5;
    // minimum = (minimum < 0) ? 0 : minimum;
    // maximum += 0.5;
    // }
    // minimum = Math.floor(minimum);
    // maximum = Math.ceil(maximum);
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.US);
    for (TelemetryPoint point : points) {
      dates.add(dateFormat.format(point.getTime().toGregorianCalendar().getTime()));
    }
    return dates;
  }

  /**
   * Return the date list inside this model.
   * 
   * @return the date list.
   */
  public List<String> getMarkersList() {
    // check if both streams are empty
    if (isEmpty()) {
      return new ArrayList<String>();
    }
    List<String> markersList = new ArrayList<String>();
    for (int i = 0; i < maxStreamLength; i++) {
      markersList.add(String.valueOf(i));
    }
    return markersList;
  }

  /**
   * @return the selectedProjects
   */
  public List<ProjectRecord> getSelectedProjects() {
    List<ProjectRecord> list = new ArrayList<ProjectRecord>();
    list.add(selectedProject1);
    list.add(selectedProject2);
    return list;
  }

  /**
   * Return the chart for the given project
   * 
   * @param project the given project.
   * @return the chart link.
   */
  /*
   * public String getProjectChart(Project project) { String chartLink =
   * this.projectCharts.get(project); if (chartLink == null) { chartLink =
   * this.getChartUrl(project); this.projectCharts.put(project, chartLink); } return chartLink; }
   */

  /**
   * Return the comma-separated list of parameters in String.
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
   * public String getOverallChart() { if (overallChart == null) { List<TelemetryStream> streams =
   * new ArrayList<TelemetryStream>(); List<String> projectNames = new ArrayList<String>(); for
   * (Project project : this.selectedProjects) { List<SelectableTelemetryStream> streamList =
   * this.getTelemetryStream(project); for (int i = 0; i < streamList.size(); ++i) {
   * streams.add(streamList.get(i).getTelemetryStream()); projectNames.add(project.getName()); } }
   * overallChart = this.getChartUrl(projectNames, streams); } return overallChart; }
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
   * Return statistics for the stream1.
   * 
   * @return the statistics for the stream1.
   */
  public String getStream1Statistics() {
    return this.selectedProject1.toLabelMessage();
  }

  /**
   * Return statistics for the stream2.
   * 
   * @return the statistics for the stream2.
   */
  public String getStream2Statistics() {
    return this.selectedProject2.toLabelMessage();
  }

  /**
   * Change all selected flags of streams to the given flag.
   * 
   * @param flag the boolean flag.
   */
  public void changeSelectionForAll(boolean flag) {
    for (List<SelectableTrajectoryStream> streamList : this.projectStreamData.values()) {
      for (SelectableTrajectoryStream stream : streamList) {
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

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
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
   * Set the paramErrorMessage.
   * 
   * @param message the message.
   */
  public void setWarningMessage(String message) {
    this.paramErrorMessage = message;
  }
  
  /**
   * @return the paramErrorMessage
   */
  public String getWarningMessage() {
    String temp = this.paramErrorMessage;
    return temp;
  }

  
}
