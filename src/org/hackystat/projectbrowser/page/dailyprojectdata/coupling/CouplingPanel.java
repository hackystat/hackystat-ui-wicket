package org.hackystat.projectbrowser.page.dailyprojectdata.coupling;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for coupling.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class CouplingPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public CouplingPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    CouplingDataModel dataModel = session.getCouplingDataModel();
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    add(new Label("couplingType", 
        session.getContextSensitiveMenu("Coupling Type").getSelectedValue()));
    ListView memberDataListView = new ListView("couplingDataList", new PropertyModel(dataModel,
        "couplingDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CouplingData couplingData = (CouplingData) item.getModelObject();
        item.add(new Label("project", couplingData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        if ("Count".equals(valueType)) {
          item.add(new Label("bucket0", couplingData.getBucketCountString(0)));
          item.add(new Label("bucket1", couplingData.getBucketCountString(1)));
          item.add(new Label("bucket2", couplingData.getBucketCountString(2)));
          item.add(new Label("bucket3", couplingData.getBucketCountString(3)));
          item.add(new Label("bucket4", couplingData.getBucketCountString(4)));
        }
        else {
          item.add(new Label("bucket0", couplingData.getBucketPercentageString(0)));
          item.add(new Label("bucket1", couplingData.getBucketPercentageString(1)));
          item.add(new Label("bucket2", couplingData.getBucketPercentageString(2)));
          item.add(new Label("bucket3", couplingData.getBucketPercentageString(3)));
          item.add(new Label("bucket4", couplingData.getBucketPercentageString(4)));
        }
        item.add(new Label("total", couplingData.getTotalString()));
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
    return !session.getCouplingDataModel().isEmpty(); 
  }
}
