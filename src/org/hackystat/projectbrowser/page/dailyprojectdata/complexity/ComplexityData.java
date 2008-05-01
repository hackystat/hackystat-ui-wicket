package org.hackystat.projectbrowser.page.dailyprojectdata.complexity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel.DailyProjectDetailsPanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.dailyprojectdata.resource.complexity.jaxb.FileData;

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
  private List<List<FileData>> buckets = new ArrayList<List<FileData>>();
  
  /** The total number of entries added across all buckets. */
  private int total = 0;

  /**
   * Creates a new complexityData instance.  
   * @param name The name of the Project associated with this instance. 
   */
  public ComplexityData(Project name) {
    this.project = name;
    for (int i = 0; i < 5; i++) {
      buckets.add(new ArrayList<FileData>());
    }
  }
  
  
  /**
   * Adds the complexity data in this FileData instance to the appropriate buckets.
   * @param data The FileData.
   */
  public void addEntry(FileData data) {
    String methodData = data.getComplexityValues();
    List<Integer> complexities = getComplexities(methodData);
    for (Integer complexity : complexities) {
      addEntry(complexity, data);
      this.total++;
    }
  }
  
  /**
   * Updates this ComplexityData instance with information about the number of complexitys for 
   * a specific class. 
   * @param complexityCount The number of complexitys. 
   * @param data The FileData containing this complexity count.
   */
  public void addEntry(int complexityCount, FileData data) {
    try {
      if (complexityCount <= 5) {
        buckets.get(0).add(data);
      }
      else if (complexityCount <= 10) {
        buckets.get(1).add(data);
      }
      else if (complexityCount <= 15) {
        buckets.get(2).add(data);
      }
      else if (complexityCount <= 20) {
        buckets.get(3).add(data);
      }
      else {
        buckets.get(4).add(data);
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
      new DailyProjectDetailsPanel(id, "Complexity Data", 
          ((isCount) ? this.getBucketCountString(bucket) : this.getBucketPercentageString(bucket)));
    dpdPanel.getModalWindow().setContent(
        new ComplexityDetailsPanel(dpdPanel.getModalWindow().getContentId(),
        buckets.get(bucket)));
        
    return dpdPanel;
  }
  

  /**
   * Takes the string containing method complexities and returns them as a List of Integers.
   * This really should go into the DPD system.  
   * @param methodData The method data as a string. 
   * @return The method data as a list of integers. 
   */
  private List<Integer> getComplexities(String methodData) {
    List<Integer> methodComplexities = new ArrayList<Integer>();
    try {
      String[] numStringList = methodData.split(",");
      for (String numString : numStringList) {
        methodComplexities.add(Integer.parseInt(numString));
      }
    }
    catch (Exception e) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Failed to parse Complexity method data: " + methodData);
    }
    return methodComplexities;
  }
}
