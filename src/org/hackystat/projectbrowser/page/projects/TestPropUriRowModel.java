package org.hackystat.projectbrowser.page.projects;

import org.apache.wicket.util.tester.WicketTester;
import org.hackystat.projectbrowser.ProjectBrowserApplication;
import org.hackystat.projectbrowser.test.ProjectBrowserTestHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Projects Model.
 * 
 * @author Randy Cox
 */
public class TestPropUriRowModel extends ProjectBrowserTestHelper {

  /**
   * Test add prop uri row.
   */
  @Test
  public void testPropUriRowModel() { // NOPMD Wicket has its own asserts
    new WicketTester(new ProjectBrowserApplication(getTestProperties()));

    String label = "label";
    String value = "value";
    String uri1 = "uri1";
    String uri2 = "uri2";
    String uri3 = "uri3";

    PropUriRowModel model = new PropUriRowModel();
    model.setPropertyLabel(label);
    model.setPropertyValue(value);
    model.setUriPattern1(uri1);
    model.setUriPattern2(uri2);
    model.setUriPattern3(uri3);
    Assert.assertEquals("PropUriRow = " + label + value + uri1 + uri2 + uri3, model.getString());
  }
}
