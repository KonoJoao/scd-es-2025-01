package com.ecommerce.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrdersApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrdersApplication.class, args);
		System.out.println("Orders Application Started");
	}
}
