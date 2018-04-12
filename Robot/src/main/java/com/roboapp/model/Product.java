package com.roboapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
	double price;
	String productName;
	String productNo;
}
