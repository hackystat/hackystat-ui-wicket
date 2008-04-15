package org.hackystat.projectbrowser.page.sensordata;

import java.util.Date;

import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;

/**
 * Provides a page with information about SensorData. 
 * @author Philip Johnson
 */
public class SensorDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The project name this user has selected. */
  private String projectName = "";
  
  /** The date this user has selected. */
  private Date date = null;
  
  /**
   * Creates this page. 
   */
  public SensorDataPage() {
    add(new SensorDataForm("sensorDataForm", this));
  }
  
  @Override
  public void onProjectDateSubmit() {
    System.out.println("Display table: " + this.date + " " + this.projectName);
  }
  
//  public Date getDate() {
//    return this.date;
//  }
//  
//  public void setDate(Date date) {
//    this.date = date; 
//  }
//  
//  public String getProjectName() {
//    return this.projectName;
//  }
//  
//  public void setProjectName(String projectName) {
//    this.projectName = projectName;
//  }
}
