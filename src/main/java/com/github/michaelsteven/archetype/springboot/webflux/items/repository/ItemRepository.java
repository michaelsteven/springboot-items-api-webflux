package com.github.michaelsteven.archetype.springboot.webflux.items.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemEntity;

import reactor.core.publisher.Flux;


/**
 * The Interface ItemRepository.
 */
@Repository
public interface ItemRepository extends ReactiveCrudRepository<ItemEntity, Long> {
	Flux<ItemEntity> findByIdNotNull(Pageable pageable);
}
