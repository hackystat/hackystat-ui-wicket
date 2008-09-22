package org.hackystat.projectbrowser.page.projectportfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Properties;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.hackystat.projectbrowser.authentication.SigninPage;
import org.hackystat.projectbrowser.page.projectportfolio.configurationpanel.
            ProjectPortfolioConfigurationPanel;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.MiniBarChart;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.ProjectPortfolioDetailsPanel;
import org.hackystat.projectbrowser.page.projectportfolio.inputpanel.ProjectPortfolioInputPanel;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.utilities.tstamp.Tstamp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for ProjectPortfolio page.
 * @author Shaoxuan Zhang
 */
public class TestProjectPortfolioPage extends ProjectBrowserTestHelper {

  /** the test user. */
  private String testUser = "TestProjectPortfolioUser";
  /** email of the test user. */
  private String testUserEmail = "TestProjectPortfolioUser@hackystat.org";
  /** the test project. */
  private String testProject = "TestProjectPortfolioProject";
  
  /** The word of "true". */
  private static final String TRUE = "true";
  /** The word of "false". */
  private static final String FALSE = "false";
  
  /** The start date. */
  private static final String testStartDate = "2007-01-01";
  /** The end date. */
  private static final String testEndDate = "2007-01-10";

  /**
   * Initialize data for testing.
   * @throws Exception when error occur.
   */
  @Before
  public void setUp() throws Exception {
    this.generateSimData(testUser, testProject, Tstamp.makeTimestamp(testEndDate), 0);
  }
  
  /**
   * Test the daily project data page.
   */
  @Test 
  public void testProjectPortfolioPage() {  //NOPMD WicketTester has its own assert classes.
    this.generateSimData(testUser, testProject, Tstamp.makeTimestamp(), 1);
    //prepare test properties.
    Properties testProperties = getTestProperties();
    testProperties.put(ProjectBrowserProperties.AVAILABLEPAGE_KEY + ".projectportfolio", TRUE);
    testProperties.put(
        ProjectBrowserProperties.BACKGROUND_PROCESS_KEY + ".projectportfolio", FALSE);
    WicketTester tester = new WicketTester(new ProjectBrowserApplication(testProperties));
    
    tester.startPage(SigninPage.class); 
    // Let's sign in.
    FormTester signinForm = tester.newFormTester("signinForm");
    signinForm.setValue("user", testUserEmail);
    signinForm.setValue("password", testUserEmail);
    signinForm.submit("Signin");
    //first, go to project portfolio page.
    tester.clickLink("ProjectPortfolioPageLink");
    tester.assertRenderedPage(ProjectPortfolioPage.class);
    
    tester.assertInvisible("configurationPanel");
    tester.clickLink("inputPanel:inputForm:configuration");
    tester.assertComponent("configurationPanel", ProjectPortfolioConfigurationPanel.class);

    FormTester configurationForm = tester.newFormTester("configurationPanel:configurationForm");
    ListView measureList = (ListView)configurationForm.getForm().get("measureList");
    assertTrue("There should be 10 measures.", measureList.getList().size() > 2);
    //set the second measure to be uncolorable.
    configurationForm.setValue("measureList:1:colorableCheckBox", FALSE);
    //set all others to be disable.
    for (int i = 2; i < measureList.getList().size(); i++) {
      configurationForm.setValue("measureList:" + i + ":enableCheckBox", FALSE);
    }
    configurationForm.submit();
    tester.assertInvisible("configurationPanel");

    tester.assertComponent("inputPanel", ProjectPortfolioInputPanel.class);
    FormTester inputForm = tester.newFormTester("inputPanel:inputForm");

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
    inputForm.select("granularity", 0);
    inputForm.setValue("startDate", testStartDate);
    inputForm.setValue("endDate", testEndDate);
    inputForm.submit("submit");
    //check the result.
    tester.assertRenderedPage(ProjectPortfolioPage.class);
    
    tester.isInvisible("loadingProcessPanel");
    tester.assertComponent("detailPanel", ProjectPortfolioDetailsPanel.class);
    ListView measures = 
      (ListView)tester.getComponentFromLastRenderedPage("detailPanel:projectTable:0:measures");
    assertEquals("Should be only 2 measures there.", 2, measures.size());
    MiniBarChart coverage = (MiniBarChart)measures.getList().get(0);
    assertEquals("Check measure name.", "Coverage", coverage.getConfiguration().getName());
    assertTrue("Coverage should be colorable", coverage.getConfiguration().isColorable());
    
    MiniBarChart complexity = (MiniBarChart)measures.getList().get(1);
    assertEquals("Check measure name.", "CyclomaticComplexity", 
        complexity.getConfiguration().getName());
    assertFalse("Complexity should be uncolorable", complexity.getConfiguration().isColorable());
    
  }
  
  /**
   * Clear testing data.
   */
  @After
  public void clear() {
    this.clearData(testUserEmail);
  }
  
}
