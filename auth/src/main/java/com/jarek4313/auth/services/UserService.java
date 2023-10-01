package com.jarek4313.auth.services;

import com.jarek4313.auth.entity.Code;
import com.jarek4313.auth.entity.Role;
import com.jarek4313.auth.entity.User;
import com.jarek4313.auth.entity.dto.UserRegisterDto;
import com.jarek4313.auth.entity.response.AuthResponse;
import com.jarek4313.auth.exceptions.UserExistingWithMail;
import com.jarek4313.auth.exceptions.UserExistingWithName;
import com.jarek4313.auth.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
