package com.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		System.out.println("Product subscription based billing system");
		PlanPricing planPricing = new PlanPricing(1, 5);
		Product product = new Product(1, "Jira", Map.of("Basic", planPricing));

		ProductSubscription subscription = new ProductSubscription(product, new Date(), "Basic");
		User user = new User(1L, List.of(subscription));
		user.getBilling().printPricing();
	}
}

class Billing {
	float[] monthlyBill;
	float yearlyBill;

	public Billing(float[] monthlyBill, float yearlyBill) {
		this.monthlyBill = monthlyBill;
		this.yearlyBill = yearlyBill;
	}

	void printPricing() {

		System.out.println("Monthly bill " + Arrays.toString(monthlyBill));
		System.out.println("Yearly bill " + yearlyBill);
	}
}

class PlanPricing {
	float monthlyPrice;
	float yearlyPrice;

	public PlanPricing(float monthlyPrice, float yearlyPrice) {
		this.monthlyPrice = monthlyPrice;
		this.yearlyPrice = yearlyPrice;
	}

}

class Product {
	long pid;
	String name;
	Map<String, PlanPricing> subscriptionTypes;

	public Product(long pid, String name, Map<String, PlanPricing> subscriptionTypes) {
		this.pid = pid;
		this.name = name;
		this.subscriptionTypes = subscriptionTypes;
	}
	
	public float getMonthlyPriceBySubscription(String type) {
		return subscriptionTypes.get(type).monthlyPrice;
	}

	public float getYearlyPriceBySubscription(String type) {
		return subscriptionTypes.get(type).yearlyPrice;
	}
	
}

class ProductSubscription {
	Product product;
	Date dateOfSubscription;
	String subtype;

	public ProductSubscription(Product product, Date dateOfSubscription, String subtype) {
		this.product = product;
		this.dateOfSubscription = dateOfSubscription;
		this.subtype = subtype;
	}

	float[] getMonthlyBill() {
		float[] list = new float[12];
		int monthStart = dateOfSubscription.getMonth();
		for (int i = monthStart; i < 12; i++) {
			list[i] = product.getMonthlyPriceBySubscription(subtype);
		}
		return list;
	}

	float getYearlyBill() {
		return product.getYearlyPriceBySubscription(subtype);
	}


}

class User {
	Long userId;
	List<ProductSubscription> subscriptions;

	public User(Long userId, List<ProductSubscription> subscriptions) {
		this.userId = userId;
		this.subscriptions = subscriptions;
	}

	Billing getBilling() {
		return new Billing(getAllMonthlyBill(), getAllYearlyBill());
	}

	float[] getAllMonthlyBill() {

		float[] list = new float[12];

		for (ProductSubscription product : subscriptions) {

			float[] monthly = product.getMonthlyBill();
			for (int i = 0; i < 12; i++) {
				list[i] += monthly[i];
			}
		}
		return list;
	}

	float getAllYearlyBill() {

		float yearly = 0;

		for (ProductSubscription product : subscriptions) {

			yearly += product.getYearlyBill();

		}
		return yearly;
	}
}
