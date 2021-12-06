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

    public Type() {
    }

    public Type(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public Type(Long id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }
}
