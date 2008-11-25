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
      
      // Get the first day for which we want data, either the project start day or 1st of month.
      XMLGregorianCalendar startDay = getStartTime(project, session.getYear(), session.getMonth());
      // Get number of days for which we can get data, either the project end day or last of month.
      int numDays = getNumDays(project, startDay, session.getYear(), session.getMonth());

      MultiDayProjectSummary summary =
        client.getMultiDayProjectSummary(project.getOwner(), project.getName(), startDay, numDays);
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
  
  /**
   * Returns the start day to be provided to the MultiDayProjectSummary call. 
   * The start day is either the first day of the year/month or the first day of the project, 
   * whichever comes later. 
   * @param project The project of interest.
   * @param year The year specified by the user. 
   * @param month The month specified by the user. 
   * @return The start day to be used. 
   */
  private XMLGregorianCalendar getStartTime(Project project, int year, int month) {
    // Get the project start time. 
    XMLGregorianCalendar projectStartTime = project.getStartTime();
    // Now make a timestamp representing the first day of the passed month.
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    XMLGregorianCalendar userStartTime = Tstamp.makeTimestamp(calendar.getTimeInMillis());
    return Tstamp.greaterThan(projectStartTime, userStartTime) ? projectStartTime : userStartTime;
  }
  
  /**
   * Returns the number of days in the interval between the startTime and either the project
   * end day or the last day of the passed month, whichever comes first. 
   * @param project The project that we use to get the project end date.
   * @param startTime The startTime. 
   * @param year The user-specified year, from which we get the user's end day.
   * @param month The user-specified month, from which we get the user's end day.
   * @return The number of days of data that we can retrieve.
   */
  private int getNumDays(Project project, XMLGregorianCalendar startTime, int year, int month) {
    // Get the project end time. 
    XMLGregorianCalendar projectEndTime = project.getEndTime();
    // Now make a timestamp representing the last day of the passed month.
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    XMLGregorianCalendar userEndTime = Tstamp.makeTimestamp(calendar.getTimeInMillis());
    
    // Figure out which is the real end time. 
    XMLGregorianCalendar endTime = 
      Tstamp.lessThan(projectEndTime, userEndTime) ? projectEndTime : userEndTime;
    
    // Now figure out the number of days in this interval.
    return Tstamp.daysBetween(startTime, endTime);
  }
  
}
