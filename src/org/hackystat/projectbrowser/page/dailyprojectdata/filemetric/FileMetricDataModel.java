package org.hackystat.projectbrowser.page.dailyprojectdata.filemetric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.filemetric.jaxb.FileMetricDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for FileMetric DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the file types in the Project along with how
 * much LOC they had for the day.
 * @author Philip Johnson
 *
 */
public class FileMetricDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the build data, organized by Project.*/
  private Map<Project, FileMetricData> fileMetricDataMap = new HashMap<Project, FileMetricData>();
  
  /**
   * The default FileMetricDataModel, which contains no build information.
   */
  public FileMetricDataModel() {
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
      logger.fine("Getting FileMetric DPD for project: " + project.getName());
      try {
        FileMetricDailyProjectData fileMetricDpd = dpdClient.getFileMetric(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), "TotalLines");
        logger.fine("Finished getting FileMetric DPD for project: " + project.getName());
        FileMetricData data = new FileMetricData(project, fileMetricDpd.getFileData());
        this.fileMetricDataMap.put(project, data);
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception getting FileMetric DPD for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.fileMetricDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.fileMetricDataMap.isEmpty();
  }
  
  /**
   * Return the FileMetricData instance associated with the specified project.
   * Creates and returns a new FileMetricData instance if one is not yet present.
   * @param project The project. 
   * @return The FileMetricData instance for this project.  
   */
  public FileMetricData getFileMetricData(Project project) {
    if (!fileMetricDataMap.containsKey(project)) {
      fileMetricDataMap.put(project, new FileMetricData(project));
    }
    return fileMetricDataMap.get(project);
  }
  
  /**
   * Returns the list of FileMetricData instances, needed for markup.
   * @return The list of FileMetricData instances. 
   */
  public List<FileMetricData> getFileMetricDataList() {
    return new ArrayList<FileMetricData>(this.fileMetricDataMap.values());
  }
  
}
