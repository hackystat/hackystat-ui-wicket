package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.LoadingProcessPanel;
import org.hackystat.projectbrowser.page.telemetry.datapanel.TelemetryDataPanel;
import org.hackystat.projectbrowser.page.telemetry.inputpanel.TelemetryInputPanel;

/**
 * Provides a page with Telemetry analyses. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class TelemetryPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Telemetry session to hold up the state. */
  private TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
  /** the TelemetryInputPanel in this page. */
  private TelemetryInputPanel inputPanel;
  /** the TelemetryDataPanel in this page. */
  private TelemetryDataPanel dataPanel;
  /** the LoadingProcessPanel in this page. */
  private LoadingProcessPanel loadingProcessPanel;
  /**
   * Constructs the telemetry page without URL parameters.
   */
  public TelemetryPage() {
    //session.clearParamErrorMessage();
    
    inputPanel = new TelemetryInputPanel("inputPanel", this);
    inputPanel.setOutputMarkupId(true);
    add(inputPanel);
    
    dataPanel = new TelemetryDataPanel("dataPanel");
    dataPanel.setOutputMarkupId(true);
    add(dataPanel);
    
    loadingProcessPanel = new LoadingProcessPanel("loadingProcessPanel", session.getDataModel()) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void onFinished(AjaxRequestTarget target) {
        setResponsePage(TelemetryPage.class);
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
  public TelemetryPage(PageParameters parameters) {
    this();
    boolean isLoadSucceed = session.loadPageParameters(parameters);

    if (isLoadSucceed) {
      session.updateDataModel();
      loadingProcessPanel.start();
    }
  }
  
  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    loadingProcessPanel.start();
    setResponsePage(TelemetryPage.class);
  }
}
