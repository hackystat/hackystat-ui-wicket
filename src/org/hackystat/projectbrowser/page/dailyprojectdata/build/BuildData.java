package org.hackystat.projectbrowser.page.dailyprojectdata.build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Data structure for representing build information about a single project. 
 * This representation includes the Project, plus the number of passing and failing
 * build invocations.
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
  private List<Integer> buckets = new ArrayList<Integer>();
  
  /** The total number of entries added across all buckets. */
  private int total = 0;
  
  /**
   * Creates a new BuildData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public BuildData(Project name) {
    this.project = name;
    for (int i = 0; i < 2; i++) {
      buckets.add(0);
    }
  }
  
  /**
   * Increments the given bucket by the increment value. 
   * @param bucket The bucket number. 
   * @param increment How much to add to that bucket.
   */
  private void incrementBucket(int bucket, int increment) {
    buckets.set(bucket, buckets.get(bucket) + increment);
    this.total += increment;
  }
  
  /**
   * Updates this BuildData instance with information about the numbers of passes and failures
   * for a given instance in the Project.  
   * @param numPasses The number of passing unit test invocations.
   * @param numFailures The number of failing unit test invocations.
   */
  public void addEntry(int numPasses, int numFailures) {
    incrementBucket(0, numPasses);
    incrementBucket(1, numFailures);
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
}
