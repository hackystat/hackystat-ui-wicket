package org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;

/**
 * Provides the form that specifies a Date and Project name for the SensorData page.  
 * @author Philip Johnson
 */
public class ProjectDateForm extends Form {

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
  public ProjectDateForm(final String id, ProjectBrowserBasePage page) {
    super(id);
    this.page = page;
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    //set Project to Default if null
    if (session.getProject() == null) {
      session.setProject(ProjectBrowserSession.get().getDefaultProject());
    }
    //setModel(new CompoundPropertyModel(this));
    DateTextField dateTextField = 
      new DateTextField("dateTextField", new PropertyModel(session, "date"), DATA_FORMAT);
    dateTextField.add(new DatePicker());
    dateTextField.setRequired(true);
    add(dateTextField);
    // Need to add the datepicker thingy.
    
    // Now create the drop-down menu for projects. 
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(session, "project"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer()) ;
    projectMenu.setRequired(true);
    add(projectMenu);

    // Now create the drop-down menu for analysis. 
    DropDownChoice analysisMenu = 
      new DropDownChoice ("analysisMenu", 
          new PropertyModel(session, "analysis"),
          new PropertyModel(session, "analysisList"));
    analysisMenu.setRequired(true);
    add(analysisMenu);
  }
  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
    page.onProjectDateSubmit();
  }
}
