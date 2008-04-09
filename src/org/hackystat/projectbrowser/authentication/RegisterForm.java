package org.hackystat.projectbrowser.authentication;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * The form for providing an email for registration purposes. 
 * 
 * @author Philip Johnson
 */
class RegisterForm extends StatelessForm {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The email. */
  private String email;

  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   */
  public RegisterForm(final String id) {
    super(id);
    setModel(new CompoundPropertyModel(this));
    add(new TextField("email"));
  }
  
  @Override
  public void onSubmit() {
    ProjectBrowserSession.get().setRegisterFeedback("User has registered.");
  }

  /**
   * Returns the user.
   * 
   * @return The user.
   */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}