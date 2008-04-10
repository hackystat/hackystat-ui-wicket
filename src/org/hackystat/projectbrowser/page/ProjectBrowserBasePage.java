package org.hackystat.projectbrowser.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.authentication.SigninPage;
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
public class ProjectBrowserBasePage extends WebPage implements IAuthorizationStrategy,
    IUnauthorizedComponentInstantiationListener {
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
      private static final long serialVersionUID = 1L;

      /** Go to the SensorDataPage. */
      @Override
      public void onClick() {
        getSession().invalidate();
        setResponsePage(getApplication().getHomePage());
      }
    });
    add(new Label("UserEmail", new PropertyModel(ProjectBrowserSession.get(), "userEmail")));
  }

  public boolean isActionAuthorized(Component arg0, Action arg1) {
    System.out.println("Calling action-level authorization");
    return true;
  }

  public boolean isInstantiationAuthorized(Class componentClass) {
    System.out.println("calling authorization");
    if (ProjectBrowserBasePage.class.isAssignableFrom(componentClass)) {
      System.out.println("calling isAuthenticated from session");
      return ProjectBrowserSession.get().isAuthenticated();
    }
    return true;
  }

  public void onUnauthorizedInstantiation(Component componentClass) {
    throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());

  }
}
