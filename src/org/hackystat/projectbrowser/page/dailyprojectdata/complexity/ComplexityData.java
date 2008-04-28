package org.hackystat.projectbrowser.page.dailyprojectdata.complexity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.stacktrace.StackTrace;

/**
 * Data structure for representing complexity information about a single project. 
 * This representation includes the Project, plus the number of methods in the project with 
 * complexity in each of five buckets: 0-10, 11-20, 21-30, 31-40, and 41+
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class ComplexityData implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** The five buckets for this data. */
  private List<Integer> buckets = new ArrayList<Integer>();
  
  /** The total number of entries added across all buckets. */
  private int total = 0;

  /**
   * Creates a new complexityData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public ComplexityData(Project name) {
    this.project = name;
    for (int i = 0; i < 5; i++) {
      buckets.add(0);
    }
  }
  
  /**
   * Increments the given bucket by 1. 
   * @param bucket The bucket number. 
   */
  private void incrementBucket(int bucket) {
    buckets.set(bucket, 1 + buckets.get(bucket));
    this.total++;
  }
  
  /**
   * Updates this ComplexityData instance with information about the number of complexitys for 
   * a specific class. 
   * @param complexityCount The number of complexitys. 
   */
  public void addEntry(int complexityCount) {
    try {
      if (complexityCount <= 5) {
        incrementBucket(0);
      }
      else if (complexityCount <= 10) {
        incrementBucket(1);
      }
      else if (complexityCount <= 15) {
        incrementBucket(2);
      }
      else if (complexityCount <= 20) {
        incrementBucket(3);
      }
      else {
        incrementBucket(4);
      }
    }
    catch (Exception e) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Error adding an entry to complexity DPD: " + StackTrace.toString(e));
    }
  }
  
  /**
   * Returns the current value of the specified bucket. 
   * @param bucket The bucket number, where 0 is the first one and 4 is the last one. 
   * @return The value inside the given bucket. 
   */
  public int getBucketValue(int bucket) {
    return buckets.get(bucket);
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
}
