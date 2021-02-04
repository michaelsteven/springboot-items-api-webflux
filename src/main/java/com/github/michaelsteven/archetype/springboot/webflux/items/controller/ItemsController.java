package com.github.michaelsteven.archetype.springboot.webflux.items.controller;

//import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.michaelsteven.archetype.springboot.webflux.items.model.ApiError;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.service.ItemsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The Class ItemsController.
 */
@RestController
@SecurityScheme(name = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
@Tag(name = "Items", description = "The items api can be used to perform actions on Items")
@Validated
public class ItemsController {
	
	public ItemsController(ItemsService itemsService, MessageSource messageSource) {
		this.itemsService = itemsService;
		this.messageSource = messageSource;
	}
	
	/** The Constant API_PATH. */
	private static final String API_PATH = "${api.path:/api/v1/items}";

	/** The items service. */
	private ItemsService itemsService;
	
	/** The message source. */
	private MessageSource messageSource;
	
	
    /**
     * Gets the items.
     *
     * @param pageable the pageable
     * @return the items
     */
    @Operation(summary = "Retrieve items", description = "Use this API to retrieve a paginated collection of items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    @GetMapping(value = API_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ItemDto> getItems(@RequestParam(required=false,defaultValue="0") int page, @RequestParam(required=false,defaultValue="10") int size, @RequestParam(required=false, defaultValue="id") String[] sort){
    	return itemsService.getItems(PageRequest.of(page, size, Sort.by(sort)));
	}
    
    
    /**
     * Save item.
     *
     * @param itemDto the item dto
     * @return the response entity
     */
    @Operation(summary = "Submit a new item", description = "Use this API to generate a new item. "
            + "In some cases this POST method may return a 202 ACCEPTED response code, "
            + "in which case the data returned will contain a status code, and an identifier. "
            + "The identifier can then be used in subsequent GET calls to obtain the item at a later time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "accepted", content = @Content(schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @PostMapping(API_PATH)
    public Mono<ConfirmationDto> saveItem(@Valid @RequestBody @Parameter(description = "A new item", required = true) ItemDto itemDto){
    	return Mono.just(itemDto).flatMap(itemsService::saveItem);
    }
    
    
    /**
     * Gets the item by id.
     *
     * @param id the id
     * @return the item by id
     */
    @Operation(summary = "Gets an item", description = "Use this API to retrieve an existing item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    @GetMapping(API_PATH + "/{id}")
    public Mono<ItemDto> getItemById(@PathVariable long id){
    	return itemsService.getItemById(id);
    }
    
    
    /**
     * Edits the item.
     *
     * @param id the id
     * @param itemDto the item dto
     * @return the response entity
     */
    @Operation(summary = "Modifies an item", description = "Use this API to modify an item. "
            + "In some cases this PUT method may return a 202 ACCEPTED response code, "
            + "in which case the data returned will contain a status code, and an identifier. "
            + "The identifier can then be used in subsequent GET calls to obtain the item at a later time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "accepted", content = @Content(schema = @Schema(implementation = ConfirmationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    @PutMapping(API_PATH + "/{id}")
    public Mono<ConfirmationDto> editItem(@PathVariable long id, @Valid @RequestBody @Parameter(description = "A modified item", required = true) ItemDto itemDto){
		return Mono.just(itemDto).filter(dto -> id == dto.getId())
				.switchIfEmpty(
					Mono.error(  
						new ValidationException(
							messageSource.getMessage("itemscontroller.validationexception.pathiddoesnotmatchobject", 
								new Object[] { String.valueOf(id), String.valueOf(itemDto.getId()) }, LocaleContextHolder.getLocale()
							)
						)
					)
				)
				.flatMap(itemsService::editItem);
    }
    
    
   
    /**
     * Delete by id.
     *
     * @param id the id
     * @param response the response
     */
    @Operation(summary = "Deletes an item", description = "Use this API to delete an item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @DeleteMapping(API_PATH + "/{id}")
    public void deleteById(@PathVariable long id) {
    	itemsService.deleteItemById(id);
    }
}
