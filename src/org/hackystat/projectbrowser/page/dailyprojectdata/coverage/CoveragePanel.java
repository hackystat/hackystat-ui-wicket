package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.CoverageDailyProjectData;
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
    // this.setModel(new CompoundPropertyModel(this));
    // prepare the data model.
    CoverageDataModel dataModel = (CoverageDataModel) session.getDataModel();
    if (dataModel == null) {
      dataModel = getCoverageDataModel();
      session.setDataModel(dataModel);
    }
    ListView memberDataListView = new ListView("coverageDataList", new PropertyModel(dataModel,
        "coverageDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CoverageData coverageData = (CoverageData) item.getModelObject();
        item.add(new Label("project", coverageData.getProject().getName()));
        item.add(new Label("bucket0", coverageData.getBucketValueString(0)));
        item.add(new Label("bucket0%", coverageData.getBucketPercentageString(0)));
        item.add(new Label("bucket1", coverageData.getBucketValueString(1)));
        item.add(new Label("bucket1%", coverageData.getBucketPercentageString(1)));
        item.add(new Label("bucket2", coverageData.getBucketValueString(2)));
        item.add(new Label("bucket2%", coverageData.getBucketPercentageString(2)));
        item.add(new Label("bucket3", coverageData.getBucketValueString(3)));
        item.add(new Label("bucket3%", coverageData.getBucketPercentageString(3)));
        item.add(new Label("bucket4", coverageData.getBucketValueString(4)));
        item.add(new Label("bucket4%", coverageData.getBucketPercentageString(4)));
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
    String granularity = "method"; // will make this a parameter later.

    for (Project project : session.getSelectedProjects()) {
      System.out.println("Getting DPD for project: " + project.getName());
      try {
        CoverageDailyProjectData classData = dpdClient.getCoverage(project.getOwner(),
            project.getName(), Tstamp.makeTimestamp(session.getDate().getTime()), granularity);
        System.out.println("Finished getting DPD for project: " + project.getName());
        for (ConstructData data : classData.getConstructData()) {
          coverageDataModel.add(project, data);
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting coverage data: " + e.getMessage());
      }
    }
    return coverageDataModel;
  }
}
