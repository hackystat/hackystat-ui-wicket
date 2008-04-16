package org.hackystat.projectbrowser.page.sensordata;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectSummary;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a page with information about SensorData. 
 * @author Philip Johnson
 */
public class SensorDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  SdtSummaryModel sdtSummaryModel = null;
  
  /**
   * Creates this page. 
   */
  public SensorDataPage() {
    add(new SensorDataForm("sensorDataForm", this));
  }
  
  @Override
  public void onProjectDateSubmit() {
    try {
      // Start by getting the project summary.
      Project project = ProjectBrowserSession.get().getProjectByNameId(this.projectName);
      SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
      XMLGregorianCalendar startTime = Tstamp.makeTimestamp(this.date.getTime());
      XMLGregorianCalendar endTime = Tstamp.incrementDays(startTime, 1);
      ProjectSummary summary = 
        client.getProjectSummary(project.getName(), project.getOwner(), startTime, endTime);
      // Now create the summary model from the ProjectSummary.
      this.sdtSummaryModel = new SdtSummaryModel(summary);
      
    }
    catch (Exception e) {
      this.footerFeedback = "Exception getting project summary: " + StackTrace.toString(e);
    }

  }
  
}
