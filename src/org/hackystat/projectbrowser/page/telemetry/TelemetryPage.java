package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

public class TelemetryPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  public TelemetryPage() {
    add(new Label("PageContents", "Telemetry Page contents here."));
  }
}
