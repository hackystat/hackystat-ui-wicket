package org.hackystat.projectbrowser.page.trajectory.inputpanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryChartDefinition;

/**
 * Panel to show the description of the telemetrys.
w * 
 * @author Shaoxuan Zhang, Pavel Senin
 * 
 */
public class TrajectoryDescriptionPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * @param id the wicket component id.
   */
  public TrajectoryDescriptionPanel(String id) {
    super(id);
    TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();
    ListView descriptions = new ListView("descriptions", new PropertyModel(session,
        "chartDescriptions")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        TelemetryChartDefinition teleDef = (TelemetryChartDefinition) item.getModelObject();
        item.add(new Label("name", teleDef.getName()));
        item.add(new Label("description", teleDef.getDescription()));
      }
    };
    add(descriptions);
  }

}
