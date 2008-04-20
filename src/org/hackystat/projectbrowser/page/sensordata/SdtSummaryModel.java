package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.SensorDataSummary;

/**
 * Provides a model for the summary of sdts and their counts. 
 * @author Philip Johnson.
 */
public class SdtSummaryModel implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private List<SdtSummary> sdtSummaryList = new ArrayList<SdtSummary>();
  private long total = 0;
  private long date = 0;
  private Project project = null;
    
  /**
   * The default constructor, required by Wicket. 
   */
  public SdtSummaryModel() {
    // does nothing.
  }

  /**
   * Returns the list of sdt summary instances. 
   * @return The SDT instances. 
   */
  public List<SdtSummary> getSdtList() {
    return this.sdtSummaryList;
  }
  
  /**
   * Return the Date associated with this page. 
   * @return The date for this page. 
   */
  public Date getDate() {
    return new Date(this.date);
  }
  
  /**
   * Return the Project for this page. 
   * @return The project. 
   */
  public Project getProject() {
    return this.project;
  }
  
  /**
   * Updates the SdtModel with the project summary instance. 
   * @param summary The summary instance. 
   * @param date The date for this summary.
   * @param project The project for this summary.
   */
  public final void setModel(ProjectSummary summary, Date date, Project project) {
    total = 0;
    sdtSummaryList.clear();
    this.date = date.getTime();
    this.project = project;
    List<SensorDataSummary> summaries = summary.getSensorDataSummaries().getSensorDataSummary();
    if (summaries != null) {
      for (SensorDataSummary sum : summaries) {
        total += sum.getNumInstances().longValue();
        sdtSummaryList.add(
            new SdtSummary(sum.getSensorDataType(), sum.getTool(), 
                sum.getNumInstances().longValue()));
      }
      // Now add the Total entry
      sdtSummaryList.add(new SdtSummary("Total", "All", total));
    }
  }
  
  /**
   * True if this model contains no data.
   * @return True if this model has no data. 
   */
  public boolean isEmpty() {
    return this.project == null;
  }
}
