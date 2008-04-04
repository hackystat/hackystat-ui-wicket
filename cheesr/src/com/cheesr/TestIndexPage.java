package com.cheesr;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class TestIndexPage {
  @Test public void labelContainsHelloWorld() {
    WicketTester tester = new WicketTester(new CheesrApplication()); 
    tester.setupRequestAndResponse();
    tester.startPage(Index.class); 
    tester.assertContains("Gouda");
    }
}
