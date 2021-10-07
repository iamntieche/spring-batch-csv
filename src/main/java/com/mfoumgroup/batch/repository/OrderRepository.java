package com.mfoumgroup.batch.repository;

import org.springframework.data.repository.CrudRepository;

import com.mfoumgroup.batch.entities.Order;

public interface OrderRepository  extends CrudRepository<Order, Long>{

}
