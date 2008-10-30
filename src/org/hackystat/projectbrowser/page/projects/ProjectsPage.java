package org.hackystat.projectbrowser.page.projects;

import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with information about Projects.
 * 
 * @author Philip Johnson
 * @author Randy Cox
 */
public class ProjectsPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Creates this page.
   */
  public ProjectsPage() {
    ProjectsForm form = new ProjectsForm("projectsForm", this);
    add(form);
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    session.setProjPage(this);
  }
}
