package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.sensorbase.resource.projects.jaxb.ProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.SensorDataSummaries;
import org.hackystat.sensorbase.resource.projects.jaxb.SensorDataSummary;

/**
 * A model for an individual row in the sensor data table.
 * @author Philip Johnson
 */
public class SensorDataTableRowModel implements Serializable { 
  
  /** Required for serialization. */
  private static final long serialVersionUID = 1L;
  private ProjectSummary projectSummary = null;
  private SimpleDateFormat dayFormat = new SimpleDateFormat("EEE, MMM d yyyy", Locale.US);

  /**
   * Creates a row given a project summary for a given day. 
   * @param summary The ProjectSummary instance. 
   */
  public SensorDataTableRowModel (ProjectSummary summary) {
    this.projectSummary = summary;
  }
  
  /**
   * Returns a string indicating the day associated with this row. 
   * @return The day as a string. 
   */
  public String getDayString () {
    return dayFormat.format(this.projectSummary.getStartTime().toGregorianCalendar().getTime());
  }
  
  /**
   * Returns the start time for this day. 
   * @return The start time. 
   */
  public XMLGregorianCalendar getStartTime() {
    return this.projectSummary.getStartTime();
  }
  
  /**
   * Returns the total number of sensor data instances sent on this day. 
   * @return The total number of sensor data instances. 
   */
  public int getTotal() {
    SensorDataSummaries summary = this.projectSummary.getSensorDataSummaries();
    BigInteger numInstances;
    if (summary == null) {
      return 0;
    }
    else {
      numInstances = summary.getNumInstances();
      if (numInstances == null) {
        return 0;
      }
    }
    return numInstances.intValue(); 
  }
  

  /**
   * Returns a list of SensorDataSummary instances associated with the passed sdt. 
   * @param sdt The sdt of interest. 
   * @return The SensorDataSummary instances associated with this SDT.
   */
  public List<SensorDataSummary> getSensorDataSummaryList(String sdt) {
    List<SensorDataSummary> summaryList = new ArrayList<SensorDataSummary>();
    if (this.projectSummary.isSetSensorDataSummaries()) {
      for (SensorDataSummary summary : 
        this.projectSummary.getSensorDataSummaries().getSensorDataSummary()) {
        String summarySdt = summary.getSensorDataType();
        if ((sdt.equals(summarySdt)) ||
            (isUnknownSdt(sdt) && isUnknownSdt(summarySdt))) {
          summaryList.add(summary);
        }
      }
    }
    return summaryList;
  }
  
  /**
   * True if there is no SDT associated with this sensor data instance.
   * @param sdt The SDT field.
   * @return True if the SDT field value indicates no SDT.
   */
  private boolean isUnknownSdt(String sdt) {
    return ((sdt == null) ||
            ("".equals(sdt)) ||
            ("Unknown".equals(sdt)));
  }
}
