package com.github.michaelsteven.archetype.springboot.webflux.items.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

/**
 * Instantiates a new auditable.
 */
@Data
public abstract class Auditable {

	/** The created by. */
	@CreatedBy
	private String createdBy;
	
	/** The created timestamp. */
	@CreatedDate
	@Column("created_ts")
	private Instant createdTimestamp;
	
	/** The updated by. */
	@LastModifiedBy
	private String updatedBy;
	
	/** The updated timestamp. */
	@LastModifiedDate
	@Column("updated_ts")
	private Instant updatedTimestamp;
	
}
