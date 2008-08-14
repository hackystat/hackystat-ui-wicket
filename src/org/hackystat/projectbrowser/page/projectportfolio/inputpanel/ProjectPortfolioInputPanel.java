package org.hackystat.projectbrowser.page.projectportfolio.inputpanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioPage;

/**
 * Panel to let user select the project to display.
 * @author Shaoxuan Zhang
 */
public class ProjectPortfolioInputPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 5389750577845203444L;

  /**
   * @param id the wicket id.
   * @param page the page this panel is attached to.
   */
  public ProjectPortfolioInputPanel(String id, ProjectPortfolioPage page) {
    super(id);
    add(new ProjectPortfolioInputForm("inputForm", page));
  }

}
