package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;

/**
 * Panel that provides clear cache confirmation.
 * 
 * @author Randy Cox
 */
public class ProjClearCachePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for this class.
   * 
   * @param id the wicket id.
   */
  public ProjClearCachePanel(String id) {
    super(id);

    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    final ProjectsModel model = session.getProjectsModel();

    Form projectForm = new Form("projClearCacheForm") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
    };

    TextField projName = new TextField("projectName", new PropertyModel(model, "projectName"));
    projName.setEnabled(false);
    projectForm.add(projName);

    Button clearCacheButton = new Button("clearCacheButton") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Deletes project. */
      @Override
      public void onSubmit() {
        System.err.println("ProjClearCachePanel.clearCacheButton.onSubmit()");

        DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
        TelemetryClient telemetryClient =  ProjectBrowserSession.get().getTelemetryClient();

        try {
          dpdClient.clearServerCache(model.getProjectOwner(), model.getProjectName());
          telemetryClient.clearServerCache(model.getProjectOwner(), model.getProjectName());
          session.getProjClearCachePanel().setVisible(false);
          session.getProjListPanel().setVisible(true);
        }
        catch (DailyProjectDataClientException e) {
          model.setFeedback(e.getMessage());
        }
        catch (TelemetryClientException e) {
          model.setFeedback(e.getMessage());
        }
      }
    };
    projectForm.add(clearCacheButton);

    Button cancelButton = new Button("cancelButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Cancel clear cache and return to list page. */
      @Override
      public void onSubmit() {
        System.err.println("ProjClearCachePanel.cancleButton.onSubmit()");
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        session.getProjClearCachePanel().setVisible(false);
        session.getProjListPanel().setVisible(true);
      }
    };
    projectForm.add(cancelButton);

    Label feedbackLabel = new Label("clearCacheFeedback", new PropertyModel(model, "feedback"));
    session.setFeedbackLabel(feedbackLabel);
    projectForm.add(feedbackLabel);
    add(projectForm);
  }

}
