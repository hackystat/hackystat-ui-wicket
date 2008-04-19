package org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Choice Renderer for Project choice list.
 * @author Shaoxuan Zhang
 *
 */
public class ProjectChoiceRenderer extends ChoiceRenderer {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** 
   * return the display value that present the object. 
   * @param object the bject to display.
   * @return the display value.
   */
  @Override
  public Object getDisplayValue(Object object) {
    int duplicateCount = 0;
    Project targetProject = (Project) object;
    for (Project project : ProjectBrowserSession.get().getProjectList()) {
      if (targetProject.getName().equals(project.getName())) {
        duplicateCount++;
      }
    }
    String view;
    if (duplicateCount > 1) {
      view = targetProject.getName() + " - " + targetProject.getOwner();
    }
    else {
      view = targetProject.getName();
    }
    return view;
  }
  
}