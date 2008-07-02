package org.hackystat.projectbrowser.page.telemetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Properties;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.page.loadingprocesspanel.LoadingProcessPanel;
import org.hackystat.projectbrowser.page.telemetry.inputpanel.TelemetryInputPanel;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Telemetry page.
 * @author Shaoxuan Zhang
 */
public class TestTelemetryPage extends ProjectBrowserTestHelper {
  /** the test user. */
  private String testUser = "TestTelemetryUser";
  /** email of the test user. */
  private String testUserEmail = "TestTelemetryUser@hackystat.org";
  /** the test project. */
  private String testProject = "TestTelemetryProject";
  
  /**
   * Initialize data for testing.
   */
  @Before
  public void setUp() {
    this.generateSimData(testUser, testProject, Tstamp.makeTimestamp(), 2);
  }
  
  /**
   * Test the daily project data page.
   */
  @Test 
  public void testTelemetryPage() {  //NOPMD WicketTester has its own assert classes.
    Properties testProperties = getTestProperties();
    testProperties.put(ProjectBrowserProperties.AVAILABLEPAGE_KEY + ".telemetry", "true");
    WicketTester tester = new WicketTester(new ProjectBrowserApplication(testProperties));
    tester.startPage(SigninPage.class); 
    // Let's sign in.
    FormTester signinForm = tester.newFormTester("signinForm");
    signinForm.setValue("user", testUserEmail);
    signinForm.setValue("password", testUserEmail);
    signinForm.submit("Signin");
    //first, go to daily project data page.
    tester.clickLink("TelemetryPageLink");
    tester.assertRenderedPage(TelemetryPage.class);
    tester.assertComponent("inputPanel", TelemetryInputPanel.class);
    FormTester inputForm = tester.newFormTester("inputPanel:inputForm");
    //checkt the date field.
    assertEquals("The date field should be set to yesterday.", 
          getDateYesterdayAsString(), inputForm.getTextComponentValue("endDateTextField"));

    //check the project list content.
    Component component = inputForm.getForm().get("projectMenu");
    assertTrue("Check project select field", component instanceof ListMultipleChoice);
    ListMultipleChoice projectChoice = (ListMultipleChoice) component;
    boolean pass = false;
    int index = 0;
    for (int i = 0; i < projectChoice.getChoices().size(); i++) {
      Project project = (Project)projectChoice.getChoices().get(i);
      if ("Default".equals(project.getName())) {
        index = i;
        pass = true;
      }
    }
    if (!pass) {
      fail("Default project not found in project list.");
    }
    //select that choice.
    inputForm.select("projectMenu", index);
    inputForm.select("telemetryMenu", 1);
    inputForm.submit();
    //check the result.
    tester.assertRenderedPage(TelemetryPage.class);
    
    tester.assertComponent("loadingProcessPanel", LoadingProcessPanel.class);
    
    /*
    
    tester.assertComponent("dataPanel", TelemetryDataPanel.class);
    assertEquals("chart image should be empty initially.", 
        "", tester.getTagByWicketId("selectedChart").getAttribute("src"));
        
    
    FormTester streamForm = tester.newFormTester("dataPanel:streamForm");
    streamForm.getForm().get("selectAll").setModelObject(true);
    streamForm.submit();
    assertEquals("chart image should be displayed now.", "http://chart.apis.google.com/chart?",
        tester.getTagByWicketId("selectedChart").getAttribute("src"));
        
    */
    
  }
  
  /**
   * Clear testing data.
   */
  @After
  public void clear() {
    this.clearData(testUserEmail);
  }
  
  /**
   * return a String that represent the date of today.
   * @return a String represent today.
   */
  public String getDateYesterdayAsString() {
    XMLGregorianCalendar time = Tstamp.incrementDays(Tstamp.makeTimestamp(), -1);
    String timeString = time.getYear() + "-";
    timeString += (time.getMonth() >= 10) ? time.getMonth() : "0" + time.getMonth();
    timeString += "-";
    timeString += (time.getDay() >= 10) ? time.getDay() : "0" + time.getDay();
    return timeString;
  }
}
