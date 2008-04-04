package com.cheesr;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.store.objects.Address;
import com.cheesr.store.objects.Cart;

public class CheckoutPage extends CheesrPage {
	public CheckoutPage() {
		Form form = new Form("form");
		add(form);
		add(new FeedbackPanel("feedback"));
		Address address = getCart().getBillingAddress();

		form.add(new TextField("name", new PropertyModel(address, "name"))
				.setRequired(true));
		form.add(new TextField("street", new PropertyModel(address, "street"))
				.setRequired(true));
		form
				.add(new TextField("zipcode", new PropertyModel(address,
						"zipcode")).setRequired(true).setLabel(
						new Model("Harharhar")));
		form.add(new TextField("city", new PropertyModel(address, "city"))
				.setRequired(true));
		WebMarkupContainer wmcState = new WebMarkupContainer("state-wmc");
		form.add(wmcState);
		wmcState.setVisible(false);
		wmcState.add(new DropDownChoice("state", new PropertyModel(address,
				"state"), CheesrApplication.get().getStates())
				.setRequired(true));

		form.add(new Link("cancel") {
      @Override
			public void onClick() {
				setResponsePage(Index.class);
			}
		});
		form.add(new Button("order") {
			@Override
			public void onSubmit() {
				Cart cart = getCart();
				// process cart

				// clear the cart, retaining the billing info
				cart.getCheeses().clear();
				setResponsePage(Index.class);
			}
		});
		add(new ShoppingCartPanel("cart", getCart()));
	}
}
