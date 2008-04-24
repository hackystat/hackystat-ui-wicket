package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.projectbrowser.ProjectBrowserApplication;
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
  
  /** The name of the project whose data is kept in this instance. */
  private String name;
  
  private List<Integer> buckets = new ArrayList<Integer>(5);
  /** collection of numbers of covered entries. */
  private Map<String, Integer> numCovered = new HashMap<String, Integer>();
  /** collection of numbers of uncovered entries. */
  private Map<String, Integer> numUncovered = new HashMap<String, Integer>();

  /**
   * Creates a new CoverageData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public CoverageData(String name) {
    this.name = name;
    for (int i = 0; i < 5; i++) {
      buckets.set(i, 0);
    }
  }
  
  /**
   * Increments the given bucket by 1. 
   * @param bucket The bucket number. 
   */
  private void incrementBucket(int bucket) {
    buckets.set(bucket, 1 + buckets.get(bucket));
  }
  
  /**
   * Updates this CoverageData instance with information about the covered/uncovered information
   * for a given instance in the Project.  
   * @param numCovered The number of covered somethings. 
   * @param numUncovered The number of uncovered somethings. 
   */
  public void addEntry(int numCovered, int numUncovered) {
    try {
      double percent = (double)numCovered / (double)(numCovered + numUncovered);
      if (percent < 21) {
        incrementBucket(0);
      }
      else if (percent < 41) {
        incrementBucket(1);
      }
      else if (percent < 61) {
        incrementBucket(2);
      }
      else if (percent < 81) {
        incrementBucket(3);
      }
      else {
        incrementBucket(4);
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
    return buckets.get(bucket);
  }
  
  
  
  /**
   * Return the formatted string of this coverage data. 
   * @param granularity granularity of the returning data.
   * @return the formatted string
   */
  public String getDisplayString(String granularity) {
    return convertToFormattedDisplayString(this.getNumCovered(granularity), 
                                           this.getNumUncovered(granularity));
  }
  
  /**
   * Convert the given coverage data into formatted display string.
   * Formatted as coverage%(covered/total)
   * @param numCovered covered number of the coverage data.
   * @param numUncovered uncovered number of the coverage data.
   * @return the formatted string
   */
  public static String convertToFormattedDisplayString(int numCovered, int numUncovered) {
    return (int)((double)numCovered / (numCovered + numUncovered) * 100) + "% (" + 
    numCovered + "/" + (numCovered + numUncovered) + ")";
  }
  
  /**
   * Retrieve the coverage value from the given formatted string
   * @param string the given string.
   * @return the coverage value in percent.
   */
  public static int getCoverageFromFormattedDisplayString(String string) {
    int index = string.indexOf('%');
    if (index < 0) {
      return 0;
    }
    String coverageString = string.substring(0, index);
    return Integer.valueOf(coverageString);
  }
  
  /**
   * Set coverage data with the given granularity.
   * @param granularity string of the granularity.
   * @param numCovered number of covered entries.
   * @param numUncovered number of uncovered entries.
   */
  public void setCoverage(String granularity, Integer numCovered, Integer numUncovered) {
    this.numCovered.put(granularity, numCovered);
    this.numUncovered.put(granularity, numUncovered);
  }
  
  /**
   * return the coverage value of the given granularity.
   * @param granularity string of the granularity.
   * @return the coverage value.
   */
  public double getCoverage(String granularity) {
    return (double)getNumCovered(granularity) /
                (getNumCovered(granularity) + getNumUncovered(granularity));
  }
  
  /**
   * @param granularity string of the granularity.
   * @return number of covered entries.
   */
  public int getNumCovered(String granularity) {
    return this.numCovered.get(granularity);
  }
  
  /**
   * @param granularity string of the granularity.
   * @return number of uncovered entries.
   */
  public int getNumUncovered(String granularity) {
    return this.numUncovered.get(granularity);
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Add coverage data to the specified granularity.
   * @param granularity the granularity
   * @param numCovered number of covered entries.
   * @param numUncovered number of uncovered entries.
   */
  public void addCoverage(String granularity, Integer numCovered, Integer numUncovered) {
    if (this.numCovered.get(granularity) == null) {
      this.numCovered.put(granularity, numCovered);
    }
    else {
      this.numCovered.put(granularity, numCovered + this.numCovered.get(granularity));
    }
    if (this.numUncovered.get(granularity) == null) {
      this.numUncovered.put(granularity, numUncovered);
    }
    else {
      this.numUncovered.put(granularity, numUncovered + this.numUncovered.get(granularity));
    }
    
  }
}
