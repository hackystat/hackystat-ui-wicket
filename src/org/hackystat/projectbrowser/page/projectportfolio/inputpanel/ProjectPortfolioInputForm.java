package org.hackystat.projectbrowser.page.projectportfolio.inputpanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioPage;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioSession;

/**
 * Input panel for Project Portfolio.
 * Including start/end date, project, granularity.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** The page containing this form. */
  private final ProjectPortfolioPage page;
  /** ProjectPortfolioSession that holds page state for Project Portfolio. */
  private ProjectPortfolioSession session = 
    ProjectBrowserSession.get().getProjectPortfolioSession();

  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param p the page this form is attached to.
   */
  public ProjectPortfolioInputForm(String id, ProjectPortfolioPage p) {
    super(id);
    this.page = p;

    //set Project to Default if null
    if (session.getSelectedProjects().isEmpty()) {
      session.getSelectedProjects().add(ProjectBrowserSession.get().getDefaultProject());
    }
    // Now create the drop-down menu for projects. 
    ListMultipleChoice projectMenu = 
      new ListMultipleChoice ("projectMenu", 
          new PropertyModel(session, "selectedProjects"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    projectMenu.setRequired(true);
    add(projectMenu);

    Link configurationLink = new Link("configuration") {
      /** Support serialization. */
      private static final long serialVersionUID = 0L;
      @Override
      public void onClick() {
        page.setConfigurationPanelVisible(!page.isConfigurationPanelVisible());
      }
    };
    configurationLink.add(new Label("label", new PropertyModel(this, "labelMessage")));
    add(configurationLink);
  }

  /**
   * Actions that take place when this form is submit.
   */
  @Override
  public void onSubmit() {
    session.updateDataModel();
    page.onProjectDateSubmit();
  }
  
  /**
   * @return the configuration label message
   */
  public String getLabelMessage() {
    String labelMessage;
    if (page.isConfigurationPanelVisible()) {
      labelMessage = "Hide Configuration";
    }
    else {
      labelMessage = "Show Configuration";
    }
    return labelMessage;
  }
  
  /**
   * @return true if the form is enabled.
   */
  protected boolean getIsEnable() {
    return true;
  }
  
}
