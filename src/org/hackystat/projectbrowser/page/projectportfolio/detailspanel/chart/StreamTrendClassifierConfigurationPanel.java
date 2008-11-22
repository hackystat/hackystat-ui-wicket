package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel to configure the StreamTrendClassifier.
 * @author Shaoxuan Zhang
 *
 */
public class StreamTrendClassifierConfigurationPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 6939099421173153566L;

  /**
   * @param id The Wicket component ID.
   * @param streamTrendClassifier the {@link StreamTrendClassifier} to be configured.
   */
  public StreamTrendClassifierConfigurationPanel(String id, 
      StreamTrendClassifier streamTrendClassifier) {
    super(id);
    
    add(new TextField("higherThreshold", 
        new PropertyModel(streamTrendClassifier, "higherThreshold")));
    add(new TextField("lowerThreshold", 
        new PropertyModel(streamTrendClassifier, "lowerThreshold")));
    add(new CheckBox("higherBetter", 
        new PropertyModel(streamTrendClassifier, "higherBetter")));
  }

}
