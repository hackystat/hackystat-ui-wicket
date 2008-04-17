package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data structure for coverage.
 * @author Shaoxuan Zhang
 */
public class CoverageData implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** name of this data. */
  private String name;
  /** collection of numbers of covered entries. */
  private Map<String, Integer> numCovered = new HashMap<String, Integer>();
  /** collection of numbers of uncovered entries. */
  private Map<String, Integer> numUncovered = new HashMap<String, Integer>();

  /** 
   * @param name name of this instance.
   */
  public CoverageData(String name) {
    this.name = name;
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
