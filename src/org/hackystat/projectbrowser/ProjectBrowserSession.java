package org.hackystat.projectbrowser;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.hackystat.dailyprojectdata.client.DailyProjectDataClient;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.telemetry.service.client.TelemetryClient;

/**
 * Provides a session instance that holds authentication credentials.
 * @author Philip Johnson
 *
 */
public class ProjectBrowserSession extends WebSession {
  /** Support serialization */
  private static final long serialVersionUID = 1L;
  /** The email used to connect to the SensorBase. */
  private String email = null;
  /** The password for the SensorBase. */
  private String password = null;
  /** The SensorBase client for this user. */
  // Need to make this class serializable if we want to keep it in the session and not
  // make a new one each request. 
  //private SensorBaseClient client = null; 
  /** The current signinFeedback message to display. */
  private String signinFeedback = "";
  /** The current registerFeedback message to display. */
  private String registerFeedback = "";
  /** If this user has been authenticated against the Sensorbase during this session. */
  private boolean isAuthenticated = false;
  
  /**
   * Provide a constructor that initializes WebSession.
   * @param request The request object.
   */
  public ProjectBrowserSession(Request request) {
    super(request);
  }

  /**
   * Obtain the current session. 
   * @return The current ProjectBrowserSession.
   */
  public static ProjectBrowserSession get() {
    return (ProjectBrowserSession) Session.get();
  }
  
  /**
   * Returns true if the user has been authenticated in this session.
   * @return True if the user has supplied a valid email and password for this sensorbase.
   */
  public boolean isAuthenticated() {
    return this.isAuthenticated;
  }
  
  /**
   * Used by the Signin form to provide the SensorBase authentication credentials to this session. 
   * @param user The user.
   * @param password The password. 
   */
  public void setCredentials(String user, String password) {
    this.email = user;
    this.password = password;
  }
  
  /**
   * Returns the string to be displayed in the SigninFeedback label.
   * @return A signin feedback string. 
   */
  public String getSigninFeedback() {
    return this.signinFeedback;
  }
  
  /**
   * Allows other components to set the feedback string for the signin form.
   * @param signinFeedback The message to be displayed.
   */
  public void setSigninFeedback(String signinFeedback) {
    this.signinFeedback = signinFeedback;
  }
  
  /**
   * Allows other components to set the feedback string for the register form.
   * @param registerFeedback The message to be displayed.
   */
  public void setRegisterFeedback(String registerFeedback) {
    this.registerFeedback = registerFeedback;
  }
  
  /**
   * Returns the string to be displayed in the registerFeedback label.
   * @return A register feedback string. 
   */
  public String getRegisterFeedback() {
    return this.registerFeedback;
  }
  
  /**
   * Returns true if this email/password combination is valid for this sensorbase. 
   * @param email The email. 
   * @param password The password.
   * @return True if valid for this sensorbase. 
   */
  public boolean signin(String email, String password) {
    try {
      String host = ((ProjectBrowserApplication)getApplication()).getSensorBaseHost();
      SensorBaseClient client = new SensorBaseClient(host, email, password);
      client.authenticate();
      this.email = email;
      this.password = password;
      this.isAuthenticated = true;
      return true;
    }
    catch (Exception e) {
      this.isAuthenticated = false;
      return false;
    }
  }
  
  /**
   * Returns a SensorBaseClient instance for this user and session. 
   * @return The SensorBaseClient instance. 
   */
  public SensorBaseClient getSensorBaseClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getSensorBaseHost();
    return new SensorBaseClient(host, this.email, this.password);
  }
  
  /**
   * Returns a TelemetryClient instance for this user and session.
   * @return The TelemetryClient instance. 
   */
  public TelemetryClient getTelemetryClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getTelemetryHost();
    return new TelemetryClient(host, this.email, this.password);
  }
  
  /**
   * Returns a DailyProjectDataClient instance for this user and session.
   * @return The DailyProjectDataClient instance. 
   */
  public DailyProjectDataClient getDailyProjectDataClient() {
    String host = ((ProjectBrowserApplication)getApplication()).getDailyProjectDataHost();
    return new DailyProjectDataClient(host, this.email, this.password);
  }
  
  /**
   * Gets the user's email associated with this session. 
   * @return The user.
   */
  public String getUserEmail() {
    return this.email;
  }
}
