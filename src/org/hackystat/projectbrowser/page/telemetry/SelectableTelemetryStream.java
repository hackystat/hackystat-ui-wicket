package org.hackystat.projectbrowser.page.telemetry;

import java.io.Serializable;
import org.hackystat.telemetry.service.resource.chart.jaxb.TelemetryStream;

/**
 * Group a selected flag with a TelemetryStream, 
 * so that this instance can be flaged as selected or not.
 * @author Shaoxuan
 *
 */
public class SelectableTelemetryStream implements Serializable {
  /** Support serialization. */
  public static final long serialVersionUID = 1L;
  /** Determine this stream is selected or not. */
  private boolean selected = false;
  /** The TelemetryStream of this stream. */
  private TelemetryStream telemetryStream;
  /**
   * @param telemetryStream the TelemetryStream of this instance.
   */
  public SelectableTelemetryStream(TelemetryStream telemetryStream) {
    this.telemetryStream = telemetryStream;
  }
  /**
   * @param selected the selected to set
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  /**
   * @return the selected
   */
  public boolean isSelected() {
    return selected;
  }
  /**
   * @param telemetryStream the telemetryStream to set
   */
  public void setTelemetryStream(TelemetryStream telemetryStream) {
    this.telemetryStream = telemetryStream;
  }
  /**
   * @return the telemetryStream
   */
  public TelemetryStream getTelemetryStream() {
    return telemetryStream;
  }
}
