package org.hackystat.projectbrowser.page.telemetry;

import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;
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
    IModel dataModel = new PropertyModel(session, "dataModel");
    //display project information
    add(new Label("telemetryName", 
                  new PropertyModel(dataModel, "telemetryName")));

    ListView dateList = new ListView("dateList", 
                                     new PropertyModel(dataModel, "dateList")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        String dateString = (String)item.getModelObject();
        item.add(new Label("date", dateString));
      }
    };
    add(dateList);
    
    ListView projectTable = 
      new ListView("projectTable", new PropertyModel(dataModel, "selectedProjects")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project)item.getModelObject();
        Label projectNameLabel = new Label("projectName", project.getName());
        List<TelemetryStream> streamList = session.getDataModel().getTelemetryStream(project);
        projectNameLabel.add(new AttributeModifier("rowspan", new Model(streamList.size() + 1)));
        item.add(projectNameLabel);
        
        ListView projectStream = 
          new ListView("projectStream", streamList) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            TelemetryStream stream = (TelemetryStream)item.getModelObject();
            String streamName = stream.getName();
            int index = streamName.indexOf('<');
            if (index > 0) {
              streamName = streamName.substring(0, index);
            }
            item.add(new Label("streamName", streamName));

            ListView streamData = 
              new ListView("streamData", stream.getTelemetryPoint()) {
              /** Support serialization. */
              public static final long serialVersionUID = 1L;
              @Override
              protected void populateItem(ListItem item) {
                TelemetryPoint point = (TelemetryPoint)item.getModelObject();
                String value = point.getValue();
                if (value == null) {
                  item.add(new Label("data", "N/A"));
                }
                else {
                  int i = value.indexOf('.');
                  if (i > 0 && (i + 2) < value.length()) {
                    value = value.substring(0, i + 2);
                  }
                  item.add(new Label("data", value));
                }
              }
            };
            item.add(streamData);
          }
        };
        item.add(projectStream);
      }
    };
    add(projectTable);

    WebComponent chartUrl = new WebComponent("overallChart");
    chartUrl.add(
        new AttributeModifier("src", true, 
                              new PropertyModel(dataModel, "overallChart")));
    add(chartUrl);
    
    add(new Label("overallChartUrl", new PropertyModel(dataModel, "overallChart")));
    
  }
  
  /**
   * Display this panel only if the SdtSummaryModel contains information. 
   * @return True if this panel should be displayed.
   */
  @Override
  public boolean isVisible() {
    return !session.getDataModel().isEmpty();
  }
}
