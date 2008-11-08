package org.hackystat.projectbrowser.page.trajectory.validator;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Provides a telemetry validator. Since we support the only metrics so far, it's the easiest way to
 * report errors.
 * 
 * @author Pavel Senin.
 */
public class TelemetrySelectionValidator extends AbstractFormValidator {

  /** For serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Form components to be checked. The first must be the projectMenu (a ListMultipleChoice), the
   * second must be a date (a DateTextField), and the third (if present) is another DateTextField.
   * This enables the validator to be used with either forms with a single date (such as DPDs) as
   * well as forms with a start and end date (such as Telemetry).
   */
  private final FormComponent[] components;

  /**
   * Takes a Project menu and a single Date field.
   * 
   * @param telemetrySelection foo.
   */
  public TelemetrySelectionValidator(FormComponent telemetrySelection) {
    if (telemetrySelection == null) {
      throw new IllegalArgumentException("projectMenu cannot be null");
    }
    if (!(telemetrySelection instanceof DropDownChoice)) {
      throw new IllegalArgumentException("ProjectDateValidator not given a ListMultipleChoice");
    }
    components = new FormComponent[] { telemetrySelection };
  }

  /**
   * Returns the form components.
   * 
   * @return The form components.
   */
  public FormComponent[] getDependentFormComponents() {
    return components.clone();
  }

  /**
   * Performs the validation. Note that this validation must handle a projectMenu plus a single
   * date, or a projectMenu plus two dates (start and end date).
   * 
   * @param trajectorySelectionForm foo.
   */
  public void validate(Form trajectorySelectionForm) {
    if (components[0] instanceof DropDownChoice) {
      DropDownChoice telemetryMenu = (DropDownChoice) components[0];
      String telemetrySelection = (String) telemetryMenu.getConvertedInput();
      if (!telemetrySelection.equalsIgnoreCase("Build")) { // NOPMD
        error(telemetryMenu, "UnsupportedTelemetry");
      }
    }
  }
}
