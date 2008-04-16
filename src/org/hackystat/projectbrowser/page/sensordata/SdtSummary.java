package org.hackystat.projectbrowser.page.sensordata;

import java.io.Serializable;

/**
 * A simple class containing a summary of a single SDT instance. 
 * @author Philip Johnson
 */
public class SdtSummary implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private String sdtName;
  private String tool;
  private long count; 

  /**
   * Construct this instance. 
   * @param sdtName The SDT name. 
   * @param tool The tool that generated instances with this SDT.
   * @param count The number of instances of this type and tool.
   */
  public SdtSummary(String sdtName, String tool, long count) {
    this.sdtName = sdtName;
    this.tool = tool;
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
  public long getCount () {
    return this.count;
  }
 
  /**
   * The tool responsible for generating these instances. 
   * @return The tool that generated these instances. 
   */
  public String getTool() {
    return this.tool;
  }
}
