package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.CoverageDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Coverage DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of classes whose method-level percentage
 * falls into each of five buckets, from 0-20% to 80-100%.
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 *
 */
public class CoverageDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the coverage data, organized by Project.*/
  private Map<Project, CoverageData> coverageDataMap = new HashMap<Project, CoverageData>();
  
  /**
   * The default CoverageDataModel, which contains no coverage information.
   */
  public CoverageDataModel() {
    // Do nothing
  }
  
  /**
   * Updates this data model to reflect the coverage information associated with the selected 
   * projects and granularity.
   */
  public void update() {
    this.clear();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    List<Project> projects = session.getSelectedProjects();
    String granularity = session.getContextSensitiveMenu("Coverage Type").getSelectedValue();
    
    for (Project project : projects) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Getting DPD for project: " + project.getName());
      try {
        CoverageDailyProjectData classData = dpdClient.getCoverage(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), granularity);
        logger.info("Finished getting DPD for project: " + project.getName());
        for (ConstructData data : classData.getConstructData()) {
          CoverageData coverageData = this.getCoverageData(project);
          coverageData.addEntry(data.getNumCovered(), data.getNumUncovered());
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting coverage data for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.coverageDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.coverageDataMap.isEmpty();
  }
  
  /**
   * Return the CoverageData instance associated with the specified project.
   * Creates and returns a new CoverageData instance if one is not yet present.
   * @param project The project. 
   * @return The CoverageData instance for this project.  
   */
  public CoverageData getCoverageData(Project project) {
    if (!coverageDataMap.containsKey(project)) {
      coverageDataMap.put(project, new CoverageData(project));
    }
    return coverageDataMap.get(project);
  }
  
  
  /**
   * Adds information about a specific file's coverage to this data model.
   * @param project The project associated with this file. 
   * @param data The file coverage information.
   */
  public void add(Project project, ConstructData data) {
    CoverageData coverageData = this.getCoverageData(project);
    coverageData.addEntry(data.getNumCovered(), data.getNumUncovered());
  }
  
  /**
   * Returns a list of CoverageData instances for display in the table.
   * @return A list of CoverageData instances. 
   */
  public List<CoverageData> getCoverageDataList() {
    List<CoverageData> coverageDataList = new ArrayList<CoverageData>();
    coverageDataList.addAll(coverageDataMap.values());
    return coverageDataList;
  }

}
