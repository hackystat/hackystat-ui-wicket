package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
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
  /** Data model that holds this panel's state. */
  TelemetryChartDataModel dataModel = null;

  /**
   * @param id the wicket component id.
   */
  public TelemetryDataPanel(String id) {
    super(id);
    String feedback = "";
    for (Project project : session.getSelectedProjects()) {
      feedback += project.getName() + "-" + project.getOwner() + ", ";
    }
    session.setFeedback(feedback);
    dataModel = session.getDataModel();
    //display project information
    add(new Label("telemetryName", new PropertyModel(dataModel, "telemetryName")));
    
    ListView projectList = 
      new ListView("projectList", new PropertyModel(dataModel, "selectedProjects")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project)item.getModelObject();
        item.add(new Label("projectName", project.getName()));
        item.add(new Label("startDate", new PropertyModel(dataModel, "startDateString")));
        item.add(new Label("endDate", new PropertyModel(dataModel, "endDateString")));
        WebComponent chartUrl = new WebComponent("chartUrl");
        chartUrl.add(
            new AttributeModifier("src", true, new Model(dataModel.getProjectChart(project))));
        add(chartUrl);
        item.add(chartUrl);
      }
    };
    this.add(projectList);
    
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
