package org.hackystat.projectbrowser.page.projectdatepanel;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

public class ProjectDatePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  public ProjectDatePanel(String id, ProjectBrowserBasePage page) {
    super(id);
    add(new FeedbackPanel("feedback"));
    add(new ProjectDateForm("projectDateForm", page));
  }


}
