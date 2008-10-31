package org.hackystat.projectbrowser.page.todate.inputpanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.todate.ToDatePage;
import org.hackystat.projectbrowser.page.todate.ToDateSession;
import org.hackystat.telemetry.service.resource.chart.jaxb.Type;

/**
 * Input panel for telemetry viewer.
 * Including telemetry name, start/end date, project, granularity and parameters.
 * @author Shaoxuan Zhang
 */
public class ToDateInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** The page containing this form. */
  private ToDatePage page = null;
  /** TelemetrySession that holds page state for telemetry. */
  private ToDateSession session = ProjectBrowserSession.get().getToDateSession();
  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param p the page this page is attached to.
   */
  public ToDateInputForm(String id, ToDatePage p) {
    super(id);
    this.page = p;

    //set Project to Default if null
    if (session.getSelectedProjects().isEmpty()) {
      session.getSelectedProjects().add(ProjectBrowserSession.get().getDefaultProject());
    }
    // Now create the menu for projects. 
    CheckBoxMultipleChoice projectMenu = 
      new CheckBoxMultipleChoice ("projectMenu", 
          new PropertyModel(session, "selectedProjects"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    projectMenu.setRequired(true);
    add(projectMenu);

    Link configurationLink = new Link("configuration") {
      /** Support serialization. */
      private static final long serialVersionUID = 0L;
      @Override
      public void onClick() {
        page.setConfigurationPanelVisible(!page.isConfigurationPanelVisible());
      }
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    configurationLink.add(new Label("label", new PropertyModel(this, "labelMessage")));
    add(configurationLink);
    
    Button submitButton = new Button("submit") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        ProjectBrowserSession.get().getToDateSession().updateDataModel();
        page.onProjectDateSubmit();
      }
      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    add(submitButton);
    
  }

  /**
   * @return the configuration label message
   */
  public String getLabelMessage() {
    String labelMessage;
    if (page.isConfigurationPanelVisible()) {
      labelMessage = "Hide Configuration";
    }
    else {
      labelMessage = "Show Configuration";
    }
    return labelMessage;
  }
  
  /**
   * Disable the whole form when data loading in process.
   * @return true if the form is enabled.
   */
  @Override
  public boolean isEnabled() {
    return getIsEnable();
  }
  
  /**
   * @return true if the form is enabled.
   */
  protected boolean getIsEnable() {
    return !ProjectBrowserSession.get().getToDateSession().getDataModel().isInProcess();
  }
  
  /**
   * Return a FormComponent according to the parameter type.
   * DropDownChoice for Enumerated.
   * CheckBox for Boolean.
   * TextField for Text and Integer.
   * @param id the wicket component id.
   * @param type the parameter type.
   * @return a FormComponent.
   */
  public Component getComponent(String id, Type type) {
    Component component;
    if ("Enumerated".equals(type.getName())) {
      DropDownChoice choice = 
        new DropDownChoice(id, new Model(type.getDefault()), type.getValue()) {           
        /** Support serialization. */
        public static final long serialVersionUID = 1L; 
        /**
         * Called any time a component that has this behavior registered is rendering the 
         * component tag.
         * @param tag the tag that is rendered
         */
        @Override 
        protected void onComponentTag(ComponentTag tag) { 
          tag.setName("select");
          super.onComponentTag(tag);
        } 
      };
      //choice.setPersistent(true);
      component = choice;
    }
    else if ("Boolean".equals(type.getName())) {
      CheckBox checkBox = new CheckBox(id, new Model(type.getDefault())) {           
        /** Support serialization. */
        public static final long serialVersionUID = 1L; 
        @Override 
        protected void onComponentTag(ComponentTag tag) { 
          tag.setName("input");
          tag.put("type", "checkbox");
          tag.remove("style"); //need for firefox on mac.
          super.onComponentTag(tag);
        } 
      };
      //checkBox.setPersistent(true);
      component = checkBox;
    }
    else if ("Text".equals(type.getName()) || "Integer".equals(type.getName())) {
      TextField textField = new TextField(id, new Model(type.getDefault())) {           
        /** Support serialization. */
        public static final long serialVersionUID = 1L; 
        @Override 
        protected void onComponentTag(ComponentTag tag) { 
          tag.setName("input");
          tag.put("type", "text");
          super.onComponentTag(tag);
        } 
      };
      //textField.setPersistent(true);
      component = textField;
    }
    else {
      component = new Label(id, new Model("Parameter Type " + type.getName() + " not recognized."));
    }
    return component;
  }
}
