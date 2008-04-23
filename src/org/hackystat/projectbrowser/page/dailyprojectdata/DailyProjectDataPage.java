package org.hackystat.projectbrowser.page.dailyprojectdata;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.coverage.CoveragePanel;
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
  /** Daily project data session to hold up the state. */
  private DailyProjectDataSession session = 
    ProjectBrowserSession.get().getDailyProjectDataSession();
  /** the wicket id for the data panel. */
  private static final String WICKET_PANEL_ID = "dataPanel";
  /**
   * Return a panel according the analysis field in DailyProjectDataSession.
   * @param id the wicket id.
   * @return the panel
   */
  private Panel getDataPanel(String id) {
    Panel panel;
    String analysis = session.getAnalysis();
    if ("Unit Test".equals(analysis)) {
      panel = new UnitTestPanel(id);
    }
    else if ("Coverage".equals(analysis)) {
      panel = new CoveragePanel(id);
    }
    else {
      panel = new Panel(id);
      panel.setVisible(false);
    }
    return panel;
  }

  /**
   * Creates the DPD page. 
   */
  public DailyProjectDataPage() {
    add(new DpdInputPanel("projectDatePanel", this));
    add(new Panel(WICKET_PANEL_ID).setVisible(false));
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
  }

  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    session.setDataModel(null);
    session.setFeedback("Selected projects are: " + session.getSelectedProjects());
    this.replace(getDataPanel(WICKET_PANEL_ID));
  }
}
