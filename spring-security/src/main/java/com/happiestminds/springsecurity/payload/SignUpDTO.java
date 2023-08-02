package com.happiestminds.springsecurity.payload;

import lombok.Data;

@Data
public class SignUpDTO {

    private String name;
    private String email;
    private String username;
    private String password;
}
