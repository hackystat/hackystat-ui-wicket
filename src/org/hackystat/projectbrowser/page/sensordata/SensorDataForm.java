package org.hackystat.projectbrowser.page.sensordata;

import java.util.Date;

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * Provides the form that specifies a Date and Project name for the SensorData page.  
 * @author Philip Johnson
 */
public class SensorDataForm extends Form {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The date. */
  private long utcTime = new Date().getTime();
  
  /** The project name. */
  private String projectName = "Default";
  
  
  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   */
  public SensorDataForm(final String id) {
    super(id);
    setModel(new CompoundPropertyModel(this));
    // Create the date field.
    DateTextField dateTextField = new DateTextField("dateTextField", new PropertyModel(this,
    "date"), new StyleDateConverter("S-", true));
    dateTextField.add(new DatePicker());
    add(dateTextField);
    // Need to add the datepicker thingy.
    
    // Now create the drop-down menu for projects. 
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(this, "projectName"),
          new PropertyModel(ProjectBrowserSession.get(), "projectNames"));
    add(projectMenu);
  }
  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
    // Not implemented yet. 
  }
  
  /**
   * Set the date displayed on this form.  
   * @param date The date. 
   */
  public void setDate(Date date) {
    this.utcTime = date.getTime();
  }
  
  /**
   * Return the date set on this form. 
   * @return  The date associated with this form. 
   */
  public Date getDate() {
    return new Date(this.utcTime);
  }
  
  /**
   * Returns the project name associated with this form. 
   * @return The project name for this form.
   */
  public String getProjectName() {
    return this.projectName;
  }
  
  /**
   * Gets the project name associated with this form. 
   * @param projectName The project name. 
   */
  public void setProjectName(String projectName) {
    this.projectName = projectName;
    
  }

}
