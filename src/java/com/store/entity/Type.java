package com.store.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "type")
@Getter
@Setter
@ToString
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "label")
    String label;

    @Column(name = "description")
    String description;

    @Column(name = "author")
    String author;

    public Type() {
    }

    public Type(String label, String description, String author) {
        this.label = label;
        this.description = description;
        this.author = author;
    }

    public Type(Long id, String label, String description, String author) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.author = author;
    }
}
