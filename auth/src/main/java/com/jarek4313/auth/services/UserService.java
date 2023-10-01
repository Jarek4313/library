package com.jarek4313.auth.services;

import com.jarek4313.auth.entity.Role;
import com.jarek4313.auth.entity.User;
import com.jarek4313.auth.entity.dto.UserRegisterDto;
import com.jarek4313.auth.exceptions.UserExistingWithMail;
import com.jarek4313.auth.exceptions.UserExistingWithName;
import com.jarek4313.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
}
