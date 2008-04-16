package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Creates a new summary model. 
   * @param summary The project summary.
   */
  public SdtSummaryModel(ProjectSummary summary) {
    setList(summary);
  }
  
  /**
   * Returns the list of sdt summary instances. 
   * @return The SDT instances. 
   */
  public List<SdtSummary> getList() {
    return this.sdtSummaryList;
  }
  
  /**
   * Updates the SdtModel with the project summary instance. 
   * @param summary The summary instance. 
   */
  public void setList(ProjectSummary summary) {
    total = 0;
    sdtSummaryList.clear();
    List<SensorDataSummary> summaries = summary.getSensorDataSummaries().getSensorDataSummary();
    if (summaries != null) {
      for (SensorDataSummary sum : summaries) {
        total += sum.getNumInstances().longValue();
        sdtSummaryList.add(
            new SdtSummary(sum.getSensorDataType(), sum.getNumInstances().intValue()));
      }
    }
  }
}
