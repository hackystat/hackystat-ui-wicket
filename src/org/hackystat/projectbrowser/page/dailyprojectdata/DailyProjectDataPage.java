package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.component.UnitTestPage;
import org.hackystat.projectbrowser.page.projectdatepanel.ProjectDateForm;
import org.hackystat.projectbrowser.page.projectdatepanel.ProjectDatePanel;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;

/**
 * Provides a page with DailyProjectData analysis. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Max number of items per page. */
  protected static final int MEMBERDATA_PER_PAGE = 50;
  /**
   * Creates the DPD page. 
   * @param parameters parameters to configure the page.
   */
  public DailyProjectDataPage(PageParameters parameters) {
    loadParameters(parameters);
    add(new BookmarkablePageLink("UnitTestPageLink", UnitTestPage.class, parameters));
    add(new ProjectDatePanel("projectDatePanel", this));
  }

  
  /**
   * return a PageParameters object that represent the parameters of this page.
   * @return a PageParameters.
   */
  protected PageParameters getParameters() {
    PageParameters parameters = new PageParameters(); 
    parameters.add("projectOwner", this.getProject().getOwner()); 
    parameters.add("projectName", this.getProject().getName()); 
    SimpleDateFormat dateFormat = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.US);
    parameters.add("date", dateFormat.format(this.getDate())); 
    return parameters;
  }
  
  /**
   * load project name, project owner and date from the given parameters.
   * @param parameters the parameters to load from
   */
  private void loadParameters(PageParameters parameters) {
    String projectName = parameters.getString("projectName", "");
    String projectOwner = parameters.getString("projectOwner", "");
    if (!"".equals(projectName) && !"".equals(projectOwner)) {
      for (Project project : ProjectBrowserSession.get().getProjectList()) {
        if (projectName.equals(project.getName()) && projectOwner.equals(project.getOwner())) {
          this.setProject(project);
        }
      }
      if (this.getProject() == null) {
        this.footerFeedback = "You are not able to access Project " + projectName + " - " + 
          projectOwner + ".\n" +
          		"You have to be the member to access the project.";
      }
    }
    Date tempDate = this.getDateToday();
    SimpleDateFormat dateFormat = new SimpleDateFormat(ProjectDateForm.DATA_FORMAT, Locale.US);
    try {
      String date = parameters.getString("date","");
      if (!"".equals(date)) {
        tempDate = dateFormat.parse(date);
      }
    }
    catch (ParseException e) {
      this.footerFeedback = e.getMessage();
    }
    this.setDate(tempDate);
  }

  /**
   * The action to be performed when the user has set the Project and Date fields. 
   */
  @Override
  public void onProjectDateSubmit() {
    this.setResponsePage(this.getPage().getClass(), this.getParameters());
  }
}
