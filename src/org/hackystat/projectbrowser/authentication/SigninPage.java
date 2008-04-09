package org.hackystat.projectbrowser.authentication;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.telemetry.service.client.TelemetryClient;

/**
 * Provides a signin page for either logging in with a previous username and password, or 
 * else registering with the system. 
 * 
 * @author Philip Johnson
 */
public class SigninPage extends WebPage {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Create the SigninPage. 
   */
  public SigninPage() {
    add(new SigninForm("signinForm"));
    add(new Label("signinFeedback", 
        new PropertyModel(ProjectBrowserSession.get(), "signinFeedback")));
    add(new RegisterForm("registerForm"));
    add(new Label("registerFeedback", 
        new PropertyModel(ProjectBrowserSession.get(), "registerFeedback")));
   
    add(new Label("serviceInfo", getServiceInfo()));
  }
  
  /**
   * Returns a string indicating which services are available. 
   * @return A string indicating service availability. 
   */
  private String getServiceInfo() {
    ProjectBrowserApplication app = (ProjectBrowserApplication)getApplication();
    String sensorbase = app.getProjectBrowserProperty(ProjectBrowserProperties.SENSORBASE_HOST_KEY);
    boolean sensorbaseOk = SensorBaseClient.isHost(sensorbase);
    String dpd = app.getProjectBrowserProperty(ProjectBrowserProperties.DAILYPROJECTDATA_HOST_KEY);
    boolean dpdOk = DailyProjectDataClient.isHost(dpd);
    String telemetry = app.getProjectBrowserProperty(ProjectBrowserProperties.TELEMETRY_HOST_KEY);
    boolean telemetryOk = TelemetryClient.isHost(telemetry);
    
    StringBuffer serviceInfo = new StringBuffer();
    if (sensorbaseOk || dpdOk || telemetryOk) {
      serviceInfo.append("Contacted: ");
      serviceInfo.append(sensorbaseOk ? sensorbase + " " : "");
      serviceInfo.append(dpdOk ? dpd + " " : "");
      serviceInfo.append(telemetryOk ? telemetry + " " : "");
    }
    if (!sensorbaseOk || !dpdOk || !telemetryOk) {
      serviceInfo.append("Failed to contact: ");
      serviceInfo.append(!sensorbaseOk ? sensorbase + " ": "");
      serviceInfo.append(!dpdOk ? dpd + " " : "");
      serviceInfo.append(!telemetryOk ? telemetry + " " : "");
    }
    return serviceInfo.toString();
  }
}
