package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;

/**
 * A simple class containing a summary of a single SDT instance. 
 * @author Philip Johnson
 */
public class SdtSummary implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private String sdtName;
  private int count; 

  /**
   * Construct this instance. 
   * @param sdtName The SDT name. 
   * @param count The number of instances of this type. 
   */
  public SdtSummary(String sdtName, int count) {
    this.sdtName = sdtName;
    this.count = count;
  }
  
  /**
   * Get the sdt name. 
   * @return The sdt name. 
   */
  public String getSdtName() {
    return this.sdtName;
  }
  
  /**
   * Get the number of times it occurred. 
   * @return The count of sdt instances of this type. 
   */
  public int getCount () {
    return this.count;
  }
}
