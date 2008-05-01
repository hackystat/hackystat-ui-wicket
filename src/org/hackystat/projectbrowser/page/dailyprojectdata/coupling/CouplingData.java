package org.hackystat.projectbrowser.page.dailyprojectdata.coupling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel.DailyProjectDetailsPanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.stacktrace.StackTrace;

/**
 * Data structure for representing coupling information about a single project. 
 * This representation includes the Project, plus the number of classes in the project with 
 * coupling in each of five buckets: 0-5, 6-10, 11-15, 16-20, and 20+
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class CouplingData implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** The five buckets for this data. */
  private List<List<org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData>> buckets = 
    new ArrayList<List<org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData>>();
  
  /** The total number of entries added across all buckets. */
  private int total = 0;

  /**
   * Creates a new couplingData instance and initializes the buckets to zero.  
   * @param name The name of the Project associated with this instance. 
   */
  public CouplingData(Project name) {
    this.project = name;
    for (int i = 0; i < 5; i++) {
      buckets.add(
          new ArrayList<org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData>());
    }
  }

  
  /**
   * Updates this CouplingData instance with information about the number of couplings for 
   * a specific class. 
   * @param couplingCount The number of couplings. 
   * @param data The Coupling data. 
   */
  public void addEntry(int couplingCount, 
      org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData data) {
    try {
      if (couplingCount <= 5) {
        buckets.get(0).add(data);
      }
      else if (couplingCount <= 10) {
        buckets.get(1).add(data);
      }
      else if (couplingCount <= 15) {
        buckets.get(2).add(data);
      }
      else if (couplingCount <= 20) {
        buckets.get(3).add(data);
      }
      else {
        buckets.get(4).add(data);
      }
      total++;
    }
    catch (Exception e) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Error adding an entry to coupling DPD: " + StackTrace.toString(e));
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
      new DailyProjectDetailsPanel(id, "Coupling Data", 
          ((isCount) ? this.getBucketCountString(bucket) : this.getBucketPercentageString(bucket)));
    dpdPanel.getModalWindow().setContent(
        new CouplingDetailsPanel(dpdPanel.getModalWindow().getContentId(),
        buckets.get(bucket)));
        
    return dpdPanel;
  }
}
