package org.hackystat.projectbrowser.page.dailyprojectdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.page.ProjectBrowserBasePage;
import org.hackystat.projectbrowser.page.dailyprojectdata.component.UnitTestPage;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Provides a page with DailyProjectData analysis. 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDataPage extends ProjectBrowserBasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Date format used in date field input. */
  public static final String DATA_FORMAT = "yyyy-MM-dd";
  /** date from input. */
  private Date date = null;
  /** project name of the chosen project. */
  private String projectName = null;
  /** project owner of the chosen project. */
  private String projectOwner = null;
  /**
   * Creates the DPD page. 
   * @param parameters parameters to configure the page.
   */
  public DailyProjectDataPage(PageParameters parameters) {
    FeedbackPanel feedback = new FeedbackPanel("feedback");
    this.add(feedback);
    loadParameters(parameters);
    add(new Label("PageContents", "Welcome to DailyProjectData browser"));
    add(new BookmarkablePageLink("UnitTestPageLink", UnitTestPage.class, parameters));
    add(new ProjectSelectForm("projectSelectForm", this));
  }

  /**
   * Update the link to newest parameters.
   */
  /*
  protected void updateLinks() {
    PageParameters parameters = getParameters(); 
    this.replace(new BookmarkablePageLink("UnitTestPageLink", UnitTestPage.class, parameters));
  }
  */
  
  /**
   * return a PageParameters object that represent the parameters of this page.
   * @return a PageParameters.
   */
  protected PageParameters getParameters() {
    PageParameters parameters = new PageParameters(); 
    parameters.add("projectOwner", projectOwner); 
    parameters.add("projectName", projectName); 
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT, Locale.US);
    parameters.add("date", dateFormat.format(date)); 
    return parameters;
  }
  /**
   * load project name, project owner and date from the given parameters.
   * @param parameters the parameters to load from
   */
  private void loadParameters(PageParameters parameters) {
    String projectName = parameters.getString("projectName", "");
    if (!"".equals(projectName)) {
      this.setProjectName(projectName);
    }
    String projectOwner = parameters.getString("projectOwner", "");
    if (!"".equals(projectOwner)) {
      this.setProjectOwner(projectOwner);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT, Locale.US);
    try {
      String date = parameters.getString("date","");
      if (!"".equals(date)) {
        Date tempDate = dateFormat.parse(date);
        this.setDate(tempDate);
      }
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
  }


  /**
   * @param date the date to set
   */
  protected final void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the date
   */
  protected final Date getDate() {
    return date;
  }

  /**
   * @param projectName the projectName to set
   */
  protected final void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  /**
   * @return the projectName
   */
  protected final String getProjectName() {
    return projectName;
  }

  /**
   * @param projectOwner the projectOwner to set
   */
  protected final void setProjectOwner(String projectOwner) {
    this.projectOwner = projectOwner;
  }

  /**
   * @return the projectOwner
   */
  protected final String getProjectOwner() {
    return projectOwner;
  }

  /**
   * Form for selecting project and date to view date from.
   * @author Shaoxuan Zhang
   */
  private class ProjectSelectForm extends Form {
    
    /** Support serialization. */
    private static final long serialVersionUID = 1L;
    //private Map<String, Project> projectMap;
    
    /**
     * creates the ProjectSelectForm
     * @param id the id for of the form.
     * @param dpdPage the dpdPage where this form is.
     */
    public ProjectSelectForm(String id, DailyProjectDataPage dpdPage) {
      super(id);
      // add date field
      if (date == null) {
        date = this.getDateToday();
      }
      DateTextField dateField = new DateTextField("dateField", new PropertyModel(dpdPage, "date"),
          DATA_FORMAT);
      
      dateField.add(new DatePicker());
      dateField.setRequired(true);
      add(dateField);

      // add project choices
      // first, get the list of project
      List<String> projectList = new ArrayList<String>();
      //projectMap = new HashMap<String, Project>();
      try {
        SensorBaseClient sensorBaseClient = ProjectBrowserSession.get().getSensorBaseClient();
        ProjectIndex projectIndex = sensorBaseClient.getProjectIndex(ProjectBrowserSession.get()
            .getUserEmail());
        for (ProjectRef projectRef : projectIndex.getProjectRef()) {
          Project project = sensorBaseClient.getProject(projectRef);
          //projectMap.put(project.getName() + " - " + project.getOwner(), project);
          projectList.add(project.getName() + " - " + project.getOwner());
        }
      }
      catch (SensorBaseClientException e) {
        e.printStackTrace();
      }
      // second, create the choice list instance.
      DropDownChoice projectChoice = new DropDownChoice("projectChoice", new Model(), projectList);
      projectChoice.setRequired(true);
      if (projectName != null && projectOwner != null) {
        projectChoice.setModelObject(projectName + " - " + projectOwner);
      }
      /*
      else if (projectList.size() > 0) {
        projectChoice.setModelObject(projectList.get(0));
      }
      */
      add(projectChoice);
    }

    /**
     * parse the selection String into project name and project owner.
     */
    @Override
    protected void onSubmit() {
      String project = this.get("projectChoice").getModelObjectAsString();
      if (project != null) {
        //Project p = projectMap.get(project);
        int divideIndex = project.indexOf('-');
        projectName = project.substring(0, divideIndex - 1).trim();
        projectOwner = project.substring(divideIndex + 1).trim();
        PageParameters parameters = getParameters(); 
        this.setResponsePage(this.getPage().getClass(), parameters);
        //updateLinks();
      }
    }

    /**
     * make a Date that represent today, at 0:00:00.
     * @return the Date object.
     */
    public final Date getDateToday() {
      XMLGregorianCalendar time = Tstamp.makeTimestamp();
      time.setTime(0, 0, 0);
      return time.toGregorianCalendar().getTime();
    }
  }
}
