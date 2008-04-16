package org.hackystat.projectbrowser.page.dailyprojectdata.component;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataPage;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.users.jaxb.User;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Page to show daily unit test data of the project.
 * @author Shaoxuan Zhang
 */
public class UnitTestPage extends DailyProjectDataPage {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * create the UnitTestPage
   * @param parameters the parameters to configure the page.
   */
  public UnitTestPage(PageParameters parameters) {
    super(parameters);
    this.setModel(new CompoundPropertyModel(this));
    if (this.getProject() == null) {
      add(new Label("projectName", ""));
      add(new Label("projectOwner", ""));
    }
    else {
      add(new Label("projectName", this.getProject().getName()));
      add(new Label("projectOwner", this.getProject().getOwner()));
    }
    add(new Label("date", this.getDate().toString()));
    PageableListView memberDataListView = new PageableListView("memberDataList",
        getUnitTestMemberDataList(), MEMBERDATA_PER_PAGE) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        MemberData memberData = (MemberData) item.getModelObject();
        User user = new User();
        try {
          user = ProjectBrowserSession.get().getSensorBaseClient().
                      getUser(memberData.getMemberUri());
        }
        catch (SensorBaseClientException e) {
          e.printStackTrace();
        }
        item.add(new Label("name", user.getEmail()));
        item.add(new Label("testSuccessCount", memberData.getSuccess().toString()));
        item.add(new Label("testFailureCount", memberData.getFailure().toString()));
      }
    };
    add(memberDataListView);
  }

  /**
   * Retrieve project members' unit test data of the project.
   * @return a List of MemberData that contain unit test data of each member of the project.
   */
  protected List<MemberData> getUnitTestMemberDataList() {
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    List<MemberData> memberDataList = new ArrayList<MemberData>();
    if (this.getProject() == null) {
      this.footerFeedback = "You did not selected a project to display.";
      return memberDataList;
    }
    try {
      memberDataList = dpdClient.getUnitTest(getProject().getOwner(), getProject().getName(),
          Tstamp.makeTimestamp(this.getDate().getTime())).getMemberData();
    }
    catch (DailyProjectDataClientException e) {
      this.footerFeedback = "No unit test data of project " + getProject().getName() + " found on "
       + getDate();
    }
    return memberDataList;
  }
  
  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    super.onProjectDateSubmit();
  }
}
