package org.hackystat.projectbrowser.page.telemetry;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
import org.hackystat.utilities.tstamp.Tstamp;
/**
 * Panel for showing telemetry content.
 * @author Shaoxuan Zhang
 *
 */
public class TelemetryDataPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that hold the page state.*/
  TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();

  /**
   * @param id the wicket component id.
   */
  public TelemetryDataPanel(String id) {
    super(id);
    //display project information
    if (session.getProject() == null) {
      add(new Label("projectName", ""));
      add(new Label("projectOwner", ""));
    }
    else {
      add(new Label("projectName", session.getProject().getName()));
      add(new Label("projectOwner", session.getProject().getOwner()));
    }
    add(new Label("startDate", new PropertyModel(session, "startDateString")));
    add(new Label("endDate", new PropertyModel(session, "endDateString")));
    add(new Label("telemetryName", new PropertyModel(session, "telemetryName")));
    WebComponent chartUrl = new WebComponent("chartUrl");
    chartUrl.add(new AttributeModifier("src", true, new PropertyModel(session, "chartUrl")));
    add(chartUrl);
  }
  
  /**
   * Return the Telemetry Chart Data.
   * @return a List of TelemetryStreams.
   */
  protected List<TelemetryStream> getTelemetryChart() {
    TelemetryClient client = ProjectBrowserSession.get().getTelemetryClient();
    if (session.getTelemetryName() != null && session.getProject() != null) {
      try {
        return client.getChart(session.getTelemetryName(), 
                        session.getProject().getOwner(), 
                        session.getProject().getName(), 
                        session.getGranularity(), 
                        Tstamp.makeTimestamp(session.getStartDate().getTime()), 
                        Tstamp.makeTimestamp(session.getEndDate().getTime()), 
                        session.getParameterAsString()).getTelemetryStream();
      }
      catch (TelemetryClientException e) {
        e.printStackTrace();
      }
    }
    return new ArrayList<TelemetryStream>();
  }
}
