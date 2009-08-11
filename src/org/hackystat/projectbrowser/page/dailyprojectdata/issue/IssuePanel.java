package org.hackystat.projectbrowser.page.dailyprojectdata.issue;

import java.util.ArrayList;
import java.util.List;
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
public class IssuePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public IssuePanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    IssueDataModel dataModel = session.getIssueDataModel();
    final List<String> openHeaders = new ArrayList<String>();
    openHeaders.addAll(dataModel.getOpenIssueStatus());
    ListView openStatusHeaders = new ListView("issueOpenStatusHeads", openHeaders) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        final String statusName = item.getModelObjectAsString();
        item.add(new Label("issueStatusName", statusName));
      }
    };
    add(openStatusHeaders);
    
    final List<String> closedHeaders = new ArrayList<String>();
    closedHeaders.addAll(dataModel.getClosedIssueStatus());
    ListView closedStatusHeaders = new ListView("issueClosedStatusHeads", closedHeaders) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        final String statusName = item.getModelObjectAsString();
        item.add(new Label("issueStatusName", statusName));
      }
    };
    add(closedStatusHeaders);
    
    ListView datalistView = new ListView("issueDataList", new PropertyModel(dataModel,
        "issueDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        IssueDpdData data = (IssueDpdData) item.getModelObject();
        item.add(new Label("project", data.getProject().getName()));
        List<String> openIssueDataList = new ArrayList<String>();
        for (String status : openHeaders) {
          Integer value = data.getIssueStatusCount().get(status);
          if (value == null) {
            value = 0;
          }
          openIssueDataList.add(value.toString());
        }
        ListView openIssueDataListView = new ListView("openIssueDataList", openIssueDataList) {
          /** Support serialization. */
          private static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            final String statusName = item.getModelObjectAsString();
            item.add(new Label("data", statusName));
          }
        };
        item.add(openIssueDataListView);
        item.add(new Label("totalOpen", String.valueOf(data.getOpenIssues())));
        List<String> closedIssueDataList = new ArrayList<String>();
        for (String status : closedHeaders) {
          Integer value = data.getIssueStatusCount().get(status);
          if (value == null) {
            value = 0;
          }
          openIssueDataList.add(value.toString());
        }
        ListView closedIssueDataListView = 
            new ListView("closedIssueDataList", closedIssueDataList) {
          /** Support serialization. */
          private static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            final String statusName = item.getModelObjectAsString();
            item.add(new Label("data", statusName));
          }
        };
        item.add(closedIssueDataListView);
        item.add(new Label("totalClosed", String.valueOf(data.getClosedIssues())));
      }
    };
    add(datalistView);
  }

  /**
   * Returns true if this panel should be displayed.  
   * The panel should be displayed if its corresponding data model has information.
   * @return True if this panel should be displayed. 
   */
  @Override
  public boolean isVisible() {
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    return !session.getIssueDataModel().isEmpty(); 
  }
}
