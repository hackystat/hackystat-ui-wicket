package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.
        PortfolioMeasureConfiguration;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.projectbrowser.page.telemetry.inputpanel.TelemetryInputForm;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;

/**
 * Panel for Project Portfolio configuration panel to input telemetry parameters.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class TelemetryParameterPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = -1318097127363264398L;
  PortfolioMeasureConfiguration measure;

  /**
   * @param id the wicket id.
   * @param m the {@link PortfolioMeasureConfiguration} to configure.
   */
  public TelemetryParameterPanel(final String id, final PortfolioMeasureConfiguration m) {
    super(id);        
    this.measure = m;
    TelemetrySession telemetrySession = ProjectBrowserSession.get().getTelemetrySession();
    ListView parameterList = 
      new ListView("parameterList", telemetrySession.getParameterList(measure.getName())) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        ParameterDefinition paramDef = (ParameterDefinition)item.getModelObject();
        item.add(new Label("name", paramDef.getName()));
        Component component = TelemetryInputForm.getComponent("field", paramDef.getType());
        if (item.getIndex() >= measure.getParameters().size() && component.getModel() != null) {
          measure.getParameters().add(component.getModel());
        }
        else {
          component.setModel(measure.getParameters().get(item.getIndex()));
        }
        item.add(component);
      }
      
      @Override
      public boolean isVisible() {
        return measure.isEnabled();
      }
    };
    add(parameterList);
  }
  
  /**
   * If this panel is visible.
   * @return true if the associated measure is enabled, false otherwise.
   */
  @Override
  public boolean isVisible() {
    return measure.isEnabled();
  }
}
