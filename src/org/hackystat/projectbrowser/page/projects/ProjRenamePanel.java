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
 * Panel that will be rendered within the popup project rename window.
 * 
 * @author Randy Cox
 */
public class ProjRenamePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for this class.
   * 
   * @param id the wicket id.
   */
  public ProjRenamePanel(String id) {
    super(id);

    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    final ProjectsModel model = session.getProjectsModel();

    Form projectForm = new Form("projRenameForm");

    TextField projName = new TextField("projectName", new PropertyModel(model, "projectName"));
    projName.setEnabled(false);
    projectForm.add(projName);

    TextField projRename = new TextField("projectRename", new PropertyModel(model, 
      "projectRename"));
    projectForm.add(projRename);

    Button renameButton = new Button("renameButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Bring up rename panel for project. */
      @Override
      public void onSubmit() {
        String email = ProjectBrowserSession.get().getEmail();
        SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();
        try {
          client.renameProject(email, model.getProjectName(), model.getProjectRename());
          session.getProjRenamePanel().setVisible(false);
          session.getProjListPanel().setVisible(true);
        }
        catch (SensorBaseClientException e) {
          model.setFeedback(e.getMessage());
        }
      }
    };
    projectForm.add(renameButton);

    Button cancelButton = new Button("cancelButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Cancel. */
      @Override
      public void onSubmit() {
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        session.getProjRenamePanel().setVisible(false);
        session.getProjListPanel().setVisible(true);
      }
    };
    projectForm.add(cancelButton);

    Label feedbackLabel = new Label("renameFeedback", new PropertyModel(model, "feedback"));
    session.setFeedbackLabel(feedbackLabel);
    projectForm.add(feedbackLabel);

    add(projectForm);
  }
}
