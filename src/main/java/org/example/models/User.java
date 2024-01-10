package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    Integer id;
    String username;
    String password;
    Role role;

    public User(String username ,String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }
}


