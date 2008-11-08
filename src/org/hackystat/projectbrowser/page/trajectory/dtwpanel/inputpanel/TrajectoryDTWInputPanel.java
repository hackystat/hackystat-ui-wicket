package org.hackystat.projectbrowser.page.trajectory.dtwpanel.inputpanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;

/**
 * The left input pabel for the DTW analysis parameters.
 * 
 * @author Pavel Senin
 * 
 */
public class TrajectoryDTWInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that hold the page state. */
  TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();

  /**
   * Constructor.
   * 
   * @param id the wicket component id.
   * @param page the parent pane.
   */
  public TrajectoryDTWInputPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    getLogger().log(Level.FINER, "[DEBUG] TrajectoryDTWInputPanel constructed.");
    add(new FeedbackPanel("feedback"));

    add(new MultiLineLabel("stream1Statistics", session.getDataModel().getStream1Statistics()));

    add(new MultiLineLabel("stream2Statistics", session.getDataModel().getStream2Statistics()));

    add(new DTWInputForm("dtwOptions", this));

    add(new MultiLineLabel("dtwStatistics", session.getDataModel().getDTWStatistics()));

    // add(new TrajectoryDTWInputForm("dtwInputForm", page));

    Button cancelButton = new Button("cancel") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        ProjectBrowserSession.get().getTrajectorySession().cancelDataUpdate();
      }

      @Override
      public boolean isEnabled() {
        return ProjectBrowserSession.get().getTrajectorySession().getDataModel().isInProcess();
      }
    };
    add(new Form("dtwCancelForm").add(cancelButton));
  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

}
