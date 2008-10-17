package org.hackystat.projectbrowser.page.dailyprojectdata.devtime;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for DevTime.
 * 
 * @author Philip Johnson
 */
public class DevTimePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public DevTimePanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    DevTimeDataModel dataModel = session.getDevTimeDataModel();
    ListView buildListView = new ListView("devTimeDataList", new PropertyModel(dataModel,
        "devTimeDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        DevTimeData buildData = (DevTimeData) item.getModelObject();
        item.add(new Label("project", buildData.getProject().getName()));
        item.add(new Label("devtimedata", buildData.getDevTimeData()));
        item.add(new Label("total", String.valueOf(buildData.getTotalDevTime())));
      }
    };
    add(buildListView);
  }

  /**
   * Returns true if this panel should be displayed.  
   * The panel should be displayed if its corresponding data model has information.
   * @return True if this panel should be displayed. 
   */
  @Override
  public boolean isVisible() {
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    return !session.getDevTimeDataModel().isEmpty(); 
  }
}
