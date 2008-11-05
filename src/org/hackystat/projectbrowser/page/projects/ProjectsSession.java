package org.hackystat.projectbrowser.page.projects;

import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Session instance for the projects data page to hold its state.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * @author Randy Cox
 */
public class ProjectsSession implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** Holds the Project View model associated with this session. */
  private ProjectsModel projectsModel = new ProjectsModel();

  /** Main projects page. */
  private Page projPage;

  /** Holds list panel. */
  private Panel projListPanel;

  /** Holds edit panel. */
  private Panel projEditPanel;

  /** Holds rename panel. */
  private Panel projRenamePanel;

  /** Holds rename delete. */
  private Panel projDeletePanel;

  /** Holds leave panel. */
  private Panel projLeavePanel;

  /** Holds reply panel. */
  private Panel projReplyPanel;

  /** Holds clear cache panel. */
  private Panel projClearCachePanel;

  /** The feedback string. */
  private Label feedbackLabel;

  /** If project is new. */
  private Boolean newProject;

  /**
   * Check if current project is newly create or edited.
   * 
   * @return the newProject
   */
  public Boolean isNewProject() {
    return newProject;
  }

  /**
   * Set value of current project: newly create or edited.
   * 
   * @param newProject the newProject to set
   */
  public void setNewProject(Boolean newProject) {
    this.newProject = newProject;
  }

  /**
   * Sets the ProjectsModel for this page.
   * 
   * @param projectsModel The projectsModel.
   */
  public void setProjectsModel(ProjectsModel projectsModel) {
    this.projectsModel = projectsModel;
  }

  /**
   * Returns the current ProjectsModel for this page.
   * 
   * @return The ProjectsModel.
   */
  public ProjectsModel getProjectsModel() {
    return this.projectsModel;
  }

  /**
   * Get list panel.
   * 
   * @return the projListPanel
   */
  public Panel getProjListPanel() {
    return projListPanel;
  }

  /**
   * Set list panel.
   * 
   * @param projListPanel the projListPanel to set
   */
  public void setProjListPanel(Panel projListPanel) {
    this.projListPanel = projListPanel;
  }

  /**
   * Get edit panel.
   * 
   * @return the projEditPanel
   */
  public Panel getProjEditPanel() {
    return projEditPanel;
  }

  /**
   * Set edit panel.
   * 
   * @param projEditPanel the projEditPanel to set
   */
  public void setProjEditPanel(Panel projEditPanel) {
    this.projEditPanel = projEditPanel;
  }

  /**
   * Set clear cache panel.
   * 
   * @return the projClearCachePanel
   */
  public Panel getProjClearCachePanel() {
    return projClearCachePanel;
  }

  /**
   * Get clear cache panel.
   * 
   * @param projClearCachePanel the projClearCachePanel to set
   */
  public void setProjClearCachePanel(Panel projClearCachePanel) {
    this.projClearCachePanel = projClearCachePanel;
  }

  /**
   * Get feedback label.
   * 
   * @return the feedbackLabel
   */
  public Label getFeedbackLabel() {
    return feedbackLabel;
  }

  /**
   * Set feedback label.
   * 
   * @param feedbackLabel the feedbackLabel to set
   */
  public void setFeedbackLabel(Label feedbackLabel) {
    this.feedbackLabel = feedbackLabel;
  }

  /**
   * Get project page.
   * 
   * @return the projPage
   */
  public Page getProjPage() {
    return projPage;
  }

  /**
   * Set project page.
   * 
   * @param projPage the projPage to set
   */
  public void setProjPage(Page projPage) {
    this.projPage = projPage;
  }

  /**
   * Get rename panel.
   * 
   * @return the projRenamePanel
   */
  public Panel getProjRenamePanel() {
    return projRenamePanel;
  }

  /**
   * Set rename panel.
   * 
   * @param projRenamePanel the projRenamePanel to set
   */
  public void setProjRenamePanel(Panel projRenamePanel) {
    this.projRenamePanel = projRenamePanel;
  }

  /**
   * Get delete panel.
   * 
   * @return the projDeletePanel
   */
  public Panel getProjDeletePanel() {
    return projDeletePanel;
  }

  /**
   * Set delete panel.
   * 
   * @param projDeletePanel the projDeletePanel to set
   */
  public void setProjDeletePanel(Panel projDeletePanel) {
    this.projDeletePanel = projDeletePanel;
  }

  /**
   * Get leave panel.
   * 
   * @return the projLeavePanel
   */
  public Panel getProjLeavePanel() {
    return projLeavePanel;
  }

  /**
   * Set leave panel.
   * 
   * @param projLeavePanel the projLeavePanel to set
   */
  public void setProjLeavePanel(Panel projLeavePanel) {
    this.projLeavePanel = projLeavePanel;
  }

  /**
   * Get reply panel.
   * 
   * @return the projReplyPanel
   */
  public Panel getProjReplyPanel() {
    return projReplyPanel;
  }

  /**
   * Set reply panel.
   * 
   * @param projReplyPanel the projReplyPanel to set
   */
  public void setProjReplyPanel(Panel projReplyPanel) {
    this.projReplyPanel = projReplyPanel;
  }
}
