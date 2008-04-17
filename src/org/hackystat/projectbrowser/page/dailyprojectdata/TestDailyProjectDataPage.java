package org.hackystat.projectbrowser.page.dailyprojectdata;

import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.hackystat.utilities.tstamp.Tstamp;
import org.junit.Test;

/**
 * Test class for DailyProjectDataPage
 * @author Shaoxuan Zhang
 *
 */
public class TestDailyProjectDataPage extends ProjectBrowserTestHelper {

  /**
   * Test the daily project data page.
   */
  @Test 
  public void testDailyProjectDataPage() {  //NOPMD WicketTester has its own assert classes.
    WicketTester tester = new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    tester.startPage(SigninPage.class); 
    // Let's sign in.
    String testUser = "TestUser@hackystat.org";
    FormTester signinForm = tester.newFormTester("signinForm");
    signinForm.setValue("user", testUser);
    signinForm.setValue("password", testUser);
    signinForm.submit("Signin");
    //first, go to daily project data page.
    tester.clickLink("DailyProjectDataPageLink");
    tester.assertRenderedPage(DailyProjectDataPage.class);
    /*
    FormTester projectForm = tester.newFormTester("projectDateForm");
    //checkt the date field.
    assertEquals("The date field should be set to today.", getDateToday(), 
        projectForm.getTextComponentValue("dateField"));
    //checkt the project list content. 
    Component component = projectForm.getForm().get("projectChoice");
    assertTrue("Check project select field", component instanceof DropDownChoice);
    DropDownChoice projectChoice = (DropDownChoice) component;
    boolean pass = false;
    for (Object value : projectChoice.getChoices()) {
      if (((String)value).contains("Default -")) {
        pass = true;
      }
    }
    if (!pass) {
      fail("Default project not found in project list.");
    }
    //retrieve the project name of first choice.
    String projectName = (String)projectChoice.getChoices().get(0);
    projectName = projectName.substring(0, projectName.indexOf('-')).trim();
    //select that choice.
    projectForm.select("projectChoice", 0);
    projectForm.select("analysisMenu", 1);
    projectForm.submit();
    //check the result.
    tester.assertContains("Unit Test");
    */
  }
  
  /**
   * return a String that represent the date of today.
   * @return a String represent today.
   */
  public String getDateToday() {
    XMLGregorianCalendar time = Tstamp.makeTimestamp();
    String timeString = time.getYear() + "-";
    timeString += (time.getMonth() >= 10) ? time.getMonth() : "0" + time.getMonth();
    timeString += "-";
    timeString += (time.getDay() >= 10) ? time.getDay() : "0" + time.getDay();
    return timeString;
  }
}
