package org.hackystat.projectbrowser.page.dailyprojectdata.filemetric;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hackystat.dailyprojectdata.resource.filemetric.jaxb.FileData;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Gets the DPD for total lines of code.
 * @author Philip Johnson
 */
public class FileMetricData implements Serializable {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project whose data is kept in this instance. */
  private Project project;
  
  /** Holds the total LOC. */
  private int totalLoc = 0;
  
  /** Maintains the (filetype, LOC) data. */
  private Map<String, Integer> fileType2Loc = new TreeMap<String, Integer>(); 
  
  /**
   * Constructs a FileMetricData instance with FileData data. 
   * @param project The project associated with this DevTime data. 
   * @param fileData The individual FileData data. 
   */
  public FileMetricData(Project project, List<FileData> fileData) {
    this.project = project;
    // Update fileType2Loc with the total LOC associated with this file type.
    for (FileData data : fileData) {
      String fileName = data.getFileUri();
      int loc = (int) data.getSizeMetricValue();
      this.totalLoc += loc;
      // Files without a .suffix get file type 'unknown'.
      String fileType = "unknown";
      if (fileName.contains(".")) {
        fileType = fileName.substring(fileName.lastIndexOf('.'));
      }
      if (fileType2Loc.get(fileType) == null) {
        fileType2Loc.put(fileType, 0);
      }
      fileType2Loc.put(fileType, fileType2Loc.get(fileType) + loc);
    }
  }
  
  /**
   * Constructs an empty FileMetricData instance.
   * Used to initialize the session state.  
   * @param project The project.
   */
  public FileMetricData(Project project) {
    this.project = project;
  }
  
  /**
   * Returns the project associated with this instance. 
   * @return The project. 
   */
  public Project getProject() {
    return this.project;
  }
  
  /**
   * Returns the total aggregate LOC for this project on this day. 
   * @return The total DevTime. 
   */
  public int getTotalLoc () {
    return this.totalLoc;
  }
  
  /**
   * Returns a String listing each filetype and their LOC.
   * @return A String of FileMetric info for this project and day. 
   */
  public String getFileMetricData() {
    StringBuffer buff = new StringBuffer();
    for (Map.Entry<String, Integer> entry : fileType2Loc.entrySet()) {
      buff.append(String.format("%s(%d) ", entry.getKey(), entry.getValue()));
    }
    return buff.toString();
  }
}
