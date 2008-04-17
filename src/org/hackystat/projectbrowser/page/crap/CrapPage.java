package org.hackystat.projectbrowser.page.crap;

import org.apache.wicket.markup.html.basic.Label;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with Crap analyses. 
 * @author Philip Johnson
 */
public class CrapPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs the telemetry page. 
   */
  public CrapPage() {
    add(new Label("PageContents", "Crap Page contents here."));
  }
}