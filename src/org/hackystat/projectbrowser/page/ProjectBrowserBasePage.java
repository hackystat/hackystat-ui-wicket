package org.hackystat.projectbrowser.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataPage;
import org.hackystat.projectbrowser.page.projects.ProjectsPage;
import org.hackystat.projectbrowser.page.sensordata.SensorDataPage;
import org.hackystat.projectbrowser.page.telemetry.TelemetryPage;

/**
 * Provides a base class with associated markup that all ProjectBrowser pages (except for signin)
 * should inherit from. See http://wicket.apache.org/examplemarkupinheritance.html for an
 * explanation of how this works.
 * 
 * @author Philip Johnson
 */
public class ProjectBrowserBasePage extends WebPage {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create the ProjectBrowserBasePage.
   */
  public ProjectBrowserBasePage() {
    add(new BookmarkablePageLink("SensorDataPageLink", SensorDataPage.class));
    add(new BookmarkablePageLink("ProjectsPageLink", ProjectsPage.class));
    add(new BookmarkablePageLink("DailyProjectDataPageLink", DailyProjectDataPage.class));
    add(new BookmarkablePageLink("TelemetryPageLink", TelemetryPage.class));
    add(new Link("LogoutLink") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      /** Go to the home page after invalidating the session. */
      @Override
      public void onClick() {
        getSession().invalidate();
        setResponsePage(getApplication().getHomePage());
      }
    });
    add(new Label("UserEmail", new PropertyModel(ProjectBrowserSession.get(), "userEmail")));
  }
}
