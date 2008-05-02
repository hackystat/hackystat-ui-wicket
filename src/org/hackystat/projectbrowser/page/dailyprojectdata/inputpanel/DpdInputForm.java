package org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.DateValidator;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.contextsensitive.ContextSensitivePanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.projectbrowser.page.validator.ProjectDateValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

/**
 * Provides the form for use in specifying DPD analyses.  This form allows the user to select
 * a data and a set of Projects and an analysis.  Depending upon the analysis that is selected,
 * one or more form fields could be displayed. 
 * @author Philip Johnson
 */
public class DpdInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;

  /** Date format used in date field input. */
  public static final String DATA_FORMAT = "yyyy-MM-dd";

  /** The page containing this form. */
  ProjectBrowserBasePage page = null;
  
  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param page the page this page is attached to.
   */
  public DpdInputForm(final String id, ProjectBrowserBasePage page) {
    super(id);
    this.page = page;
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();

    // [1] Create the Date field, always visible, always required.
    DateTextField dateTextField = 
      new DateTextField("dateTextField", new PropertyModel(session, "date"), DATA_FORMAT);
    dateTextField.add(new DatePicker());
    dateTextField.add(DateValidator.maximum(new Date()));
    dateTextField.setRequired(true);
    add(dateTextField);
    
    // [2] Create the multi-choice menu for projects, always visible, always required.
    ListMultipleChoice projectMenu = 
      new ListMultipleChoice ("projectMenu", 
          new PropertyModel(session, "selectedProjects"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer())  ;
    projectMenu.setRequired(true);
    add(projectMenu);
    
    // Add a validator that makes sure the date is within the project interval.
    add(new ProjectDateValidator(projectMenu, dateTextField));
    //set Project to Default if null
    if (session.getSelectedProjects().size() <= 0) {
      session.getSelectedProjects().add(ProjectBrowserSession.get().getDefaultProject());
    }
    
    // [3] Create the drop-down list for Analyses, always visible, always required.
    DropDownChoice analysisMenu = 
      new DropDownChoice ("analysisMenu", 
          new PropertyModel(session, "analysis"),
          new PropertyModel(session, "analysisList"));
    analysisMenu.setRequired(true);
    add(analysisMenu);
    
    // [4] Create the ContextSensitivePanel containing analysis-specific menus.
    ContextSensitivePanel csPanel = new ContextSensitivePanel("dpdContextSensitivePanel", 
        session.getContextSensitiveMenus());
    session.setContextSensitivePanel(csPanel);
    add(csPanel);
    
    // [5] Add the Ajax behavior to the analysis menu that will make the appropriate
    // context-sensitive menus visible depending upon the selected analysis.
    analysisMenu.add(new AjaxFormComponentUpdatingBehavior("onchange") {
      /** Support serialization. */ 
      public static final long serialVersionUID = 1L;
      
      /**
       * When the user changes the Analysis menu selection, we want to set the visibility of
       * all context sensitive menus appropriately.
       */
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        String values = "Values";
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        // Make the appropriate context-sensitive menus visible, depending upon the analysis.
        if ("Coverage".equals(session.getAnalysis())) {
          session.getContextSensitivePanel().setVisible(target, values, "Coverage Type");
        }
        else if ("Coupling".equals(session.getAnalysis())) {
          session.getContextSensitivePanel().setVisible(target, values, "Coupling Type");
        }
        else if ("Complexity".equals(session.getAnalysis())) {
          session.getContextSensitivePanel().setVisible(target, values);
        }
        else if ("UnitTest".equals(session.getAnalysis())) {
          session.getContextSensitivePanel().setVisible(target, values);
        }
        else if ("Build".equals(session.getAnalysis())) {
          session.getContextSensitivePanel().setVisible(target, values);
        }
        else {
          // Make all context-sensitive menus not visible.
          session.getContextSensitivePanel().setVisible(target);
        }
      } 
      } 
    );
  }
  
  
  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
    page.onProjectDateSubmit();
  }
}
