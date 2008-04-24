package org.hackystat.projectbrowser.page.telemetry;

import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
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
    
    Form streamForm = new Form("streamForm") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        session.getDataModel().resetSelectedChart();
      }
    };
    add(streamForm);
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
    streamForm.add(dateList);
    
    
    ListView projectTable = 
      new ListView("projectTable", new PropertyModel(dataModel, "selectedProjects")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project)item.getModelObject();
        Label projectNameLabel = new Label("projectName", project.getName());
        List<SelectableTelemetryStream> streamList = 
            session.getDataModel().getTelemetryStream(project);
        projectNameLabel.add(new AttributeModifier("rowspan", new Model(streamList.size() + 1)));
        item.add(projectNameLabel);
        
        ListView projectStream = 
          new ListView("projectStream", streamList) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            SelectableTelemetryStream stream = (SelectableTelemetryStream)item.getModelObject();
            String streamName = stream.getTelemetryStream().getName();
            int index = streamName.indexOf('<');
            if (index > 0) {
              streamName = streamName.substring(0, index);
            }
            item.add(new Label("streamName", streamName));
            
            item.add(new CheckBox("streamCheckBox", new PropertyModel(stream, "selected")));
            
            ListView streamData = 
              new ListView("streamData", stream.getTelemetryStream().getTelemetryPoint()) {
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
    streamForm.add(projectTable);

    //add the overall chart.
    WebComponent chartUrl = new WebComponent("overallChart");
    chartUrl.add(new AttributeModifier("src", true, 
                              new PropertyModel(dataModel, "overallChart")));
    add(chartUrl);
    PopupWindowPanel chartUrlWindow = new PopupWindowPanel("chartUrlWindow", "Google Chart URL");
    chartUrlWindow.getModalWindow().setContent(
        new Label(chartUrlWindow.getModalWindow().getContentId(), 
                  new PropertyModel(dataModel, "overallChart")));
    add(chartUrlWindow);

    //add the selected chart.
    WebComponent selectedchartUrl = new WebComponent("selectedChart");
    selectedchartUrl.add(new AttributeModifier("src", true, 
                              new PropertyModel(dataModel, "selectedChart")));
    add(selectedchartUrl);
    PopupWindowPanel selectedchartUrlWindow = 
      new PopupWindowPanel("selectedChartUrlWindow", "Google Chart URL");
    selectedchartUrlWindow.getModalWindow().setContent(
        new Label(selectedchartUrlWindow.getModalWindow().getContentId(), 
                  new PropertyModel(dataModel, "selectedChart")));
    add(selectedchartUrlWindow);
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
