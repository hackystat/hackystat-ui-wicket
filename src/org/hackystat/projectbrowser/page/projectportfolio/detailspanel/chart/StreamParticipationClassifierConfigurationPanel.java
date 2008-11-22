package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel to configure the StreamParticipationClassifier.
 * @author Shaoxuan Zhang
 *
 */
public class StreamParticipationClassifierConfigurationPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = -8949053875882960644L;
  
  /**
   * @param id The Wicket component id.
   * @param streamParticipationClassifier The {@link StreamParticipationClassifier} to set.
   */
  public StreamParticipationClassifierConfigurationPanel(String id, 
      StreamParticipationClassifier streamParticipationClassifier) {
    super(id);

    add(new TextField("memberPercentage", 
        new PropertyModel(streamParticipationClassifier, "memberPercentage")));
    add(new TextField("thresholdValue", 
        new PropertyModel(streamParticipationClassifier, "thresholdValue")));
    add(new TextField("frequencyPercentage", 
        new PropertyModel(streamParticipationClassifier, "frequencyPercentage")));
  }


}
