package org.hackystat.projectbrowser.page.telemetry.datapanel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.apache.wicket.model.IModel;
import org.hackystat.projectbrowser.googlechart.ChartType;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.page.loadingprocesspanel.Processable;
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
 * @author Shaoxuan Zhang
 */
public class TelemetryChartDataModel implements Serializable, Processable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** the width of this chart. */
  private int width = 850;
  /** the height of this chart. */
  private int height = 350;
  
  /** The start date this user has selected. */
  private long startDate = 0;
  /** The end date this user has selected. */
  private long endDate = 0;
  /** The granularity of the chart. Either Day, Week, or Month. */
  private String granularity = "Day";
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The analysis this user has selected. */
  private String telemetryName = null;
  /** The parameters for this telemetry chart. */
  private List<String> parameters = new ArrayList<String>();
  /** Store the data retrieved from telemetry service. */
  private final Map<Project, List<SelectableTelemetryStream>> projectStreamData = 
    new HashMap<Project, List<SelectableTelemetryStream>>();
  /** Chart with selected project streams. */
  private String selectedChart = null;
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
  /** The URL of google chart. */
  // private String chartUrl = "";
  
  /**
   * @param startDate the start date of this model..
   * @param endDate the end date of this model..
   * @param selectedProjects the project ofs this model.
   * @param telemetryName the telemetry name of this model.
   * @param granularity the granularity of this model, Day or Week or Month.
   * @param parameters the list of parameters
   * @param telemetryHost the telemetry host
   * @param email the user's email
   * @param password the user's passowrd
   */
  public void setModel(Date startDate, Date endDate, List<Project> selectedProjects,
      String telemetryName, String granularity, List<IModel> parameters,
      String telemetryHost, String email, String password) {
    this.processingMessage = "";
    this.telemetryHost = telemetryHost;
    this.email = email;
    this.password = password;
    this.startDate = startDate.getTime();
    this.endDate = endDate.getTime();
    this.granularity = granularity;
    this.selectedProjects = selectedProjects;
    this.telemetryName = telemetryName;
    //clear old data.
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
    this.processingMessage = "Retrieving telemetry chart <" + getTelemetryName() + 
    "> from Hackystat Telemetry service.\n";
    Logger logger = HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
    try {
      TelemetryClient client = new TelemetryClient(this.telemetryHost, this.email, this.password);
      //for each selected project
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        //prepare
        Project project = this.selectedProjects.get(i);
        this.processingMessage += "Retrieving data for project: " + project.getName() + 
            " (" + (i + 1) + " of " + this.selectedProjects.size() + ").\n";
        
        logger.info("Retrieving chart <" + getTelemetryName()
            + "> for project: " + project.getName());
        
        List<SelectableTelemetryStream> streamList = new ArrayList<SelectableTelemetryStream>();
        //retrieve data from server.
        List<TelemetryStream> streams = client.getChart(this.getTelemetryName(),
            project.getOwner(), project.getName(), granularity,
            Tstamp.makeTimestamp(startDate), Tstamp.makeTimestamp(endDate), 
            this.getParameterAsString()).getTelemetryStream();
        for (TelemetryStream stream : streams) {
          streamList.add(new SelectableTelemetryStream(stream));
        }
        this.projectStreamData.put(project, streamList);

        logger.info("Finished retrieving chart <" + getTelemetryName() + 
            "> for project: " + project.getName());
      }
    }
    catch (TelemetryClientException e) {
      String errorMessage = "Errors when retrieving " + getTelemetryName() + 
      " telemetry data: " + e.getMessage() + ". Please try again.";
      this.processingMessage += errorMessage + "\n";

      logger.warning(errorMessage);
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
   * update the selectedChart.
   * @return true if the chart is successfully updated.
   */
  public boolean updateSelectedChart() {
    List<SelectableTelemetryStream> streams = new ArrayList<SelectableTelemetryStream>();
    boolean dashed = false;
    for (Project project : this.selectedProjects) {
      //assign a same marker and line style to streams of the same project.
      List<SelectableTelemetryStream> streamList = this.getTelemetryStream(project);
      String projectMarker = null;
      double thickness = 2;
      double lineLength = 1;
      double blankLength = 0;
      boolean isLineStylePicked = false;
      for (SelectableTelemetryStream stream : streamList) {
        if (stream.isSelected() && !stream.isEmpty()) {
          //marker.
          if (projectMarker == null) {
            projectMarker = GoogleChart.getNextMarker();
          }
          //line style, dash or solid
          if (!isLineStylePicked) {
            isLineStylePicked = true;
            if (dashed) {
              lineLength = 6;
              blankLength = 3;
            }
            dashed = !dashed;
          }
          //add marker and line style to the stream.
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
   * Return the google chart url that present all streams within the given list.
   * 
   * @param streams the given stream list.
   * @return the URL string of the chart.
   */
  public String getChartUrl(List<SelectableTelemetryStream> streams) {
    GoogleChart googleChart = new GoogleChart(ChartType.LINE, this.getWidth(), this.getHeight());
    
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
        streamAxisMap.put(streamUnitName, axis);
      }
      else {
        double axisMax = axis.getMaximum();
        double axisMin = axis.getMinimum();
        axis.setMaximum((axisMax > streamMax) ? axisMax : streamMax);
        axis.setMinimum((axisMin < streamMin) ? axisMin : streamMin);
      }
    }
    
    //Assign colors.
    //IF there are more than 1 unit axis, streams will be colored according to the axes.
    if (streamAxisMap.size() > 1 || streams.size() == 1) {
      //generate enough random color.
      List<String> colors = RandomColorGenerator.generateRandomColorInHex(streamAxisMap.size());
      //color the axis.
      int index = 0;
      for (TelemetryStreamYAxis axis : streamAxisMap.values()) {
        axis.setColor(colors.get(index++));
      }
      //color the streams.
      for (SelectableTelemetryStream stream : streams) {
        stream.setColor(streamAxisMap.get(stream.getUnitName()).getColor());
      }
    }
    else {
      //if there is only one axis, but have multiple stream names, streams will be colored
      //according to the stream name.
      for (TelemetryStreamYAxis axis : streamAxisMap.values()) {
        axis.setColor("000000");
      }
      Map<String, String> streamColorMap = new HashMap<String, String>();
      for (SelectableTelemetryStream stream : streams) {
        streamColorMap.put(stream.getStreamName(), "");
      }
      if (streamColorMap.size() > 1) {
        List<String> colors = RandomColorGenerator.generateRandomColorInHex(streamColorMap.size());
        int index = 0;
        for (Entry<String, String> streamName : streamColorMap.entrySet()) {
          streamName.setValue(colors.get(index++));
        }
        for (SelectableTelemetryStream stream : streams) {
          stream.setColor(streamColorMap.get(stream.getStreamName()));
        }
      }
      else {
        //if there is only one stream name as well. All stream will be colored separately.
        List<String> colors = RandomColorGenerator.generateRandomColorInHex(streams.size());
        int index = 0;
        for (SelectableTelemetryStream stream : streams) {
          stream.setColor(colors.get(index++));
        }
      }
    }
    
    
    //add the y axis.
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

    //add the streams to the chart.
    for (SelectableTelemetryStream stream : streams) {
      TelemetryStreamYAxis axis = streamAxisMap.get(stream.getUnitName());
      this.addStreamToChart(stream, axis.getMinimum(), axis.getMaximum(), googleChart);
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
      googleChart.addColor(stream.getStreamColor());
      //add line style;
      googleChart.addLineStyle(stream.getThickness(), 
          stream.getLineLength(), stream.getBlankLength());
      /*googleChart.addAxisRange(axisType, range, randomColor);*/
      //add markers
      /*stream.setMarker(GoogleChart.getRandomMarker());*/
      googleChart.getChartMarker().add(stream.getMarker());
      googleChart.getMarkerColors().add(stream.getMarkerColor());
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
  public List<String> getDateList() {
    if (this.selectedProjects.isEmpty()) {
      return new ArrayList<String>();
    }
    List<SelectableTelemetryStream> streamList = this.getTelemetryStream(this.selectedProjects
        .get(0));
    if (streamList.isEmpty()) {
      return new ArrayList<String>();
    }
    return getDateList(streamList.get(0).getTelemetryStream().getTelemetryPoint());
  }

  /**
   * @return the selectedProjects
   */
  public List<Project> getSelectedProjects() {
    return selectedProjects;
  }

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
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }
  
  /**
   * Check if the size of the chart is within range.
   * @return true if multiple of width and height is not bigger than MAX_SIZE.
   */
  public boolean isSizeWithinRange() {
    return this.width <= GoogleChart.MAX_SIZE / this.height;
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

  /**
   * @param width the width to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(int height) {
    this.height = height;
  }
}
