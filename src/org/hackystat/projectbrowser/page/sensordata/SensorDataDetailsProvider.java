package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataIndex;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides an IDataProvider for the SensorDataDetails panel. The goal of this implementation is to
 * support a pageable list view of the selected sensor data, where each page is retrieved from the
 * SensorBase when it is actually requested by the user.
 * 
 * @author Philip Johnson
 * 
 */
public class SensorDataDetailsProvider implements IDataProvider {

  /** For serialization. */
  private static final long serialVersionUID = 1L;
  /** Contains the list of SensorDataDetails for the selected summary. */
  private List<SensorDataDetails> detailsList = new ArrayList<SensorDataDetails>();
  
  /**
   * Creates an empty SensorDataDetailsProvider. 
   */
  public SensorDataDetailsProvider () {
    // don't do anything here. 
  }
  
  /**
   * Used by the SdtSummary link to indicate how the model should be updated.
   * @param sdtName The sdtName.
   * @param tool The tool.
   */
  public void setSensorDataDetailsProvider(String sdtName, String tool) {
    detailsList.clear();
    SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    //String projectName = session.getProjectName();
    //String owner = ProjectBrowserSession.get().getUserEmail();
    String projectName = session.getProject().getName();
    String owner = session.getProject().getOwner();
    XMLGregorianCalendar startTime = Tstamp.makeTimestamp(session.getDate().getTime());
    XMLGregorianCalendar endTime = Tstamp.incrementDays(startTime, 1);
    // Retrieve all sensor data instances if SdtName is "Total".
    SensorDataIndex index = null;
    try {
      if ("Total".equals(sdtName)) {
        index = client.getProjectSensorData(owner, projectName, startTime, endTime);
      }
      else {
        index = client.getProjectSensorData(owner, projectName, startTime, endTime, sdtName);
      }
      for (SensorDataRef ref : index.getSensorDataRef()) {
        //SensorData data = client.getSensorData(ref);
        // HACK.  Replace above call to get an index with just the tool's sensor data. 
        if ("All".equals(tool) || tool.equals(ref.getTool())) {
          detailsList.add(new SensorDataDetails(ref));
        }
      }
    }
    catch (Exception e) {
      System.out.println("Error getting sensor data: " + StackTrace.toString(e));
    }
  }

  /**
   * Provides an iterator over the specified subset of SensorData.
   * 
   * @param first The first row to display.
   * @param count The minimum number of rows that the iterator will work on.
   * @return The iterator for the specified range. 
   */
  @SuppressWarnings("unchecked")
  public Iterator iterator(int first, int count) {
    return this.detailsList.subList(first, first + count).iterator();
  }

  /**
   * Callback used by the consumer of this data provider to wrap SensorDataDetails instances 
   * retrieved from iterator(int, int) with a model (usually a detachable one).
   * 
   * @param data The SensorDataDetails instance returned from the iterator that needs to be wrapped.
   * @return A model that wraps this instance.  
   */
  public IModel model(Object data) {
    return new Model((Serializable) data);
  }

  /**
   * The total number of items in this provider.
   * @return The total size of this model.
   */
  public int size() {
    return this.detailsList.size();
  }

  /**
   * Hook for detachable models, but we're not doing that yet. 
   */
  public void detach() {
    // Does nothing for now. 
  }

  /**
   * Returns true if this model does not contain any data. 
   * @return True if no data. 
   */
  public boolean isEmpty() {
    return detailsList.isEmpty();
  }
  
}
