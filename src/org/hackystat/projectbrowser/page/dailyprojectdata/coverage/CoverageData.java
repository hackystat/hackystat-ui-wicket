package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel.DailyProjectDetailsPanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.stacktrace.StackTrace;

/**
 * Data structure for representing coverage information about a single project. 
 * This representation includes the Project, plus the number of classes in the project with 
 * coverage in each of five buckets: 0-20%, 21-40%, 41-60%, 61-80%, and 81-100%.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class CoverageData implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** The five buckets for this data. */
  private List<List<ConstructData>> buckets = new ArrayList<List<ConstructData>>();
  
  /** The total number of entries added across all buckets. */
  private int total = 0;
  
  /**
   * Creates a new CoverageData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public CoverageData(Project name) {
    this.project = name;
    for (int i = 0; i < 5; i++) {
      buckets.add(new ArrayList<ConstructData>());
    }
  }
  
  /**
   * Increments the given bucket by 1. 
   * @param bucket The bucket number.
   * @param data The Construct Data.  
   */
  private void incrementBucket(int bucket, ConstructData data) {
    buckets.get(bucket).add(data);
    this.total++;
  }
  
  /**
   * Updates this CoverageData instance with information about the covered/uncovered information
   * for a given instance in the Project.  
   * @param data The construct data. 
   */
  public void addEntry(ConstructData data) {
    
    int numCovered = data.getNumCovered();
    int numUncovered = data.getNumUncovered();
    // If no method level coverage, ignore.
    if ((numCovered == 0) && (numUncovered == 0)) {
      return;
    }
    try {
      double percent = (double)numCovered / (double)(numCovered + numUncovered);
      if (percent <= 0.20) {
        incrementBucket(0, data);
      }
      else if (percent <= 0.40) {
        incrementBucket(1, data);
      }
      else if (percent <= 0.60) {
        incrementBucket(2, data);
      }
      else if (percent <= 0.80) {
        incrementBucket(3, data);
      }
      else {
        incrementBucket(4, data);
      }
    }
    catch (Exception e) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Error adding an entry to Coverage DPD: " + StackTrace.toString(e));
    }
  }
  
  /**
   * Returns the current value of the specified bucket. 
   * @param bucket The bucket number, where 0 is the first one and 4 is the last one. 
   * @return The value inside the given bucket. 
   */
  public int getBucketValue(int bucket) {
    return buckets.get(bucket).size();
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
    DailyProjectDetailsPanel dpdPanel = 
      new DailyProjectDetailsPanel(id, "Coverage Data", 
          ((isCount) ? this.getBucketCountString(bucket) : this.getBucketPercentageString(bucket)));
    dpdPanel.getModalWindow().setContent(
        new CoverageDetailsPanel(dpdPanel.getModalWindow().getContentId(),
        buckets.get(bucket)));
        
    return dpdPanel;
  }
}
