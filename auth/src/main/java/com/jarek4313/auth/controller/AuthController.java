package com.jarek4313.auth.controller;


import com.jarek4313.auth.entity.Code;
import com.jarek4313.auth.entity.dto.UserRegisterDto;
import com.jarek4313.auth.entity.response.AuthResponse;
import com.jarek4313.auth.exceptions.UserExistingWithMail;
import com.jarek4313.auth.exceptions.UserExistingWithName;
import com.jarek4313.auth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> addNewUser(@Valid @RequestBody UserRegisterDto user) {
        try {
            log.info("--START REGISTER USER");
            userService.register(user);
            log.info("--STOP REGISTER USER");
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserExistingWithName e) {
            log.info("User dont exist in database");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.USER_ALREADY_EXISTS_LOGIN));
        }catch (UserExistingWithMail existing){
            log.info("User dont exist in database with this mail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.USER_ALREADY_EXISTS_MAIL));
        }
    }
}
