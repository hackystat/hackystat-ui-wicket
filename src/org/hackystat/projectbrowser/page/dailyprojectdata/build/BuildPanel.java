package org.hackystat.projectbrowser.page.dailyprojectdata.build;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for build.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class BuildPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public BuildPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    BuildDataModel dataModel = session.getBuildDataModel();
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    ListView buildListView = new ListView("buildDataList", new PropertyModel(dataModel,
        "buildDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        BuildData buildData = (BuildData) item.getModelObject();
        item.add(new Label("project", buildData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        item.add(buildData.getPanel("bucket0", 0, ("Count".equals(valueType))));
        item.add(buildData.getPanel("bucket1", 1, ("Count".equals(valueType))));
        item.add(new Label("total", buildData.getTotalString()));
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
    return !session.getBuildDataModel().isEmpty(); 
  }
}
