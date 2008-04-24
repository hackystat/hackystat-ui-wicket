package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataModel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * The data model for Coverage DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of classes whose method-level percentage
 * falls into each of five buckets, from 0-20% to 80-100%.
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 *
 */
public class CoverageDataModel extends DailyProjectDataModel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the coverage data, organized by Project.*/
  private Map<Project, CoverageData> coverageDataMap = new HashMap<Project, CoverageData>();

  /**
   * Return the CoverageData instance associated with the specified project.
   * Creates and returns a new CoverageData instance if one is not yet present.
   * @param project The project. 
   * @return The CoverageData instance for this project.  
   */
  public CoverageData getCoverageData(Project project) {
    if (!coverageDataMap.containsKey(project)) {
      coverageDataMap.put(project, new CoverageData(project));
    }
    return coverageDataMap.get(project);
  }
  
  
  /**
   * Adds information about a specific file's coverage to this data model.
   * @param project The project associated with this file. 
   * @param data The file coverage information.
   */
  public void add(Project project, ConstructData data) {
    CoverageData coverageData = this.getCoverageData(project);
    coverageData.addEntry(data.getNumCovered(), data.getNumUncovered());
  }
  
  /**
   * Returns a list of CoverageData instances for display in the table.
   * @return A list of CoverageData instances. 
   */
  public List<CoverageData> getCoverageDataList() {
    List<CoverageData> coverageDataList = new ArrayList<CoverageData>();
    coverageDataList.addAll(coverageDataMap.values());
    return coverageDataList;
  }

}
