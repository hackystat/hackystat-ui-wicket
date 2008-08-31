/**
 * 
 */
package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * First page in projects area that list all projects.
 * 
 * @author Randy Cox
 */
public class ProjListPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for this class.
   * 
   * @param id the wicket id.
   */
  public ProjListPanel(String id) {
    super(id);

    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    final ProjectsModel model = session.getProjectsModel();

    Form projectForm = new Form("projListForm");

    Button newButton = new Button("newButton") {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Create empty project in edit form for data entry. */
      @Override
      public void onSubmit() {
        model.createProject();
        model.setFeedback("");
        session.setNewProject(true);
        session.getProjListPanel().setVisible(false);
        session.getProjEditPanel().setVisible(true);
      }
    };
    projectForm.add(newButton);

    /**
     * Action button attached to the project in the current row in list.
     * @author Randy Cox
     */
    final class EditButton extends Button {
      /** For serialization. */
      private static final long serialVersionUID = 1L;
      /** Stores project attached to this button. */
      private Project project;

      /**
       * Constructor.
       * 
       * @param id wicket id
       * @param project to attache to this button
       */
      public EditButton(String id, Project project) {
        super(id);
        this.project = project;
      }

      /** Edit the project with edit form. */
      @Override
      public void onSubmit() {
        model.setProject(this.project);
        model.loadPropUriRowsView();
        model.setFeedback("");
        session.setNewProject(false);
        session.getProjListPanel().setVisible(false);
        session.getProjEditPanel().setVisible(true);
      }

      /** 
       * Turn on only when project has allows the action of this button.
       * @return true if button is turned on. 
       * */
      @Override
      public boolean isVisible() {
        ProjectsModel model = new ProjectsModel(this.project);
        return model.isEditable();
      }
    }
    ;

    /**
     * Action button attached to the project in the current row in list.
     * @author Randy Cox
     */
    final class RenameButton extends Button {
      /** For serialization. */
      private static final long serialVersionUID = 1L;
      /** Project to rename. */
      private Project project;

      /**
       * Constructor.
       * 
       * @param id wicket id
       * @param project to rename.
       */
      public RenameButton(String id, Project project) {
        super(id);
        this.project = project;
      }

      /** Rename project. */
      @Override
      public void onSubmit() {
        model.setProject(this.project);
        model.setFeedback("");
        session.getProjListPanel().setVisible(false);
        session.getProjRenamePanel().setVisible(true);
      }

      /** 
       * Turn on only when project has allows the action of this button.
       * @return true if button is turned on. 
       * */
      @Override
      public boolean isVisible() {
        ProjectsModel model = new ProjectsModel(this.project);
        return model.isRenameable();
      }
    }
    ;

    /**
     * Action button attached to the project in the current row in list.
     * @author Randy Cox
     */
   final class DeleteButton extends Button {
      /** For serialization. */
      private static final long serialVersionUID = 1L;
      /** Project to delete. */
      private Project project;

      /**
       * Constructor.
       * 
       * @param id wicket id
       * @param project to delete
       */
      public DeleteButton(String id, Project project) {
        super(id);
        this.project = project;
      }

      /** Delete project. */
      @Override
      public void onSubmit() {
        model.setProject(this.project);
        model.setFeedback("");
        session.getProjListPanel().setVisible(false);
        session.getProjDeletePanel().setVisible(true);
      }

      /** 
       * Turn on only when project has allows the action of this button.
       * @return true if button is turned on. 
       * */
      @Override
      public boolean isVisible() {
        ProjectsModel model = new ProjectsModel(this.project);
        return model.isDeletable();
      }
    }
    ;

    /**
     * Action button attached to the project in the current row in list.
     * @author Randy Cox
     */
    final class LeaveButton extends Button {
      /** For serialization. */
      private static final long serialVersionUID = 1L;
      /** Project to leave. */
      private Project project;

      /**
       * Constructor.
       * 
       * @param id wicket id
       * @param project to leave.
       */
      public LeaveButton(String id, Project project) {
        super(id);
        this.project = project;
      }

      /** Leave project. */
      @Override
      public void onSubmit() {
        model.setProject(this.project);
        model.setFeedback("");
        session.getProjListPanel().setVisible(false);
        session.getProjLeavePanel().setVisible(true);
      }

      /** 
       * Turn on only when project has allows the action of this button.
       * @return true if button is turned on. 
       * */
      @Override
      public boolean isVisible() {
        ProjectsModel model = new ProjectsModel(this.project);
        return model.isLeavable();
      }
    }
    ;

    /**
     * Action button attached to the project in the current row in list.
     * @author Randy Cox
     */
   final class ReplyButton extends Button {
      /** For serialization. */
      private static final long serialVersionUID = 1L;
      /** Project to reply about. */
      private Project project;

      /**
       * Constructor.
       * 
       * @param id wicket id
       * @param project to reply about.
       */
      public ReplyButton(String id, Project project) {
        super(id);
        this.project = project;
      }

      /** Bring up reply panel to reply to an invitation. */
      @Override
      public void onSubmit() {
        model.setProject(this.project);
        model.setFeedback("");
        session.getProjListPanel().setVisible(false);
        session.getProjReplyPanel().setVisible(true);
      }

      /** 
       * Turn on only when project has allows the action of this button.
       * @return true if button is turned on. 
       * */
      @Override
      public boolean isVisible() {
        ProjectsModel model = new ProjectsModel(this.project);
        return model.isRepliable();
      }
    }
    ;

    ListView projectTable = new ListView("projectTable", new PropertyModel(model, "projects")) {
      /** For serialization. */
      private static final long serialVersionUID = 1L;

      /** Add line of project info to table. */
      @Override
      public void populateItem(final ListItem item) {
        final ProjectsModel model = new ProjectsModel((Project) item.getModelObject());
        item.add(new EditButton("editButton", model.getProject()));
        item.add(new RenameButton("renameButton", model.getProject()));
        item.add(new DeleteButton("deleteButton", model.getProject()));
        item.add(new LeaveButton("leaveButton", model.getProject()));
        item.add(new ReplyButton("replyButton", model.getProject()));
        item.add(new Label("projectName", new PropertyModel(model, "projectName")));

        Label projectOwner = new Label("projectOwner", new PropertyModel(model, 
          "projectOwnerBold"));
        projectOwner.setEscapeModelStrings(false);
        item.add(projectOwner);

        item.add(new Label("projectDesc", new PropertyModel(model, "projectDesc")));
        item.add(new MultiLineLabel("projectSpan", new PropertyModel(model, "projectSpan")));

        String maxHeightStr = ((ProjectBrowserApplication) ProjectBrowserApplication.get())
          .getProjectBrowserProperty(ProjectBrowserProperties.PROJECTS_TEXTMAXHEIGHT_KEY);
        final int maxHeight = Integer.parseInt(maxHeightStr);

        ListMultipleChoice projectMembers = new ListMultipleChoice("projectMembers",
            new PropertyModel(model, "projectConsolidatedMembers")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          /** Turns this choice box off when no members are present. */
          @Override
          public boolean isVisible() {
            return ((model.getProjectConsolidatedMembers().size() > maxHeight));
          }
        };
        projectMembers.setEnabled(false);
        projectMembers.setMaxRows(maxHeight);
        projectMembers.setEscapeModelStrings(false);
        item.add(projectMembers);
        
        MultiLineLabel projectMembersLabel = new MultiLineLabel("projectMembersLabel", 
            new PropertyModel(model, "projectConsolidatedMembersStr")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          /** Turns this choice box off when no members are present. */
          @Override
          public boolean isVisible() {
            return ((model.getProjectConsolidatedMembers().size() <= maxHeight));
          }
        };
        projectMembersLabel.setEscapeModelStrings(false);
        item.add(projectMembersLabel);

        item.add(new MultiLineLabel("projectUriPatterns", new PropertyModel(model,
            "projectUriPatternsStr")));
        item.add(new MultiLineLabel("projectProperties", new PropertyModel(model,
            "projectPropertiesStr")));
      }
    };
    projectForm.add(projectTable);
    add(projectForm);
  }
};
