package org.hackystat.projectbrowser.page.dailyprojectdata.devtime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hackystat.dailyprojectdata.resource.devtime.jaxb.MemberData;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Provides data about DevTime for a single project and day. 
 * @author Philip Johnson
 */
public class DevTimeData implements Serializable {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** Holds the total DevTime in minutes. */
  private int totalDevTime = 0;
  
  /** Maintains the (user, devtime) data. DevTime is in minutes. */
  private Map<String, Integer> user2devTime = new TreeMap<String, Integer>(); 
  
  /**
   * Constructs a DevTimeData instance with member data. 
   * @param project The project associated with this DevTime data. 
   * @param memberData The individual member data. 
   */
  public DevTimeData(Project project, List<MemberData> memberData) {
    this.project = project;
    for (MemberData data : memberData) {
      String member = convertUriToEmail(data.getMemberUri());
      int devTime = data.getDevTime().intValue();
      this.totalDevTime += devTime;
      user2devTime.put(member, devTime);
    }
  }
  
  /**
   * Constructs an empty DevTimeData instance.
   * Used to initialize the session state.  
   * @param project The project.
   */
  public DevTimeData(Project project) {
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
   * Returns the total aggregate DevTime for this project and day. 
   * @return The total DevTime. 
   */
  public int getTotalDevTime () {
    return this.totalDevTime;
  }
  
  /**
   * Returns a String listing each project member with DevTime and their associated
   * DevTime in minutes. 
   * @return A String of DevTime info for this project and day. 
   */
  public String getDevTimeData() {
    StringBuffer buff = new StringBuffer();
    for (Map.Entry<String, Integer> entry : user2devTime.entrySet()) {
      buff.append(String.format("%s(%d) ", entry.getKey(), entry.getValue()));
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
