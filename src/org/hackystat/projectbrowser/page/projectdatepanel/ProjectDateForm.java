package org.hackystat.projectbrowser.page.projectdatepanel;

import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides the form that specifies a Date and Project name for the SensorData page.  
 * @author Philip Johnson
 */
public class ProjectDateForm extends Form {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** Date format used in date field input. */
  public static final String DATA_FORMAT = "yyyy-MM-dd";
  
  /** The project name. */
  private String projectNameId = null;

  /** The page containing this form. */
  ProjectBrowserBasePage page = null;
  
  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param page 
   */
  public ProjectDateForm(final String id, ProjectBrowserBasePage page) {
    super(id);
    setModel(new CompoundPropertyModel(this));
    // Create the date field.
    if (page.getDate() == null) {
      page.setDate(getDateToday());
    }
    DateTextField dateTextField = new DateTextField("dateTextField", 
        new PropertyModel(page, "date"), DATA_FORMAT);
    dateTextField.add(new DatePicker());
    dateTextField.setRequired(true);
    add(dateTextField);
    // Need to add the datepicker thingy.
    
    // Now create the drop-down menu for projects. 
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(this, "projectNameId"),
          new PropertyModel(ProjectBrowserSession.get(), "projectNames"));
    projectMenu.setRequired(true);
    add(projectMenu);
  }
  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
    ProjectBrowserSession.get().getProjectByNameId(this.projectNameId);
    page.onProjectDateSubmit();
  }

  /**
   * make a Date that represent today, at 0:00:00.
   * @return the Date object.
   */
  public final Date getDateToday() {
    XMLGregorianCalendar time = Tstamp.makeTimestamp();
    time.setTime(0, 0, 0);
    return time.toGregorianCalendar().getTime();
  }
}
