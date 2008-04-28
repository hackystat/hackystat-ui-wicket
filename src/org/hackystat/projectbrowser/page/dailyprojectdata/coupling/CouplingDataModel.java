package org.hackystat.projectbrowser.page.dailyprojectdata.coupling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * The data model for Coupling DPD display.  This data model accommodates multiple Projects.
 * For each project, the data model indicates the number of classes whose level of coupling
 * falls into each of five buckets, from 0-10, 11-20, 21-30, 31-40, 41+.
 * At present, we only get "class" coupling from the "DependencyFinder" tool. This will change
 * after we update the DPD client to not require the tool argument.
 * @author Philip Johnson
 *
 */
public class CouplingDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** Holds the coupling data, organized by Project.*/
  private Map<Project, CouplingData> couplingDataMap = new HashMap<Project, CouplingData>();
  
  /**
   * The default CouplingDataModel, which contains no coupling information.
   */
  public CouplingDataModel() {
    // Do nothing
  }
  
  /**
   * Updates this data model to reflect the coupling information associated with the selected 
   * projects and granularity.
   */
  public void update() {
    this.clear();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    List<Project> projects = session.getSelectedProjects();
    String couplingType = session.getContextSensitiveMenu("Coupling Type").getSelectedValue();
    
    for (Project project : projects) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Getting Coupling DPD for project: " + project.getName());
      try {
        CouplingDailyProjectData classData = dpdClient.getCoupling(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), "class",
            "DependencyFinder");
        logger.info("Finished getting Coupling DPD for project: " + project.getName());
        for (org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData data : 
          classData.getCouplingData()) {
            CouplingData couplingData = this.getCouplingData(project);
            int couplingCount = 0;
            if ("Afferent".equals(couplingType)) {
              couplingCount = data.getAfferent().intValue();
            }
            else if ("Efferent".equals(couplingType)) {
              couplingCount = data.getEfferent().intValue();
            }
            else {
              couplingCount = data.getAfferent().intValue() + data.getEfferent().intValue();
            }
            
          couplingData.addEntry(couplingCount);
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting coupling data for project " + project + ": " +
            e.getMessage());
      }
    }
  }
  
  
  /**
   * Sets this model to its empty state. 
   */
  public void clear() {
    this.couplingDataMap.clear();
  }
  

  /**
   * Returns true if this data model contains no information.
   * Used to figure out if the associated panel should be visible. 
   * @return True if the data model is empty. 
   */
  public boolean isEmpty() {
    return this.couplingDataMap.isEmpty();
  }
  
  /**
   * Return the CouplingData instance associated with the specified project.
   * Creates and returns a new CouplingData instance if one is not yet present.
   * @param project The project. 
   * @return The CouplingData instance for this project.  
   */
  public CouplingData getCouplingData(Project project) {
    if (!couplingDataMap.containsKey(project)) {
      couplingDataMap.put(project, new CouplingData(project));
    }
    return couplingDataMap.get(project);
  }
  
  /**
   * Returns the list of CouplingData instances, needed for markup.
   * @return The list of CouplingData instances. 
   */
  public List<CouplingData> getCouplingDataList() {
    return new ArrayList<CouplingData>(this.couplingDataMap.values());
  }
  
}