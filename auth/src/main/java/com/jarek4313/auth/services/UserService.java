package com.jarek4313.auth.services;

import com.jarek4313.auth.entity.Code;
import com.jarek4313.auth.entity.Role;
import com.jarek4313.auth.entity.User;
import com.jarek4313.auth.entity.dto.UserRegisterDto;
import com.jarek4313.auth.entity.response.AuthResponse;
import com.jarek4313.auth.entity.response.LoginResponse;
import com.jarek4313.auth.exceptions.UserExistingWithMail;
import com.jarek4313.auth.exceptions.UserExistingWithName;
import com.jarek4313.auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final JwtService jwtService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    public void register(UserRegisterDto userRegisterDto)
            throws UserExistingWithName, UserExistingWithMail {
        userRepository.findUserByLogin(userRegisterDto.getLogin()).ifPresent(value -> {
            log.info("Users alredy exist with this name");
            throw new UserExistingWithName("Users alredy exist with this name");
        });
        userRepository.findUserByEmail(userRegisterDto.getEmail()).ifPresent(value->{
            log.info("Users alredy exist with this mail");
            throw new UserExistingWithMail("Users alredy exist with this mail");
        });
        User user = new User();
        user.setLock(true);
        user.setEnabled(false);
        user.setLogin(userRegisterDto.getLogin());
        user.setPassword(userRegisterDto.getPassword());
        user.setEmail(userRegisterDto.getEmail());
        user.setRole(Role.USER);
        saveUser(user);
        emailService.sendActivation(user);
    }

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
//        log.info("--START login service");
        User user = userRepository.findUserByLoginAndLockAndEnabled(authRequest.getUsername()).orElse(null);
        if (Objects.nonNull(user)) {
            Authentication authenticate =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookies("refresh", generateToken(authRequest.getUsername(), refreshExp), refreshExp);
                Cookie cookie = cookieService.generateCookies("Authorization", generateToken(authRequest.getUsername(),exp), exp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                        UserRegisterDto
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build());
            } else {
                return ResponseEntity.ok(new AuthResponse(Code.USER_IS_NOT_ACTIVATE));
            }
        }
        log.info("User dont exist");
        return ResponseEntity.ok(new AuthResponse(Code.USER_INVALID_CREDENTIAL));
    }

    private String generateToken(String username, int exp) {
        return jwtService.generateToken(username, exp);
    }

    public ResponseEntity<?> loginByToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            String refresh = null;
            for (Cookie value: Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
            String login = jwtService.getSubject(refresh);
            User user = userRepository.findUserByLoginAndLockAndEnabled(login).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(
                        UserRegisterDto.builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build());
            }
            log.info("Can't login user don't exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.USER_IS_NOT_ACTIVATE));
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.INVALID_TOKEN));
        }
    }

    private void validateToken(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie value: Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
        } else {
            log.info("Can't login because in token is empty");
            throw new IllegalArgumentException("Token can't be null");
        }

        try {
            jwtService.validateToken(token);
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            jwtService.validateToken(refresh);
            Cookie refreshCookie = cookieService.generateCookies("refresh", jwtService.refreshToken(refresh, refreshExp), refreshExp);
            Cookie cookie = cookieService.generateCookies("Authorization", jwtService.refreshToken(refresh, exp), exp);
            response.addCookie(cookie);
            response.addCookie(refreshCookie);
        }
    }

    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            return ResponseEntity.ok(new LoginResponse(true));
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return ResponseEntity.ok(new LoginResponse(false));
        }
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete all cookies");
        Cookie cookie = cookieService.removeCookie(request.getCookies(), "Authorization");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(request.getCookies(), "refresh");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }
}
