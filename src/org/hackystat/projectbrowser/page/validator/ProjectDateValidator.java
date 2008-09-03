package org.hackystat.projectbrowser.page.validator;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.hackystat.sensorbase.resource.projects.ProjectUtils;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a project date validator. This validator will work with forms that have 
 * a multiple selection menu for Projects, plus either one or two date fields.
 * It provides two constructors: one for forms with a Project menu and one date, 
 * and one for forms with a Project menu and two dates.  
 * @author Philip Johnson
 */
public class ProjectDateValidator extends AbstractFormValidator {

  /** For serialization. */
  private static final long serialVersionUID = 1L;

  /** 
   * Form components to be checked. The first must be the projectMenu (a ListMultipleChoice),
   * the second must be a date (a DateTextField), and the third (if present) is another
   * DateTextField.  This enables the validator to be used with either forms with a single date
   * (such as DPDs) as well as forms with a start and end date (such as Telmetry). 
   */
  private final FormComponent[] components;

  /**
   * Takes a Project menu and a single Date field.
   * @param projectMenu The project menu component. 
   * @param dateField The Date field component.
   */
  public ProjectDateValidator(FormComponent projectMenu, FormComponent dateField) {
    if (projectMenu == null) {
      throw new IllegalArgumentException("projectMenu cannot be null");
    }
    if (dateField == null) {
      throw new IllegalArgumentException("dateField cannot be null");
    }
    if (!(projectMenu instanceof ListMultipleChoice)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a ListMultipleChoice");
    }
    if (!(dateField instanceof DateTextField)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a DateTextField");
    }
    components = new FormComponent[] { projectMenu, dateField };
  }
  
  /**
   * Takes a Project menu (ListMultipleChoice) and two Date fields (DateTextField).
   * @param projectMenu The project menu component. 
   * @param startDateField The Date field component.
   * @param endDateField The Date field component.
   */
  public ProjectDateValidator(FormComponent projectMenu, FormComponent startDateField, 
      FormComponent endDateField) {
    if (projectMenu == null) {
      throw new IllegalArgumentException("projectMenu cannot be null");
    }
    if (startDateField == null) {
      throw new IllegalArgumentException("startDateField cannot be null");
    }
    if (!(projectMenu instanceof ListMultipleChoice)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a ListMultipleChoice");
    }
    if (!(startDateField instanceof DateTextField)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a DateTextField");
    }
    if (!(endDateField instanceof DateTextField)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a DateTextField");
    }
    components = new FormComponent[] { projectMenu, startDateField, endDateField };
  }

  /**
   * Returns the form components.
   * @return The form components. 
   */
  public FormComponent[] getDependentFormComponents() {
    return components.clone();
  }

  /**
   * Performs the validation. 
   * Note that this validation must handle a projectMenu plus a single date, or a 
   * projectMenu plus two dates (start and end date). 
   * This method is ran if all components returned by getDependentFormComponents() are valid.
   * @param projectDateForm The form to validate. 
   */
  @SuppressWarnings("unchecked")
  public void validate(Form projectDateForm) {
    ListMultipleChoice projectMenu = (ListMultipleChoice)components[0]; 
    DateTextField startDateField = (DateTextField)components[1];
    DateTextField endDateField = null;
    if (components.length == 3) {
      endDateField = (DateTextField)components[2];
    }
    List<Project> projects = (List<Project>)projectMenu.getConvertedInput();
    Date date1 = (Date)startDateField.getConvertedInput();
    XMLGregorianCalendar tomorrow = Tstamp.incrementDays(Tstamp.makeTimestamp(), 1);
    Date date2 = null;
    if (endDateField != null) {
      date2 = (Date)endDateField.getConvertedInput();
    }
    
    for (Project project : projects) {
      XMLGregorianCalendar startTime = Tstamp.makeTimestamp(date1.getTime());
      if (!ProjectUtils.isValidStartTime(project, startTime)) { //NOPMD
        error(startDateField, "DateBeforeProjectStartTime");
      }
      else if (!ProjectUtils.isValidEndTime(project, startTime)) { //NOPMD
        error(startDateField, "DateAfterProjectEndTime");
      }
      else if (Tstamp.greaterThan(startTime, tomorrow)) {
        error(startDateField, "DateInFuture");
      }
      // Only check the end date if this form actually contained an end date. 
      if (date2 != null) { 
        XMLGregorianCalendar endTime = Tstamp.makeTimestamp(date2.getTime());
        if (!ProjectUtils.isValidStartTime(project, endTime)) { //NOPMD
          error(endDateField, "DateBeforeProjectStartTime");
        }
        else if (!ProjectUtils.isValidEndTime(project, endTime)) { //NOPMD
          error(endDateField, "DateAfterProjectEndTime");
        }
        else if (Tstamp.greaterThan(endTime, tomorrow)) {
          error(endDateField, "DateInFuture");
        }
      }
    }
  }
}
