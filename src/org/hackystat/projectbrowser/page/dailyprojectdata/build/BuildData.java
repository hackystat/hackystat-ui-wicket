package org.hackystat.projectbrowser.page.dailyprojectdata.build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.dailyprojectdata.resource.build.jaxb.MemberData;
import org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel.DailyProjectDetailsPanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Data structure for representing build information about a single project. 
 * This representation includes the Project, plus Buckets containing 
 * DPD Build MemberData instances for the passing and failing instances. 
 * Bucket 0 contains MemberData instances with passing build info, and 
 * Bucket 1 contains MemberData instances with failing build info.  Note
 * that the same MemberData instance can appear in both buckets. 
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class BuildData implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** The two buckets for this data. */
  private List<MemberData> successes = new ArrayList<MemberData>();
  private List<MemberData> failures = new ArrayList<MemberData>();
  
  /** The total number of builds across all buckets. */
  private int total = 0;
  
  /**
   * Creates a new BuildData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public BuildData(Project name) {
    this.project = name;
  }
  
  /**
   * Updates this BuildData instance with information about the numbers of passes and failures
   * for a given instance in the Project.  
   * @param memberData The data to be added.
   */
  public void addEntry(MemberData memberData) {
    if (memberData.getSuccess() > 0) {
      this.successes.add(memberData);
    }
    if (memberData.getFailure() > 0) {
      this.failures.add(memberData);
    }
    total += memberData.getSuccess() + memberData.getFailure();
  }
  
  /**
   * Returns the current value of the specified bucket. 
   * @param bucket The bucket number, where 0 is successes and 1 is failures. 
   * @return The value inside the given bucket. 
   */
  public int getBucketValue(int bucket) {
    List<MemberData> dataList = ((bucket == 0) ? successes : failures);
    int count = 0;
    for (MemberData data : dataList) {
      count += ((bucket == 0) ? data.getSuccess() : data.getFailure()); 
    }
    return count;
  }
  
  /**
   * Returns the total number of entries across all buckets. 
   * @return The total number of entries. 
   */
  public int getTotal() {
    return this.total;
  }
  
  /**
   * Returns the total number of entries across all buckets as a string. 
   * @return The total number of entries. 
   */
  public String getTotalString() {
    return String.valueOf(this.total);
  }
  
  /**
   * Returns the bucket value as a percentage of the total number of entries across all buckets.
   * Returns zero if there are no entries. 
   * @param bucket The bucket whose percentage is to be returned.
   * @return The bucket as a percentage.
   */
  public int getBucketPercentage(int bucket) {
    if (getTotal() == 0) {
      return 0;
    }
    else {
      double percent = (double)getBucketValue(bucket) / (double)getTotal();
      return ((int) (percent * 100));
    }
  }
    
  /**
   * Returns the current value of the specified bucket as a string. 
   * @param bucket The bucket number, where 0 is the first one and 4 is the last one. 
   * @return The value inside the given bucket. 
   */
  public String getBucketCountString(int bucket) {
    return String.valueOf(getBucketValue(bucket));
  }
  
  /**
   * Returns the bucket percentage as a string.
   * @param bucket The bucket.
   * @return Its percentage as a string.
   */
  public String getBucketPercentageString(int bucket) {
    return getBucketPercentage(bucket) + "%";
  }
  
 
  /**
   * Return the project associated with this data. 
   * @return The project.
   */
  public Project getProject() {
    return project;
  }
  
  /**
   * Returns a details panel containing information about this bucket.
   * @param id The wicket id for this panel.
   * @param bucket The bucket of interest. 
   * @param isCount True if the count should be returned, false if percentage. 
   * @return The DailyProjectDetailsPanel instance. 
   */
  public DailyProjectDetailsPanel getPanel(String id, int bucket, boolean isCount) {
    StringBuffer members = new StringBuffer();
    List<MemberData> dataList = ((bucket == 0) ? successes : failures);
    for (MemberData data : dataList) {
      members.append(data.getMemberUri()).append(' ');
    }
    DailyProjectDetailsPanel dpdPanel = 
      new DailyProjectDetailsPanel(id, "Build Data", 
          ((isCount) ? this.getBucketCountString(bucket) : this.getBucketPercentageString(bucket)));
    dpdPanel.getModalWindow().setContent(new Label(dpdPanel.getModalWindow().getContentId(),
        members.toString()));
        
    return dpdPanel;
  }
}
