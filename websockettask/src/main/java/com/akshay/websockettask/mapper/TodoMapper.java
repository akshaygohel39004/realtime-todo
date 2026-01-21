package com.akshay.websockettask.mapper;

import com.akshay.websockettask.DTO.TodoDto;
import com.akshay.websockettask.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoDto toDto(Todo todo);

    Todo toEntity(TodoDto todoDto);

    List<TodoDto> toDtoList(List<Todo> todos);
}
