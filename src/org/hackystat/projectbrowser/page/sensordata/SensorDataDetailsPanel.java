package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * Creates a panel for displaying sensor data. 
 * @author Philip Johnson
 */
public class SensorDataDetailsPanel extends Panel {
  
  /** For serialization */
  private static final long serialVersionUID = 1L;
  
  /**
   * Creates a panel to display a table of sensor data instances for a given SDT. 
   * @param id The wicket ID. 
   */
  public SensorDataDetailsPanel(String id) {
    super(id);
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    SensorDataDetailsModel model = session.getSensorDataDetailsModel();
    add(new Label("sdtName", new PropertyModel(session, "sdtName")));
    add(new Label("tool", new PropertyModel(session, "tool")));
    add(new ListView("sensorDataDetailsList", new PropertyModel(model, "details")) {
      /** For serialization */
      private static final long serialVersionUID = 1L;

      /** 
       * How to populate the table.
       * @param item The SensorDataDetails item.  
       */
      @Override
      protected void populateItem(ListItem item) {
        SensorDataDetails details = (SensorDataDetails) item.getModelObject();
        item.add(new Label("timestamp", details.getTimeStamp()));
        item.add(new Label("runtime", details.getRuntime()));
        item.add(new Label("sdtName", details.getSdtName()));
        item.add(new Label("resource", details.getResource()));
        item.add(new Label("owner", details.getOwner()));
        item.add(new Label("tool", details.getTool()));
        item.add(new Label("properties", details.getProperties()));
      }
    });
  }
  
  /**
   * Display this panel only if the SdtDetailsModel contains information. 
   * @return True if this panel should be displayed.
   */
  @Override
  public boolean isVisible() {
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    SensorDataDetailsModel model = session.getSensorDataDetailsModel();
    return !model.isEmpty();
  }

}
