package org.hackystat.projectbrowser.page.todate;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.LoadingProcessPanel;
import org.hackystat.projectbrowser.page.todate.configurationpanel.ToDateConfigurationPanel;
import org.hackystat.projectbrowser.page.todate.detailpanel.ToDateDetailPanel;
import org.hackystat.projectbrowser.page.todate.inputpanel.ToDateInputPanel;

/**
 * Provides a page with Telemetry analyses. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class ToDatePage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Telemetry session to hold up the state. */
  private ToDateSession session = ProjectBrowserSession.get().getToDateSession();
  /** the LoadingProcessPanel in this page. */
  private LoadingProcessPanel loadingProcessPanel;
  /** the ProjectPortfolioConfigurationPanel in this page. */
  private ToDateConfigurationPanel configurationPanel;
  /**
   * Constructs the telemetry page without URL parameters.
   */
  public ToDatePage() {
    //session.clearParamErrorMessage();
    add(new FeedbackPanel("feedback"));

    session.initializeDataModel();

    configurationPanel = new ToDateConfigurationPanel("configurationPanel",
                                                                session.getDataModel());
    configurationPanel.setOutputMarkupId(true);
    configurationPanel.setVisible(false);
    add(configurationPanel);
    
    ToDateInputPanel inputPanel = new ToDateInputPanel("inputPanel", this);
    inputPanel.setOutputMarkupId(true);
    add(inputPanel);
    
    ToDateDetailPanel detailPanel = new ToDateDetailPanel("detailPanel", session);
    detailPanel.setOutputMarkupId(true);
    add(detailPanel);
    
    loadingProcessPanel = new LoadingProcessPanel("loadingProcessPanel", session.getDataModel()) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void onFinished(AjaxRequestTarget target) {
        setResponsePage(ToDatePage.class);
      }
    };
    loadingProcessPanel.setOutputMarkupId(true);
    add(loadingProcessPanel);
    
    this.get("FooterFeedback").setModel(new PropertyModel(session, "feedback"));
    this.get("FooterFeedback").setOutputMarkupId(true);
    
    add(new MultiLineLabel("paramErrorMessage", new PropertyModel(session, "paramErrorMessage")));
  }

  /**
   * Constructs the telemetry page. 
   * @param parameters the parameters from URL request.
   */
  public ToDatePage(PageParameters parameters) {
    this();
    boolean isLoadSucceed = session.loadPageParameters(parameters);

    if (isLoadSucceed) {
      session.updateDataModel();
      loadingProcessPanel.start();
    }
  }

  /**
   * @return true if ConfigurationPanel is visible.
   */
  public boolean isConfigurationPanelVisible() {
    return configurationPanel.isVisible();
  }
  
  /**
   * @param visible the visible boolean to set.
   */
  public void setConfigurationPanelVisible(boolean visible) {
    configurationPanel.setVisible(visible);
  }
  
  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    loadingProcessPanel.start();
    setResponsePage(ToDatePage.class);
  }
}
