package org.hackystat.projectbrowser.page.dailyprojectdata.coupling;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import org.hackystat.dailyprojectdata.resource.coupling.jaxb.CouplingData;

/**
 * Panel showing the drilldown of build data. 
 * @author Philip Johnson
 */
public class CouplingDetailsPanel extends Panel {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket component id.
   * @param dataList the List of CouplingData.
   */
  public CouplingDetailsPanel(String id, List<CouplingData> dataList) {
    super(id);
      
    ListView buildListView = new ListView("couplingDetails", dataList) {
        
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        CouplingData data = (CouplingData) item.getModelObject();
        item.add(new Label("file", data.getUri()));
        item.add(new Label("afferent", String.valueOf(data.getAfferent())));
        item.add(new Label("efferent", String.valueOf(data.getEfferent())));
      }
    };
    add(buildListView);
  }

}
