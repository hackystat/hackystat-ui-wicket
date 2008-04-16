package org.hackystat.projectbrowser.page.projectdatepanel;

import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
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
    this.page = page;
    //setModel(new CompoundPropertyModel(this));
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
    ChoiceRenderer renderer = new ChoiceRenderer() {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      public Object getDisplayValue(Object object) {
        int duplicateCount = 0;
        Project targetProject = (Project) object;
        for (Project project : ProjectBrowserSession.get().getProjectList()) {
          if (targetProject.getName().equals(project.getName())) {
            duplicateCount++;
          }
        }
        String view;
        if (duplicateCount > 1) {
          view = targetProject.getName() + " - " + targetProject.getOwner();
        }
        else {
          view = targetProject.getName();
        }
        return view;
      }
    };
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(page, "project"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          renderer) ;
    projectMenu.setRequired(true);
    add(projectMenu);
  }
  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
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
