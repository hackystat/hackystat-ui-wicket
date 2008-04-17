package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.io.Serializable;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Parent Model class for daily project data.
 * @author Shaoxuan Zhang
 */
public abstract class DailyProjectDataModel implements Serializable {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /** The date for this page.  Represented as a long to avoid findbugs errors. */
  protected long date = 0;

  /** The project this user has selected. */
  protected Project project = null;
  
}
