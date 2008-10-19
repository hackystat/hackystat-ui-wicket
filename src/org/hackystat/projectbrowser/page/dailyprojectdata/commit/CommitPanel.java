package org.hackystat.projectbrowser.page.dailyprojectdata.commit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;


/**
 * Data panel for Commit.
 * 
 * @author Philip Johnson
 */
public class CommitPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public CommitPanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    CommitDataModel dataModel = session.getCommitDataModel();
    ListView listView = new ListView("commitDataList", new PropertyModel(dataModel,
        "commitDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CommitData data = (CommitData) item.getModelObject();
        item.add(new Label("project", data.getProject().getName()));
        item.add(new Label("commitdata", data.getCommitData()));
        item.add(new Label("total", String.format("%d/%d", data.getTotalCommit(), 
            data.getTotalChurn())));
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
    return !session.getCommitDataModel().isEmpty(); 
  }
}
