package com.store.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "usrs")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "login")
    String login;

    @Column(name = "name")
    String name;

    public User(Long id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }

    public User(String login, String name) {
        this.login = login;
        this.name = name;
    }
}
