package org.hackystat.projectbrowser.authentication;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * The form for providing credentials to sign in to the Sensorbase. 
 * 
 * @author Philip Johnson
 */
class SigninForm extends Form {

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
  }
  
  @Override
  public void onSubmit() {
    ProjectBrowserSession.get().setSigninFeedback("User has signed in.");
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

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUser(String username) {
    this.user = username;
  }
}