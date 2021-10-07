package com.mfoumgroup.batch.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mfoumgroup.batch.entities.Purchaser;

public interface PurchaserRepository extends CrudRepository<Purchaser, Long> {

	@Query("select * from DIM_EMPLOYEE_PURCHASER where EMAIL = :email")
	public Purchaser findByEmail(@Param("email") String email);

}
