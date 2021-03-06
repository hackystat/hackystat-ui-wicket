package org.hackystat.projectbrowser.page.dailyprojectdata.coverage;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import org.hackystat.dailyprojectdata.resource.coverage.jaxb.ConstructData;

/**
 * Panel showing the drilldown of build data. 
 * @author Philip Johnson
 */
public class CoverageDetailsPanel extends Panel {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket component id.
   * @param dataList the List of data. 
   */
  public CoverageDetailsPanel(String id, List<ConstructData> dataList) {
    super(id);
      
    ListView buildListView = new ListView("coverageDetails", dataList) {
        
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        ConstructData data = (ConstructData) item.getModelObject();
        item.add(new Label("file", data.getName()));
        item.add(new Label("covered", String.valueOf(data.getNumCovered())));
        item.add(new Label("uncovered", String.valueOf(data.getNumUncovered())));
      }
    };
    add(buildListView);
  }

}
