package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.contextsensitive.ContextSensitiveMenu;
import org.hackystat.projectbrowser.page.contextsensitive.ContextSensitivePanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.complexity.ComplexityDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coupling.CouplingDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coverage.CoverageDataModel;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputForm;
import org.hackystat.projectbrowser.page.dailyprojectdata.unittest.UnitTestDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Session instance for the daily project data page to hold its state.
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The date this user has selected in the ProjectDate form. */
  private long date = ProjectBrowserBasePage.getDateYesterday().getTime();

  /** The projects this user has selected. */
  private List<Project> selectedProjects = new ArrayList<Project>();
  
  /** The analysis this user has selected. Defaults to Coverage */
  private String analysis = "Coverage";
  
  /** The list of analysis choices. */
  private List<String> analysisList = 
    Arrays.asList("Coverage", "Coupling", "Complexity", "UnitTest");

  /** the feedback string. */
  private String feedback = "";

  /** The context sensitive panel.  We keep a pointer to this in the session for Ajax updating. */
  private ContextSensitivePanel csPanel; 
  
  /** Holds the state of the context-sensitive menus in the context sensitive panel. */
  private Map<String, ContextSensitiveMenu> csMenus = new HashMap<String, ContextSensitiveMenu>();
  
  /** The Coverage data model. */
  private CoverageDataModel coverageDataModel = new CoverageDataModel();
  
  /** The Unit Test analysis data model. */
  private UnitTestDataModel unitTestDataModel = new UnitTestDataModel();
  
  /** The coupling data model. */
  private CouplingDataModel couplingDataModel = new CouplingDataModel();

  /** The complexity data model. */
  private ComplexityDataModel complexityDataModel = new ComplexityDataModel();

  /**
   * Initialize this session, including the list of context-sensitive menus.
   */
  public DailyProjectDataSession() {
    // Initialize the context sensitive menus.  
    // Since the default analysis is Coverage, the Values and Coverage Type menus are visible.
    csMenus.put("Values", new ContextSensitiveMenu("Values", "Count", 
        Arrays.asList("Count", "Percentage"), true));
    csMenus.put("Coverage Type", new ContextSensitiveMenu("Coverage Type", "Method", 
        Arrays.asList("Block", "Class", "Conditional", "Element", "Line", "Method", "Statement"), 
        true));
    csMenus.put("Coupling Type", new ContextSensitiveMenu("Coupling Type", "Afferent+Efferent", 
        Arrays.asList("Afferent", "Efferent", "Afferent+Efferent"), 
        false));
  }

  /**
   * Gets the date associated with this page. 
   * @return The date for this page. 
   */
  public Date getDate() {
    return new Date(this.date);
  }
  
  /**
   * Sets the date associated with this page. 
   * @param date The date for this page. 
   */
  public void setDate(Date date) {
    this.date = date.getTime();
  }

  /**
   * Returns the current date in yyyy-MM-dd format.  
   * @return The date as a simple string. 
   */
  public String getDateString() {
    SimpleDateFormat format = new SimpleDateFormat(DpdInputForm.DATA_FORMAT, Locale.ENGLISH);
    return format.format(new Date(this.date));
  }
  
  
  /**
   * Returns the list of projects selected by the user. 
   * @return The list of projects selected by the user. 
   */
  public List<Project> getSelectedProjects() {
    return this.selectedProjects;
  }
  
  /**
   * Sets the set of selected projects
   * @param projects The projects.
   */
  public void setSelectedProjects(List<Project> projects) {
    this.selectedProjects = projects;
  }

  /**
   * Sets the selected analysis.
   * @param analysis The analysis to set.
   */
  public void setAnalysis(String analysis) {
    this.analysis = analysis;
  }

  /**
   * Gets the selected analysis.
   * @return The analysis.
   */
  public String getAnalysis() {
    return analysis;
  }

  /**
   * Returns the list of possible analyses. 
   * @return The analysisList.
   */
  public List<String> getAnalysisList() {
    return analysisList;
  }

  /**
   * Sets the feedback string. 
   * @param feedback The feedback to set.
   */
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  /**
   * Gets the feedback string, and also clears it.
   * @return The feedback string. 
   */
  public String getFeedback() {
    String returnString = this.feedback;
    this.feedback = "";
    return returnString;
  }

  /**
   * Gets all context sensitive menus.
   * @return The context sensitive menus.
   */
  public List<ContextSensitiveMenu> getContextSensitiveMenus() {
    return new ArrayList<ContextSensitiveMenu>(this.csMenus.values());
  }
  
  /**
   * Gets the context sensitive menu with the passed name, or null if not found.
   * @param name The name of the context sensitive menu.
   * @return The menu instance, or null if not found.
   */
  public ContextSensitiveMenu getContextSensitiveMenu(String name) {
    return this.csMenus.get(name);
  }
  
  /**
   * Get the context sensitive panel holding the context sensitive menus.
   * @return The context sensitive panel.
   */
  public ContextSensitivePanel getContextSensitivePanel() { 
    return this.csPanel;
  }
  
  /**
   * Sets the panel containing the context sensitive menus.
   * @param panel The panel.
   */
  public void setContextSensitivePanel(ContextSensitivePanel panel) {
    this.csPanel = panel;
  }
  
  /**
   * Get the Coverage data model. 
   * @return The Coverage data model associated with this session.
   */
  public CoverageDataModel getCoverageDataModel() {
    return this.coverageDataModel;
  }
  
  /**
   * Gets the Unit test model associated with this session.
   * @return The Unit Test model. 
   */
  public UnitTestDataModel getUnitTestDataModel() {
    return this.unitTestDataModel;
  }
  
  
  /**
   * Gets the Coupling model associated with this session.
   * @return The Coupling model. 
   */
  public CouplingDataModel getCouplingDataModel() {
    return this.couplingDataModel;
  }
  
  /**
   * Gets the Complexity model associated with this session.
   * @return The Complexity model. 
   */
  public ComplexityDataModel getComplexityDataModel() {
    return this.complexityDataModel;
  }
  
  /**
   * Sets all analysis data models to their empty state. 
   */
  public void clearDataModels() {
    this.coverageDataModel.clear();
    this.unitTestDataModel.clear();
    this.couplingDataModel.clear();
    this.complexityDataModel.clear();
  }
}
