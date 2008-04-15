package org.hackystat.projectbrowser.page.sensordata;

import java.util.Date;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Provides a page with information about SensorData. 
 * @author Philip Johnson
 */
public class SensorDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * Creates this page. 
   */
  public SensorDataPage() {
    add(new SensorDataForm("sensorDataForm", this));
  }
  
  @Override
  public void onProjectDateSubmit() {
    Date date = this.date;
    String projectName = this.projectName;
    Project project = ProjectBrowserSession.get().getProjectByNameId(this.projectName);
    this.footerFeedback = "Got project: " + project;    

  }
  
}
