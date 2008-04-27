package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;

/**
 * Page to show daily unit test data of the project.
 * @author Shaoxuan Zhang
 */
public class UnitTestPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * create the UnitTestPage
   * @param id wicket component id.
   */
  public UnitTestPanel(final String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    this.setModel(new CompoundPropertyModel(this));
    //if (session.getProject() == null) {
      add(new Label("projectName", ""));
      add(new Label("projectOwner", ""));
//    }
//    else {
//      add(new Label("projectName", session.getProject().getName()));
//      add(new Label("projectOwner", session.getProject().getOwner()));
//    }
    add(new Label("date", new PropertyModel(session, "dateString")));
    UnitTestDataModel dataModel = session.getUnitTestDataModel();
    ListView memberDataListView = 
      new ListView("memberDataList", new PropertyModel(dataModel, "memberDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        MemberData memberData = (MemberData) item.getModelObject();
        item.add(new Label("name", getEmailFromUri(memberData.getMemberUri())));
        item.add(new Label("testSuccessCount", memberData.getSuccess().toString()));
        item.add(new Label("testFailureCount", memberData.getFailure().toString()));
      }

      private String getEmailFromUri(String memberUri) {
        int index = memberUri.lastIndexOf('/');
        return memberUri.substring(index + 1);
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
