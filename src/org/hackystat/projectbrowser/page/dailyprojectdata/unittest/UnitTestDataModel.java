package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data model for unit test panel.
 * @author Shaoxuan Zhang
 */
public class UnitTestDataModel implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** the member data list. */
  private List<MemberData> memberDataList = new ArrayList<MemberData>();
  
  /**
   * The default constructor, which creates an empty UnitTest data model.
   */
  public UnitTestDataModel() {
    // Do nothing.
  }
  
  /**
   * Returns true if this data model is empty. 
   * If empty, it should not be displayed. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.memberDataList.isEmpty();
  }
  
  /**
   * Sets this data model to an empty state. 
   */
  public void clear() {
    this.memberDataList.clear();
  }
  
  /**
   * Update the current UnitTest data model based upon the user selections. 
   */
  public void update() {
    this.setMemberDataList(getUnitTestMemberDataList());
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
  
  
  /**
   * @param memberDataList the memberDataList to set
   */
  private void setMemberDataList(List<MemberData> memberDataList) {
    this.memberDataList = memberDataList;
  }
 
}
