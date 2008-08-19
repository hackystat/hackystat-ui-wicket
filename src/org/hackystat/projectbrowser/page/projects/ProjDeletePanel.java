package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;

/**
 * Panel that provides delete confirmation.
 * 
 * @author Randy Cox
 */
public class ProjDeletePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for this class.
   * 
   * @param id the wicket id.
   */
  public ProjDeletePanel(String id) {
    super(id);

    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    final ProjectsModel model = session.getProjectsModel();

    Form projectForm = new Form("projDeleteForm") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
    };

    TextField projName = new TextField("projectName", new PropertyModel(model, "projectName"));
    projName.setEnabled(false);
    projectForm.add(projName);

    Button deleteButton = new Button("deleteButton") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Deletes project. */
      @Override
      public void onSubmit() {
        String email = ProjectBrowserSession.get().getEmail();
        SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
        try {
          client.deleteProject(email, model.getProjectName());
          session.getProjDeletePanel().setVisible(false);
          session.getProjListPanel().setVisible(true);
        }
        catch (SensorBaseClientException e) {
          model.setFeedback(e.getMessage());
        }
      }
    };
    projectForm.add(deleteButton);

    Button cancelButton = new Button("cancelButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Cancel delete and return to list page. */
      @Override
      public void onSubmit() {
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        session.getProjDeletePanel().setVisible(false);
        session.getProjListPanel().setVisible(true);
      }
    };
    projectForm.add(cancelButton);

    Label feedbackLabel = new Label("deleteFeedback", new PropertyModel(model, "feedback"));
    session.setFeedbackLabel(feedbackLabel);
    projectForm.add(feedbackLabel);
    add(projectForm);
  }

}
