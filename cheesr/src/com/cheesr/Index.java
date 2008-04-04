package com.cheesr;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

import com.cheesr.store.objects.Cheese;

public class Index extends CheesrPage {
	public Index() {
		PageableListView cheeses = new PageableListView("cheeses",
				getCheeses(), 5) {
			@Override
			protected void populateItem(ListItem item) {
				Cheese cheese = (Cheese) item.getModelObject();
				item.add(new Label("name", cheese.getName()));
				item.add(new Label("description", cheese.getDescription()));
				item.add(new Label("price", "$" + cheese.getPrice()));

				item.add(new Link("add", item.getModel()) {

					@Override
					public void onClick() {
						Cheese selected = (Cheese) getModelObject();
						getCart().add(selected);
					}
				});
			}
		};
		add(cheeses);
		add(new PagingNavigator("navigator", cheeses));
		add(new ShoppingCartPanel("cart", getCart()));
		add(new Link("checkout") {
			@Override
			public void onClick() {
				setResponsePage(new CheckoutPage());
			}

			@Override
			public boolean isVisible() {
				return !getCart().getCheeses().isEmpty();
			}
		});
	}
}
