package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with Telemetry analyses. 
 * @author Philip Johnson
 */
public class TelemetryPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs the telemetry page. 
   */
  public TelemetryPage() {
    add(new Label("PageContents", "Telemetry Page contents here."));
  }
}
