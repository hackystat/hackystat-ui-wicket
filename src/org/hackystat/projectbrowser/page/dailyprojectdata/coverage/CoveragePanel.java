package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for coverage.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class CoveragePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public CoveragePanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    CoverageDataModel dataModel = session.getCoverageDataModel();
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    add(new Label("coverageType", 
        session.getContextSensitiveMenu("Coverage Type").getSelectedValue()));
    ListView memberDataListView = new ListView("coverageDataList", new PropertyModel(dataModel,
        "coverageDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CoverageData coverageData = (CoverageData) item.getModelObject();
        item.add(new Label("project", coverageData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        boolean isCount = ("Count".equals(valueType));
        item.add(coverageData.getPanel("bucket0", 0, isCount));
        item.add(coverageData.getPanel("bucket1", 1, isCount));
        item.add(coverageData.getPanel("bucket2", 2, isCount));
        item.add(coverageData.getPanel("bucket3", 3, isCount));
        item.add(coverageData.getPanel("bucket4", 4, isCount));
        item.add(new Label("total", coverageData.getTotalString()));
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
    return !session.getCoverageDataModel().isEmpty(); 
  }
}
