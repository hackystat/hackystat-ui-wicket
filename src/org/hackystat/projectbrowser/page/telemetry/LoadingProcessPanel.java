package org.hackystat.projectbrowser.page.telemetry;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.hackystat.projectbrowser.page.telemetry.datapanel.TelemetryChartDataModel;

/**
 * Panel to show progress of a lengthy operation.
 * 
 * @author Shaoxuan Zhang
 */
public class LoadingProcessPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** the data model. */
  private TelemetryChartDataModel dataModel;

  /**
   * @param id the wicket component id.
   * @param model the data model associating with this panel.
   */
  @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
  public LoadingProcessPanel(String id, TelemetryChartDataModel model) {
    super(id);
    this.dataModel = model;
    MultiLineLabel label = 
      new MultiLineLabel("processingMessage", new PropertyModel(dataModel, "processingMessage"));
    add(label);
    if (dataModel.isInProcess()) {
      start();
    }
  }

  /**
   * start panel self update.
   */
  public final void start() {
    AjaxSelfUpdatingTimerBehavior selfUpdateBehavior = 
      new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
      @Override
      protected void onPostProcessTarget(AjaxRequestTarget target) {
        if (!dataModel.isInProcess()) {
          // do custom action
          onFinished(target);
          // stop the self update
          stop();
        }
      }
    };
    add(selfUpdateBehavior);
  }
  /**
   * visible when data loading is in process or data loading end with error.
   * @return true if this panel is visible.
   */
  @Override
  public boolean isVisible() {
    return dataModel.isInProcess() || !dataModel.isComplete();
  }
  
  /**
   * The method called when loading process is completed.
   * @param target an AjaxRequestTarget.
   */
  protected void onFinished(AjaxRequestTarget target) {
    //do nothing here. add implementation in subclasses.
  }
}
