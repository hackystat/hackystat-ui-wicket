package com.cheesr;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

import com.cheesr.store.objects.Cart;

public class CheesrSession extends WebSession {
	private Cart cart = new Cart();

	public CheesrSession(WebApplication application, Request request) {
		super(request);
	}

	public Cart getCart() {
		return cart;
	}
}
