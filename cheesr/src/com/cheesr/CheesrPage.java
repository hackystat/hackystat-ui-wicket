package com.cheesr;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;

import com.cheesr.store.objects.Cart;
import com.cheesr.store.objects.Cheese;

public abstract class CheesrPage extends WebPage {
	public CheesrPage() {
	}

	public CheesrSession getCheesrSession() {
		return (CheesrSession) getSession();
	}

	public Cart getCart() {
		return getCheesrSession().getCart();
	}
	
	public List<Cheese> getCheeses() {
		return CheesrApplication.get().getCheeses();
	}
}
