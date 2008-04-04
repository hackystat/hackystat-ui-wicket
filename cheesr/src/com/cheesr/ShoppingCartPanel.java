package com.cheesr;

import java.text.NumberFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.store.objects.Cart;
import com.cheesr.store.objects.Cheese;

public class ShoppingCartPanel extends Panel {
	private Cart cart;
	public ShoppingCartPanel(String id, Cart cart) {
		super(id);
		this.cart = cart;
		add(new ListView("cart", new PropertyModel(this, "cart.cheeses")) {

			@Override
			protected void populateItem(ListItem item) {
				Cheese cheese = (Cheese) item.getModelObject();
				item.add(new Label("name", cheese.getName()));
				item.add(new Label("price", "$" + cheese.getPrice()));

				item.add(removeLink("remove", item));
			}
		});
		add(new Label("total", new Model() {
			@Override
			public Object getObject() {
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				return nf.format(getCart().getTotal());
			}
		}));
	}

	public Cart getCart() {
		return cart;
	}
}
