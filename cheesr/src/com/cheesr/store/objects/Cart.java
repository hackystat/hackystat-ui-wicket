package com.cheesr.store.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
	private List<Cheese> cheeses = new ArrayList<Cheese>();
	private Address billingAddress = new Address();

	public List<Cheese> getCheeses() {
		return cheeses;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public double getTotal() {
		double total = 0;
		for (Cheese cheese : cheeses) {
			total += cheese.getPrice();
		}
		return total;
	}

	public void add(Cheese cheese) {
		cheeses.add(cheese);
	}

	public void remove(Cheese cheese) {
		cheeses.remove(cheese);
	}
}
