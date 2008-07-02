package org.hackystat.projectbrowser.page.sensordata;

import java.util.Properties;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.page.WelcomePage;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.junit.Test;

/**
 * Tests the SensorDataPage.  
 * @author Philip Johnson
 */
public class TestSensorDataPage extends ProjectBrowserTestHelper {
  
  /**
   * This is more of a stub, since the page has no contents yet, but shows how to login and 
   * get there. 
   */
  @Test 
  public void testSensorDataPage() {  //NOPMD WicketTester has its own assert classes.
    Properties testProperties = getTestProperties();
    testProperties.put(ProjectBrowserProperties.AVAILABLEPAGE_KEY + ".sensordata", "true");
    WicketTester tester = new WicketTester(new ProjectBrowserApplication(testProperties));
    tester.startPage(SigninPage.class); 
    // Let's sign in.
    String testUser = "TestUser@hackystat.org";
    FormTester signinForm = tester.newFormTester("signinForm");
    signinForm.setValue("user", testUser);
    signinForm.setValue("password", testUser);
    signinForm.submit("Signin");
    // Check to see that signin was successful; we're now at the SensorDataPage.     
    tester.assertRenderedPage(WelcomePage.class);
    tester.clickLink("SensorDataPageLink");
    tester.assertRenderedPage(SensorDataPage.class);
  }
}
