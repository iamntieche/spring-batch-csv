package com.mfoumgroup.batch.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor(staticName="of")
@Getter
@ToString
@Table("DIM_DATE")
public class PurchaseDate {

	@Id
	@Column("ID")
	private Long id;
	
	@Column("DATE_TIME")
	private Date date;

}
