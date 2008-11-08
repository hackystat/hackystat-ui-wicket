package org.hackystat.projectbrowser.page.trajectory.dtwpanel.datapanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;

/**
 * The DTW analysis panel displaying charts.
 * 
 * @author Pavel Senin
 *
 */
public class TrajectoryDTWDataPanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that hold the page state. */
  TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();

  /**
   * Constructor.
   * 
   * @param id the wicket component id.
   * @param page the parent page.
   */
  public TrajectoryDTWDataPanel(String id, ProjectBrowserBasePage page) {
    super(id);
    getLogger().log(Level.FINER, "[DEBUG] TrajectoryDTWDataPanel constructed.");
    IModel dataModel = new PropertyModel(session, "dataModel");

    // add the selected chart.
    //
    WebComponent selectedchartUrl = new WebComponent("selectedChart") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isVisible() {
        getLogger().log(Level.FINER,
            "[DEBUG] Selected chart URL:" + session.getDataModel().isChartEmpty());
        return !session.getDataModel().isChartEmpty();
      }
    };
    selectedchartUrl.add(new AttributeModifier("src", true, new PropertyModel(dataModel,
        "selectedChart")));
    add(selectedchartUrl);

    // adding the help popup
    //
    PopupWindowPanel selectedchartUrlWindow = new PopupWindowPanel("selectedChartUrlWindow",
        "Google Chart URL") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isVisible() {
        return !session.getDataModel().isChartEmpty();
      }
    };
    selectedchartUrlWindow.getModalWindow().setContent(
        new Label(selectedchartUrlWindow.getModalWindow().getContentId(), new PropertyModel(
            dataModel, "selectedChart")));
    add(selectedchartUrlWindow);

    // add the normalized chart.
    //
    WebComponent normalizedTSChartUrl = new WebComponent("normalizedTSChart") {
      /** Support serialization. */
      public static final long serialVersionUID = 2L;

      @Override
      public boolean isVisible() {
        getLogger().log(Level.FINER,
            "[DEBUG] Normalized chart URL:" + session.getDataModel().isNormalizedTSChartEmpty());
        return !session.getDataModel().isNormalizedTSChartEmpty();
      }
    };
    normalizedTSChartUrl.add(new AttributeModifier("src", true, new PropertyModel(dataModel,
        "normalizedTSChart")));
    add(normalizedTSChartUrl);

    // add the DTW chart.
    //
    WebComponent dtwChartUrl = new WebComponent("DTWChart") {
      /** Support serialization. */
      public static final long serialVersionUID = 3L;

      @Override
      public boolean isVisible() {
        getLogger().log(Level.FINER,
            "[DEBUG] DTW chart URL:" + session.getDataModel().isDTWChartEmpty());
        return !session.getDataModel().isDTWChartEmpty();
      }
    };
    dtwChartUrl.add(new AttributeModifier("src", true, new PropertyModel(dataModel, "DTWChart")));
    add(dtwChartUrl);

  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

}
