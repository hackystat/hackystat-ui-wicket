package org.hackystat.projectbrowser.page.trajectory.inputpanel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Panel to let user select the project and telemetry to display.
 * @author Shaoxuan Zhang, Pavel Senin
 */
public class TrajectoryInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public TrajectoryInputPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    add(new FeedbackPanel("feedback"));
    add(new TrajectoryInputForm("inputForm", page));
    
    Button cancelButton = new Button("cancel") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        ProjectBrowserSession.get().getTrajectorySession().cancelDataUpdate();
      }
      @Override
      public boolean isEnabled() {
        return ProjectBrowserSession.get().getTrajectorySession().getDataModel().isInProcess();
      }
    };
    add(new Form("cancelForm").add(cancelButton));
  }

}
