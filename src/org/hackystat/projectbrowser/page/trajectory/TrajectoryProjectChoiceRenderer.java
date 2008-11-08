package org.hackystat.projectbrowser.page.trajectory;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * The choice renderer.
 * 
 * @author Pavel Senin.
 * 
 */
public class TrajectoryProjectChoiceRenderer extends ChoiceRenderer {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Return the display value that present the object.
   * 
   * @param object the object to display.
   * @return the display value.
   */
  @Override
  public Object getDisplayValue(Object object) {
    int duplicateCount = 0;
    ProjectRecord targetProject = (ProjectRecord) object;
    for (ProjectRecord projectRecord : ProjectBrowserSession.get().getTrajectorySession()
        .getProjectList()) {
      if (targetProject.getProject().getName().equals(projectRecord.getProject().getName())) {
        duplicateCount++;
      }
    }
    String view;
    if (duplicateCount > 1) {
      view = targetProject.getProject().getName() + " - " + targetProject.getProject().getOwner();
    }
    else {
      view = targetProject.getProject().getName();
    }
    return view;
  }
}
