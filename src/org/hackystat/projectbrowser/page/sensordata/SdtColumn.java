package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Implements a column holding SDT summary information in the SensorDataDetailsPanel. 
 * @author Philip Johnson
 *
 */
public class SdtColumn extends AbstractColumn {
  
  /** for serialization. */
  private static final long serialVersionUID = 1L;
  private String sdtName = "";

  /**
   * Constructs the SensorDataType column.
   * @param model The model containintg SDT info.
   * @param sdtName The name of the SDT to be displayed in this column. 
   */
  public SdtColumn(IModel model, String sdtName) {
    super(model);
    this.sdtName = sdtName;
  }

  /**
   * How to populate an individual cell. 
   * @param cellItem the Item instance that will be displayed in the cell.
   * @param componentId Use this to assign to the component added to the cell.
   * @param model the model used to figure out what to display in the cell. 
   */
  @Override
  public void populateItem(Item cellItem, String componentId, IModel model) {
    SensorDataTableRowModel rowModel = (SensorDataTableRowModel) model.getObject();
    cellItem.add(new SdtSummaryPanel(componentId, rowModel.getSensorDataSummaryList(this.sdtName),
        rowModel.getStartTime()));
  }

}
