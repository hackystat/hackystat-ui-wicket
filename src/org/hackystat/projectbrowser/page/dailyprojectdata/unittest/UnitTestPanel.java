package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for unittest.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class UnitTestPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public UnitTestPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    UnitTestDataModel dataModel = session.getUnitTestDataModel();
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    ListView memberDataListView = new ListView("unitTestDataList", new PropertyModel(dataModel,
        "unitTestDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        UnitTestData unittestData = (UnitTestData) item.getModelObject();
        item.add(new Label("project", unittestData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        if ("Count".equals(valueType)) {
          item.add(new Label("bucket0", unittestData.getBucketCountString(0)));
          item.add(new Label("bucket1", unittestData.getBucketCountString(1)));
        }
        else {
          item.add(new Label("bucket0", unittestData.getBucketPercentageString(0)));
          item.add(new Label("bucket1", unittestData.getBucketPercentageString(1)));
        }
        item.add(new Label("total", unittestData.getTotalString()));
      }
    };
    add(memberDataListView);
  }

  /**
   * Returns true if this panel should be displayed.  
   * The panel should be displayed if its corresponding data model has information.
   * @return True if this panel should be displayed. 
   */
  @Override
  public boolean isVisible() {
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    return !session.getUnitTestDataModel().isEmpty(); 
  }
}
