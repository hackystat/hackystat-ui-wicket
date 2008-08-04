package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.List;

import org.hackystat.sensorbase.resource.projects.jaxb.MultiDayProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectSummary;

/**
 * Provides a model for the summary of sdts and their counts. 
 * @author Philip Johnson.
 */
public class SdtSummaryModel implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private Project project = null;
  private List<ProjectSummary> projectSummaries = null;
    
  /**
   * The default constructor, required by Wicket. 
   */
  public SdtSummaryModel() {
    // does nothing.
  }

  /**
   * Returns the list of ProjectSummary instances.
   * @return The ProjectSummary instances. 
   */
  public List<ProjectSummary> getSummaryList() {
    return this.projectSummaries;
  }
  
  /**
   * Return the Project for this page. 
   * @return The project. 
   */
  public Project getProject() {
    return this.project;
  }
  
  /**
   * Updates the SdtModel with the MultiDayProjectSummary instance. 
   * @param summary The summary instance. 
   * @param project The project for this summary.
   */
  public final void setModel(MultiDayProjectSummary summary, Project project) {
    this.projectSummaries = summary.getProjectSummary();
    this.project = project;
  }
  
  /**
   * True if this model contains no data.
   * @return True if this model has no data. 
   */
  public boolean isEmpty() {
    return this.project == null;
  }
}
