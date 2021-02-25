package com.github.michaelsteven.archetype.springboot.webflux.items.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new item entity.
 */
@Data
@NoArgsConstructor
@Entity
@Table("items")
public class ItemEntity extends Auditable {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
}
