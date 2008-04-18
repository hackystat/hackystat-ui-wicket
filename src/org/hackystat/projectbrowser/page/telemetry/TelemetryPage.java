package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.telemetry.inputpanel.TelemetryInputPanel;

/**
 * Provides a page with Telemetry analyses. 
 * @author Philip Johnson
 */
public class TelemetryPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Telemetry session to hold up the state. */
  TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
  
  /**
   * Constructs the telemetry page. 
   */
  public TelemetryPage() {
    add(new TelemetryInputPanel("inputPanel", this));
    add(new TelemetryDataPanel("dataPanel"));
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
  }
  
  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    this.replace(new TelemetryDataPanel("dataPanel"));
  }
}
