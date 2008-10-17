package org.hackystat.projectbrowser.page.dailyprojectdata.devtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.devtime.jaxb.DevTimeDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for DevTime DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the members in the Project along with how
 * much DevTime they worked on the associated project for the day.
 * @author Philip Johnson
 *
 */
public class DevTimeDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the build data, organized by Project.*/
  private Map<Project, DevTimeData> devTimeDataMap = new HashMap<Project, DevTimeData>();
  
  /**
   * The default DevTimeDataModel, which contains no build information.
   */
  public DevTimeDataModel() {
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
      logger.fine("Getting DevTime DPD for project: " + project.getName());
      try {
        DevTimeDailyProjectData devTimeDpd = dpdClient.getDevTime(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()));
        logger.fine("Finished getting DevTime DPD for project: " + project.getName());
        DevTimeData data = new DevTimeData(project, devTimeDpd.getMemberData());
        this.devTimeDataMap.put(project, data);
      }

      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception getting DevTime DPD for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.devTimeDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.devTimeDataMap.isEmpty();
  }
  
  /**
   * Return the DevTimeData instance associated with the specified project.
   * Creates and returns a new DevTimeData instance if one is not yet present.
   * @param project The project. 
   * @return The DevTimeData instance for this project.  
   */
  public DevTimeData getDevTimeData(Project project) {
    if (!devTimeDataMap.containsKey(project)) {
      devTimeDataMap.put(project, new DevTimeData(project));
    }
    return devTimeDataMap.get(project);
  }
  
  /**
   * Returns the list of DevTimeData instances, needed for markup.
   * @return The list of DevTimeData instances. 
   */
  public List<DevTimeData> getDevTimeDataList() {
    return new ArrayList<DevTimeData>(this.devTimeDataMap.values());
  }
  
}
