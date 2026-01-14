package com.akshay.websockettask.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "todo_collections")
@Data

//here Todo Collections is considered as noun
public class TodoCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "todoCollection", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<Todo> todos;
}
