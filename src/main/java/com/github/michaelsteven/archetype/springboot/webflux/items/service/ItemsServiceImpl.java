package com.github.michaelsteven.archetype.springboot.webflux.items.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.michaelsteven.archetype.springboot.webflux.items.configuration.ItemDtoMapper;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemEntity;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemStatus;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.event.Compliance;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.event.ComplianceAction;
import com.github.michaelsteven.archetype.springboot.webflux.items.repository.ItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Class ItemsServiceImpl.
 */
@Service
@Validated
public class ItemsServiceImpl implements ItemsService {
		
	public static final Logger logger = LoggerFactory.getLogger(ItemsServiceImpl.class);
	
	private ItemRepository itemRepository;
	private MessageSource messageSource;
	private ItemDtoMapper itemDtoMapper;

	
	/**
	 * Constructor.
	 * 
	 * @param itemRepository
	 * @param messageSource
	 */
	public  ItemsServiceImpl(ItemRepository itemRepository, MessageSource messageSource, ItemDtoMapper itemDtoMapper) {
		this.itemRepository = itemRepository;
		this.messageSource = messageSource;
		this.itemDtoMapper = itemDtoMapper;
	}
	
	
	/**
	 * Gets the items.
	 *
	 * @param pageable the pageable
	 * @return the items
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Flux<ItemDto> getItems(Pageable pageable){
		//return itemRepository.findAll().map(item -> itemDtoMapper.mapToDto(item));
		return itemRepository.findByIdNotNull(pageable).map(item -> itemDtoMapper.mapToDto(item));
	}
	
	
	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Mono<ItemDto> getItemById(long id){
		 return itemRepository.findById(id).map(itemDtoMapper::mapToDto);
	}
	
	
	/**
	 * Save item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Compliance(action = ComplianceAction.create)
	public Mono<ConfirmationDto> saveItem(@NotNull @Valid ItemDto itemDto) {
		return itemRepository.save(itemDtoMapper.mapToEntity(itemDto)).map(item -> createConfirmationDto(ItemStatus.SUBMITTED, item));
	}
	
	
	/**
	 * Edits the item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Compliance(action = ComplianceAction.update)
	public Mono<ConfirmationDto> editItem(@NotNull @Valid ItemDto itemDto) {
		Mono<ItemEntity> fallback =  Mono.error(  
						new ValidationException(
								messageSource.getMessage("itemsservice.validationexception.entitynotfoundforid", 
										new Object[] { String.valueOf(itemDto.getId()) },
										LocaleContextHolder.getLocale()
										)
								)
				);
		return itemRepository.findById(itemDto.getId()).switchIfEmpty(fallback).map(item -> createConfirmationDto(ItemStatus.SUBMITTED, item));
	}
	
	
	/**
	 * Delete item by id.
	 *
	 * @param id the id
	 */
	@Override
	@Compliance(action = ComplianceAction.delete)
	public void deleteItemById(long id){
		itemRepository.findById(id).flatMap(this.itemRepository::delete);
	}
	
	
	/**
	 * Creates the confirmation dto.
	 *
	 * @param itemStatus the item status
	 * @param entity the entity
	 * @return the confirmation dto
	 */
	private ConfirmationDto createConfirmationDto(ItemStatus itemStatus, ItemEntity entity) {
		ConfirmationDto confirmationDto = new ConfirmationDto();
		confirmationDto.setStatus(itemStatus);
		if(null != entity) {
			confirmationDto.setId(entity.getId());
			if(null != entity.getCreatedTimestamp()) {
				ZonedDateTime dateSubmitted = ZonedDateTime.ofInstant(entity.getCreatedTimestamp(), ZoneOffset.UTC);
				confirmationDto.setDateSubmitted(dateSubmitted);
			}
		}
		else {
			logger.warn("itemEntity is null");
		}
		return confirmationDto;
	}
}
