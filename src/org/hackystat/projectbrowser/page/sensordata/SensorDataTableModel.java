package org.hackystat.projectbrowser.page.sensordata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hackystat.sensorbase.resource.projects.jaxb.MultiDayProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectSummary;
import org.hackystat.sensorbase.resource.projects.jaxb.SensorDataSummary;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

/**
 * Provides a model that summarizes for each day in a month, how many sensor data instances 
 * of a given type were associated with that day. 
 * @author Philip Johnson
 */
public class SensorDataTableModel extends SortableDataProvider {
  
  /** Required for serialization. */
  private static final long serialVersionUID = 1L;
  private List<SensorDataTableRowModel> rows = new ArrayList<SensorDataTableRowModel>();
  private MultiDayProjectSummary multiDaySummary; 

  /**
   * Default constructor.
   */
  public SensorDataTableModel() {
    // do nothing.
  }
  
  /**
   * Used to set up the model. 
   * @param multiDaySummary The multidayprojrectsummary instance. 
   * @param project The project. 
   */
  public void setModel(MultiDayProjectSummary multiDaySummary, Project project) {
    this.multiDaySummary = multiDaySummary;
    this.rows.clear();
    for (ProjectSummary summary : multiDaySummary.getProjectSummary()) {
      rows.add(new SensorDataTableRowModel(summary));
    }
  }
  
  /**
   * Gets a set of strings containing the name of all Sdts sent during this month. 
   * @return The set of all SDT names.
   */
  public Set<String> getSdtSet() {
    Set<String> sdts = new TreeSet<String>();
    if (this.multiDaySummary == null) {
      return sdts;
    }
    for (ProjectSummary projectSummary : this.multiDaySummary.getProjectSummary()) {
      for (SensorDataSummary summary : 
        projectSummary.getSensorDataSummaries().getSensorDataSummary()) {
        String sdt = summary.getSensorDataType();
        if ((sdt == null) || ("".equals(sdt))) {
          sdt = "Unspecified";
        }
        sdts.add(sdt);
      }
    }
    return sdts;
  }


  /**
   * Returns an iterator over the rows in this model.
   * @param first The first element in the iterator. (Ignored) 
   * @param count The total number of instances.  (Ignored)
   * @return The iterator.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Iterator iterator(int first, int count) {
    return rows.iterator();
  }

  /**
   * The model associated with a row in this table.
   * @param obj The SensorDataTableRowModel.
   * @return The model.
   */
  @Override
  public IModel model(Object obj) {
    return new Model((SensorDataTableRowModel) obj);
  }

  /**
   * The total number of rows in this table.
   * @return The total number of rows. 
   */
  @Override
  public int size() {
    return rows.size();
  }

  /**
   * Does nothing at the moment. 
   */
  @Override
  public void detach() {
    // does nothing.
  }
  
  /**
   * True if this model contains no data.
   * @return True if this model has no data. 
   */
  public boolean isEmpty() {
    return this.rows.size() == 0;
  }

}
