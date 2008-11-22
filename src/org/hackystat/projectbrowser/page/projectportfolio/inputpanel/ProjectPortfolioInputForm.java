package org.hackystat.projectbrowser.page.projectportfolio.inputpanel;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
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
    CheckBoxMultipleChoice projectMenu = 
      new CheckBoxMultipleChoice ("projectMenu", 
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

    //StartDateTextField
    DateTextField startDateTextField = 
      new DateTextField("startDateTextField", new PropertyModel(session, "startDate"), 
          ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    startDateTextField.add(new DatePicker());
    startDateTextField.setRequired(true);
    add(startDateTextField);
    
    //EndDateTextField
    DateTextField endDateTextField = 
      new DateTextField("endDateTextField", new PropertyModel(session, "endDate"), 
          ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    endDateTextField.add(new DatePicker());
    endDateTextField.setRequired(true);
    add(endDateTextField);
    
    //granularity
    add(new DropDownChoice("granularity", 
                           new PropertyModel(session, "granularity"), 
                           session.getGranularities()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    });
    
    Button configurationButton = 
      new Button("configuration", new PropertyModel(this, "labelMessage")) {
      /** Support serialization. */
      private static final long serialVersionUID = 0L;
      @Override
      public void onSubmit() {
        page.setConfigurationPanelVisible(!page.isConfigurationPanelVisible());
      }
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    //configurationButton.add(new Label("label", new PropertyModel(this, "labelMessage")));
    add(configurationButton);

    Button submitButton = new Button("submit") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        session.getDataModel().saveUserConfiguration();
        session.updateDataModel();
        page.onProjectDateSubmit();
      }
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    add(submitButton);
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
    return !session.getDataModel().isInProcess();
  }
  
}
