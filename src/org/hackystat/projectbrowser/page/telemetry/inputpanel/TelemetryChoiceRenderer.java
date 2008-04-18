package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartDefinition;

/**
 * Choice Renderer for Telemetry choice list.
 * @author Shaoxuan Zhang
 */
public class TelemetryChoiceRenderer extends ChoiceRenderer {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** 
   * return the display value that present the object. 
   * @param object the bject to display.
   * @return the display value.
   */
  public Object getDisplayValue(Object object) {
    return ((TelemetryChartDefinition)object).getName();
  }
}
