package org.hackystat.projectbrowser.page.telemetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.page.telemetry.datapanel.TelemetryDataPanel;
import org.hackystat.projectbrowser.page.telemetry.inputpanel.TelemetryInputPanel;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.hackystat.utilities.tstamp.Tstamp;
import org.junit.Test;

/**
 * Tests for Telemetry page.
 * @author Shaoxuan Zhang
 */
public class TestTelemetryPage extends ProjectBrowserTestHelper {

  /**
   * Test the daily project data page.
   */
  @Test 
  public void testTelemetryPage() {  //NOPMD WicketTester has its own assert classes.
    WicketTester tester = new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    tester.startPage(SigninPage.class); 
    // Let's sign in.
    String testUser = "TestUser@hackystat.org";
    String testProject = "TelemetryProject";
    this.generateSimData("TestUser", testProject, Tstamp.makeTimestamp(), 4);
    FormTester signinForm = tester.newFormTester("signinForm");
    signinForm.setValue("user", testUser);
    signinForm.setValue("password", testUser);
    signinForm.submit("Signin");
    //first, go to daily project data page.
    tester.clickLink("TelemetryPageLink");
    tester.assertRenderedPage(TelemetryPage.class);
    tester.assertComponent("inputPanel", TelemetryInputPanel.class);
    try {
      tester.assertComponent("dataPanel", TelemetryDataPanel.class);
      fail();
    }
    catch (NullPointerException e) {
      System.out.println("Confirmed that data panel did not show initially.");
    }
    FormTester inputForm = tester.newFormTester("inputPanel:inputForm");
    //inputForm.selectMultiple("projectMenu", new int[]{0,1,2});
    inputForm.submit();
    System.out.println(tester.getComponentFromLastRenderedPage("FooterFeedback").getModelObject());
    tester.assertComponent("dataPanel", TelemetryDataPanel.class);
    assertEquals("chart image should be empty initially.", 
        "", tester.getTagByWicketId("selectedChart").getAttribute("src"));
    /*
    FormTester streamForm = tester.newFormTester("dataPanel:streamForm");
    streamForm.getForm().get("selectAll").setModelObject(true);
    tester.assertListView("dataPanel:streamForm:projectTable", new ArrayList<String>());
    streamForm.submit();
    assertEquals("chart image should be displayed now.", "http://chart.apis.google.com/chart?",
        tester.getTagByWicketId("selectedChart").getAttribute("src"));
        */
  }
  
  /**
   * return a String that represent the date of today.
   * @return a String represent today.
   */
  public String getDateTodayAsString() {
    XMLGregorianCalendar time = Tstamp.makeTimestamp();
    String timeString = time.getYear() + "-";
    timeString += (time.getMonth() >= 10) ? time.getMonth() : "0" + time.getMonth();
    timeString += "-";
    timeString += (time.getDay() >= 10) ? time.getDay() : "0" + time.getDay();
    return timeString;
  }
}
