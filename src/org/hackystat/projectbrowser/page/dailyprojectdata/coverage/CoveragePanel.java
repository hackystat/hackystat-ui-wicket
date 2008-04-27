package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.util.logging.Logger;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.CoverageDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data panel for coverage.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 * 
 */
public class CoveragePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create this panel, providing the appropriate wicket ID.
   * 
   * @param id The wicket component id.
   */
  public CoveragePanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    // prepare the data model.
    CoverageDataModel dataModel = (CoverageDataModel) session.getDataModel();
    if (dataModel == null) {
      dataModel = getCoverageDataModel();
      session.setDataModel(dataModel);
    }
    add(new Label("valuesType", session.getContextSensitiveMenu("Values").getSelectedValue()));
    add(new Label("coverageType", 
        session.getContextSensitiveMenu("Coverage Type").getSelectedValue()));
    ListView memberDataListView = new ListView("coverageDataList", new PropertyModel(dataModel,
        "coverageDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CoverageData coverageData = (CoverageData) item.getModelObject();
        item.add(new Label("project", coverageData.getProject().getName()));
        DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
        String valueType = session.getContextSensitiveMenu("Values").getSelectedValue();
        if ("Count".equals(valueType)) {
          item.add(new Label("bucket0", coverageData.getBucketCountString(0)));
          item.add(new Label("bucket1", coverageData.getBucketCountString(1)));
          item.add(new Label("bucket2", coverageData.getBucketCountString(2)));
          item.add(new Label("bucket3", coverageData.getBucketCountString(3)));
          item.add(new Label("bucket4", coverageData.getBucketCountString(4)));
        }
        else {
          item.add(new Label("bucket0", coverageData.getBucketPercentageString(0)));
          item.add(new Label("bucket1", coverageData.getBucketPercentageString(1)));
          item.add(new Label("bucket2", coverageData.getBucketPercentageString(2)));
          item.add(new Label("bucket3", coverageData.getBucketPercentageString(3)));
          item.add(new Label("bucket4", coverageData.getBucketPercentageString(4)));
        }
        item.add(new Label("total", coverageData.getTotalString()));
      }
    };
    add(memberDataListView);
  }

  /**
   * Return a coverageDataModel that represents the newest data.
   * 
   * @return The coverage data model.
   */
  private CoverageDataModel getCoverageDataModel() {
    CoverageDataModel coverageDataModel = new CoverageDataModel();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    String granularity = session.getContextSensitiveMenu("Coverage Type").getSelectedValue();
    
    for (Project project : session.getSelectedProjects()) {
      Logger logger = ((ProjectBrowserApplication)ProjectBrowserApplication.get()).getLogger();
      logger.info("Getting DPD for project: " + project.getName());
      try {
        CoverageDailyProjectData classData = dpdClient.getCoverage(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), granularity);
        logger.info("Finished getting DPD for project: " + project.getName());
        for (ConstructData data : classData.getConstructData()) {
          coverageDataModel.add(project, data);
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting coverage data for project " + project + ": " +
            e.getMessage());
      }
    }
    return coverageDataModel;
  }
}
