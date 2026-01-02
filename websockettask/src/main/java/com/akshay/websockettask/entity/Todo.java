package com.akshay.websockettask.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "todos")
@Data

//here we considered Todo as noun due to this reference :- https://wordtype.org/of/todo

public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID todoId;

    @Column(nullable = false)
    private String subject;

    private String description;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private TodoCollection todoCollection;
}
