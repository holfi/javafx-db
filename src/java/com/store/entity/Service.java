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

    public Service() {
    }

    public Service(Long id, String serviceCode) {
        this.id = id;
        this.serviceCode = serviceCode;
    }

    public Service(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
