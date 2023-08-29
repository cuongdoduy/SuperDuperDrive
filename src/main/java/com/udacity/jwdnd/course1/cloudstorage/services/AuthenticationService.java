package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialRepository;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserRepository;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private CredentialRepository credentialRepository;
    private UserRepository userRepository;
    private HashService hashService;

    public AuthenticationService(
            CredentialRepository credentialRepository,
            UserRepository userRepository,
            HashService hashService) {

        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = this.userRepository.getUserByUsername(username);

        if (user != null) {

            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password, encodedSalt);

            if (user.getPassword().equals(hashedPassword)) {
                return new UsernamePasswordAuthenticationToken(
                        username, password, new ArrayList<>());
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}