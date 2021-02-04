package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.webflux.items.model.ItemEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface ItemDtoMapper {

	ItemDtoMapper INSTANCE = Mappers.getMapper(ItemDtoMapper.class);

    //@Mapping(source = "dto.id", target = "id")
    ItemEntity mapToEntity(ItemDto dto);

    //@Mapping(source = "entity.id", target = "id")
    ItemDto mapToDto(ItemEntity entity);

    List<ItemEntity> mapListToEntity(List<ItemDto> dtoList);

    List<ItemDto> mapListToDto(List<ItemEntity> entity);

    default Mono<ItemEntity> mapMonoDtoToMonoEntity(Mono<ItemDto> monoDto) {
        return Mono.just(mapToEntity(monoDto.block()));
    }

    default Mono<ItemDto> mapMonoEntityToMonoDto(Mono<ItemEntity> monoEntity) {
        return Mono.just(mapToDto(monoEntity.block()));
    }

    default Flux<ItemEntity> mapFluxDtoToFluxEntity(Flux<ItemDto> fluxDto ) {
        return Flux.fromIterable(mapListToEntity(fluxDto.collectList().block()));
    }

    default Flux<ItemDto> mapFluxEntityToFluxDto(Flux<ItemEntity> fluxEntities) {
        return Flux.fromIterable(mapListToDto(fluxEntities.collectList().block()));
    }
}