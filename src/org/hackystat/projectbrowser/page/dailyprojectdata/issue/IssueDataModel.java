package org.hackystat.projectbrowser.page.dailyprojectdata.issue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.issue.jaxb.IssueDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Issue DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the members in the Project along with 
 * the issue information for the given day.
 * @author Philip Johnson
 *
 */
public class IssueDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the commit data, organized by Project.*/
  private Map<Project, IssueDpdData> issueDataMap = new HashMap<Project, IssueDpdData>();
  
  private final Set<String> openIssueStatus = new TreeSet<String>();
  private final Set<String> closedIssueStatus = new TreeSet<String>();

  private List<String> openIssueStatusValue = 
    Arrays.asList(new String[]{"New", "Accepted", "Started"});
  /**
   * The default CommitDataModel, which contains no commit information.
   */
  public IssueDataModel() {
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
      logger.fine("Getting Issue DPD for project: " + project.getName());
      try {
        IssueDailyProjectData issueDpd = dpdClient.getIssue(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()));
        logger.fine("Finished getting Issue DPD for project: " + project.getName());
        IssueDpdData data = new IssueDpdData(project, issueDpd);
        this.issueDataMap.put(project, data);
        for (String status : data.getIssueStatusCount().keySet()) {
          if (openIssueStatusValue.contains(status)) {
            openIssueStatus.add(status);
          }
          else {
            closedIssueStatus.add(status);
          }
        }
      }

      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception getting Issue DPD for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.issueDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.issueDataMap.isEmpty();
  }
  
  /**
   * Return the IssueData instance associated with the specified project.
   * Creates and returns a new IssueData instance if one is not yet present.
   * @param project The project. 
   * @return The IssueData instance for this project.  
   */
  public IssueDpdData getIssueData(Project project) {
    if (!issueDataMap.containsKey(project)) {
      issueDataMap.put(project, new IssueDpdData(project));
    }
    return issueDataMap.get(project);
  }
  
  /**
   * Returns the list of IssueData instances, needed for markup.
   * @return The list of IssueData instances. 
   */
  public List<IssueDpdData> getIssueDataList() {
    return new ArrayList<IssueDpdData>(this.issueDataMap.values());
  }

  /**
   * @return the openIssueStatus
   */
  public Set<String> getOpenIssueStatus() {
    return openIssueStatus;
  }

  /**
   * @return the closedIssueStatus
   */
  public Set<String> getClosedIssueStatus() {
    return closedIssueStatus;
  }
  
}
