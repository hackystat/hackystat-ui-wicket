package org.hackystat.projectbrowser.page.dailyprojectdata.filemetric;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for FileMetric.
 * 
 * @author Philip Johnson
 */
public class FileMetricPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public FileMetricPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    FileMetricDataModel dataModel = session.getFileMetricDataModel();
    ListView listView = new ListView("fileMetricDataList", new PropertyModel(dataModel,
        "fileMetricDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        FileMetricData data = (FileMetricData) item.getModelObject();
        item.add(new Label("project", data.getProject().getName()));
        item.add(new Label("filemetricdata", data.getFileMetricData()));
        item.add(new Label("total", String.valueOf(data.getTotalLoc())));
      }
    };
    add(listView);
  }

  /**
   * Returns true if this panel should be displayed.  
   * The panel should be displayed if its corresponding data model has information.
   * @return True if this panel should be displayed. 
   */
  @Override
  public boolean isVisible() {
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    return !session.getFileMetricDataModel().isEmpty(); 
  }
}
