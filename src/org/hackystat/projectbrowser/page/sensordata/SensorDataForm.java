package org.hackystat.projectbrowser.page.sensordata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;

/**
 * Provides the form that specifies a Month and Project name for the SensorData page.  
 * @author Philip Johnson
 */
public class SensorDataForm extends Form {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
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
    
    // The drop-down menu for months.
    DropDownChoice monthMenu = 
      new DropDownChoice("monthMenu", new PropertyModel(session, "month"), getMonths(), 
          new IChoiceRenderer() {
            /** Support serialization. */
            private static final long serialVersionUID = 1L;

            /**
             * Convert the passed integer to its month name.
             * @param object the month as an int.
             */
            @Override
            public Object getDisplayValue(Object object) {
              SimpleDateFormat format = new SimpleDateFormat("MMM", Locale.US);
              Calendar cal = Calendar.getInstance();
              cal.set(Calendar.DAY_OF_MONTH, 1);
              cal.set(Calendar.MONTH, ((Integer) object).intValue());
              return format.format(cal.getTime());
            }

            /** Required for IChoiceRenderer interface. */
            @Override
            public String getIdValue(Object arg0, int index) {
              return String.valueOf(index);
            }
          });
    add(monthMenu);
    
    // Create the drop-down menu for years. 
    DropDownChoice yearMenu = 
      new DropDownChoice("yearMenu", new PropertyModel(session, "year"), getYears());
    add(yearMenu);
    
    // Create the drop-down menu for projects. 
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
  
  /**
   * Return a list of years, which is a list of ten years where the current year is year 3.
   * 
   * @return The list of years.
   */
  private List<Integer> getYears() {
    List<Integer> years = new ArrayList<Integer>(5);
    Calendar cal = Calendar.getInstance();
    for (int i = cal.get(Calendar.YEAR) - 5; i <= cal.get(Calendar.YEAR); i++) {
      years.add(Integer.valueOf(i));
    }
    return years;
  }
  
  /**
   * Return a list of months, which is simply a list of ints from 0 to 11.
   * 
   * @return The list of months as integers.
   */
  private List<Integer> getMonths() {
    List<Integer> months = new ArrayList<Integer>(12);
    for (int i = 0; i < 12; i++) {
      months.add(Integer.valueOf(i));
    }
    return months;
  }

}
