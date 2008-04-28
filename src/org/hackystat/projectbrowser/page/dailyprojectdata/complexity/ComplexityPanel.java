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
        if ("Count".equals(valueType)) {
          item.add(new Label("bucket0", complexityData.getBucketCountString(0)));
          item.add(new Label("bucket1", complexityData.getBucketCountString(1)));
          item.add(new Label("bucket2", complexityData.getBucketCountString(2)));
          item.add(new Label("bucket3", complexityData.getBucketCountString(3)));
          item.add(new Label("bucket4", complexityData.getBucketCountString(4)));
        }
        else {
          item.add(new Label("bucket0", complexityData.getBucketPercentageString(0)));
          item.add(new Label("bucket1", complexityData.getBucketPercentageString(1)));
          item.add(new Label("bucket2", complexityData.getBucketPercentageString(2)));
          item.add(new Label("bucket3", complexityData.getBucketPercentageString(3)));
          item.add(new Label("bucket4", complexityData.getBucketPercentageString(4)));
        }
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
