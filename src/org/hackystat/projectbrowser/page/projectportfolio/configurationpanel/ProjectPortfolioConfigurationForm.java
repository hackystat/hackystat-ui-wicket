package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioPage;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.
        PortfolioMeasureConfiguration;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;

/**
 * Form in Project Portfolio configuration panel to input data.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class ProjectPortfolioConfigurationForm extends Form {
  /** Support serialization. */
  private static final long serialVersionUID = 447730202032199770L;
  
  /** The associtated ProjectPortfolioDataModel. */
  private ProjectPortfolioDataModel dataModel;
  /** The colors to choose from. */
  /*
  private static final String[] colors = { "ffff00", // yellow
      "ff6600", // orange
      //"ff8080", // pink
      "ff00ff", // magenta
      "ff0000", // red
      "6600ff", //purple
      //"808080", // gray
      "00ffff", // cyan
      "00ff00", // green
      "0066ff", 
      //"0000ff", // blue
      //"000000", // black
  };
  */
  /** The introductions. */
  private static final String introductions = 
    "Enabled : Only enabled measure will be shown in detail panel\n\n"
    + "Colorable : Colorable measure's chart and value will be colored according to the following"
    + " setting. Noncolorable measure's chart and value will be black.\n\n"
    + "Is Higher Better: If higher value means better."
    + "Higher Threshold : The threshold of high value. \n\n"
    + "Lower Threshold : The threshold of low value.\n\n";
  /** The word style. */
  //private static final String STYLE_KEY = "style";
  /** THe preceding of HTTP background color style setting. */
  //private static final String BACKGROUND_COLOR_PRECEDING = "background-color:#";
  
  /**
   * @param id the wicket component id.
   * @param d the data model that will be configure here.
   */
  public ProjectPortfolioConfigurationForm(String id, ProjectPortfolioDataModel d) {
    super(id);
    
    this.dataModel = d;
    /*
    //General settings
    add(new TextField("timePhrase", new PropertyModel(dataModel, "timePhrase")));
    add(new DropDownChoice("granularity", 
                           new PropertyModel(dataModel, "telemetryGranularity"), 
                           dataModel.getGranularities()));
    add(new CheckBox("includeCurrentWeek", new PropertyModel(dataModel, "includeCurrentWeek")));
    */
    
    //Color settings.
    /*
    final Select goodColorSelect = new Select("goodColorSelect", new PropertyModel(dataModel,
    "goodColor"));
    goodColorSelect.add(new ColorSelectOptionList("goodColorOptionList", Arrays.asList(colors)));
    goodColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
        BACKGROUND_COLOR_PRECEDING + dataModel.getGoodColor())));
    goodColorSelect.setOutputMarkupId(true);
    goodColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(goodColorSelect));
    add(goodColorSelect);

    final Select sosoColorSelect = new Select("sosoColorSelect", new PropertyModel(dataModel,
    "sosoColor"));
    sosoColorSelect.add(new ColorSelectOptionList("sosoColorOptionList", Arrays.asList(colors)));
    sosoColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
        BACKGROUND_COLOR_PRECEDING + dataModel.getSosoColor())));
    sosoColorSelect.setOutputMarkupId(true);
    sosoColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(goodColorSelect));
    add(sosoColorSelect);

    final Select badColorSelect = new Select("badColorSelect", new PropertyModel(dataModel,
    "badColor"));
    badColorSelect.add(new ColorSelectOptionList("badColorOptionList", Arrays.asList(colors)));
    badColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
        BACKGROUND_COLOR_PRECEDING + dataModel.getBadColor())));
    badColorSelect.setOutputMarkupId(true);
    badColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(goodColorSelect));
    add(badColorSelect);
    */
    
    //measure specified settings.
    
    final ListView measureList = new ListView("measureList", dataModel.getMeasures()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final ListItem item) {
        item.setOutputMarkupId(true);
        final PortfolioMeasureConfiguration measure = 
          (PortfolioMeasureConfiguration) item.getModelObject();
        
        String cellClass;
        if (item.getIndex() % 2 == 0) {
          cellClass = "even";
        }
        else {
          cellClass = "odd";
        }
        item.add(new AttributeModifier("class", true, new Model(cellClass)));

        item.add(new Label("measureNameLabel", measure.getDisplayName()));

        // Add measure's configuration panel
        final Panel measurePanel = measure.getConfigurationPanel("measurePanel");
        measurePanel.setVisible(measure.hasClassifier() && measure.isEnabled());
        measurePanel.setOutputMarkupId(true);
        item.add(measurePanel);
        
        item.add(new AjaxCheckBox("enableCheckBox", new PropertyModel(measure, "enabled")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void onUpdate(AjaxRequestTarget target) {
            //measurePanel.setVisible(measure.isEnabled());
            target.addComponent(getForm());
          }
        });
        final DropDownChoice colorMethodMenu = new DropDownChoice("colorMethod", 
            new Model(measure.getClassiferName()),
            ProjectPortfolioDataModel.availableClassifiers) {
          /** Support serialization. */
          private static final long serialVersionUID = 5465487314644465276L;
          @Override
          public boolean isVisible() {
            return measure.isEnabled();
          }
        };
        colorMethodMenu.add(new AjaxFormComponentUpdatingBehavior("onchange") {
          /** Support serialization. */
          private static final long serialVersionUID = -6447496738809283902L;

          @Override
          protected void onUpdate(AjaxRequestTarget target) {
            String colorMethod = colorMethodMenu.getModelObjectAsString();
            measure.setStreamClassifier(colorMethod);
            target.addComponent(getFormComponent().getForm());
          }
          
        });
        item.add(colorMethodMenu);
        
        // Add parameter List

        item.add(new TelemetryParameterPanel("telemetryParameters", measure));
      }
    };
    add(measureList);
    PopupWindowPanel parameterPopup = new PopupWindowPanel("instructionPopup",
        "Configuration instruction", "Configuration instruction");
    parameterPopup.getModalWindow().setContent(
        new MultiLineLabel(parameterPopup.getModalWindow().getContentId(), introductions));
    add(parameterPopup);

    Button okButton = new Button("submit") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      public void onSubmit() {
        dataModel.saveUserConfiguration();
        ((ProjectPortfolioPage)this.getPage()).setConfigurationPanelVisible(false);
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
   * Set the panel to be invisible after form submitted.
   */
  /*
  @Override
  public void onSubmit() {
    this.dataModel.saveUserConfiguration();
    this.getParent().setVisible(false);
  }
  */

  /**
   * The list of select options for the color select field.
   * 
   * @author Shaoxuan Zhang
   */
  /*
  public static class ColorSelectOptionList extends ListView {
    public ColorSelectOptionList(final String id, final List<?> list) {
      super(id, list);
    }
    private static final long serialVersionUID = -3842069135751729472L;
    @Override
    protected void populateItem(final ListItem item) {
      item.setRenderBodyOnly(true);
      final String colorHex = item.getModelObjectAsString();
      final SelectOption colorSelectOption = new SelectOption("option", new Model(
          (Serializable) item.getModelObject())) {
        private static final long serialVersionUID = 4298345446185051771L;

        protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
          replaceComponentTagBody(markupStream, openTag, "");
        }
      };
      colorSelectOption.add(new SimpleAttributeModifier(STYLE_KEY, BACKGROUND_COLOR_PRECEDING
          + colorHex));
      item.add(colorSelectOption);
    }
  }
  */

  /**
   * Change the component's background color to the color value within the component.
   * 
   * @author Shaoxuan Zhang
   */
  /*
  public static class AjaxColorSelectChangeBackgroundColorBehavior extends OnChangeAjaxBehavior {
    private static final long serialVersionUID = -5326547000439295241L;
    Select select;
    public AjaxColorSelectChangeBackgroundColorBehavior(Select select) {
      this.select = select;
    }
    @Override
    protected void onUpdate(AjaxRequestTarget target) {
      select.add(new AttributeModifier(STYLE_KEY, true, new Model(BACKGROUND_COLOR_PRECEDING
          + select.getModelObjectAsString())));
      target.addComponent(select);
    }
  }
  */

}
