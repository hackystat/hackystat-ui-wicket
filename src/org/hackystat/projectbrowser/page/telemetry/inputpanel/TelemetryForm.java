package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;

/**
 * Form for user to select telemetry chart.
 * @author Shaoxuan Zhang
 */
public class TelemetryForm extends Form {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that holds page state for telemetry. */
  TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
  
  /**
   * @param id the wicket component id.
   */
  public TelemetryForm(String id) {
    super(id);
    // Create the drop-down menu for telemetry. 
    DropDownChoice telemetryMenu = 
      new DropDownChoice ("telemetryMenu", new PropertyModel(session, "telemetryName"),
          new PropertyModel(session, "telemetryList"));
    telemetryMenu.setRequired(true);
    this.add(telemetryMenu);
  }

  /**
   * invoked when this form is submitted.
   */
  @Override
  public void onSubmit() {
    super.onSubmit();
    session.getParameters().clear();
  }
}
