package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.loadingprocesspanel.Processable;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.PortfolioDefinitions;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure.DefaultValues;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.sensorbase.resource.projects.ProjectUtils;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.analyzer.configuration.ExtensionFileFilter;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartData;
import org.hackystat.utilities.logger.HackystatLogger;
import org.hackystat.utilities.tstamp.Tstamp;
import org.hackystat.utilities.uricache.UriCache;
import org.xml.sax.SAXException;

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

  /** host of the telemetry host. */
  private String telemetryHost;
  /** email of the user. */
  private String email;
  /** password of the user. */
  private String password;
  /** The telemetry session. */
  private TelemetrySession telemetrySession;
  
  /** the granularity this data model focus. */
  private String granularity = "Week";
  /** If current day, week or month will be included in portfolio. */
  //private boolean includeCurrentWeek = true;
  /** The start date this user has selected. */
  private long startDate = 0;
  /** The end date this user has selected. */
  private long endDate = 0;
  
  /** 
   * the time phrase this data model focus. 
   * In scale of telemetryGranularity, from current to the past. 
   * */
  //private int timePhrase = 5;
  
  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  /** The charts in this model. */
  private Map<Project, List<MiniBarChart>> measuresCharts = 
        new HashMap<Project, List<MiniBarChart>>();
  /** The thresholds. */
  private final List<MeasureConfiguration> measures = new ArrayList<MeasureConfiguration>();
  /** Alias for measure. Maps names from definition to names for display. */
  private final Map<String, String> measureAlias = new HashMap<String, String>();
  /** The portfolio measure configuration loaded from xml file. */
  private static final PortfolioDefinitions portfolioDefinitions = loadPortfolioDefinitions();

  /** The configuration saving capacity. */
  private static Long capacity = 1000L;
  /** The max life of the saved configuration. */
  private static Double maxLife = 300.0;
  /** The user's email. */
  private String userEmail = ProjectBrowserSession.get().getEmail();

  /** The background color for table cells. */
  private String backgroundColor = "000000";
  /** The font color for table cells. */
  private String fontColor = "ffffff";
  /** The font color for N/A. */
  private String naColor = "888888";
  /** The color for good state. */
  private String goodColor = "00ff00";
  /** The color for soso state. */
  private String sosoColor = "ffff00";
  /** The color for bad state. */
  private String badColor = "ff0000";
  
  /**
   * Constructor that initialize the measures.
   * @param telemetryHost the telemetry host
   * @param email the user's email
   * @param password the user's passowrd
   */
  public ProjectPortfolioDataModel(String telemetryHost, String email, String password) {
    telemetrySession = ProjectBrowserSession.get().getTelemetrySession();
    this.telemetryHost = telemetryHost;
    this.email = email;
    this.password = password;
    this.initializeMeasures();
    this.loadUserConfiguration();
    //List<ParameterDefinition> paramDefList = telemetrySession.getParameterList(measure.getName());
  }

  /**
   * @param startDate the start date.
   * @param endDate the end date.
   * @param selectedProjects the selected projects.
   * @param granularity the granularity this data model focus.
   */
  public void setModel(long startDate, long endDate, List<Project> selectedProjects, 
      String granularity) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.granularity = granularity;
    this.selectedProjects = selectedProjects;
    for (MeasureConfiguration measure : getEnabledMeasures()) {
      checkMeasureParameters(measure);
    }
  }
  
  /**
   * Initialize the measure configurations
   */
  private void initializeMeasures() {
    //Load default measures
    measures.clear();
    measures.add(new MeasureConfiguration("Coverage", true, 40, 90, true, this));
    measures.add(new MeasureConfiguration("CyclomaticComplexity", true, 10, 20, false, this));
    measures.add(new MeasureConfiguration("Coupling", true, 10, 20, false, this));
    measures.add(new MeasureConfiguration("Churn", true, 400, 900, false, this));
    measures.add(new MeasureConfiguration("CodeIssue", true, 10, 30, false, this));
    //measures.add(new MeasureConfiguration("Commit", false, 0, 0, true, this));
    //measures.add(new MeasureConfiguration("Build", false, 0, 0, true, this));
    //measures.add(new MeasureConfiguration("UnitTest", false, 0, 0, true, this));
    measures.add(new MeasureConfiguration("FileMetric", false, 0, 0, true, this));
    measures.add(new MeasureConfiguration("DevTime", false, 0, 0, true, this));
    
    measureAlias.put("CyclomaticComplexity", "Complexity");
    measureAlias.put("FileMetric", "Size(LOC)");
    
    //Load additional user customized measures.
    if (portfolioDefinitions != null) {
      for (Measure measure : portfolioDefinitions.getMeasures().getMeasure()) {
        DefaultValues defaultValues = measure.getDefaultValues();
        measures.add(new MeasureConfiguration(measure.getName(), defaultValues.isColorable(), 
            defaultValues.getDefaultLowerThresold(), defaultValues.getDefaultHigherThresold(), 
            defaultValues.isHigherBetter(), this));
        if (measure.getAlias() != null && measure.getAlias().length() > 0) {
          measureAlias.put(measure.getName(), measure.getAlias());
        }
      }
    }

    for (MeasureConfiguration measure : getEnabledMeasures()) {
      checkMeasureParameters(measure);
    }
  }

  /**
   * Print the portfolio measure for logging purpose.
   * @param measure the PortfolioMeasure to log.
   * @return the string
   */
  private String printPortfolioMeasure(PortfolioMeasure measure) {
    String s = "/";
    return "<" + measure.getMeasureName() + ": " + 
                s + measure.isEnabled() + 
                s + measure.isColorable() + 
                s + measure.isHigherBetter() + 
                s + measure.getLowerThreshold() + 
                s + measure.getHigherThreshold() + 
                s + measure.getParameters() + "> ";
  }
  
  /**
   * Save user's configuration to system's cache.
   * @return change maked in this saving.
   */
  public String saveUserConfiguration() {
    StringBuffer log = new StringBuffer();
    UriCache userCache = this.getUserConfiguartionCache();
    for (MeasureConfiguration measure : this.measures) {
      String uri = userEmail + "/" + measure.getName();
      PortfolioMeasure oldMeasure = (PortfolioMeasure)userCache.get(uri);
      PortfolioMeasure newMeasure = new PortfolioMeasure(measure);
      if (oldMeasure == null || !oldMeasure.equals(newMeasure)) {
        userCache.put(uri, newMeasure);
        log.append(printPortfolioMeasure(newMeasure));
      }
    }

    if (log.length() > 0) {
      ProjectBrowserSession.get().logUsage("CONFIGURATION: {changed} " + log.toString());
    }
    return log.toString();
  }
  
  /**
   * Load user's configuration from system's cache.
   */
  private void loadUserConfiguration() {
    UriCache userCache = this.getUserConfiguartionCache();
    for (MeasureConfiguration measure : this.measures) {
      String uri = userEmail + "/" + measure.getName();
      PortfolioMeasure saved = (PortfolioMeasure)userCache.get(uri);
      if (saved != null) {
        measure.loadFrom(saved);
      }
    }
  }
  
  /**
   * Reset user's configuration cache.
   */
  public void resetUserConfiguration() {
    //UriCache userCache = this.getUserConfiguartionCache();
    //userCache.clearAll();
    this.initializeMeasures();
    String msg = this.saveUserConfiguration();
    if (msg.length() > 0) {
      ProjectBrowserSession.get().logUsage("CONFIGURATION: {reset to default} ");
    }
  }
  
  /**
   * Load the portfolio definitions from the xml file.
   * @return a PortfolioDefinitions instance. null if fail to load the file.
   */
  private static PortfolioDefinitions loadPortfolioDefinitions() {
    PortfolioDefinitions portfolioDefinitions = new PortfolioDefinitions(); 
    portfolioDefinitions.setMeasures(new Measures());
    //load the basic default definitions.
    String filePath = 
      System.getProperty("user.dir") + "/xml/definitions/basic.portfolio.definition.xml";
    File defFile = new File(filePath);
    portfolioDefinitions.getMeasures().getMeasure().addAll(
        getDefinitions(defFile).getMeasures().getMeasure());
    
    String defDirString = 
      ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getPortfolioDefinitionDir();
    if (defDirString != null && defDirString.length() > 0) {
      File defDir = new File(defDirString);
      File[] xmlFiles = defDir.listFiles(new ExtensionFileFilter(".xml"));
      for (File xmlFile : xmlFiles) {
        portfolioDefinitions.getMeasures().getMeasure().addAll(
            getDefinitions(xmlFile).getMeasures().getMeasure());
      }
    }
    return portfolioDefinitions;
  }
  
  /**
   * Returns a TelemetryDefinitions object constructed from defStream, or null if unsuccessful.
   * @param defFile The input stream containing a TelemetryDefinitions object in XML format.
   * @return The TelemetryDefinitions object.
   */
  private static PortfolioDefinitions getDefinitions (File defFile) {
    // Read in the definitions file.
    try {
      JAXBContext jaxbContext = JAXBContext
      .newInstance(org.hackystat.projectbrowser.page.projectportfolio.jaxb.ObjectFactory.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      //add schema checking
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      String schemaPath = 
        System.getProperty("user.dir") + "/xml/schema/portfolioDefinitions.xsd";
      File schemaFile = new File(schemaPath.replace("/", System.getProperty("file.separator")));
      Schema schema = schemaFactory.newSchema(schemaFile);
      unmarshaller.setSchema(schema);
      return (PortfolioDefinitions) unmarshaller.unmarshal(defFile);
    } 
    catch (JAXBException e1) {
      Logger  logger = getLogger();
      logger.severe("Error occurs when loading " + defFile.getName() + 
          " > Can not parse with jaxb > " + e1.getMessage());
    }
    catch (SAXException e) {
      Logger  logger = getLogger();
      logger.warning("Error occurs when loading schema file. > " + e.getMessage());
    }
    return null;
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
      //prepare start and end time.
      XMLGregorianCalendar startTime = getStartTimestamp();
      XMLGregorianCalendar endTime = getEndTimestamp();
      for (int i = 0; i < this.selectedProjects.size() && inProcess; i++) {
        //prepare project's information
        Project project = this.selectedProjects.get(i);
        String owner = project.getOwner();
        String projectName = project.getName();

        this.processingMessage += "Retrieve data for project " + projectName + 
        " (" + (i + 1) + " of " + this.selectedProjects.size() + ").\n";
        
        //get charts of this project
        List<MiniBarChart> charts = new ArrayList<MiniBarChart>();
        
        List<MeasureConfiguration> enableMeasures = getEnabledMeasures();
        
        for (int j = 0; j < enableMeasures.size(); ++j) {
          MeasureConfiguration measure = enableMeasures.get(j);
          
          this.processingMessage += "---> Retrieve " + measure.getName() + "<" + 
          measure.getParamtersString() + ">" + " (" + (i + 1) + " .. " + (j + 1) + 
          " of " + enableMeasures.size() + ").\n";
          //get data from hackystat
          if (!ProjectUtils.isValidStartTime(project, startTime)) {
            startTime = project.getStartTime();
          }
          String s = "/";
          getLogger().finest("retriving telemetry: " + measure.getName() + s + owner + s + 
              projectName + s + granularity + s + startTime + s + endTime + s + 
              measure.getParamtersString());
          TelemetryChartData chartData = telemetryClient.getChart(measure.getName(), 
              owner, projectName, granularity, startTime, endTime, 
              measure.getParamtersString());
          //Log warning when portfolio definition refers to multi-stream telemetry chart.
          if (chartData.getTelemetryStream().size() > 1 && i == 0) {
            Logger logger = getLogger();
            logger.warning("Telemetry chart:" + measure.getName() + " has more than 1 stream. " +
            		"Should use chart than contain only one stream. " +
            		"Please check your portfolio and telemetry definitions");
          }
          MiniBarChart chart = new MiniBarChart(chartData.getTelemetryStream().get(0), measure);
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
   * Check the parameter in the given measure configuration.
   * Set the parameter to default if it is incorrect.
   * @param measure the given MeasureConfiguration.
   */
  private void checkMeasureParameters(MeasureConfiguration measure) {
    List<ParameterDefinition> paramDefList = telemetrySession.getParameterList(measure.getName());
    if (measure.getParameters().size() != paramDefList.size()) {
      measure.getParameters().clear();
      for (ParameterDefinition paramDef : paramDefList) {
        measure.getParameters().add(new Model(paramDef.getType().getDefault()));
      }
    }
  }
  
  /**
   * Cancel the data loading process.
   */
  public void cancelDataUpdate() {
    this.processingMessage += "Process Cancelled.\n";
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
    parameters.put(
        TelemetrySession.START_DATE_KEY, getStartTimestamp().toString());
    parameters.put(TelemetrySession.END_DATE_KEY, getEndTimestamp().toString());
    parameters.put(TelemetrySession.SELECTED_PROJECTS_KEY, 
        project.getName() + TelemetrySession.PROJECT_NAME_OWNER_SEPARATR + project.getOwner());
    parameters.put(TelemetrySession.GRANULARITY_KEY, this.granularity);
    parameters.put(TelemetrySession.TELEMETRY_PARAMERTERS_KEY, measure.getParamtersString());
    
    return parameters;
  }

  /**
   * Return the end time stamp for analysis.
   * If includeCurrentWeek, it will return the time stamp of yesterday.
   * If !includeCurrentWeek, it will return the time stamp of last Saturday.
   * @return the XMLGregorianCalendar instance.
   */
  private XMLGregorianCalendar getEndTimestamp() {
	/*
    if (!this.includeCurrentWeek) {
      GregorianCalendar date = new GregorianCalendar();
      date.setTime(ProjectBrowserBasePage.getDateBefore(1));
      date.setFirstDayOfWeek(Calendar.MONDAY);
      int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
      XMLGregorianCalendar endTime = Tstamp.makeTimestamp(date.getTimeInMillis());
      endTime = Tstamp.incrementDays(endTime, - dayOfWeek);
      return endTime;
    }
    return Tstamp.makeTimestamp(ProjectBrowserBasePage.getDateBefore(1).getTime());
    */
	return Tstamp.makeTimestamp(this.endDate);
  }

  /**
   * Return the start time stamp for analysis.
   * @return the XMLGregorianCalendar instance.
   */
  private XMLGregorianCalendar getStartTimestamp() {
	  /*
    int days;
    if ("Month".equals(this.telemetryGranularity)) {
      days = this.timePhrase * 30;
    }
    else if ("Week".equals(this.telemetryGranularity)) {
      days = this.timePhrase * 7;
    }
    else {
      days = this.timePhrase;
    }
    return Tstamp.makeTimestamp(ProjectBrowserBasePage.getDateBefore(days).getTime());
    */
	return Tstamp.makeTimestamp(this.startDate);
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
  /*
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
  */

  /**
   * @return the measures
   */
  public List<MeasureConfiguration> getMeasures() {
    return measures;
  }
  
  /**
   * @return the enabled measures
   */
  public final List<MeasureConfiguration> getEnabledMeasures() {
    List<MeasureConfiguration> enableMeasures = new ArrayList<MeasureConfiguration>();
    for (MeasureConfiguration measure : measures) {
      if (measure.isEnabled()) {
        enableMeasures.add(measure);
      }
    }
    return enableMeasures;
  }
  
  /**
   * Return the names of enabled measures. 
   * If alias available for the measure, alias will be returned.
   * Otherwise, name in measure's definition will be returned.
   * @return the names of the enabled measures.
   */
  public List<String> getEnabledMeasuresName() {
    List<String> names = new ArrayList<String>();
    for (MeasureConfiguration measure : measures) {
      if (measure.isEnabled()) {
        //Convert to alias if available.
        if (this.measureAlias.containsKey(measure.getName())) {
          names.add(this.measureAlias.get(measure.getName()));
        }
        else {
          names.add(measure.getName());
        }
      }
    }
    return names;
  }

  /**
   * @param backgroundColor the backgroundColor to set
   */
  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
   * @return the backgroundColor
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * @param goodColor the goodColor to set
   */
  public void setGoodColor(String goodColor) {
    this.goodColor = goodColor;
  }

  /**
   * @return the goodColor
   */
  public String getGoodColor() {
    return goodColor;
  }

  /**
   * @param sosoColor the sosoColor to set
   */
  public void setSosoColor(String sosoColor) {
    this.sosoColor = sosoColor;
  }

  /**
   * @return the sosoColor
   */
  public String getSosoColor() {
    return sosoColor;
  }

  /**
   * @param badColor the badColor to set
   */
  public void setBadColor(String badColor) {
    this.badColor = badColor;
  }

  /**
   * @return the badColor
   */
  public String getBadColor() {
    return badColor;
  }

  /**
   * @param fontColor the fontColor to set
   */
  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  /**
   * @return the fontColor
   */
  public String getFontColor() {
    return fontColor;
  }

  /**
   * @return the naColor
   */
  public String getNAColor() {
    return naColor;
  }

  /**
   * @return the measureAlias
   */
  public Map<String, String> getMeasureAlias() {
    return measureAlias;
  }

  /**
   * @return the userConfiguartionCache
   */
  private UriCache getUserConfiguartionCache() {
    return new UriCache(userEmail, "portfolio", maxLife, capacity);
  }

  /**
   * @return the logger that associated to this web application.
   */
  private static Logger getLogger() {
    return HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
  }
}
