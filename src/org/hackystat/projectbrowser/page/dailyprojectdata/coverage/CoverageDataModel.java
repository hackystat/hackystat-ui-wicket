package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  /** collection of granularities. */
  private String[] granularities = {"class", "method", "line", "block"};
  
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
   * @param granularity the given granularity.
   * @return coverage value of the given granularity in percent.
   */
  public String getCoverageDisplayString(String granularity) {
    int numCovered = 0;
    int numUncovered = 0;
    for (CoverageData data : this.coverageDataList) {
      numCovered += data.getNumCovered(granularity);
      numUncovered += data.getNumUncovered(granularity);
    }
    return CoverageData.convertToFormattedDisplayString(numCovered, numUncovered);
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
    coverageData.setCoverage(granularity, data.getNumCovered(), data.getNumUncovered());
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

  /**
   * Merge the data entries into package.
   */
  public void mergeIntoPackage() {
    Map<String, CoverageData> coverageDataMap = new HashMap<String, CoverageData>();
    for (CoverageData data : this.coverageDataList) {
      String packageName = getPackageName(data.getName());
      CoverageData newData = coverageDataMap.get(packageName);
      if (newData == null) {
        newData = new CoverageData(packageName);
        coverageDataMap.put(packageName, newData);
      }
      for (String g : granularities) {
        newData.addCoverage(g, data.getNumCovered(g), data.getNumUncovered(g));
      }
    }
    this.coverageDataList.clear();
    this.coverageDataList.addAll(coverageDataMap.values());
  }

  /**
   * Return the package name within the given file name.
   * @param name the given file name.
   * @return the package name.
   */
  private String getPackageName(String name) {
    int beginIndex = name.indexOf("src");
    beginIndex += 4;
    int endIndex = name.lastIndexOf('/');
    if (endIndex == -1) {
      endIndex = name.lastIndexOf('\\');
    }
    String packageName = name.substring(beginIndex, endIndex);
    packageName = packageName.replace('/', '.');
    packageName = packageName.replace('\\', '.');
    return packageName;
  }

}
