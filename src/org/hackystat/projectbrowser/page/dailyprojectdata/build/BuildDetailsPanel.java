package org.hackystat.projectbrowser.page.dailyprojectdata.build;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import org.hackystat.dailyprojectdata.resource.build.jaxb.MemberData;

/**
 * Panel showing the drilldown of build data. 
 * @author Philip Johnson
 */
public class BuildDetailsPanel extends Panel {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket component id.
   * @param dataList The list of member data. 
   * @param displaySuccesses Whether to display success counts or failure counts. 
   */
  public BuildDetailsPanel(String id, List<MemberData> dataList, boolean displaySuccesses) {
    super(id);
      
    ListView buildListView = new ListView("buildDetails", dataList) {
        
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        MemberData data = (MemberData) item.getModelObject();
        item.add(new Label("user", data.getMemberUri()));
        item.add(new Label("success", String.valueOf(data.getSuccess())));
        item.add(new Label("failure", String.valueOf(data.getFailure())));
      }
    };
    add(buildListView);
  }

}
