package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;

/**
 * The Panel contains a table of sensor data information, displayed in a modal window. 
 * @author Philip Johnson
 */
public class SensorDataTablePanel extends Panel {
  
  /** For serialization */
  private static final long serialVersionUID = 1L;
  
  /**
   * Creates a panel to display a table of sensor data information.
   * @param id The wicket ID. 
   * @param page The page associated with this panel.
   */
  public SensorDataTablePanel(String id, SensorDataPage page) {
    super(id);
    
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    
    SensorDataTableModel model = session.getSensorDataTableModel();
    
    // Set up the columns for the table.
    List<IColumn> columns = new ArrayList<IColumn>();
    columns.add(new PropertyColumn(new Model("Day"), "DayString"));
    columns.add(new PropertyColumn(new Model("Total"), "Total"));
    for (String sdt : model.getSdtSet()) {
      columns.add(new SdtColumn(new Model(sdt), sdt));
    }

    
    // Add the table, making sure that all days in every month are in one page.
    DataTable table = new DataTable("SensorDataTable", 
        columns.toArray(new IColumn[columns.size()]), model, 32);
    table.addTopToolbar(new HeadersToolbar(table, model));
    add(table);
    
  }
  
  /**
   * Returns true if this panel should be made visible.
   * @return True if visible.
   */
  @Override
  public boolean isVisible() {
    SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
    SensorDataTableModel model = session.getSensorDataTableModel();
    return !model.isEmpty();
  }
}
