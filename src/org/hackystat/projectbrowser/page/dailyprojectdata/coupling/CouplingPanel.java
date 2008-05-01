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
        boolean isCount = ("Count".equals(valueType));
        item.add(couplingData.getPanel("bucket0", 0, isCount));
        item.add(couplingData.getPanel("bucket1", 1, isCount));
        item.add(couplingData.getPanel("bucket2", 2, isCount));
        item.add(couplingData.getPanel("bucket3", 3, isCount));
        item.add(couplingData.getPanel("bucket4", 4, isCount));
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
