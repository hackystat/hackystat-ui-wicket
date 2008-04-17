package org.hackystat.projectbrowser.googlechart;

/**
 * Enumeration for Google Chart Type.
 * @author Shaoxuan Zhang
 */
public enum ChartType {
  /** line chart. */
  LINE("lc"),
  /** spark line chart. */
  SPARK_LINE("ls"),
  /** horizontal bars that present data from different data sets in a single bar. */
  HORIZONTAL_SERIES_BAR("bhs"),
  /** vertical bars that present data from different data set in a single bar. */
  VERTICAL_SERIES_BAR("bvs"),
  /** horizontal bars that present data from different data sets in different bars. */
  HORIZONTAL_GROUP_BAR("bhg"),
  /** vertical bars that present data from different data sets in different bars. */
  VERTICAL_GROUP_BAR("bvg"),
  /** pie chart. */
  PIE("p"),
  /** 3D pie chart. */
  PIE_3D("p3"),
  /** venn diagrams chart. */
  VENN_DIAGRAMS("v"),
  /** scatter plots chart. */
  SCATTER_PLOTS("s"),
  /** radar chart. */
  RADAR("r"),
  /** spline radar chart. */
  SPLINE_RADAR("rs")
  ;
  /** abbreviation of this enumeration. */
  private final String abbrev;

  /** 
   * constructor with a parameter.
   * @param abbrev the abbreviation of this enumeration.
   */
  ChartType(String abbrev) {
    this.abbrev = abbrev;
  }
  /** @return the abbrev */
  public String abbrev() {
    return this.abbrev;
  }
}
