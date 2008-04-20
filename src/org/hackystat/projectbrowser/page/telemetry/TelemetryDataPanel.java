package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
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
    dataModel = session.getDataModel();
    //display project information
    if (dataModel.getProject() == null) {
      add(new Label("projectName", ""));
      add(new Label("projectOwner", ""));
    }
    else {
      add(new Label("projectName", dataModel.getProject().getName()));
      add(new Label("projectOwner", dataModel.getProject().getOwner()));
    }
    add(new Label("startDate", new PropertyModel(dataModel, "startDateString")));
    add(new Label("endDate", new PropertyModel(dataModel, "endDateString")));
    add(new Label("telemetryName", new PropertyModel(dataModel, "telemetryName")));
    WebComponent chartUrl = new WebComponent("chartUrl");
    chartUrl.add(new AttributeModifier("src", true, new PropertyModel(dataModel, "chartUrl")));
    add(chartUrl);
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
