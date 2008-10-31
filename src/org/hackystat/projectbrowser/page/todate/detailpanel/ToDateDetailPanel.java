package org.hackystat.projectbrowser.page.todate.detailpanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.todate.ToDatePage;
import org.hackystat.projectbrowser.page.todate.ToDateSession;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Panel for showing telemetry content.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class ToDateDetailPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** TelemetrySession that hold the page state. */
  ToDateSession session;
  /** The model that holds the data. */
  private ToDateDataModel dataModel;

  /**
   * @param id the wicket component id.
   * @param s the TelemetrySession.
   */
  public ToDateDetailPanel(String id, ToDateSession s) {
    super(id);
    this.session = s;
    this.dataModel = session.getDataModel();

    BookmarkablePageLink permalink = new BookmarkablePageLink("permalink", 
        ToDatePage.class, session.getPageParameters());
    add(permalink);
    
    ListView measureHeads = new ListView("measureHeads", dataModel.getEnabledMeasuresName()) {
      /** Support serialization. */
      private static final long serialVersionUID = -6222175445067187421L;

      @Override
      protected void populateItem(ListItem item) {
        item.add(new Label("measureName", item.getModelObjectAsString()));
      }
    };
    add(measureHeads);
    
    ListView dateList = new ListView("projectTable", dataModel.getSelectedProjects()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project)item.getModelObject();
        item.add(new Label("projectName", project.getName()));
        item.add(new Label("members", String.valueOf(project.getMembers().getMember().size() + 1)));
        
        ListView dateList = new ListView("measures", dataModel.getProjectData().get(project)) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            String value = item.getModelObjectAsString();
            Label valueLabel = new Label("value", value);
            item.add(valueLabel);

          }
        };
        item.add(dateList);
      }
    };
    add(dateList);
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
