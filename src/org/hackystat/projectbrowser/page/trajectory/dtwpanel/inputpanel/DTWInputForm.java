package org.hackystat.projectbrowser.page.trajectory.dtwpanel.inputpanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;

/**
 * The input form for the DTW alignment parameters.
 * 
 * @author Pavel Senin.
 * 
 */
public class DTWInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** The page containing this form. */
  // private TrajectoryDTWInputPanel page = null;
  /** TelemetrySession that holds page state for telemetry. */
  private TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();

  /**
   * Constructor.
   * 
   * @param id The panel ID.
   * @param pane The parent pane.
   */
  public DTWInputForm(String id, TrajectoryDTWInputPanel pane) {

    super(id);
    // this.page = pane;

    // Create the drop-down menu for step patterns.
    DropDownChoice stepMenu = new DropDownChoice("dtwStep", new PropertyModel(session, "DTWStep"),
        new PropertyModel(session, "availableDTWSteps")) {
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean wantOnSelectionChangedNotifications() {
        return true;
      }

      // @Override
      // protected void onSelectionChanged(java.lang.Object newSelection) {
      // session.getParameters().clear();
      // }

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    stepMenu.setRequired(true);
    add(stepMenu);
    session.setDTWStep(session.getAvailableDTWSteps().get(0));

    // Create the drop-down menu for windowing.
    DropDownChoice windowMenu = new DropDownChoice("windowType", new PropertyModel(session,
        "windowType"), new PropertyModel(session, "availableWindowTypes")) {
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean wantOnSelectionChangedNotifications() {
        return true;
      }

      // @Override
      // protected void onSelectionChanged(java.lang.Object newSelection) {
      // session.getParameters().clear();
      // }

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    session.setWindowType(session.getAvailableWindowTypes().get(0));
    windowMenu.setRequired(true);
    add(windowMenu);

    // Create the drop-down menu for windowing.
    DropDownChoice openEndMenu = new DropDownChoice("openEndType", new PropertyModel(session,
        "openEndType"), new PropertyModel(session, "availableOpenEndTypes")) {
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean wantOnSelectionChangedNotifications() {
        return true;
      }

      // @Override
      // protected void onSelectionChanged(java.lang.Object newSelection) {
      // session.getParameters().clear();
      // }

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    openEndMenu.setRequired(true);
    add(openEndMenu);
    session.setOpenEndType(session.getAvailableOpenEndTypes().get(0));

    // indent for the dates
    TextField windowSizeTextField = new TextField("windowSizeTextField", new PropertyModel(session,
        "windowSize"), java.lang.Integer.class) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    windowSizeTextField.setRequired(false);
    add(windowSizeTextField);

    Button submitButton = new Button("doDtw") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        getLogger().log(Level.FINER, "[DEBUG] doDTW is HIT");
        if ((session.getDataModel().updateSelectedChart())
            && (session.getDataModel().updateNormalizedTSChart())
            && (session.getDataModel().updateDTWChart())) {
          assert true;
        }
        else {
          session.setFeedback("Failed to run DTW analysis!");
        }
      }

    };
    submitButton.setDefaultFormProcessing(false);
    add(submitButton);

  }

  /**
   * @return true if the form is enabled.
   */
  protected final boolean getIsEnable() {
    return !ProjectBrowserSession.get().getTelemetrySession().getDataModel().isInProcess();
  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

}
