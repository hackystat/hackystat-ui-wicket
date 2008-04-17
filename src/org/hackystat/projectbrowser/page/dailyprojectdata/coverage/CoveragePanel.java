package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClientException;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;
import org.hackystat.dailyprojectdata.resource.coverage.jaxb.CoverageDailyProjectData;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataSession;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Data panel for coverage.
 * @author Shaoxuan Zhang
 *
 */
public class CoveragePanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** collection of granularities. */
  private String[] granularities = {"class", "method", "line", "block"};

  /**
   * @param id wicket component id.
   */
  public CoveragePanel(String id) {
    super(id);
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    this.setModel(new CompoundPropertyModel(this));
    if (session.getProject() == null) {
      add(new Label("projectName", ""));
      add(new Label("projectOwner", ""));
    }
    else {
      add(new Label("projectName", session.getProject().getName()));
      add(new Label("projectOwner", session.getProject().getOwner()));
    }
    add(new Label("date", new PropertyModel(session, "dateString")));
    CoverageDataModel dataModel = (CoverageDataModel) session.getDataModel();
    if (dataModel == null) {
      dataModel = getCoverageDataModel();
      session.setDataModel(dataModel);
    }
    add(new Label("classCoverage", new PropertyModel(dataModel, "classCoverage")));
    ListView memberDataListView = 
      new ListView("memberDataList", new PropertyModel(dataModel, "coverageDataList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        CoverageData coverageData = (CoverageData) item.getModelObject();
        item.add(new Label("name", getEmailFromUri(coverageData.getName())));
        item.add(new Label("methodCoverage", coverageData.getDisplayString("method")));
        item.add(new Label("lineCoverage", coverageData.getDisplayString("line")));
        item.add(new Label("blockCoverage", coverageData.getDisplayString("block")));
      }

      private String getEmailFromUri(String memberUri) {
        int index = memberUri.lastIndexOf('/');
        return memberUri.substring(index + 1);
      }
    };
    add(memberDataListView);
  }

  /**
   * Return a coverageDataModel that represents the newest data.
   * @return the coverage data model.
   */
  private CoverageDataModel getCoverageDataModel() {
    CoverageDataModel coverageDataModel = new CoverageDataModel();
    DailyProjectDataClient dpdClient = ProjectBrowserSession.get().getDailyProjectDataClient();
    DailyProjectDataSession session = ProjectBrowserSession.get().getDailyProjectDataSession();
    for (String granularity : granularities) {
      try {
        CoverageDailyProjectData classData = dpdClient.getCoverage(session.getProject().getOwner(), 
            session.getProject().getName(),
            Tstamp.makeTimestamp(session.getDate().getTime()), granularity);
        for (ConstructData data : classData.getConstructData()) {
          coverageDataModel.add(data, classData.getGranularity());
        }
      }
      catch (DailyProjectDataClientException e) {
        session.setFeedback("Exception when getting coverage data: " + e.getMessage());
      }
    }
    return coverageDataModel;
  }

}
