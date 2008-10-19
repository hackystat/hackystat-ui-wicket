package org.hackystat.projectbrowser.page.dailyprojectdata.commit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hackystat.dailyprojectdata.resource.commit.jaxb.MemberData;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Represents commit and churn data for a given project and day.
 * @author Philip Johnson
 *
 */
public class CommitData implements Serializable {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** Holds the total Commits. */
  private int totalCommit = 0;
  
  /** Holds the total Churn. */
  private int totalChurn = 0;
  
  /** Maintains the (user, commit/churn) data. */
  private Map<String, String> user2commit = new TreeMap<String, String>(); 

  /**
   * Constructs a CommitData instance with member data. 
   * @param project The project associated with this Commit data. 
   * @param memberData The individual member data. 
   */
  public CommitData(Project project, List<MemberData> memberData) {
    this.project = project;
    for (MemberData data : memberData) {
      String member = convertUriToEmail(data.getMemberUri());
      int commits = data.getCommits();
      int churn = 0;
      this.totalCommit += commits;
      try {
        churn += data.getLinesAdded();
        churn += data.getLinesAdded();
        churn += data.getLinesModified();
      }
      catch (Exception e) { //NOPMD
        // do nothing, it's because lines modified was null.
      }
      this.totalChurn += churn;
      user2commit.put(member, String.format("%d/%d", commits, churn));
    }
  }
  
  /**
   * Constructs an empty CommitData instance.
   * Used to initialize the session state.  
   * @param project The project.
   */
  public CommitData(Project project) {
    this.project = project;
  }
  
  /**
   * Returns the project associated with this instance. 
   * @return The project. 
   */
  public Project getProject() {
    return this.project;
  }
  
  /**
   * Returns the total aggregate Commit for this project and day. 
   * @return The total Commit. 
   */
  public int getTotalCommit () {
    return this.totalCommit;
  }
  
  /**
   * Returns the total aggregate Churn for this project and day. 
   * @return The total Churn. 
   */
  public int getTotalChurn () {
    return this.totalChurn;
  }
  
  /**
   * Returns a String listing each project member with their commit and churn totals.
   * @return A String of Commit info for this project and day. 
   */
  public String getCommitData() {
    StringBuffer buff = new StringBuffer();
    for (Map.Entry<String, String> entry : user2commit.entrySet()) {
      buff.append(String.format("%s(%s) ", entry.getKey(), entry.getValue()));
    }
    return buff.toString();
  }
  
  /**
   * Converts a project member string to an email address.
   * The member string might be a URI (starting with http) or the desired email address. 
   * @param member The member string. 
   * @return The email address corresponding to the member string. 
   */
  private String convertUriToEmail(String member) {
    if (member.startsWith("http:")) {
      int lastSlash = member.lastIndexOf('/');
      if (lastSlash < 0) {
        throw new IllegalArgumentException("Could not convert owner to URI");
      }
      return member.substring(lastSlash + 1); 
    }
    // Otherwise owner is already the email. 
    return member;
  }
}
