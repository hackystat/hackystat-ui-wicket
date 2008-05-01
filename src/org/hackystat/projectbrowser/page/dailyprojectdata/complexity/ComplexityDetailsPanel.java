package org.hackystat.projectbrowser.page.dailyprojectdata.complexity;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import org.hackystat.dailyprojectdata.resource.complexity.jaxb.FileData;

/**
 * Panel showing the drilldown of build data. 
 * @author Philip Johnson
 */
public class ComplexityDetailsPanel extends Panel {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /**
   * @param id the wicket component id.
   * @param dataList the list of FileData items. 
   */
  public ComplexityDetailsPanel(String id, List<FileData> dataList) {
    super(id);
      
    ListView buildListView = new ListView("complexityDetails", dataList) {
        
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem item) {
        FileData data = (FileData) item.getModelObject();
        item.add(new Label("file", data.getFileUri()));
        item.add(new Label("complexities", data.getComplexityValues()));
      }
    };
    add(buildListView);
  }

}
