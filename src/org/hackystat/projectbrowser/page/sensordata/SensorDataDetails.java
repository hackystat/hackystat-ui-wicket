package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;

import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorData;
import org.hackystat.sensorbase.resource.sensordata.jaxb.Property;
import org.hackystat.sensorbase.resource.sensordata.jaxb.Properties;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorDataRef;
import org.hackystat.utilities.stacktrace.StackTrace;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * A representation of a SensorData instance. 
 * If constructed with a SensorDataRef, will do lazy loading. 
 * @author Philip Johnson
 */
public class SensorDataDetails implements Serializable {

  /** For serialization. */
  private static final long serialVersionUID = 1L;
  private long timestamp = 0;
  private long runtime = 0;
  private String sdtName = null;
  private String resource = null;
  private String owner = null;;
  private String tool = null;
  private String properties = null;
  private SensorDataRef ref = null;
  
  /**
   * Create a SensorData instance representation suitable for display in Wicket. 
   * @param data The SensorData instance from the SensorBase. 
   */
  public SensorDataDetails(SensorData data) {
    populateFields(data);
  }
  
  /**
   * Creates a SensorDataDetails from a SensorDataRef.  The ref will
   * be used to retrieve a SensorData object the first time that an accessor on
   * this instance is invoked.
   * @param ref The SensorDataRef.
   */
  public SensorDataDetails(SensorDataRef ref) {
    this.ref = ref;
  }
  
  /**
   * Populates the internal fields given a SensorData instance.
   * @param data The instance. 
   */
  private void populateFields(SensorData data) {
    this.timestamp = data.getTimestamp().toGregorianCalendar().getTimeInMillis();
    this.runtime = data.getRuntime().toGregorianCalendar().getTimeInMillis();
    this.sdtName = data.getSensorDataType();
    this.resource = data.getResource();
    this.owner = data.getOwner();
    this.tool = data.getTool();
    StringBuffer buff = new StringBuffer();
    Properties props = data.getProperties();
    if (props != null) {
      for (Property prop : data.getProperties().getProperty()) {
        String key = prop.getKey();
        String value = prop.getValue();
        buff.append(key).append(" = ").append(value).append(',');
      }
    }
    this.properties = buff.toString();
  }
  
  /**
   * Returns a SensorData instance, given the reference.
   * Can also return null if an error occurs during retrieval. 
   * @param ref The sensor data reference. 
   * @return The sensor data instance. 
   */
  private SensorData getSensorData(SensorDataRef ref) {
    SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
    try {
      return client.getSensorData(ref);
    }
    catch (SensorBaseClientException e) {
      System.out.println("Error getting sensor data: " + StackTrace.toString(e));
    }
    return null;
  }
  
  /**
   * Checks to see whether this SensorDataDetails instance contains only a reference,
   * and if so, retrieves the actual sensor data instance and populates the fields with it.
   */
  private void dereference() {
    if ((this.timestamp == 0) && (this.ref != null)) {
      SensorData data = getSensorData(this.ref);
      if (data != null) {
        this.populateFields(data);
      }
    }
  }

  /**
   * Return the timestamp. 
   * @return The timestamp.
   */
  public String getTimeStamp() {
    dereference();
    return Tstamp.makeTimestamp(this.timestamp).toString();
  }
  
  /**
   * Return the runtime. 
   * @return The runtime. 
   */
  public String getRuntime() {
    dereference();
    return Tstamp.makeTimestamp(this.runtime).toString();
  }
  
  /**
   * Return the SDT name. 
   * @return The sdt name.
   */
  public String getSdtName() {
    dereference();
    return this.sdtName;
  }
  
  /**
   * Return the resource.
   * @return The resource. 
   */
  public String getResource() {
    dereference();
    return this.resource;
  }
  
  /**
   * Return the owner. 
   * @return The owner. 
   */
  public String getOwner() {
    dereference();
    return this.owner;
  }
  
  /**
   * Return the tool.
   * @return The tool.
   */
  public String getTool() {
    dereference();
    return this.tool;
  }
  
  /**
   * Return the properties. 
   * @return The properties. 
   */
  public String getProperties() {
    dereference();
    return this.properties;
  }
}
