package com.jarek4313.auth.controller;


import com.jarek4313.auth.entity.Code;
import com.jarek4313.auth.entity.User;
import com.jarek4313.auth.entity.dto.UserRegisterDto;
import com.jarek4313.auth.entity.response.AuthResponse;
import com.jarek4313.auth.entity.response.ValidationMessageResponse;
import com.jarek4313.auth.exceptions.UserDontExistException;
import com.jarek4313.auth.exceptions.UserExistingWithMail;
import com.jarek4313.auth.exceptions.UserExistingWithName;
import com.jarek4313.auth.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> addNewUser( @RequestBody UserRegisterDto user) {
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

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        log.info("--TRY LOGIN USER");
        return userService.login(response, user);
    }

    @GetMapping(path = "/auto-login")
    public ResponseEntity<?> autoLogin(HttpServletResponse response, HttpServletRequest request) {
        log.info("--TRY AUTO LOGIN USER");
        return userService.loginByToken(request, response);
    }

    @GetMapping(path = "/logged-in")
    public ResponseEntity<?> loggedIn(HttpServletResponse response, HttpServletRequest request) {
        log.info("--CHECK USER LOGGED-IN");
        return userService.loggedIn(request, response);
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
        log.info("--TRY LOGOUT USER");
        return userService.logout(request, response);
    }

    @GetMapping(path = "/validate")
    public ResponseEntity<AuthResponse> validateToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("--START validate token");
            userService.validateToken(request, response);
            log.info("--STOP validate token");
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            log.info("Token is not correct");
            return ResponseEntity.status(401).body(new AuthResponse(Code.INVALID_TOKEN));
        }
    }

    @GetMapping(path = "/authorize")
    public ResponseEntity<AuthResponse> authorize(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("--START authorize");
            userService.validateToken(request, response);
            userService.authorize(request);
            log.info("--STOP authorize");
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            log.info("Token is not correct");
            return ResponseEntity.status(401).body(new AuthResponse(Code.INVALID_TOKEN));
        } catch (UserDontExistException e1) {
            log.info("User dont exist");
            return ResponseEntity.status(401).body(new AuthResponse(Code.USER_IS_NOT_ACTIVATE));
        }
    }

    @GetMapping(path = "/activate")
    public ResponseEntity<AuthResponse> activateUser(@RequestParam String uuid) {
        try {
            log.info("--START activate user");
            userService.activateUser(uuid);
            log.info("--STOP activate user");
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e){
            log.info("User dont exist in database");
            return ResponseEntity.status(400).body(new AuthResponse(Code.USER_DONT_EXIST));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessageResponse handleValidationExceptions(
            MethodArgumentNotValidException ex
    ){
        return new ValidationMessageResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
