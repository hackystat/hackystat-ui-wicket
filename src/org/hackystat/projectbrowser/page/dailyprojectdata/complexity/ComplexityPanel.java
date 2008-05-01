package org.hackystat.projectbrowser.page.dailyprojectdata.complexity;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for complexity.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class ComplexityPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public ComplexityPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    ComplexityDataModel dataModel = session.getComplexityDataModel();
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    add(new Label("complexityType", "Cyclomatic")); 
    ListView memberDataListView = new ListView("complexityDataList", new PropertyModel(dataModel,
        "complexityDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        ComplexityData complexityData = (ComplexityData) item.getModelObject();
        item.add(new Label("project", complexityData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        boolean isCount = ("Count".equals(valueType));
        item.add(complexityData.getPanel("bucket0", 0, isCount));
        item.add(complexityData.getPanel("bucket1", 1, isCount));
        item.add(complexityData.getPanel("bucket2", 2, isCount));
        item.add(complexityData.getPanel("bucket3", 3, isCount));
        item.add(complexityData.getPanel("bucket4", 4, isCount));
        item.add(new Label("total", complexityData.getTotalString()));
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
    return !session.getComplexityDataModel().isEmpty(); 
  }
}
