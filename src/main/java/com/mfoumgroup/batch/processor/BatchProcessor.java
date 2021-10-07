package com.mfoumgroup.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.mfoumgroup.batch.dto.ConvertedInputData;
import com.mfoumgroup.batch.dto.InputData;
import com.mfoumgroup.batch.entities.Order;
import com.mfoumgroup.batch.entities.Product;
import com.mfoumgroup.batch.entities.PurchaseDate;
import com.mfoumgroup.batch.entities.Purchaser;
import com.mfoumgroup.batch.entities.Supplier;
import com.mfoumgroup.batch.repository.ProductRepository;
import com.mfoumgroup.batch.repository.PurchaseDateRepository;
import com.mfoumgroup.batch.repository.PurchaserRepository;
import com.mfoumgroup.batch.repository.SupplierRepository;

/**
 * Spring batch : processing file reader
 * 
 * @author mfoum
 *
 */
public class BatchProcessor implements ItemProcessor<InputData, ConvertedInputData> {

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private PurchaserRepository purchaserRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PurchaseDateRepository purchaseDateRepository;

	@Override
	public ConvertedInputData process(InputData item) throws Exception {
		ConvertedInputData convertedInputData = new ConvertedInputData();
		Order order = new Order();
		Supplier supplier = supplierRepository.findByName(item.getSupplierName());
		Purchaser purchaser = purchaserRepository.findByEmail(item.getPurchaserEmail());
		Product product = productRepository.findByEanCode(item.getProductEanCode());
		PurchaseDate purchaseDate = purchaseDateRepository.findByDate(item.getTransactionDate());

		if (supplier == null) {
			supplier = Supplier.of(null, item.getSupplierName(), item.getSupplierAddress());
			convertedInputData.setSupplier(supplier);
		} else {
			order.setSupplierId(supplier.getId());
		}

		if (purchaser == null) {
			purchaser = Purchaser.of(null, item.getPurchaserFirstName(), item.getPurchaserLastName(),
					item.getPurchaserEmail());
			convertedInputData.setPurchaser(purchaser);
		} else {
			order.setPurchaserId(purchaser.getId());
		}

		if (product == null) {
			product = Product.of(null, item.getProductName(), item.getProductType(), item.getProductEanCode());
			convertedInputData.setProduct(product);
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		} else {
			order.setProductId(product.getId());
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		}

		if (purchaseDate == null) {
			purchaseDate = PurchaseDate.of(null, item.getTransactionDate());
			convertedInputData.setPurchaseDate(purchaseDate);
		} else {
			order.setDateId(purchaseDate.getId());
		}

		convertedInputData.setOrder(order);

		return convertedInputData;
	}

}
