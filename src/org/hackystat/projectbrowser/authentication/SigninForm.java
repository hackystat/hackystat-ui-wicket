package org.hackystat.projectbrowser.authentication;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.sensordata.SensorDataPage;

/**
 * The form for providing credentials to sign in to the Sensorbase. 
 * 
 * @author Philip Johnson
 */
class SigninForm extends StatelessForm {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The sensorbase user. */
  private String user;
  /** The sensorbase password. */
  private String password;

  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   */
  public SigninForm(final String id) {
    super(id);
    setModel(new CompoundPropertyModel(this));
    add(new TextField("user"));
    add(new PasswordTextField("password"));
    add(new Label("signinFeedback", 
        new PropertyModel(ProjectBrowserSession.get(), "signinFeedback")));
  }
  
  /**
   * Process the user action after submitting a username/password.  Note that they will go 
   * to the page they requested if it was not the home page. 
   */
  @Override
  public void onSubmit() {
    boolean signinSuccessful = ProjectBrowserSession.get().signin(user, password);
    if (signinSuccessful) {
      if (!continueToOriginalDestination()) {
        setResponsePage(new SensorDataPage());
      }
    }
    else {
      ProjectBrowserSession.get().setSigninFeedback("User/Password not correct.");
    }
  }

  /**
   * Returns the password.
   * 
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the user.
   * 
   * @return The user.
   */
  public String getUser() {
    return user;
  }

  /**
   * Sets the password. 
   * @param password The password. 
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the user. 
   * @param user The user. 
   */
  public void setUser(String user) {
    this.user = user;
  }
}