package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.MeasureConfiguration;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDataModel;

/**
 * Form in Project Portfolio configuration panel to input data.
 * @author Shaoxuan Zhang
 *
 */
public class ProjectPortfolioConfigurationForm extends Form {
  /** Support serialization. */
  private static final long serialVersionUID = 447730202032199770L;

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
        final MeasureConfiguration measure = (MeasureConfiguration)item.getModelObject();
        
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
        
        item.add(new TextField("higherThresholdTextField", 
                               new PropertyModel(measure, "higherThreshold")) {
          /** Support serialization. */
          private static final long serialVersionUID = -7434510173892738329L;
          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        });
        item.add(new TextField("lowerThresholdTextField", 
                               new PropertyModel(measure, "lowerThreshold")) {
          /** Support serialization. */
          private static final long serialVersionUID = -1675316116473661403L;
          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        });
        item.add(new CheckBox("isHigherTheBetterCheckBox", 
                              new PropertyModel(measure, "isHigherTheBetter")) {
          /** Support serialization. */
          private static final long serialVersionUID = -8182770470885819457L;
          @Override
          public boolean isEnabled() {
            return measure.isEnabled() && measure.isColorable();
          }
        });
      }
    };
    add(measureList);
  }
  
  /**
   * Set the panel to be invisible after form submitted.
   */
  public void onSubmit() {
    this.getParent().setVisible(false);
  }

}
