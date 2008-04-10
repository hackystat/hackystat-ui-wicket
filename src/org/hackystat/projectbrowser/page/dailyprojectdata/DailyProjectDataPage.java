package org.hackystat.projectbrowser.page.dailyprojectdata;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with DailyProjectData analyses. 
 * @author Philip Johnson
 */
public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Creates the DPD page. 
   */
  public DailyProjectDataPage() {
    add(new Label("PageContents", "Daily Project Data Page contents here."));
  }
}
