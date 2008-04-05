package com.cheesr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import com.cheesr.store.objects.Cheese;

public class TestIndexPage {
  @Test public void testIndex() {
    WicketTester tester = new WicketTester(new CheesrApplication()); 
    tester.startPage(Index.class);
    // See that some text appears in the Index.html page. 
    tester.assertContains("Gouda");
    // See that the following components are rendered.
    assertNotNull("wicket ID cheeses found.", tester.getTagByWicketId("cheeses"));
    assertNotNull("wicket ID navigator found.", tester.getTagByWicketId("navigator"));
    assertNotNull("wicket ID cart found.", tester.getTagByWicketId("cart"));
    // See that the checkout pane is not shown initially.
    tester.assertInvisible("checkout");
    // If we want to look at the model directly, we can do something like this.
    Model cheesesModel = (Model)tester.getComponentFromLastRenderedPage("cheeses").getModel();
    List<Cheese> cheeseList = (List<Cheese>)cheesesModel.getObject();
    assertEquals("Testing first cheese is gouda", "Gouda", cheeseList.get(0).getName());
    // Here's how to click on a link inside the Cheeses list
    tester.clickLink("cheeses:0:add");
    tester.assertModelValue("cart:total", "$1.65");
    // Now the checkout link is visible, so we can click it.
    tester.clickLink("checkout");
    // Now we check that we've come to the Checkout page. 
    tester.assertRenderedPage(CheckoutPage.class);
    
    // Now let's try to fill out the checkout page form.
    FormTester formTester = tester.newFormTester("form");
    assertEquals("", formTester.getTextComponentValue("name"));
    formTester.setValue("name", "Philip");
    formTester.setValue("street", "Main Street");
    formTester.setValue("zipcode", "96822");
    formTester.setValue("city", "Anchorage");
    formTester.setValue("state", "Alaska");
    formTester.submit("order");
    tester.assertRenderedPage(Index.class);
    }
}
