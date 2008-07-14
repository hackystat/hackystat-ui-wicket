package org.hackystat.projectbrowser.page.projectportfolio;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.LoadingProcessPanel;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDetailsPanel;
import org.hackystat.projectbrowser.page.projectportfolio.inputpanel.ProjectPortfolioInputPanel;

/**
 * Provides a page with Project Portfolio.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioPage extends ProjectBrowserBasePage {

  /** For serialization. */
  private static final long serialVersionUID = 1L;
  /** ProjectPortfolio session to hold up the state. */
  private ProjectPortfolioSession session = 
    ProjectBrowserSession.get().getProjectPortfolioSession();
  /** the LoadingProcessPanel in this page. */
  private LoadingProcessPanel loadingProcessPanel;
  /** the ProjectPortfolioInputPanel in this page. */
  private ProjectPortfolioInputPanel inputPanel;
  /** the ProjectPortfolioDetailsPanel in this page. */
  private ProjectPortfolioDetailsPanel detailsPanel;
  
  /**
   * Construct the page.
   */
  public ProjectPortfolioPage () {
    super();

    inputPanel = new ProjectPortfolioInputPanel("inputPanel", this);
    inputPanel.setOutputMarkupId(true);
    add(inputPanel);

    detailsPanel = new ProjectPortfolioDetailsPanel("detailPanel");
    detailsPanel.setOutputMarkupId(true);
    add(detailsPanel);
    
    loadingProcessPanel = new LoadingProcessPanel("loadingProcessPanel", session.getDataModel()) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void onFinished(AjaxRequestTarget target) {
        setResponsePage(ProjectPortfolioPage.class);
      }
    };
    loadingProcessPanel.setOutputMarkupId(true);
    add(loadingProcessPanel);
    
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
    this.get("FooterFeedback").setOutputMarkupId(true);
  }

  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    loadingProcessPanel.start();
    setResponsePage(ProjectPortfolioPage.class);
  }
}
