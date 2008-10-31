package org.hackystat.projectbrowser.page.todate.configurationpanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.projectbrowser.page.todate.ToDatePage;
import org.hackystat.projectbrowser.page.todate.detailpanel.ToDateDataModel;
import org.hackystat.projectbrowser.page.todate.detailpanel.ToDateMeasureConfiguration;
import org.hackystat.telemetry.service.resource.chart.jaxb.ParameterDefinition;
import org.hackystat.telemetry.service.resource.chart.jaxb.Type;

/**
 * Form in Project Portfolio configuration panel to input data.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class ToDateConfigurationForm extends StatelessForm {
  /** Support serialization. */
  private static final long serialVersionUID = 447730202032199770L;
  
  /** The associtated ProjectPortfolioDataModel. */
  private ToDateDataModel dataModel;
  
  /**
   * @param id the wicket component id.
   * @param d the data model that will be configure here.
   */
  public ToDateConfigurationForm(String id, ToDateDataModel d) {
    super(id);
    
    this.dataModel = d;
    //measure specified settings.
    
    //StartDateTextField
    DateTextField startDateTextField = 
      new DateTextField("startDateTextField", new PropertyModel(dataModel, "startDate"), 
          ProjectBrowserBasePage.DATA_FORMAT);
    startDateTextField.add(new DatePicker());
    startDateTextField.setRequired(true);
    add(startDateTextField);
    
    ListView measureList = new ListView("measureList", dataModel.getMeasures()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        final ToDateMeasureConfiguration measure = 
          (ToDateMeasureConfiguration) item.getModelObject();

        item.add(new Label("measureNameLabel", measure.getDisplayName()));

        item.add(new AjaxCheckBox("enableCheckBox", new PropertyModel(measure, "enabled")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void onUpdate(AjaxRequestTarget arg0) {
            arg0.addComponent(this.getForm());
          }
        });
        
     // Add parameter List
        TelemetrySession telemetrySession = ProjectBrowserSession.get().getTelemetrySession();
        ListView parameterList = 
          new ListView("parameterList", telemetrySession.getParameterList(measure.getName())) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            ParameterDefinition paramDef = (ParameterDefinition)item.getModelObject();
            item.add(new Label("name", paramDef.getName()));
            Component component = getComponent("field", paramDef.getType());
            if (item.getIndex() >= measure.getParameters().size() && component.getModel() != null) {
              measure.getParameters().add(component.getModel());
            }
            else {
              component.setModel(measure.getParameters().get(item.getIndex()));
            }
            //disable the cumulative parameter.
            if ("cumulative".equals(paramDef.getName())) {
              component.setEnabled(false);
            }
            item.add(component);
          }
          
          @Override
          public boolean isVisible() {
            return measure.isEnabled();
          }
        };
        item.add(parameterList);
      }
    };
    add(measureList);

    Button okButton = new Button("submit") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        dataModel.saveUserConfiguration();
        ((ToDatePage)this.getPage()).setConfigurationPanelVisible(false);
      }
    };
    add(okButton);
    
    Button resetButton = new Button("reset") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        dataModel.resetUserConfiguration();
      }
    };
    add(resetButton);
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
