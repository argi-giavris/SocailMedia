package org.example.dto;

import org.example.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUsernameAndRoleDto {

    String username;
    Role role;
}
