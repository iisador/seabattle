package ru.isador.games.seabattle.domain.user;

import java.io.Serializable;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
@UserDefinition
public class User implements Serializable {

    @Id
    @Username
    private String name;

    @Password
    private String password;

    @Roles
    private String role;

    @Column(name = "raw_pass")
    private String rawPass;

    public User() {
    }

    public User(String name, String password, String role, String rawPass) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.rawPass = rawPass;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getRawPass() {
        return rawPass;
    }
}
