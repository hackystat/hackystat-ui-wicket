package org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * MAY NOT NEED THIS.
 * Provides the link instance for the DailyProjectDetailsPanel. 
 * This contains a custom label, which is typically a count.
 * You actually create these things with anonymous classes that 
 * override the onClick() method to get the desired behavior. 
 * 
 * @author Philip Johnson
 */
public class DailyProjectDetailsPanelLink extends Link {
  
  /** For serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Creates the link for the DailyProjectDetailsPanel.
   * @param id The wicket ID.
   * @param count The count of instances. 
   */
  public DailyProjectDetailsPanelLink(String id, String count) {
    super(id);
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
