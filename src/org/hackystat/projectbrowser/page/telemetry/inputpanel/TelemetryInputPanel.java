package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Panel to let user select the project and telemetry to display.
 * @author Shaoxuan Zhang
 */
public class TelemetryInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public TelemetryInputPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    add(new FeedbackPanel("feedback"));
    add(new TelemetryForm("telemetryForm"));
    add(new ProjectParameterForm("inputForm", page));
  }

}
