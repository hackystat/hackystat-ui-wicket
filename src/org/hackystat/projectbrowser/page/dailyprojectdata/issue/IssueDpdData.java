package org.hackystat.projectbrowser.page.dailyprojectdata.issue;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.hackystat.dailyprojectdata.resource.issue.jaxb.IssueDailyProjectData;
import org.hackystat.dailyprojectdata.resource.issue.jaxb.IssueData;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Represents commit and churn data for a given project and day.
 * @author Philip Johnson
 *
 */
public class IssueDpdData implements Serializable {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  private final int openIssues;
  private final int closedIssues;
  
  private final Map<String, Integer> issueStatusCount = new TreeMap<String, Integer>();

  /**
   * Constructs a CommitData instance with member data. 
   * @param project The project associated with this Commit data. 
   * @param issueDpd The individual member data. 
   */
  public IssueDpdData(Project project, IssueDailyProjectData issueDpd) {
    this.project = project;
    openIssues = issueDpd.getOpenIssues();
    closedIssues = issueDpd.getIssueData().size() - openIssues;
    for (IssueData issueData : issueDpd.getIssueData()) {
      String status = issueData.getStatus();
      if (status != null && status.length() > 0) {
        if (issueStatusCount.containsKey(status)) {
          issueStatusCount.put(status, issueStatusCount.get(status) + 1);
        }
        else {
          issueStatusCount.put(status, 1);
        }
      }
    }
  }
  
  /**
   * Constructs an empty CommitData instance.
   * Used to initialize the session state.  
   * @param project The project.
   */
  public IssueDpdData(Project project) {
    this.project = project;
    openIssues = 0;
    closedIssues = 0;
  }
  
  /**
   * Returns the project associated with this instance. 
   * @return The project. 
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * @return the openIssues
   */
  public int getOpenIssues() {
    return openIssues;
  }

  /**
   * @return the closedIssues
   */
  public int getClosedIssues() {
    return closedIssues;
  }

  /**
   * @return the issueStatusCount
   */
  public Map<String, Integer> getIssueStatusCount() {
    return issueStatusCount;
  }
  
}
