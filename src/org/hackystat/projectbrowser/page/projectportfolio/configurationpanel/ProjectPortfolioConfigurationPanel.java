package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;

/**
 * Panel for user to configure Project Portfolio.
 * Such as select analyses and configure their parameters.
 * @author Shaoxuan Zhang
 *
 */
public class ProjectPortfolioConfigurationPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 7222939734399409429L;
  /** The configuration form to use. */
  private ProjectPortfolioConfigurationForm configForm;

  /**
   * @param id the wicket component id.
   * @param dataModel the data model that will be configure here.
   */
  public ProjectPortfolioConfigurationPanel(String id, ProjectPortfolioDataModel dataModel) {
    super(id);
    
    configForm = 
      new ProjectPortfolioConfigurationForm("configurationForm", dataModel);
    configForm.setOutputMarkupId(true);
    add(configForm);
  }

  /**
   * @return the configuration form.
   */
  public ProjectPortfolioConfigurationForm getForm() {
    return this.configForm;
  }
}
