package org.hackystat.projectbrowser.googlechart;

public enum ChartType {
  LINE("lc"),
  SPARK_LINE("ls"),
  HORIZONTAL_SERIES_BAR("bhs"),
  VERTICAL_SERIES_BAR("bvs"),
  HORIZONTAL_GROUP_BAR("bhg"),
  VERTICAL_GROUP_BAR("bvg"),
  PIE("p"),
  PIE_3D("p3"),
  VENN_DIAGRAMS("v"),
  SCATTER_PLOTS("s"),
  RADAR("r"),
  SPLINE_RADAR("rs")
  ;
  private final String abbrev;
  
  ChartType(String abbrev) {
    this.abbrev = abbrev;
  }
  public String abbrev() {
    return this.abbrev;
  }
}
