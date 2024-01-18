package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    Integer id;
    String username;
    String password;
    Role role;

    public User(String username ,String password, String email, Role role) {
        emailHasValidFormat(username);

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

    public void emailHasValidFormat(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean isCorrectFormat = Pattern.compile(regexPattern)
                .matcher(email)
                .matches();

        if (!isCorrectFormat) {
            throw new RuntimeException("Invalid email format");
        }
    }

}


