package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.markup.html.form.Form;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * Provide form with Project management functions.
 * 
 * @author Philip Johnson
 * @author Randy Cox
 */
public class ProjectsForm extends Form {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The page containing this form. */
  ProjectsPage page = null;

  /**
   * The constructor for this page.
   * 
   * @param id Form identification
   * @param page Page this form is imbedded in
   */
  public ProjectsForm(final String id, final ProjectsPage page) {
    super(id);
    this.page = page;

    /** Get session and model object for future use. */
    final ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();

    final ProjListPanel listPanel = new ProjListPanel("projListPanel");
    listPanel.setVisible(true);
    add(listPanel);
    session.setProjListPanel(listPanel);

    final ProjEditPanel editPanel = new ProjEditPanel("projEditPanel");
    editPanel.setVisible(false);
    add(editPanel);
    session.setProjEditPanel(editPanel);

    final ProjRenamePanel renamePanel = new ProjRenamePanel("projRenamePanel");
    renamePanel.setVisible(false);
    add(renamePanel);
    session.setProjRenamePanel(renamePanel);

    final ProjDeletePanel deletePanel = new ProjDeletePanel("projDeletePanel");
    deletePanel.setVisible(false);
    add(deletePanel);
    session.setProjDeletePanel(deletePanel);

    final ProjLeavePanel leavePanel = new ProjLeavePanel("projLeavePanel");
    leavePanel.setVisible(false);
    add(leavePanel);
    session.setProjLeavePanel(leavePanel);

    final ProjReplyPanel replyPanel = new ProjReplyPanel("projReplyPanel");
    replyPanel.setVisible(false);
    add(replyPanel);
    session.setProjReplyPanel(replyPanel);

    final ProjClearCachePanel clearCachePanel = new ProjClearCachePanel("projClearCachePanel");
    clearCachePanel.setVisible(false);
    add(clearCachePanel);
    session.setProjClearCachePanel(clearCachePanel);
  }
}