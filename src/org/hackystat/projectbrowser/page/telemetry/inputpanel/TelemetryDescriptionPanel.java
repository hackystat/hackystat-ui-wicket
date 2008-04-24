package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartDefinition;

/**
 * Panel to show the description of the telemetrys.
 * @author Shaoxuan Zhang
 *
 */
public class TelemetryDescriptionPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * @param id the wicket component id.
   */
  public TelemetryDescriptionPanel(String id) {
    super(id);
    TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
    ListView descriptions = 
      new ListView("descriptions", new PropertyModel(session, "chartDescriptions")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        TelemetryChartDefinition teleDef = (TelemetryChartDefinition)item.getModelObject();
        item.add(new Label("name", teleDef.getName()));
        item.add(new Label("description", teleDef.getDescription()));
      }
    };
    add(descriptions);
  }

}
