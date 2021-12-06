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
    int code;

    @Column(name = "name")
    String name;

    public StatusCode() {
    }

    public StatusCode(Long id, int code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
