package com.github.michaelsteven.archetype.springboot.webflux.items.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;

import com.github.michaelsteven.archetype.springboot.webflux.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Interface ItemsService.
 */
public interface ItemsService {
	
	/**
	 * Gets the items.
	 *
	 * @param pageable the pageable
	 * @return the items
	 */
	public abstract Flux<ItemDto> getItems(Pageable pageable);
	
	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	public abstract Mono<ItemDto> getItemById(long id);
	
	/**
	 * Save item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	public abstract Mono<ConfirmationDto> saveItem(@NotNull @Valid ItemDto itemDto);
	
	/**
	 * Edits the item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	public abstract Mono<ConfirmationDto> editItem(@NotNull @Valid ItemDto itemDto);
	
	/**
	 * Delete item by id.
	 *
	 * @param id the id
	 */
	public abstract void deleteItemById(long id);

}
