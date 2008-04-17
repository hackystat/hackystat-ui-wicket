package org.hackystat.projectbrowser.page.dailyprojectdata;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.unittest.UnitTestPanel;
import org.hackystat.projectbrowser.page.projectdatepanel.ProjectDatePanel;

/**
 * Provides a page with DailyProjectData analysis. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Daily project data session to hold up the data. */
  private DailyProjectDataSession session = 
    ProjectBrowserSession.get().getDailyProjectDataSession();
  /** the wicket id for the data panel. */
  private static final String WICKET_PANEL_ID = "dataPanel";
  /**
   * Creates the DPD page. 
   * @param parameters parameters to configure the page.
   */
  public DailyProjectDataPage(PageParameters parameters) {
    add(new ProjectDatePanel("projectDatePanel", this));
    add(getDataPanel(WICKET_PANEL_ID));
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
  }

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
    else {
      panel = new Panel(id);
      panel.setVisible(false);
    }
    return panel;
  }

  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    session.setDataModel(null);
    this.replace(getDataPanel(WICKET_PANEL_ID));
  }
}
