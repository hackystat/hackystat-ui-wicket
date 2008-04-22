package org.hackystat.projectbrowser.page.telemetry.inputpanel;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.ProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.projectdatepanel.ProjectDateForm;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.Type;

/**
 * 
 * @author Shaoxuan Zhang
 *
 */
public class TelemetryInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** The page containing this form. */
  ProjectBrowserBasePage page = null;
  /** TelemetrySession that holds page state for telemetry. */
  TelemetrySession session = ProjectBrowserSession.get().getTelemetrySession();
  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param page the page this page is attached to.
   */
  public TelemetryInputForm(String id, ProjectBrowserBasePage page) {
    super(id);
    this.page = page;
    
    // Create the drop-down menu for telemetry. 
    DropDownChoice telemetryMenu = 
      new DropDownChoice ("telemetryMenu", new PropertyModel(session, "telemetryName"),
          new PropertyModel(session, "telemetryList")) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected boolean wantOnSelectionChangedNotifications() {
        return true;
      }
      @Override
      protected void onSelectionChanged(java.lang.Object newSelection) {
        session.getParameters().clear();
      }
    };
    telemetryMenu.setRequired(true);
    this.add(telemetryMenu);
    if (session.getTelemetryName() == null) {
      session.setTelemetryName(session.getTelemetryList().get(0));
    }

    //set Project to Default if null
    if (session.getProject() == null) {
      session.setProject(ProjectBrowserSession.get().getDefaultProject());
    }
    //StartDateTextField
    DateTextField startDateTextField = 
      new DateTextField("startDateTextField", new PropertyModel(session, "startDate"), 
          ProjectDateForm.DATA_FORMAT);
    startDateTextField.add(new DatePicker());
    startDateTextField.setRequired(true);
    add(startDateTextField);
    //EndDateTextField
    DateTextField endDateTextField = 
      new DateTextField("endDateTextField", new PropertyModel(session, "endDate"), 
          ProjectDateForm.DATA_FORMAT);
    endDateTextField.add(new DatePicker());
    endDateTextField.setRequired(true);
    add(endDateTextField);
    //granularity
    add(new DropDownChoice("granularity", 
                           new PropertyModel(session, "granularity"), 
                           session.getGranularityList()));
    
    // Now create the drop-down menu for projects. 
    DropDownChoice projectMenu = 
      new DropDownChoice ("projectMenu", 
          new PropertyModel(session, "project"),
          new PropertyModel(ProjectBrowserSession.get(), "projectList"),
          new ProjectChoiceRenderer()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override 
      public boolean isVisible() {
        return ProjectBrowserSession.get().getTelemetrySession().getTelemetryName() != null;
      }
    };
    projectMenu.setRequired(true);
    add(projectMenu);
    
    // Add parameter List
    ListView parameterList = 
      new ListView("parameterList", new PropertyModel(session, "parameterList")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        ParameterDefinition paramDef = (ParameterDefinition)item.getModelObject();
        item.add(new Label("name", paramDef.getName()));
        Component component = getComponent("field", paramDef.getType());
        if (item.getIndex() >= session.getParameters().size()) {
          session.getParameters().add(component.getModel());
        }
        else if (session.getParameters().get(item.getIndex()) == null) {
          session.getParameters().add(item.getIndex(), component.getModel());
        }
        else {
          component.setModel(session.getParameters().get(item.getIndex()));
        }
        item.add(component);
      }
    };
    add(parameterList);
  }

  /**
   * Process the user action after submitting a project and date. 
   */
  @Override
  public void onSubmit() {
    ProjectBrowserSession.get().getTelemetrySession().updateDataModel();
    page.onProjectDateSubmit();
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
      component = new Label(id, "Parameter Type " + type.getName() + " not recognized.");
    }
    return component;
  }
}
