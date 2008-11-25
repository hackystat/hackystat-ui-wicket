package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.imageurl.ImageUrl;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioPage;
import org.hackystat.projectbrowser.page.projectportfolio.ProjectPortfolioSession;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart.MiniBarChart;
import org.hackystat.projectbrowser.page.telemetry.TelemetryPage;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Wicket panel to display the detail of the selected projects.
 * @author Shaoxuan Zhang
 *
 */
public class ProjectPortfolioDetailsPanel extends Panel {

  /** For serialization. */
  private static final long serialVersionUID = 1L;
  /** The session that holds the status. */
  private ProjectPortfolioSession session = 
        ProjectBrowserSession.get().getProjectPortfolioSession();
  /** The model that holds the data. */
  private ProjectPortfolioDataModel dataModel = session.getDataModel();
  
  /**
   * Create the Wicket component.
   * @param id the wicket id.
   */
  public ProjectPortfolioDetailsPanel(String id) {
    super(id);
    
    BookmarkablePageLink permalink = new BookmarkablePageLink("permalink", 
        ProjectPortfolioPage.class, session.getPageParameters());
    add(permalink);

    Link sortProjectLink = new Link("sortProjectName") {
      /** Support serialization. */
      private static final long serialVersionUID = -5801192573412127312L;
      @Override
      public void onClick() {
        dataModel.sortProjectNames();
      }
      
    };
    add(sortProjectLink);
    
    ListView measureHeads = new ListView("measureHeads", dataModel.getEnabledMeasuresName()) {
      /** Support serialization. */
      private static final long serialVersionUID = -6222175445067187421L;

      @Override
      protected void populateItem(final ListItem item) {
        final String measureName = item.getModelObjectAsString();
        Link sortLink = new Link("sortLink") {
          /** Support serialization. */
          private static final long serialVersionUID = 1217586293098377714L;
          @Override
          public void onClick() {
            dataModel.sortTable(item.getIndex());
          }
        };
        sortLink.add(new Label("measureName", measureName));
        item.add(sortLink);
      }
    };
    add(measureHeads);
    
    ListView dateTable = new ListView("projectTable", dataModel.getSelectedProjects()) {
      /** Support serialization. */
      public static final long serialVersionUID = 1L;
      @Override
      protected void populateItem(ListItem item) {
        Project project = (Project)item.getModelObject();
        item.add(new Label("projectName", project.getName()));
        item.add(new Label("members", String.valueOf(project.getMembers().getMember().size() + 1)));
        
        ListView dateList = new ListView("measures", dataModel.getMeasuresCharts().get(project)) {
          /** Support serialization. */
          public static final long serialVersionUID = 1L;
          @Override
          protected void populateItem(ListItem item) {
            final MiniBarChart chart = (MiniBarChart)item.getModelObject();
            
            String value;
            if (chart.getLatestValue() >= 0) {
              value = String.valueOf(chart.getLatestValue());
              int i = value.indexOf('.');
              if (i > 0 && (i + 2) < value.length()) {
                value = value.substring(0, i + 2);
              }
            }
            else {
              value = "N/A";
            }
            
            Label valueLabel = new Label("value", value);
            String colorString = "color:#" + chart.getValueColor();
            valueLabel.add(new AttributeModifier("style", true, new Model(colorString)));
            item.add(valueLabel);

            /*
            Link chartLink = new BookmarkablePageLink("chartLink", 
                TelemetryPage.class, chart.getTelemetryPageParameters()) ;
                */
            Link chartLink = new Link("chartLink") {
                  /** Support serialization. */
                  private static final long serialVersionUID = 32587804920775165L;
                  @Override
                  public void onClick() {

                    ProjectBrowserSession.get().
                      logUsage("PORTFOLIO {invoke telemtry}");
                    this.setResponsePage(TelemetryPage.class, chart.getTelemetryPageParameters());
                  }
              
            };
            chartLink.add(new ImageUrl("chart", chart.getImageUrl()));
            item.add(chartLink);
          }
        };
        item.add(dateList);
      }
    };
    add(dateTable);
    
    
  }

  /**
   * Show the panel only when the data model is not empty and no loadin is in process.
   * @return true when this panel should be shown.
   */
  @Override
  public boolean isVisible() {
    return !session.getDataModel().isEmpty() && session.getDataModel().isComplete();
  }
  
}
