package org.hackystat.projectbrowser.page.dailyprojectdata.complexity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.resource.complexity.jaxb.ComplexityDailyProjectData;
import org.hackystat.dailyprojectdata.resource.complexity.jaxb.FileData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Complexity DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of methods whose level of complexity
 * falls into each of five buckets, from 0-10, 11-20, 21-30, 31-40, 41+.
 * At present, we only get cyclomatic complexity from the JavaNCSS tool. This will change
 * after we update the DPD client to not require the tool argument.
 * @author Philip Johnson
 *
 */
public class ComplexityDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the complexity data, organized by Project.*/
  private Map<Project, ComplexityData> complexityDataMap = new HashMap<Project, ComplexityData>();
  
  /**
   * The default ComplexityDataModel, which contains no complexity information.
   */
  public ComplexityDataModel() {
    // Do nothing.
  }
  
  /**
   * Updates this data model to reflect the complexity information associated with the selected 
   * projects and granularity.
   */
  public void update() {
    this.clear();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    List<Project> projects = session.getSelectedProjects();
    // Placeholders until we set this programmatically.
    String complexityType = "Cyclomatic";
    String complexityTool = "JavaNCSS";
    
    for (Project project : projects) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Getting Complexity DPD for project: " + project.getName());
      try {
        ComplexityDailyProjectData classData = dpdClient.getComplexity(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), complexityType,
        complexityTool);
        logger.info("Finished getting Complexity DPD for project: " + project.getName());
        ComplexityData complexityData = this.getComplexityData(project);
        for (FileData data : classData.getFileData()) {
          String methodData = data.getComplexityValues();
          List<Integer> complexities = getComplexities(methodData);
          for (Integer complexity : complexities) {
            complexityData.addEntry(complexity);
          }
        }
      }
      catch (Exception e) {
        session.setFeedback("Exception when getting complexity data for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  /**
   * Takes the string containing method complexities and returns them as a List of Integers.
   * This really should go into the DPD system.  
   * @param methodData The method data as a string. 
   * @return The method data as a list of integers. 
   */
  private List<Integer> getComplexities(String methodData) {
    List<Integer> methodComplexities = new ArrayList<Integer>();
    try {
      String[] numStringList = methodData.split(",");
      for (String numString : numStringList) {
        methodComplexities.add(Integer.parseInt(numString));
      }
    }
    catch (Exception e) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Failed to parse Complexity method data: " + methodData);
    }
    return methodComplexities;
  }
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.complexityDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.complexityDataMap.isEmpty();
  }
  
  /**
   * Return the ComplexityData instance associated with the specified project.
   * Creates and returns a new ComplexityData instance if one is not yet present.
   * @param project The project. 
   * @return The ComplexityData instance for this project.  
   */
  public ComplexityData getComplexityData(Project project) {
    if (!complexityDataMap.containsKey(project)) {
      complexityDataMap.put(project, new ComplexityData(project));
    }
    return complexityDataMap.get(project);
  }
  
  /**
   * Returns the list of ComplexityData instances, needed for markup.
   * @return The list of ComplexityData instances. 
   */
  public List<ComplexityData> getComplexityDataList() {
    return new ArrayList<ComplexityData>(this.complexityDataMap.values());
  }
  
}