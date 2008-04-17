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
   * Formatted as coverage%(covered/total)
   * @param granularity granularity of the returning data.
   * @return the formatted string
   */
  public String getDisplayString(String granularity) {
    return (int)(this.getCoverage(granularity) * 100) + "% (" +  this.getNumCovered(granularity) + 
    "/" + (this.getNumCovered(granularity) + this.getNumUncovered(granularity)) + ")";
  }
  
  /**
   * Add coverage data with the given granularity.
   * @param granularity string of the granularity.
   * @param numCovered number of covered entries.
   * @param numUncovered number of uncovered entries.
   */
  public void addCoverage(String granularity, Integer numCovered, Integer numUncovered) {
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
}
