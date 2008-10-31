package org.hackystat.projectbrowser.page.todate.configurationpanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.todate.detailpanel.ToDateDataModel;

/**
 * Panel for user to configure Project Portfolio.
 * Such as select analyses and configure their parameters.
 * @author Shaoxuan Zhang
 *
 */
public class ToDateConfigurationPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 7222939734399409429L;

  /**
   * @param id the wicket component id.
   * @param dataModel the data model that will be configure here.
   */
  public ToDateConfigurationPanel(String id, ToDateDataModel dataModel) {
    super(id);
    
    ToDateConfigurationForm configForm = 
      new ToDateConfigurationForm("configurationForm", dataModel);
    configForm.setOutputMarkupId(true);
    add(configForm);
    
  }

}
