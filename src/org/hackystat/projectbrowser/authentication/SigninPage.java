package org.hackystat.projectbrowser.authentication;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * Provides a signin page for providing sensorbase user and password information.
 * 
 * @author Philip Johnson
 */
public class SigninPage extends WebPage {
  /**
   * The form for providing user and password details.
   * 
   * @author Philip Johnson
   */
  private static class SignInForm extends StatelessForm {
    /** The serialization id. */
    private static final long serialVersionUID = 1L;
    /** The sensorbase user. */
    private String user;
    /** The sensorbase password. */
    private String password;

    /**
     * Provide the ID for this form.
     * 
     * @param id The ID.
     */
    public SignInForm(final String id) {
      super(id);
      setModel(new CompoundPropertyModel(this));
      add(new TextField("user"));
      add(new PasswordTextField("password"));
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
     * What to do when the user presses the submit button to login.
     */
    @Override
    public final void onSubmit() {
      if (signIn(user, password)) {
        if (!continueToOriginalDestination()) {
          setResponsePage(getApplication().getHomePage());
        }
      }
      else {
        error("Unknown user / password");
      }
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public void setUser(String username) {
      this.user = username;
    }

    private boolean signIn(String username, String password) {
      ProjectBrowserSession.get().setCredentials(user, password);
      return true;
    }
  }

  public SigninPage() {
    add(new SignInForm("signInForm"));
    add(new FeedbackPanel("feedback"));
  }
}
