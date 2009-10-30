package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel to configure the DeviationClassifier.
 * @author Shaoxuan Zhang
 *
 */
public class StreamDeviationClassifierConfigurationPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 7818693587716425217L;
  
  /**
   * @param id The Wicket component ID.
   * @param deviationClassifier the {@link StreamDeviationClassifier} to be configured.
   */
  public StreamDeviationClassifierConfigurationPanel(String id, 
      StreamDeviationClassifier deviationClassifier) {
    super(id);

    add(new TextField("unacceptableDeviation", 
        new PropertyModel(deviationClassifier, "unacceptableDeviation")));
    add(new TextField("moderateDeviation", 
        new PropertyModel(deviationClassifier, "moderateDeviation")));
    add(new TextField("expectationValue", 
        new PropertyModel(deviationClassifier, "expectationValue")));
    add(new CheckBox("scaleWithGranularity", 
        new PropertyModel(deviationClassifier, "scaleWithGranularity")));
  }


}
