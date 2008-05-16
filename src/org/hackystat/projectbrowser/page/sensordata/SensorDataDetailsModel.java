package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * A model for the sensor data list. 
 * @author Philip Johnson
 */
public class SensorDataDetailsModel implements Serializable {
  /** For serialization. */
  private static final long serialVersionUID = 1L;
  private List<SensorDataDetails> detailsList = new ArrayList<SensorDataDetails>();
  
  /**
   * Creates an empty SensorDataDetailsModel.
   */
  public SensorDataDetailsModel() {
    // Don't do anything here. 
  }
  
  /**
   * Returns the list of sensor data details instances. 
   * @return The list of sensor data details. 
   */
  public List<SensorDataDetails> getDetails() {
    return this.detailsList;
  }
  
  /**
   * Sets the SensorDetailsModel given an sdtName and a Tool.  
   * This is typically called from a SensorDataPanelLink.
   * @param sdtName The name of the SDT whose instances are to be retrieved.
   * @param tool The tool for the SDTs.
   */
  public void setSensorDetailsModel(String sdtName, String tool) {
    detailsList.clear();
    SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    String projectName = session.getProject().getName();
    String owner = session.getProject().getOwner();
    XMLGregorianCalendar startTime = Tstamp.makeTimestamp(session.getDate().getTime());
    XMLGregorianCalendar endTime = Tstamp.incrementDays(startTime, 1);
    SensorDataIndex index = null;
    try {
      // Retrieve all sensor data instances if SdtName is "Total".
      if ("Total".equals(sdtName)) {
        index = client.getProjectSensorData(owner, projectName, startTime, endTime);
      }
      // Retrieve the sensor data instances for the specified tool
      else {
        index = client.getProjectSensorData(owner, projectName, startTime, endTime, sdtName, tool);
      }
      for (SensorDataRef ref : index.getSensorDataRef()) {
        detailsList.add(new SensorDataDetails(ref));
        // HACK.  Replace above call to get an index with just the tool's sensor data. 
        // if ("All".equals(tool) || tool.equals(ref.getTool())) {
        //   detailsList.add(new SensorDataDetails(ref));
        // }
      }
    }
    catch (Exception e) {
      System.out.println("Error getting sensor data: " + StackTrace.toString(e));
    }
  }
  
  /**
   * Returns true if this model does not contain any data. 
   * @return True if no data. 
   */
  public boolean isEmpty() {
    return detailsList.isEmpty();
  }
  

}
