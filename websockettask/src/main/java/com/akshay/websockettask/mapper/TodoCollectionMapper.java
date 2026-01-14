package com.akshay.websockettask.mapper;

import com.akshay.websockettask.DTO.TodoCollectionDto;
import com.akshay.websockettask.entity.TodoCollection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoCollectionMapper {

    TodoCollectionDto toDto(TodoCollection todoCollection);

    TodoCollection toEntity(TodoCollectionDto todoCollectionDto);

    List<TodoCollectionDto> toDtoList(List<TodoCollection> entities);
}
