package com.github.michaelsteven.archetype.springboot.webflux.items.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new item entity.
 */
@Data
@NoArgsConstructor
//@Entity
@Table("items")
public class ItemEntity {

	/** The id. */
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The created timestamp. */
	@CreatedDate
	@Column("created_ts")
	private Instant createdTimestamp;
}
