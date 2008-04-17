package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;
import org.hackystat.sensorbase.resource.sensordata.jaxb.SensorData;
import org.hackystat.sensorbase.resource.sensordata.jaxb.Property;
import org.hackystat.sensorbase.resource.sensordata.jaxb.Properties;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * A representation of a SensorData instance. 
 * It might be cool to construct this with the SensorDataRef, then retrieve it if 
 * necessary when there is an attempt to access the field. 
 * @author Philip Johnson
 */
public class SensorDataDetails implements Serializable {

  /** For serialization. */
  private static final long serialVersionUID = 1L;
  private long timestamp;
  private long runtime;
  private String sdtName;
  private String resource;
  private String owner;
  private String tool;
  private String properties;
  
  /**
   * Create a SensorData instance representation suitable for display in Wicket. 
   * @param data The SensorData instance from the SensorBase. 
   */
  public SensorDataDetails(SensorData data) {
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
   * Return the timestamp. 
   * @return The timestamp.
   */
  public String getTimeStamp() {
    return Tstamp.makeTimestamp(this.timestamp).toString();
  }
  
  /**
   * Return the runtime. 
   * @return The runtime. 
   */
  public String getRuntime() {
    return Tstamp.makeTimestamp(this.runtime).toString();
  }
  
  /**
   * Return the SDT name. 
   * @return The sdt name.
   */
  public String getSdtName() {
    return this.sdtName;
  }
  
  /**
   * Return the resource.
   * @return The resource. 
   */
  public String getResource() {
    return this.resource;
  }
  
  /**
   * Return the owner. 
   * @return The owner. 
   */
  public String getOwner() {
    return this.owner;
  }
  
  /**
   * Return the tool.
   * @return The tool.
   */
  public String getTool() {
    return this.tool;
  }
  
  /**
   * Return the properties. 
   * @return The properties. 
   */
  public String getProperties() {
    return this.properties;
  }
}
