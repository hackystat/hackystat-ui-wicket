package org.hackystat.projectbrowser.page.sensordata;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.projects.jaxb.MultiDayProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
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
    add(new SensorDataTablePanel("SensorDataTablePanel", this));
  }
  
  /**
   * What to do when the user selects a project and date. 
   */
  @Override
  public void onProjectDateSubmit() {
    try {
      // Set the footerFeedback to null. 
      this.footerFeedback = "";
      SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
      Project project = session.getProject();
      SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
      Calendar calendar = Calendar.getInstance();
      // calendar.setTime(new Date());
      // Set it to the first second of the first day of the month, get the start time.
      calendar.set(Calendar.YEAR, session.getYear());
      calendar.set(Calendar.MONTH, session.getMonth());
      calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      XMLGregorianCalendar startTime = Tstamp.makeTimestamp(calendar.getTimeInMillis());
      // Set it to the last second of the last day of the month, get the end time. 
      int numDays = calendar.getMaximum(Calendar.DAY_OF_MONTH);

      MultiDayProjectSummary summary =
        client.getMultiDayProjectSummary(project.getOwner(), project.getName(), startTime, numDays);
      // Update the model with this info.
      session.getSensorDataTableModel().setModel(summary, project);
      // Since the table in this panel has a variable number of columns that depend upon the model
      // data, we must rebuild this panel each time we update the model.
      addOrReplace(new SensorDataTablePanel("SensorDataTablePanel", this));
      
    }
    catch (Exception e) {
      this.footerFeedback = "Exception getting project summary: " + StackTrace.toString(e);
    }

  }
  
}
