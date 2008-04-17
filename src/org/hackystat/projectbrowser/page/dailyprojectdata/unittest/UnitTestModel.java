package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import java.util.ArrayList;
import java.util.List;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataModel;

/**
 * Data model for unit test panel.
 * @author Shaoxuan Zhang
 */
public class UnitTestModel extends DailyProjectDataModel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** the member data list. */
  private List<MemberData> memberDataList = new ArrayList<MemberData>();
  
  /**
   * @param memberDataList the memberDataList to set
   */
  public void setMemberDataList(List<MemberData> memberDataList) {
    this.memberDataList = memberDataList;
  }
  /**
   * @return the memberDataList
   */
  public List<MemberData> getMemberDataList() {
    return memberDataList;
  }
}
