package com.jarek4313.auth.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jarek4313.auth.entity.Role;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserRegisterDto {
    @Length(min = 5,max = 50, message = "Login powinien mieć od 5 do 50 znaków")
    private String login;
    @Email
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 8,max = 75, message = "Hasło powinno skałdać się od 8 do 75 znaków")
    private String password;
    private Role role;
}
