package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * A panel for holding the SDT summary information. 
 * @author Philip Johnson
 */
public class SdtSummaryPanel extends Panel {

  /** For serialization */
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  private SdtSummaryModel summaryModel = null;

  /**
   * Creates a panel to display summary information. 
   * @param id The wicket ID. 
   * @param summaryModel The model that this panel will display. 
   */
  public SdtSummaryPanel(String id, SdtSummaryModel summaryModel) {
    super(id);
    this.summaryModel = summaryModel;
    // Set up the table
    add(new ListView("SdtSummary", new PropertyModel(this, "summaryModel.getList")) {
      /** For serialization */
      private static final long serialVersionUID = 1L;

      /** 
       * How to populate the table.
       * @param item The summary item.  
       */
      @Override
      protected void populateItem(ListItem item) {
        SdtSummary summary = (SdtSummary) item.getModelObject();
        item.add(new Label("sdtName", summary.getSdtName()));
        item.add(new Label("count", String.valueOf(summary.getCount())));
      }
    });
    add(new Label("total", new Model() {
      /** for serialization. */
      private static final long serialVersionUID = 1L;

      /**
       * Gets the total field. 
       * @return the Total.
       */
      @Override
      public Object getObject() {
        return null;
      }
    }));
  }
}
