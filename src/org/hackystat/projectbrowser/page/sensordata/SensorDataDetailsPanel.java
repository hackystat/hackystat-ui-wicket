package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
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
    SensorDataDetailsProvider provider = session.getSensorDataDetailsProvider();
    add(new Label("sdtName", new PropertyModel(session, "sdtName")));
    add(new Label("tool", new PropertyModel(session, "tool")));
    DataView dataView = new DataView("sensorDataDetailsList", provider) {
      /** For serialization */
      private static final long serialVersionUID = 1L;

      /** 
       * How to populate the table.
       * @param item The SensorDataDetails item.  
       */
      @Override
      protected void populateItem(Item item) {
        SensorDataDetails details = (SensorDataDetails) item.getModelObject();
        item.add(new Label("timestamp", details.getTimeStamp()));
        item.add(new Label("runtime", details.getRuntime()));
        item.add(new Label("sdtName", details.getSdtName()));
        item.add(new Label("resource", details.getResource()));
        item.add(new Label("owner", details.getOwner()));
        item.add(new Label("tool", details.getTool()));
        item.add(new Label("properties", details.getProperties()));
      }
    };
    int itemsPerPage = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).
      getProjectBrowserProperties().getSensorDataItemsPerPage();
    dataView.setItemsPerPage(itemsPerPage);
    add(dataView);
    add(new PagingNavigator("navigator", dataView));
  }
  
  /**
   * Display this panel only if the SensorDataDetailsProvider contains information. 
   * @return True if this panel should be displayed.
   */
  @Override
  public boolean isVisible() {
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    SensorDataDetailsProvider provider = session.getSensorDataDetailsProvider();
    return !provider.isEmpty();
  }

}
