package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.MeasureConfiguration;
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
  /** The colors to choose from. */
  private static final String[] colors = {"ffff00",  //yellow
                                          "ffc800", //orange
                                          "ffafaf", //pink
                                          "ff00ff", //magenta
                                          "ff0000", //red
                                          "808080", //gray
                                          "00ffff", //cyan
                                          "00ff00", //green
                                          "0000ff", //blue
                                          "000000", //black
                                          };
  /** The word style. */
  private static final String STYLE_KEY = "style";
  /** THe preceding of HTTP background color style setting. */
  private static final String BACKGROUND_COLOR_PRECEDING = "background-color:#";

  /**
   * @param id the wicket component id.
   * @param dataModel the data model that will be configure here.
   */
  public ProjectPortfolioConfigurationForm(String id, ProjectPortfolioDataModel dataModel) {
    super(id);
    ListView measureList = new ListView("measureList", dataModel.getMeasures()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        final MeasureConfiguration measure = (MeasureConfiguration) item.getModelObject();

        item.add(new Label("measureNameLabel", new PropertyModel(measure, "measureName")));

        item.add(new AjaxCheckBox("enableCheckBox", new PropertyModel(measure, "enabled")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void onUpdate(AjaxRequestTarget arg0) {
            arg0.addComponent(this.getForm());
          }
        });

        item.add(new AjaxCheckBox("colorableCheckBox", new PropertyModel(measure, "colorable")) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void onUpdate(AjaxRequestTarget arg0) {
            arg0.addComponent(this.getForm());
          }

          @Override
          public boolean isEnabled() {
            return measure.isEnabled();
          }
        });

        item.add(new TextField("higherThresholdTextField", new PropertyModel(measure,
            "higherThreshold")) {
          /** Support serialization. */
          private static final long serialVersionUID = -7434510173892738329L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        });
        item.add(new TextField("lowerThresholdTextField", new PropertyModel(measure,
            "lowerThreshold")) {
          /** Support serialization. */
          private static final long serialVersionUID = -1675316116473661403L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        });

        final Select higherColorSelect = new Select("higherColorSelect", new PropertyModel(measure,
            "higherColor")) {
          /** Support serialization. */
          private static final long serialVersionUID = -5112893030387208238L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        };
        higherColorSelect.add(new ColorSelectOptionList("higherColorOptionList", Arrays
            .asList(colors)));
        higherColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
            BACKGROUND_COLOR_PRECEDING + measure.getHigherColor())));
        higherColorSelect.setOutputMarkupId(true);
        higherColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(higherColorSelect));
        item.add(higherColorSelect);

        final Select middleColorSelect = new Select("middleColorSelect", new PropertyModel(measure,
            "middleColor")) {
          /** Support serialization. */
          private static final long serialVersionUID = 7655043151666349981L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        };
        middleColorSelect.add(new ColorSelectOptionList("middleColorOptionList", Arrays
            .asList(colors)));
        middleColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
            BACKGROUND_COLOR_PRECEDING + measure.getMiddleColor())));
        middleColorSelect.setOutputMarkupId(true);
        middleColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(middleColorSelect));
        item.add(middleColorSelect);

        final Select lowerColorSelect = new Select("lowerColorSelect", new PropertyModel(measure,
            "lowerColor")) {
          /** Support serialization. */
          private static final long serialVersionUID = -2727213099700785976L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        };
        lowerColorSelect.add(new ColorSelectOptionList("lowerColorOptionList", Arrays
            .asList(colors)));
        lowerColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
            BACKGROUND_COLOR_PRECEDING + measure.getLowerColor())));
        lowerColorSelect.setOutputMarkupId(true);
        lowerColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(lowerColorSelect));
        item.add(lowerColorSelect);

        final Select stableColorSelect = new Select("stableColorSelect", new PropertyModel(measure,
            "stableColor")) {
          /** Support serialization. */
          private static final long serialVersionUID = 4133327290826205101L;

          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        };
        stableColorSelect.add(new ColorSelectOptionList("stableColorOptionList", Arrays
            .asList(colors)));
        stableColorSelect.add(new AttributeModifier(STYLE_KEY, true, new Model(
            BACKGROUND_COLOR_PRECEDING + measure.getStableColor())));
        stableColorSelect.setOutputMarkupId(true);
        stableColorSelect.add(new AjaxColorSelectChangeBackgroundColorBehavior(stableColorSelect));
        item.add(stableColorSelect);
      }

    };
    add(measureList);

  }

  /**
   * Set the panel to be invisible after form submitted.
   */
  @Override
  public void onSubmit() {
    this.getParent().setVisible(false);
  }

  /**
   * The list of select options for the color select field.
   * 
   * @author Shaoxuan Zhang
   */
  public static class ColorSelectOptionList extends ListView {

    /**
     * @param id the wicket component id.
     * @param list the list of options.
     */
    public ColorSelectOptionList(final String id, final List<?> list) {
      super(id, list);
    }

    /** Support serialization. */
    private static final long serialVersionUID = -3842069135751729472L;

    /**
     * display the list item.
     * 
     * @param item the ListItem
     */
    @Override
    protected void populateItem(final ListItem item) {
      item.setRenderBodyOnly(true);
      final String colorHex = item.getModelObjectAsString();
      final SelectOption colorSelectOption = new SelectOption("option", new Model(
          (Serializable) item.getModelObject())) {
        /** Support serialization. */
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

  /**
   * Change the component's background color to the color value within the component.
   * 
   * @author Shaoxuan Zhang
   */
  public static class AjaxColorSelectChangeBackgroundColorBehavior extends OnChangeAjaxBehavior {
    /** Support serialization. */
    private static final long serialVersionUID = -5326547000439295241L;
    /** The color select field component. */
    Select select;

    /**
     * @param select the color select field component
     */
    public AjaxColorSelectChangeBackgroundColorBehavior(Select select) {
      this.select = select;
    }

    /**
     * Action take place on Update. Impelement the interface.
     * 
     * @param target the AjaxRequestTarget
     */
    @Override
    protected void onUpdate(AjaxRequestTarget target) {
      select.add(new AttributeModifier(STYLE_KEY, true, new Model(BACKGROUND_COLOR_PRECEDING
          + select.getModelObjectAsString())));
      target.addComponent(select);
    }
  }

}
