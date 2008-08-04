package org.hackystat.projectbrowser.page.sensordata;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.sensorbase.resource.projects.jaxb.SensorDataSummary;

/**
 * A panel for holding the SDT summary information. 
 * @author Philip Johnson
 */
public class SdtSummaryPanel extends Panel {

  /** For serialization */
  private static final long serialVersionUID = 1L;
  /** Must be serializable, thus a long rather than an XMLGregorianCalendar. */
  private long start; 
  
  /**
   * Creates a panel to display summary information. 
   * @param id The wicket ID. 
   * @param summaryList The list of SensorDataSummary instances to be displayed in this panel.
   * @param startTime The time this summary information started.
   */
  public SdtSummaryPanel(String id, List<SensorDataSummary> summaryList, 
      XMLGregorianCalendar startTime) {
    super(id);
    this.start = startTime.toGregorianCalendar().getTimeInMillis();
    // Set up the table
    add(new ListView("ToolInfoList", summaryList) {
      /** For serialization */
      private static final long serialVersionUID = 1L;

      /** 
       * How to populate the table.
       * @param item The ProjectSummary item.  
       */
      @Override
      protected void populateItem(ListItem item) {
        SensorDataSummary summary = (SensorDataSummary)item.getModelObject();
        String tool = summary.getTool();
        String sdt = summary.getSensorDataType();
        String info = String.format("%d:%s ", summary.getNumInstances().intValue(), tool); 
        SensorDataPopupPanel sdtPopup = new SensorDataPopupPanel("SdtPopup", "SDT Info", info, sdt, 
            tool, start);
        item.add(sdtPopup);
      }
    });
  }
}
