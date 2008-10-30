package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Panel use to edit project.
 * 
 * @author Shaoxuan Zhang
 * @author Randy Cox
 */
public class ProjEditPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** Date format used in date field input. */
  private static final String DATA_FORMAT = "yyyy-MM-dd";

  /**
   * Constructor for this class.
   * 
   * @param id the wicket id.
   */
  public ProjEditPanel(String id) {
    super(id);

    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    final ProjectsModel model = session.getProjectsModel();

    final Form projectForm = new Form("projEditForm");

    TextField projName = new TextField("projectName", new PropertyModel(model, "projectName")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Allows editing of project name if the record is brand new. */
      @Override
      public boolean isEnabled() {
        return ProjectBrowserSession.get().getProjectsSession().isNewProject();
      }
    };
    projectForm.add(projName);

    Label projOwner = new Label("projectOwner", new PropertyModel(model, "projectOwner"));
    projOwner.setEscapeModelStrings(false);
    projectForm.add(projOwner);

    projectForm.add(new TextArea("projectDesc", new PropertyModel(model, "projectDesc")));

    DateTextField projectStartDate = new DateTextField("projectStartDate", new PropertyModel(model,
        "projectStartDate"), DATA_FORMAT);
    projectStartDate.add(new DatePicker());
    projectForm.add(projectStartDate);

    DateTextField projectEndDate = new DateTextField("projectEndDate", new PropertyModel(model,
        "projectEndDate"), DATA_FORMAT);
    projectEndDate.add(new DatePicker());
    projectForm.add(projectEndDate);

    final ListMultipleChoice projectMembers = new ListMultipleChoice("memberSelection",
        new PropertyModel(model, "memberSelection"), new PropertyModel(model, "projectMembers")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      /** Turns this choice box off when no members are present. */
      @Override
      public boolean isVisible() {
        return (!getChoices().isEmpty());
      }
    };
    projectMembers.setMaxRows(5);
    projectForm.add(projectMembers);

    SubmitLink deleteMemberLink = new SubmitLink("deleteMemberLink") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Delete selected member from projectMembers list. */
      @Override
      public void onSubmit() {
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        ProjectsModel model = session.getProjectsModel();
        model.removeMembers(model.getMemberSelection());
      }
    };
    projectForm.add(deleteMemberLink);

    TextArea projectMemberHelp = new TextArea("projectMemberHelp", new PropertyModel(model,
        "projectMemberHelp")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      /** Turns on when no project members. */
      @Override
      public boolean isVisible() {
        return (projectMembers.getChoices().isEmpty());
      }
    };
    projectMemberHelp.setEnabled(false);
    projectForm.add(projectMemberHelp);

    projectForm.add(new TextArea("projectInvitations", new PropertyModel(model,
        "projectInvitationsStr")));
    projectForm.add(new TextArea("projectSpectators", new PropertyModel(model,
        "projectSpectatorsStr")));

    SubmitLink addPropUriLink = new SubmitLink("addPropUriLink") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Add row */
      @Override
      public void onSubmit() {
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        ProjectsModel model = session.getProjectsModel();
        model.addPropUriRow();
      }
    };
    projectForm.add(addPropUriLink);

    final ListView projectTable = new ListView("propertyUriRowsTable", new PropertyModel(model,
        "propUriRowsView")) {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Add one row of table. */
      @Override
      public void populateItem(final ListItem item) {
        PropUriRowModel rowModel = (PropUriRowModel) item.getModelObject();
        item.add(new TextField("propertyLabel", new PropertyModel(rowModel, "propertyLabel")));
        item.add(new TextField("propertyValue", new PropertyModel(rowModel, "propertyValue")));
        item.add(new TextField("uriPattern1", new PropertyModel(rowModel, "uriPattern1")));
        item.add(new TextField("uriPattern2", new PropertyModel(rowModel, "uriPattern2")));
        item.add(new TextField("uriPattern3", new PropertyModel(rowModel, "uriPattern3")));
      }
    };
    projectForm.add(projectTable);

    Button saveButton = new Button("saveButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Save project. */
      @Override
      public void onSubmit() {
        ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
        ProjectsModel model = session.getProjectsModel();
        model.savePropUriRowsSave();

        if (model.isSemanticCheckOk()) {
          Project project = model.getProject();
          SensorBaseClient client = ProjectBrowserSession.get().getSensorBaseClient();

          try {
            client.putProject(project);
            session.getProjEditPanel().setVisible(false);
            session.getProjListPanel().setVisible(true);
          }
          catch (SensorBaseClientException e) {
            model.setFeedback(e.getMessage());
          }
        }
      }
    };
    projectForm.add(saveButton);

    Button cancelButton = new Button("cancelButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Cancel. */
      @Override
      public void onSubmit() {
        session.getProjEditPanel().setVisible(false);
        session.getProjListPanel().setVisible(true);
      }
    };
    projectForm.add(cancelButton);

    PopupWindowPanel popup = new PopupWindowPanel("helpPopup", "Project Entry Help");
    popup.getModalWindow().setContent(new ProjHelpPanel(popup.getModalWindow().getContentId()));
    projectForm.add(popup);

    Label feedbackLabel = new Label("saveFeedback", new PropertyModel(model, "feedback"));
    session.setFeedbackLabel(feedbackLabel);
    projectForm.add(feedbackLabel);

    add(projectForm);
  }
}
