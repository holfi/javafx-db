package com.store.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "store")
@Getter
@Setter
@ToString
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "type_id")
    Long typeId;

    @Column(name = "service_id")
    Long serviceId;

    @Column(name = "status_id")
    Long statusId;

    @Column(name = "url")
    String url;

    @Column(name = "namespace")
    String namespace;

    @Column(name = "doc_code")
    String docCode;

    @Column(name = "author")
    String author;

    public Store() {
    }

    public Store(Long id, Long typeId, Long serviceId, Long statusId, String url, String namespace, String docCode, String author) {
        this.id = id;
        this.typeId = typeId;
        this.serviceId = serviceId;
        this.statusId = statusId;
        this.url = url;
        this.namespace = namespace;
        this.docCode = docCode;
        this.author = author;
    }

    public Store(Long typeId, Long serviceId, Long statusId, String url, String namespace, String docCode, String author) {
        this.typeId = typeId;
        this.serviceId = serviceId;
        this.statusId = statusId;
        this.url = url;
        this.namespace = namespace;
        this.docCode = docCode;
        this.author = author;
    }
}
