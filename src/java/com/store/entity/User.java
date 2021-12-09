package com.store.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "usrs")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "login")
    String login;

    @Column(name = "name")
    String name;

    @Column(name = "password")
    String password;

    public User(Long id, String login, String name, String password) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public User(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public User(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public User(Long id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }
}
