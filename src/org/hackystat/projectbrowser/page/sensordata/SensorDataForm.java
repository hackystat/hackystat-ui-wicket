package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;

/**
 * Provides the form that specifies a Date and Project name for the SensorData page.  
 * @author Philip Johnson
 */
public class SensorDataForm extends Form {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Date format used in date field input. */
  private static final String DATA_FORMAT = "yyyy-MM-dd";
  
  /** The page that instantiated this form. */
  private SensorDataPage page; 
  
  /**
   * Create this form, supplying the wicket:id and the page. 
   * 
   * @param id The wicket:id.
   * @param page The page using this form. 
   */
  public SensorDataForm(String id, SensorDataPage page) {
    super(id);
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    this.page = page; 
    // Create the date field.
    DateTextField dateTextField = 
      new DateTextField("dateTextField", new PropertyModel(session, "date"), DATA_FORMAT);
    dateTextField.add(new DatePicker());
    add(dateTextField);
    // Now create the drop-down menu for projects. 
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(session, "project"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer());
    add(projectMenu);
  }
  /**
   * Create the summary table associated with the user's project and date selection. 
   */
  @Override
  public void onSubmit() {
    page.onProjectDateSubmit();
  }

}
