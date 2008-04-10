package org.hackystat.projectbrowser.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.authentication.SigninPage;

/**
 * Implements authorization for all pages other than the home page.
 * Users must sign in with their sensorbase email and password.   
 * @author Philip Johnson
 */
public class ProjectBrowserPageAuthentication implements IAuthorizationStrategy,
IUnauthorizedComponentInstantiationListener {

  /**
   * Sets up the authorization strategy.
   */
  public ProjectBrowserPageAuthentication() {
    ProjectBrowserApplication.get().getSecuritySettings()
    .setUnauthorizedComponentInstantiationListener(this);
  }
  
  /**
   * Individual component actions are always authorized. 
   * @param component The component in question. 
   * @param action The component in question. 
   * @return Always true.
   */
  public boolean isActionAuthorized(Component component, Action action) {
    return true;
  }

  /**
   * Page-level retrieval must be authenticated by checking the session instance to see if the user
   * is signed in. 
   * @param component The component in question. 
   * @return True if this user is signed in with a valid sensorbase user name and password.
   */
  @SuppressWarnings("unchecked")
  public boolean isInstantiationAuthorized(Class component) {
    if (ProjectBrowserBasePage.class.isAssignableFrom(component)) {
      return ProjectBrowserSession.get().isAuthenticated();
    }
    return true;
  }

  /**
   * Upon authentication failure, redirect to home page with a helpful message. 
   * @param component The component in question. 
   */
  public void onUnauthorizedInstantiation(Component component) {
    ProjectBrowserSession.get().setSigninFeedback("You must login to retrieve that page.");
    throw new RestartResponseAtInterceptPageException(SigninPage.class);
  }

}
