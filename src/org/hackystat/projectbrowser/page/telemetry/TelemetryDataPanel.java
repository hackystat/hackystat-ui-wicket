package org.hackystat.projectbrowser.page.telemetry;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.telemetry.service.client.TelemetryClient;
import org.hackystat.telemetry.service.client.TelemetryClientException;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
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
    
    /* check parameters
    for (ParameterDefinition def : session.getParameterList()) {
      System.out.print(def.getName() + "\t");
    }
    System.out.println();
    for (IModel model : session.getParameters()) {
      System.out.print(model.getObject() + "\t");
    }
    System.out.println();
    */
    
    ListView dataListView = 
      new ListView("dataList", getTelemetryDataTable()) {
      private static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        List telemetryStream = (List)item.getModelObject();
        ListView telemetryPointListView = 
          new ListView("telemetryPoints", telemetryStream) {
          private static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            String telemetryStream = (String)item.getModelObject();
            item.add(new Label("value", telemetryStream));
          }
        };
        item.add(telemetryPointListView);
      }
    };
    add(dataListView);
  }

  /**
   * Return the telemetry data as table.
   * @return table, a List of Lists.
   */
  private List<List<String>> getTelemetryDataTable() {
    List<List<String>> dataTable = new ArrayList<List<String>>();
    for (TelemetryStream stream : this.getTelemetryChart()) {
      List<String> tableEntry = new ArrayList<String>();
      tableEntry.add(stream.getName());
      for (TelemetryPoint point : stream.getTelemetryPoint()) {
        tableEntry.add(point.getValue());
      }
      dataTable.add(tableEntry);
    }
    return dataTable;
  }
  
  /**
   * Return the Telemetry Chart Data.
   * @return a List of TelemetryStreams.
   */
  private List<TelemetryStream> getTelemetryChart() {
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
  /**
   * Determine if this panel is visible or not.
   * @return true if this panel is visible
   */
  @Override
  public boolean isVisible() {
    return ProjectBrowserSession.get().getTelemetrySession().getProject() != null;
  }
}
