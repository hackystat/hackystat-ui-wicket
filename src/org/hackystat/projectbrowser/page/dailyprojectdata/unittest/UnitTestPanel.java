package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.utilities.tstamp.Tstamp;

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
    UnitTestDataModel dataModel = (UnitTestDataModel) session.getDataModel();
    if (dataModel == null) {
      dataModel = getUnitTestModel();
      session.setDataModel(dataModel);
    }
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
   * Return a unit test model that represent the newest data.
   * @return the unit test model.
   */
  private UnitTestDataModel getUnitTestModel() {
    UnitTestDataModel unitTestModel = new UnitTestDataModel();
    unitTestModel.setMemberDataList(getUnitTestMemberDataList());
    return unitTestModel;
  }

  /**
   * Retrieve project members' unit test data of the project.
   * @return a List of MemberData that contain unit test data of each member of the project.
   */
  private List<MemberData> getUnitTestMemberDataList() {
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    List<MemberData> memberDataList = new ArrayList<MemberData>();
    try {
      memberDataList = dpdClient.getUnitTest(session.getSelectedProjects().get(0).getOwner(), 
          "Default",
          Tstamp.makeTimestamp(session.getDate().getTime())).getMemberData();
    }
    catch (DailyProjectDataClientException e) {
      session.setFeedback("Exception when getting unit test data: " + e.getMessage());
    }
    return memberDataList;
  }
}
