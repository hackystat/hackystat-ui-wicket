package org.hackystat.projectbrowser;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.hackystat.projectbrowser.authentication.SigninPage;

public class ProjectBrowserApplication extends WebApplication {

  @Override
  public Class<? extends Page> getHomePage() {
    return SigninPage.class;
  }

  @Override
  public Session newSession(Request request, Response response) {
  return new ProjectBrowserSession(request);
  }
}
