package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.util.ArrayList;
import java.util.List;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataModel;

/**
 * Data model for coverage panel.
 * @author Shaoxuan Zhang
 *
 */
public class CoverageDataModel extends DailyProjectDataModel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  private List<CoverageData> coverageDataList = new ArrayList<CoverageData>();

  /**
   * return the CoverageData with the given name.
   * @param name the given name.
   * @return the CoverageData instance. null if not found.
   */
  public CoverageData getCoverageData(String name) {
    if (name == null) {
      return null;
    }
    for (CoverageData coverageData : this.coverageDataList) {
      if (name.equals(coverageData.getName())) {
        return coverageData;
      }
    }
    return null;
  }
  
  /**
   * @return coverage value of class granularity.
   */
  public int getClassCoverage() {
    int numCovered = 0;
    int numUncovered = 0;
    for (CoverageData data : this.coverageDataList) {
      numCovered += data.getNumCovered("class");
      numUncovered += data.getNumUncovered("class");
    }
    return (int)((double)numCovered / (numCovered + numUncovered) * 100);
  }
  
  /**
   * add the piece of coverage data into this data model.
   * @param data the piece of data from dpd client.
   * @param granularity granularity of this data.
   */
  public void add(ConstructData data, String granularity) {
    CoverageData coverageData = this.getCoverageData(data.getName());
    if (coverageData == null) {
      coverageData = new CoverageData(data.getName());
      this.coverageDataList.add(coverageData);
    }
    coverageData.addCoverage(granularity, data.getNumCovered(), data.getNumUncovered());
  }
  
  /**
   * @param coverageDataList the coverageDataList to set
   */
  public void setCoverageDataList(List<CoverageData> coverageDataList) {
    this.coverageDataList = coverageDataList;
  }

  /**
   * @return the coverageDataList
   */
  public List<CoverageData> getCoverageDataList() {
    return coverageDataList;
  }

}
