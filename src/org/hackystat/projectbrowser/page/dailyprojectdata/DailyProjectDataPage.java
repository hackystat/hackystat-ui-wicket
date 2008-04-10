package org.hackystat.projectbrowser.page.dailyprojectdata;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  public DailyProjectDataPage() {
    add(new Label("PageContents", "Daily Project Data Page contents here."));
  }

}
