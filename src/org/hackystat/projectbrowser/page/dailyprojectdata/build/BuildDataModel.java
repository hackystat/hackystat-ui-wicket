package org.hackystat.projectbrowser.page.dailyprojectdata.build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.build.jaxb.MemberData;
import org.hackystat.dailyprojectdata.resource.build.jaxb.BuildDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Build DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of classes whose method-level percentage
 * falls into each of five buckets, from 0-20% to 80-100%.
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 *
 */
public class BuildDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the build data, organized by Project.*/
  private Map<Project, BuildData> buildDataMap = new HashMap<Project, BuildData>();
  
  /**
   * The default BuildDataModel, which contains no build information.
   */
  public BuildDataModel() {
    // Do nothing
  }
  
  /**
   * Updates this data model to reflect the build information associated with the selected 
   * projects.
   */
  public void update() {
    this.clear();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    List<Project> projects = session.getSelectedProjects();
    
    for (Project project : projects) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Getting DPD for project: " + project.getName());
      try {
        BuildDailyProjectData classData = dpdClient.getBuild(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()));
        logger.info("Finished getting DPD for project: " + project.getName());
        // Create a BuildData instance for this project.
        BuildData buildData = this.getBuildData(project);
        for (MemberData data : classData.getMemberData()) {
          buildData.addEntry(data);
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting build data for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.buildDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.buildDataMap.isEmpty();
  }
  
  /**
   * Return the BuildData instance associated with the specified project.
   * Creates and returns a new BuildData instance if one is not yet present.
   * @param project The project. 
   * @return The BuildData instance for this project.  
   */
  public BuildData getBuildData(Project project) {
    if (!buildDataMap.containsKey(project)) {
      buildDataMap.put(project, new BuildData(project));
    }
    return buildDataMap.get(project);
  }
  
  /**
   * Returns the list of BuildData instances, needed for markup.
   * @return The list of BuildData instances. 
   */
  public List<BuildData> getBuildDataList() {
    return new ArrayList<BuildData>(this.buildDataMap.values());
  }
  
}
