package org.hackystat.projectbrowser.page.trajectory;

import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.LoadingProcessPanel;
import org.hackystat.projectbrowser.page.trajectory.datapanel.TrajectoryDataPanel;
import org.hackystat.projectbrowser.page.trajectory.dtwpanel.datapanel.TrajectoryDTWDataPanel;
import org.hackystat.projectbrowser.page.trajectory.dtwpanel.inputpanel.TrajectoryDTWInputPanel;
import org.hackystat.projectbrowser.page.trajectory.inputpanel.TrajectoryInputPanel;

/**
 * Provides a page with Trajectory analyzes.
 * 
 * @author Pavel Senin, Philip Johnson, Shaoxuan Zhang
 * 
 * 
 */
public class TrajectoryPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Trajectory session to hold up the state. */
  private TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();
  /** the TelemetryInputPanel in this page. */
  private TrajectoryInputPanel inputPanel;
  /** the TelemetryDataPanel in this page. */
  private TrajectoryDataPanel dataPanel;
  /** the LoadingProcessPanel in this page. */
  private LoadingProcessPanel loadingProcessPanel;

  // Some string constants used while logging
  //
  private static final String MARK = "[DEBUG] ";

  /**
   * Constructs the telemetry page without URL parameters.
   */
  public TrajectoryPage() {
    getLogger().info(MARK + "Trajectory page constructor invoked, hash: " + this.hashCode());
    
    add(HeaderContributor.forCss(
        org.hackystat.projectbrowser.page.trajectory.TrajectoryPage.class, 
        "trajectorypage.css"));
    
    inputPanel = new TrajectoryInputPanel("inputPanel", this);
    inputPanel.setOutputMarkupId(true);
    add(inputPanel);

    dataPanel = new TrajectoryDataPanel("dataPanel", this);
    dataPanel.setOutputMarkupId(true);
    add(dataPanel);

    loadingProcessPanel = new LoadingProcessPanel("loadingProcessPanel", session.getDataModel()) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void onFinished(AjaxRequestTarget target) {
        setResponsePage(TrajectoryPage.class);
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
   * 
   * @param parameters the parameters from URL request.
   */
  public TrajectoryPage(PageParameters parameters) {
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
    // setResponsePage(TelemetryPage.class, session.getPageParameters());
  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

  /**
   * Starts the DTW dialog.
   */
  public void goDTW() {
    TrajectoryDTWInputPanel dtwInputPanel = new TrajectoryDTWInputPanel("inputPanel", this);
    dtwInputPanel.setOutputMarkupId(true);

    TrajectoryDTWDataPanel dtwDataPanel = new TrajectoryDTWDataPanel("dataPanel", this);
    dtwDataPanel.setOutputMarkupId(true);

    remove(inputPanel);
    add(dtwInputPanel);

    remove(dataPanel);
    add(dtwDataPanel);

  }
}
