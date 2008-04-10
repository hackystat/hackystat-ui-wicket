package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

public class ProjectsPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  public ProjectsPage() {
    add(new Label("PageContents", "Projects Page contents here."));
  }
}
