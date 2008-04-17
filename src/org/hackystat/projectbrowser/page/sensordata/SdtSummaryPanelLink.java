package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * Provides the link instance for the SdtSummary Panel. 
 * This contains a custom label, which is the count.
 * @author Philip Johnson
 */
public class SdtSummaryPanelLink extends Link {
  
  /** For serialization. */
  private static final long serialVersionUID = 1L;

  protected String sdtName;
  protected String tool;
  /**
   * Creates the link in the SdtSummary Panel.
   * @param id The wicket ID.
   * @param count The count of instances. 
   * @param sdtName The sensor data type name.
   * @param tool The tool. 
   */
  public SdtSummaryPanelLink(String id, String count, String sdtName, String tool) {
    super(id);
    this.sdtName = sdtName;
    this.tool = tool;
    add(new Label("count", count));
  }

  /**
   * Don't need to provide this behavior, will be overridden. 
   */
  @Override
  public void onClick() {
    //do nothing.
  }
}
