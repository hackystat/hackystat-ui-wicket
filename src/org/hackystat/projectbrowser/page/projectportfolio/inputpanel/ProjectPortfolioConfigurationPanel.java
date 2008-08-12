package org.hackystat.projectbrowser.page.projectportfolio.inputpanel;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panel for user to configure Project Portfolio.
 * Such as select analyses and configure their parameters.
 * @author Shaoxuan Zhang
 *
 */
public class ProjectPortfolioConfigurationPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 7222939734399409429L;

  /**
   * @param id the wicket component id.
   */
  public ProjectPortfolioConfigurationPanel(String id) {
    super(id);
    
    add(new ProjectPortfolioConfigurationForm("configurationForm"));
    
  }


}
