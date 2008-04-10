package org.hackystat.projectbrowser;

import java.util.logging.Logger;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.page.ProjectBrowserPageAuthentication;
import org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataPage;
import org.hackystat.projectbrowser.page.projects.ProjectsPage;
import org.hackystat.projectbrowser.page.sensordata.SensorDataPage;
import org.hackystat.projectbrowser.page.telemetry.TelemetryPage;
import org.hackystat.utilities.logger.HackystatLogger;

/**
 * The top-level web application instance for this ProjectBrowser. 
 * @author Philip Johnson
 */
public class ProjectBrowserApplication extends WebApplication {
  
  /** Holds the properties read in from ~/.hackystat/projectbrowser/projectbrowser.properties. */
  private ProjectBrowserProperties properties;
  /** Holds the HackystatLogger for this Service. */
  private Logger logger; 
  

  /**
   * Returns the home page for this application (SigninPage).
   * @return The home page. 
   */
  @Override
  public Class<? extends Page> getHomePage() {
    return SigninPage.class;
  }


  /**
   * Defines ProjectBrowserSession as the session instance created in this app. 
   * @param request The request. 
   * @param response The response.
   * @return The current ProjectBrowserSession instance. 
   */
  @Override
  public Session newSession(Request request, Response response) {
    return new ProjectBrowserSession(request);
  }
  
  /**
   * Do default setup and initialization when this web application is started up. 
   */
  @Override
  public void init() {
    this.properties = new ProjectBrowserProperties();
    this.logger = HackystatLogger.getLogger("org.hackystat.projectbrowser", "projectbrowser");
    mountBookmarkablePage("sensordata", SensorDataPage.class);
    mountBookmarkablePage("projects", ProjectsPage.class);
    mountBookmarkablePage("dailyprojectdata", DailyProjectDataPage.class);
    mountBookmarkablePage("telemetry", TelemetryPage.class);
    getSecuritySettings().setAuthorizationStrategy(new ProjectBrowserPageAuthentication());
    super.init();

  }
  
  /**
   * Returns the ProjectBrowserProperties instance associated with this web app.
   * @return The properties.
   */
  public ProjectBrowserProperties getProjectBrowserProperties() {
    return this.properties;
  }
  
  /**
   * Returns the value associated with key, or null if not found. Key should be one of the public
   * static strings declared in ProjectBrowserProperties. 
   * @param key The key. 
   * @return The value associated with this key, or null if not found. 
   */
  public String getProjectBrowserProperty(String key) {
    return this.properties.get(key);
  }
  
  /**
   * Returns the logger for this service.
   * @return The logger.
   */
  public Logger getLogger() {
    return this.logger;
  }
  
  /**
   * Returns the DPD host.
   * @return The DPD host. 
   */
  public String getDailyProjectDataHost() {
    return getProjectBrowserProperty(ProjectBrowserProperties.DAILYPROJECTDATA_HOST_KEY);
  }

  /**
   * Returns the sensorbase host.
   * @return The sensorbase host. 
   */
  public String getSensorBaseHost() {
    return getProjectBrowserProperty(ProjectBrowserProperties.SENSORBASE_HOST_KEY);
  }

  /**
   * Returns the telemetry host.
   * @return The telemetry host. 
   */
  public String getTelemetryHost() {
    return getProjectBrowserProperty(ProjectBrowserProperties.TELEMETRY_HOST_KEY);
  }

}
