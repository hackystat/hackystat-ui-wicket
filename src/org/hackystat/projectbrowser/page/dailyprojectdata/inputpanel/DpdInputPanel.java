package org.hackystat.projectbrowser.page.dailyprojectdata.inputpanel;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Panel to let user select the date and project to display.
 * @author Shaoxuan Zhang
 */
public class DpdInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public DpdInputPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    DpdInputForm dpdInputForm = new DpdInputForm("dpdInputForm", page);
    dpdInputForm.add(new FeedbackPanel("feedback"));
    add(dpdInputForm);
  }


}
