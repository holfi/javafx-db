package com.store.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "status_code")
@Getter
@ToString
public class StatusCode {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "code")
    Long code;

    @Column(name = "name")
    String name;

    @Column(name = "author")
    String author;

    public StatusCode() {
    }

    public StatusCode(Long id, Long code, String name, String author) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.author = author;
    }

    public StatusCode(Long code, String name, String author) {
        this.code = code;
        this.name = name;
        this.author = author;
    }
}
