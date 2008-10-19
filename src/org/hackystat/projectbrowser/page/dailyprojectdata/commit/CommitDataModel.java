package org.hackystat.projectbrowser.page.dailyprojectdata.commit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.commit.jaxb.CommitDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Commit DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the members in the Project along with 
 * the commit and churn information for the given day.
 * @author Philip Johnson
 *
 */
public class CommitDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the commit data, organized by Project.*/
  private Map<Project, CommitData> commitDataMap = new HashMap<Project, CommitData>();
  
  /**
   * The default CommitDataModel, which contains no commit information.
   */
  public CommitDataModel() {
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
      logger.fine("Getting Commit DPD for project: " + project.getName());
      try {
        CommitDailyProjectData commitDpd = dpdClient.getCommit(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()));
        logger.fine("Finished getting Commit DPD for project: " + project.getName());
        CommitData data = new CommitData(project, commitDpd.getMemberData());
        this.commitDataMap.put(project, data);
      }

      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception getting Commit DPD for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.commitDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.commitDataMap.isEmpty();
  }
  
  /**
   * Return the CommitData instance associated with the specified project.
   * Creates and returns a new CommitData instance if one is not yet present.
   * @param project The project. 
   * @return The CommitData instance for this project.  
   */
  public CommitData getCommitData(Project project) {
    if (!commitDataMap.containsKey(project)) {
      commitDataMap.put(project, new CommitData(project));
    }
    return commitDataMap.get(project);
  }
  
  /**
   * Returns the list of CommitData instances, needed for markup.
   * @return The list of CommitData instances. 
   */
  public List<CommitData> getCommitDataList() {
    return new ArrayList<CommitData>(this.commitDataMap.values());
  }
  
}
