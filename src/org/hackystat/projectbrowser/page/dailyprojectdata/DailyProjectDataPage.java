package org.hackystat.projectbrowser.page.dailyprojectdata;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.build.BuildPanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.complexity.ComplexityPanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coupling.CouplingPanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.coverage.CoveragePanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.devtime.DevTimePanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel.DpdInputPanel;
import org.hackystat.projectbrowser.page.dailyprojectdata.unittest.UnitTestPanel;

/**
 * Provides a page with DailyProjectData analysis. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Daily project data session to hold the state. */
  private DailyProjectDataSession session = 
    ProjectBrowserSession.get().getDailyProjectDataSession();
  /** The wicket id for the input panel. */
  private static final String dpdInputPanelId = "dpdInputPanel";
  /** The wicket id for the data panel. */
  private static final String dpdDataPanelId = "dpdDataPanel";

  /**
   * Creates the DPD page. 
   */
  public DailyProjectDataPage() {
    add(HeaderContributor.forCss(
        org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataPage.class, 
        "dailyprojectdata.css"));
    add(new DpdInputPanel(dpdInputPanelId, this));
    if ("Coverage".equals(session.getAnalysis())) {
      add(new CoveragePanel(dpdDataPanelId));
    }
    else if ("UnitTest".equals(session.getAnalysis())) {
      add(new UnitTestPanel(dpdDataPanelId));
    }
    else if ("Coupling".equals(session.getAnalysis())) {
      add(new CouplingPanel(dpdDataPanelId));
    }
    else if ("Complexity".equals(session.getAnalysis())) {
      add(new ComplexityPanel(dpdDataPanelId));
    }
    else if ("Build".equals(session.getAnalysis())) {
      add(new BuildPanel(dpdDataPanelId));
    }
    else if ("DevTime".equals(session.getAnalysis())) {
      add(new DevTimePanel(dpdDataPanelId));
    }
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
  }

  /**
   * The action to be performed when the user has set the Project and Date fields. 
   * We will (1) clear all of our data models; (2) update the appropriate data model based upon
   * the user's settings; and (3) set the data panel.
   */
  @Override
  public void onProjectDateSubmit() {
    // Get rid of all prior state.
    session.clearDataModels();
    // Update a single model depending upon the user's settings. 
    if ("Coverage".equals(session.getAnalysis())) {
      session.getCoverageDataModel().update();
      this.replace(new CoveragePanel(dpdDataPanelId));
    }
    else if ("UnitTest".equals(session.getAnalysis())) {
      session.getUnitTestDataModel().update();
      this.replace(new UnitTestPanel(dpdDataPanelId));
    }
    else if ("Coupling".equals(session.getAnalysis())) {
      session.getCouplingDataModel().update();
      this.replace(new CouplingPanel(dpdDataPanelId));
    }
    else if ("Complexity".equals(session.getAnalysis())) {
      session.getComplexityDataModel().update();
      this.replace(new ComplexityPanel(dpdDataPanelId));
    }
    else if ("Build".equals(session.getAnalysis())) {
      session.getBuildDataModel().update();
      this.replace(new BuildPanel(dpdDataPanelId));
    }
    else if ("DevTime".equals(session.getAnalysis())) {
      session.getDevTimeDataModel().update(); 
      this.replace(new DevTimePanel(dpdDataPanelId));
    }
  }
}
