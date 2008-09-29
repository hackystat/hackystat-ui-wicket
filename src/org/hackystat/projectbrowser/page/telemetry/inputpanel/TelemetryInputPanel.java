package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Panel to let user select the project and telemetry to display.
 * @author Shaoxuan Zhang
 */
public class TelemetryInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public TelemetryInputPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    add(new TelemetryInputForm("inputForm", page));
    
    Button cancelButton = new Button("cancel") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        ProjectBrowserSession.get().getTelemetrySession().cancelDataUpdate();
      }
      @Override
      public boolean isEnabled() {
        return ProjectBrowserSession.get().getTelemetrySession().getDataModel().isInProcess();
      }
    };
    add(new Form("cancelForm").add(cancelButton));
  }

}
