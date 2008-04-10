package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

public class SensorDataPage extends ProjectBrowserBasePage {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  public SensorDataPage() {
    add(new Label("PageContents", "SensorData Page contents here."));
  }

}
