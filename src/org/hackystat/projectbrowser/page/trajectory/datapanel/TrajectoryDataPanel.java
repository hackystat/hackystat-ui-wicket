package org.hackystat.projectbrowser.page.trajectory.datapanel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.imageurl.ImageUrl;
import org.hackystat.projectbrowser.page.popupwindow.PopupWindowPanel;
import org.hackystat.projectbrowser.page.trajectory.ProjectRecord;
import org.hackystat.projectbrowser.page.trajectory.TrajectoryPage;
import org.hackystat.projectbrowser.page.trajectory.TrajectorySession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryPoint;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Panel for showing telemetry content.
 *
 * @author Shaoxuan Zhang, Pavel Senin
 *
 */
public class TrajectoryDataPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The page containing this form. */
  private TrajectoryPage page = null;
  /** TelemetrySession that hold the page state. */
  TrajectorySession session = ProjectBrowserSession.get().getTrajectorySession();

  /**
   * @param id the wicket component id.
   * @param p the page this page is attached to.
   */
  public TrajectoryDataPanel(String id, TrajectoryPage p) {
    super(id);
    this.page = p;
    getLogger().log(
        Level.FINER,
        "[DEBUG] TrajectoryDataPanel constructed: INPROCESS: "
            + session.getDataModel().isInProcess() + ", COMPLETE: "
            + session.getDataModel().isComplete());
    try {
      while ((session.getDataModel().isInProcess()) && (!session.getDataModel().isComplete())) {
        getLogger().log(Level.FINER, "[DEBUG] sleeping for a sec");
        Thread.sleep(1000);
      }
    }
    catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    IModel dataModel = new PropertyModel(session, "dataModel");

    BookmarkablePageLink restLink = new BookmarkablePageLink("restLink", TrajectoryPage.class,
        session.getPageParameters());

    // restLink.add(new Label("restLinkLabel", new Model(restLink)));
    add(restLink);

    Form streamForm = new Form("streamForm") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        if (!session.getDataModel().updateSelectedChart()) {
          session.setFeedback("Failed to get a chart image. "
              + " Please check to make sure you've selected at least one stream!");
        }
      }
    };

    Button submitButton = new Button("dtw") {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      public void onSubmit() {
        getLogger().log(Level.FINER, "[DEBUG] DTW is HIT");
        if ((session.getDataModel().updateSelectedChart())
            && (session.getDataModel().updateNormalizedTSChart())
            && (session.getDataModel().updateDTWChart())) {
          page.goDTW();
        }
        else {
          session.setFeedback("Failed to start analysis dialog. "
              + " Please check to make sure you've selected at least one stream!");
        }
      }

    };
    submitButton.setDefaultFormProcessing(false);
    streamForm.add(submitButton);

    add(streamForm);
    // table headers
    add(new Label("telemetryName", new PropertyModel(dataModel, "telemetryName")));

    ListView markersList = new ListView("markersList",
        new PropertyModel(dataModel, "markersList")) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        String dateString = (String) item.getModelObject();
        item.add(new Label("marker", dateString));
      }
    };
    streamForm.add(markersList);

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
        final ProjectRecord projectRecord = (ProjectRecord) item.getModelObject();
        Project project = projectRecord.getProject();
        Label projectNameLabel = new Label("projectName", project.getName());
        List<SelectableTrajectoryStream> streamList = session.getDataModel().getTrajectoryStream(
            project);
        projectNameLabel.add(new AttributeModifier("rowspan", new Model(streamList.size() + 1)));
        item.add(projectNameLabel);
        ListView datesList = new ListView("datesList", getDateList(projectRecord.getIndent(),
            streamList.get(0).getTelemetryStream().getTelemetryPoint())) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem item) {
            String date = (String) item.getModelObject();
            item.add(new Label("date", date));
          }
        };
        item.add(datesList);

        ListView projectStream = new ListView("projectStream", streamList) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem item) {
            SelectableTrajectoryStream stream = (SelectableTrajectoryStream) item.getModelObject();
            String streamName = stream.getTelemetryStream().getName();
            String streamUnit = stream.getTelemetryStream().getYAxis().getUnits();
            /*
             * int index = streamName.indexOf('<'); if (index > 0) { streamName =
             * streamName.substring(0, index); }
             */
            item.add(new Label("streamName", streamName));

            item.add(new Label("streamUnit", streamUnit));

            /*
             * WebComponent streamMarker = new WebComponent("streamMarker"); streamMarker.add(new
             * AttributeModifier("src", true, new PropertyModel(stream, "markerImageUrl")));
             * item.add(streamMarker);
             */

            item.add(new ImageUrl("streamMarker", stream.getMarkerImageUrl()));

            WebComponent colorCell = new WebComponent("colorCell");
            colorCell.add(new AttributeModifier("style", true, new PropertyModel(stream,
                "backgroundColorValue")));
            item.add(colorCell);

            item.add(new CheckBox("streamCheckBox", new PropertyModel(stream, "selected")));

            ListView streamData = new ListView("streamData", getValuesList(projectRecord
                .getIndent(), stream.getTelemetryStream().getTelemetryPoint())) {
              /** Support serialization. */
              public static final long serialVersionUID = 1L;

              @Override
              protected void populateItem(ListItem item) {
                String value = (String) item.getModelObject();
                item.add(new Label("data", value));
              }
            };
            item.add(streamData);
          }
        };
        item.add(projectStream);
      }
    };
    streamForm.add(projectTable);

    // add the selected chart.
    WebComponent selectedchartUrl = new WebComponent("selectedChart") {
      /** Support serialization. */
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

    // attribute to decide to show panel content or not.
    /*
     * AttributeModifier visibleAttribute = new AttributeModifier("style", true, new
     * AbstractReadOnlyModel() { private static final long serialVersionUID = 2013912742253160111L;
     *
     * @Override public Object getObject() { return (!session.getDataModel().isEmpty() &&
     * session.getDataModel().isComplete()) ? "" : "display:none"; } }); add(visibleAttribute);
     */

  }

  /**
   * Show the panel only when the data model is not empty and no loading is in process.
   *
   * @return true when this panel should be shown.
   */
  @Override
  public boolean isVisible() {
    return !session.getDataModel().isEmpty() && session.getDataModel().isComplete();
  }

  /**
   * Return the date list within the list of points.
   *
   * @param indent the indent.
   * @param points the point list.
   * @return the date list.
   */
  private List<String> getDateList(Integer indent, List<TelemetryPoint> points) {
    List<String> dates = new ArrayList<String>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.US);
    XMLGregorianCalendar startDate = Tstamp.makeTimestamp(points.get(0).getTime()
        .toGregorianCalendar().getTimeInMillis());
    for (int i = indent; i > 0; i--) {
      dates.add(dateFormat.format(Tstamp.incrementDays(startDate, -i).toGregorianCalendar()
          .getTime()));
    }
    for (TelemetryPoint point : points) {
      dates.add(dateFormat.format(point.getTime().toGregorianCalendar().getTime()));
    }
    return dates;
  }

  /**
   * Return the values list within the list of points.
   *
   * @param indent the indent.
   * @param points the point list.
   * @return the values list.
   */
  private List<String> getValuesList(Integer indent, List<TelemetryPoint> points) {
    List<String> values = new ArrayList<String>();
    for (int i = indent; i > 0; i--) {
      values.add("N/A");
    }
    for (TelemetryPoint point : points) {
      String value = point.getValue();
      if (value == null) {
        values.add("N/A");
      }
      else {
        int i = value.indexOf('.');
        if (i > 0 && (i + 2) < value.length()) {
          value = value.substring(0, i + 2);
        }
        values.add(value);
      }
    }
    return values;
  }

  /**
   * @return the logger that associated to this web application.
   */
  private Logger getLogger() {
    return ((ProjectBrowserApplication) Application.get()).getLogger();
  }

}
