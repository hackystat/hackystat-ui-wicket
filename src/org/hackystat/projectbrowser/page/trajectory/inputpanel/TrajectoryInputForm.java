package org.hackystat.projectbrowser.page.trajectory.inputpanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.projectbrowser.page.trajectory.TrajectoryProjectChoiceRenderer;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;
import org.hackystat.projectbrowser.page.trajectory.validator.ProjectRecordDateValidator;
import org.hackystat.projectbrowser.page.trajectory.validator.TelemetrySelectionValidator;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.Type;

/**
 * Input panel for the Trajectory viewer. Includes: project1, project2, intervals, telemetry name,
 * granularity and parameters.
 * 
 * @author Shaoxuan Zhang, Pavel Senin
 */
public class TrajectoryInputForm extends Form {

  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** The page containing this form. */
  private ProjectBrowserBasePage page = null;
  /** TelemetrySession that holds page state for telemetry. */
  private TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();

  /**
   * Create this form, supplying the wicket:id.
   * 
   * @param id The wicket:id.
   * @param p the page this page is attached to.
   */
  public TrajectoryInputForm(String id, ProjectBrowserBasePage p) {

    super(id);
    this.page = p;

    // set Projects selection to Default if null
    // i've not yet fixed all of this for two projects
    if (null == session.getSelectedProject1().getProject()) {
      session.getSelectedProject1().setProject(ProjectBrowserSession.get().getDefaultProject());
    }
    if (null == session.getSelectedProject2().getProject()) {
      session.getSelectedProject2().setProject(ProjectBrowserSession.get().getDefaultProject());
    }

    addProject1Pane();

    addProject2Pane();

    addTelemetryPane();

    Button submitButton = new Button("submit") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        getLogger().log(Level.FINER,
            "[DEBUG] TrajectoryInputForm: " + "SUBMIT HIT -> updating the TrajectorySession.");
        ProjectBrowserSession.get().getTrajectorySession().updateDataModel();
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
   * Add the project1 related fields to the pane.
   */
  private void addProject1Pane() {

    // Create the drop-down menu for projects.
    ListChoice projectMenu = new ListChoice("project1List", new PropertyModel(session,
        "selectedProject1"), new PropertyModel(session, "projectList"),
        new TrajectoryProjectChoiceRenderer()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    projectMenu.setRequired(true);
    add(projectMenu);

    // StartDateTextField
    DateTextField startDateTextField = new DateTextField("project1StartDate", new PropertyModel(
        session, "project1StartDate"), ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    startDateTextField.add(new DatePicker());
    startDateTextField.setRequired(true);
    add(startDateTextField);

    // EndDateTextField
    DateTextField endDateTextField = new DateTextField("project1EndDate", new PropertyModel(
        session, "project1EndDate"), ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    endDateTextField.add(new DatePicker());
    endDateTextField.setRequired(true);
    add(endDateTextField);

    // Add a validator that makes sure the date interval is within all project intervals.
    // add(new ProjectDateValidator(projectMenu, startDateTextField, endDateTextField));

    // indent for the dates
    TextField indentTextField = new TextField("project1Indent", new PropertyModel(session,
        "project1Indent"), java.lang.Integer.class) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    indentTextField.setRequired(false);
    add(indentTextField);

    // Add a validator that makes sure the date interval is within all project intervals.
    add(new ProjectRecordDateValidator(projectMenu, startDateTextField, endDateTextField,
        indentTextField));
  }

  /**
   * Add project2 related fields to the pane.
   */
  private void addProject2Pane() {
    // Create the drop-down menu for projects.
    ListChoice projectMenu = new ListChoice("project2List", new PropertyModel(session,
        "selectedProject2"), new PropertyModel(session, "projectList"),
        new TrajectoryProjectChoiceRenderer()) {

      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    projectMenu.setRequired(true);
    add(projectMenu);

    // StartDateTextField
    DateTextField startDateTextField = new DateTextField("project2StartDate", new PropertyModel(
        session, "project2StartDate"), ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    startDateTextField.add(new DatePicker());
    startDateTextField.setRequired(true);
    add(startDateTextField);

    // EndDateTextField
    DateTextField endDateTextField = new DateTextField("project2EndDate", new PropertyModel(
        session, "project2EndDate"), ProjectBrowserBasePage.DATA_FORMAT) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    endDateTextField.add(new DatePicker());
    endDateTextField.setRequired(true);
    add(endDateTextField);

    // indent for the dates
    TextField indentTextField = new TextField("project2Indent", new PropertyModel(session,
        "project2Indent"), java.lang.Integer.class) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    indentTextField.setRequired(false);
    add(indentTextField);

    // Add a validator that makes sure the date interval is within all project intervals.
    add(new ProjectRecordDateValidator(projectMenu, startDateTextField, endDateTextField,
        indentTextField));
  }

  /**
   * Add telemetry related stuff to the pane.
   */
  private void addTelemetryPane() {
    // Create the drop-down menu for telemetry chart selection.
    //
    DropDownChoice telemetryMenu = new DropDownChoice("telemetryMenu", new PropertyModel(session,
        "telemetryName"), new PropertyModel(session, "telemetryList")) {
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

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    };
    telemetryMenu.setRequired(true);
    add(new TelemetrySelectionValidator(telemetryMenu));
    add(telemetryMenu);
    // add the popup window
    PopupWindowPanel telemetryPopup = new PopupWindowPanel("chartDefPopup",
        "Telemetry Descriptions");
    telemetryPopup.getModalWindow().setContent(
        new TrajectoryDescriptionPanel(telemetryPopup.getModalWindow().getContentId()));
    add(telemetryPopup);
    if (session.getTelemetryName() == null && !session.getTelemetryList().isEmpty()) {
      session.setTelemetryName(session.getTelemetryList().get(0));
    }

    // granularity
    add(new DropDownChoice("granularity", new PropertyModel(session, "granularity"), session
        .getGranularityList()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isEnabled() {
        return getIsEnable();
      }
    });

    // Add parameter List
    ListView parameterList = new ListView("parameterList", new PropertyModel(session,
        "parameterList")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        ParameterDefinition paramDef = (ParameterDefinition) item.getModelObject();
        item.add(new Label("name", paramDef.getName()));
        Component component = getComponent("field", paramDef.getType());
        if (item.getIndex() >= session.getParameters().size() && component.getModel() != null) {
          session.getParameters().add(component.getModel());
        }
        else {
          component.setModel(session.getParameters().get(item.getIndex()));
        }
        item.add(component);
        PopupWindowPanel parameterPopup = 
                                        new PopupWindowPanel("parameterPopup", paramDef.getName());
        parameterPopup.getModalWindow().setContent(
            new Label(parameterPopup.getModalWindow().getContentId(), paramDef.getDescription()));
        item.add(parameterPopup);
      }

      @Override
      public boolean isVisible() {
        return getIsEnable();
      }
    };
    add(parameterList);

  }

  /**
   * Disable the whole form when data loading in process.
   * 
   * @return true if the form is enabled.
   */
  @Override
  public boolean isEnabled() {
    return getIsEnable();
  }

  /**
   * @return true if the form is enabled.
   */
  protected final boolean getIsEnable() {
    return !ProjectBrowserSession.get().getTelemetrySession().getDataModel().isInProcess();
  }

  /**
   * Return a FormComponent according to the parameter type. DropDownChoice for Enumerated. CheckBox
   * for Boolean. TextField for Text and Integer.
   * 
   * @param id the wicket component id.
   * @param type the parameter type.
   * @return a FormComponent.
   */
  public final Component getComponent(String id, Type type) {
    Component component;
    if ("Enumerated".equals(type.getName())) {
      DropDownChoice choice = new DropDownChoice(id, new Model(type.getDefault()), 
                                                                          type.getValue()) {
        /** Support serialization. */
        public static final long serialVersionUID = 1L;

        /**
         * Called any time a component that has this behavior registered is rendering the component
         * tag.
         * 
         * @param tag the tag that is rendered
         */
        @Override
        protected void onComponentTag(ComponentTag tag) {
          tag.setName("select");
          super.onComponentTag(tag);
        }
      };
      // choice.setPersistent(true);
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
          tag.remove("style"); // need for firefox on mac.
          super.onComponentTag(tag);
        }
      };
      // checkBox.setPersistent(true);
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
      // textField.setPersistent(true);
      component = textField;
    }
    else {
      component = new Label(id, new Model("Parameter Type " + type.getName() + " not recognized."));
    }
    return component;
  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

}
