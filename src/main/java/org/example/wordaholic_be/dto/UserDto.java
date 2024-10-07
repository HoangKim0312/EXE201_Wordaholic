package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private boolean enabled;
    private boolean active;
}
