package com.store.entity;


import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "service")
@Getter
@ToString
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;


    @Column(name = "service_code")
    String serviceCode;

    @Column(name = "author")
    String author;

    public Service() {
    }

    public Service(Long id, String serviceCode, String author) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.author = author;
    }

    public Service(String serviceCode, String author) {
        this.serviceCode = serviceCode;
        this.author = author;
    }
}
