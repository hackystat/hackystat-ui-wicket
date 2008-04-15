package org.hackystat.projectbrowser.page.sensordata;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with information about SensorData. 
 * @author Philip Johnson
 */
public class SensorDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * Creates this page. 
   */
  public SensorDataPage() {
    add(new SensorDataForm("sensorDataForm"));
  }
}
