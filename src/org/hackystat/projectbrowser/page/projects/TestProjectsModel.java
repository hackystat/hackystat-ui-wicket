package org.hackystat.projectbrowser.page.projects;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.ProjectBrowserSession;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Projects Model.
 * 
 * @author Randy Cox
 */
public class TestProjectsModel extends ProjectBrowserTestHelper {

  /**
   * Test add prop uri row.
   */
  @Test
  public void testAddPropUriRow() { // NOPMD WicketTester has its own assert classes.
    new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    ProjectsModel model = session.getProjectsModel();

    model.createProject();
    int count = model.getPropUriRowsView().size();
    model.addPropUriRow();
    Assert.assertEquals("addPropUriRow should add one row.", count + 1, model.getPropUriRowsView()
        .size());
  }

  /**
   * Test get extra uri rows.
   */
  @Test
  public void testGetExtraUriRows() { // NOPMD WicketTester has its own assert classes.
    new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    ProjectsModel model = session.getProjectsModel();

    int testValue = 11;
    model.setExtraPropertyUriRows(testValue);
    Assert.assertEquals("getExtraUriRows get/set should be same.", testValue, model
        .getExtraPropertyUriRows());
  }

  /**
   * Test uri pattern access.
   */
  @Test
  public void testUriRowAccess() { // NOPMD WicketTester has its own assert classes.
    List<String> list = new ArrayList<String>();
    list.add("URI_1");
    list.add("URI_2");

    new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    ProjectsModel model = session.getProjectsModel();
    model.createProject();
    model.setProjectUriPatterns(list);
    String result = model.getProjectUriCommaStr();
    Assert.assertEquals("Uri comma str", "URI_1, URI_2", result);
  }

  /**
   * Test remove bold.
   */
  @Test
  public void testRemoveBold() { //NOPMD Wicket has its own asserts
    new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    ProjectsModel model = session.getProjectsModel();
    String line = "test line";
    String boldLine = "<b>" + line + "</b>";
    Assert.assertEquals(line, model.removeBold(boldLine));
  }

  /**
   * Test remove bold.
   */
  @Test
  public void testProjectMembers() { //NOPMD Wicket has its own asserts
    new WicketTester(new ProjectBrowserApplication(getTestProperties()));
    ProjectsSession session = ProjectBrowserSession.get().getProjectsSession();
    ProjectsModel model = session.getProjectsModel();
    model.createProject();
    
    List<String> members = new ArrayList<String>();
    members.add("member1");
    members.add("member2");
    members.add("member3");
    model.setProjectMembers(members);
    
    Assert.assertEquals(3, model.getProjectMembers().size());
    Assert.assertEquals("member1", model.getProjectMembers().get(0));
    Assert.assertEquals("member2", model.getProjectMembers().get(1));
    Assert.assertEquals("member3", model.getProjectMembers().get(2));
    
    members.remove(1);

    model.removeMembers(members);
    Assert.assertEquals(1, model.getProjectMembers().size());
    Assert.assertEquals("member2", model.getProjectMembers().get(0));
  }

}
