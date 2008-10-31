package org.hackystat.projectbrowser.page.todate.inputpanel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.todate.ToDatePage;

/**
 * Panel to let user select the project and telemetry to display.
 * @author Shaoxuan Zhang
 */
public class ToDateInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public ToDateInputPanel(String id, ToDatePage page) {
    super(id);
    add(new ToDateInputForm("inputForm", page));
    
    Button cancelButton = new Button("cancel") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        ProjectBrowserSession.get().getToDateSession().cancelDataUpdate();
      }
      @Override
      public boolean isEnabled() {
        return ProjectBrowserSession.get().getToDateSession().getDataModel().isInProcess();
      }
    };
    add(new Form("cancelForm").add(cancelButton));
  }

}
