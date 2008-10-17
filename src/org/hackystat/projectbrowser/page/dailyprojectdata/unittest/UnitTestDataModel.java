package org.hackystat.projectbrowser.page.dailyprojectdata.unittest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.MemberData;
import org.hackystat.dailyprojectdata.resource.unittest.jaxb.UnitTestDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for UnitTest DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of passing and failing unit tests.  
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 *
 */
public class UnitTestDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the unittest data, organized by Project.*/
  private Map<Project, UnitTestData> unittestDataMap = new HashMap<Project, UnitTestData>();
  
  /**
   * The default UnitTestDataModel, which contains no unittest information.
   */
  public UnitTestDataModel() {
    // Do nothing
  }
  
  /**
   * Updates this data model to reflect the unittest information associated with the selected 
   * projects.
   */
  public void update() {
    this.clear();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    List<Project> projects = session.getSelectedProjects();
    
    for (Project project : projects) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.fine("Getting UnitTest DPD for project: " + project.getName());
      try {
        UnitTestDailyProjectData classData = dpdClient.getUnitTest(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()));
        logger.fine("Finished getting UnitTest DPD for project: " + project.getName());
        // Create a UnitTestData instance for this project.
        UnitTestData unittestData = this.getUnitTestData(project);
        for (MemberData data : classData.getMemberData()) {
          unittestData.addEntry(data.getSuccess().intValue(), data.getFailure().intValue());
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting unittest DPD for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.unittestDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.unittestDataMap.isEmpty();
  }
  
  /**
   * Return the UnitTestData instance associated with the specified project.
   * Creates and returns a new UnitTestData instance if one is not yet present.
   * @param project The project. 
   * @return The UnitTestData instance for this project.  
   */
  public UnitTestData getUnitTestData(Project project) {
    if (!unittestDataMap.containsKey(project)) {
      unittestDataMap.put(project, new UnitTestData(project));
    }
    return unittestDataMap.get(project);
  }
  
  /**
   * Returns the list of UnitTestData instances, needed for markup.
   * @return The list of UnitTestData instances. 
   */
  public List<UnitTestData> getUnitTestDataList() {
    return new ArrayList<UnitTestData>(this.unittestDataMap.values());
  }
  
}
