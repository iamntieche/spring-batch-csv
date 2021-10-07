package com.mfoumgroup.batch.dto;

import com.mfoumgroup.batch.entities.Order;
import com.mfoumgroup.batch.entities.Product;
import com.mfoumgroup.batch.entities.PurchaseDate;
import com.mfoumgroup.batch.entities.Purchaser;
import com.mfoumgroup.batch.entities.Supplier;

import lombok.Data;

@Data
public class ConvertedInputData {

	private Supplier supplier;

	private Purchaser purchaser;

	private Product product;

	private PurchaseDate purchaseDate;

	private Order order;
}
