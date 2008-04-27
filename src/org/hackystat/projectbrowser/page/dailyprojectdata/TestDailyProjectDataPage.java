package org.hackystat.projectbrowser.page.dailyprojectdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
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
    FormTester projectForm = tester.newFormTester("dpdInputPanel:dpdInputForm");
    //checkt the date field.
    assertEquals("The date field should be set to yesterday.", getDateYesterdayAsString(), 
        projectForm.getTextComponentValue("dateTextField"));
    //check the project list content.
    Component component = projectForm.getForm().get("projectMenu");
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
    projectForm.select("projectMenu", index);
    projectForm.select("analysisMenu", 1);
    projectForm.submit();
    //check the result.
    tester.assertRenderedPage(
        org.hackystat.projectbrowser.page.dailyprojectdata.DailyProjectDataPage.class);
    //tester.assertLabel("dpdDataPanel:valuesType", "Count");
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
