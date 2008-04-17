package org.hackystat.projectbrowser.page.projectdatepanel;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Panel to let user select the date and project to display.
 * @author Shaoxuan Zhang
 */
public class ProjectDatePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public ProjectDatePanel(String id, ProjectBrowserBasePage page) {
    super(id);
    add(new FeedbackPanel("feedback"));
    add(new ProjectDateForm("projectDateForm", page));
  }


}
