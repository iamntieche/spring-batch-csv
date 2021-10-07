package com.mfoumgroup.batch.dto;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class InputData {

private String productName;
	
	private String productType;
	
	private String productEanCode;
	
	private Double productAmount;
	
	private Integer productQuantity;
	
	private String purchaserFirstName;

	private String purchaserLastName;
	
	private String purchaserEmail;
	
	private String supplierName;
	
	private String supplierAddress;
	
	private Date transactionDate;
}
