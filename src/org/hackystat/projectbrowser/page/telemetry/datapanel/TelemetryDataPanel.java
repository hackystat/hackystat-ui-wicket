package org.hackystat.projectbrowser.page.telemetry.datapanel;

import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.googlechart.GoogleChart;
import org.hackystat.projectbrowser.imageurl.ImageUrl;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.projectbrowser.page.telemetry.TelemetryPage;
import org.hackystat.projectbrowser.page.telemetry.TelemetrySession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;

/**
 * Panel for showing telemetry content.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class TelemetryDataPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that hold the page state. */
  TelemetrySession session;

  /**
   * @param id the wicket component id.
   * @param s the TelemetrySession.
   */
  public TelemetryDataPanel(String id, TelemetrySession s) {
    super(id);
    this.session = s;
    IModel dataModel = new PropertyModel(session, "dataModel");

    BookmarkablePageLink restLink = new BookmarkablePageLink("restLink", TelemetryPage.class,
        session.getPageParameters());

    // restLink.add(new Label("restLinkLabel", new Model(restLink)));
    add(restLink);

    Form streamForm = new Form("streamForm") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        if (session.getDataModel().isSizeWithinRange()) {
          if (!session.getDataModel().updateSelectedChart()) {
            session.setFeedback("Failed to get a chart image. "
                + " Please check to make sure you've selected at least one stream!");
          }
        }
        else {
          session.setFeedback(
              "The size of the chart cannot be greater than " + GoogleChart.MAX_SIZE);
        }
      }
    };
    add(streamForm);
    // table headers
    add(new Label("telemetryName", new PropertyModel(dataModel, "telemetryName")));

    ListView dateList = new ListView("dateList", new PropertyModel(dataModel, "dateList")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        String dateString = (String) item.getModelObject();
        item.add(new Label("date", dateString));
      }
    };
    streamForm.add(dateList);

    // checkbox to select stream to display
    streamForm.add(new CheckBox("selectAll", new Model()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void onSelectionChanged(java.lang.Object newSelection) {
        session.getDataModel().changeSelectionForAll((Boolean) newSelection);
      }

      @Override
      protected boolean wantOnSelectionChangedNotifications() {
        return true;
      }
    });

    // stream data
    ListView projectTable = new ListView("projectTable", new PropertyModel(dataModel,
        "selectedProjects")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project) item.getModelObject();
        Label projectNameLabel = new Label("projectName", project.getName());
        List<SelectableTelemetryStream> streamList = session.getDataModel().getTelemetryStream(
            project);
        projectNameLabel.add(new AttributeModifier("rowspan", new Model(streamList.size() + 1)));
        item.add(projectNameLabel);

        ListView projectStream = new ListView("projectStream", streamList) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem item) {
            SelectableTelemetryStream stream = (SelectableTelemetryStream) item.getModelObject();
            String streamName = stream.getTelemetryStream().getName();
            String streamUnit = stream.getTelemetryStream().getYAxis().getUnits();

            item.add(new CheckBox("streamCheckBox", new PropertyModel(stream, "selected")));
            item.add(new Label("streamName", streamName));
            item.add(new Label("streamUnit", streamUnit));

            /*
             * WebComponent streamMarker = new WebComponent("streamMarker"); streamMarker.add(new
             * AttributeModifier("src", true, new PropertyModel(stream, "markerImageUrl")));
             * item.add(streamMarker);
             */

            item.add(new ImageUrl("streamMarker", stream.getMarkerImageUrl()));

            ListView streamData = new ListView("streamData", stream.getTelemetryStream()
                .getTelemetryPoint()) {
              /** Support serialization. */
              public static final long serialVersionUID = 1L;

              @Override
              protected void populateItem(ListItem item) {
                TelemetryPoint point = (TelemetryPoint) item.getModelObject();
                String value = point.getValue();
                if (value == null) {
                  item.add(new Label("data", "N/A"));
                }
                else {
                  int i = value.indexOf('.');
                  if (i > 0 && (i + 2) < value.length()) {
                    value = value.substring(0, i + 2);
                  }
                  item.add(new Label("data", value));
                }
              }
            };
            item.add(streamData);
          }
        };
        item.add(projectStream);
      }
    };
    streamForm.add(projectTable);

    streamForm.add(new TextField("width", new PropertyModel(dataModel, "width")));
    streamForm.add(new TextField("height", new PropertyModel(dataModel, "height")));
    
    // add the selected chart.
    WebComponent selectedchartUrl = new WebComponent("selectedChart") {
      public static final long serialVersionUID = 1L;

      @Override
      public boolean isVisible() {
        return !session.getDataModel().isChartEmpty();
      }
    };
    selectedchartUrl.add(new AttributeModifier("src", true, new PropertyModel(dataModel,
        "selectedChart")));
    add(selectedchartUrl);

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

  }

  /**
   * Show the panel only when the data model is not empty and no loadin is in process.
   * 
   * @return true when this panel should be shown.
   */
  @Override
  public boolean isVisible() {
    return !session.getDataModel().isEmpty() && session.getDataModel().isComplete();
  }

}
