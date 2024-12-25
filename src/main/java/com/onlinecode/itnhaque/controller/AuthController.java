package com.onlinecode.itnhaque.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;

import com.onlinecode.itnhaque.domain.User;
import com.onlinecode.itnhaque.domain.request.ReqLoginDTO;
import com.onlinecode.itnhaque.domain.response.ResLoginDTO;
import com.onlinecode.itnhaque.service.UserService;
import com.onlinecode.itnhaque.util.SecurityUtil;

import jakarta.validation.Valid;

public class AuthController {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;

        @Value("${jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(
                        AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService,
                        PasswordEncoder passwordEncoder) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
        }

        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
                // Create an authentication token using the username and password
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword());

                // Authenticate the user => implement the method loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // Set the logged-in user information into the context (can be used later)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Get authorities from Authentication
                String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(" "));

                // create access token
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), authorities);

                ResLoginDTO userDTO = new ResLoginDTO();
                userDTO.setAccessToken(access_token);
                User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
                if (currentUserDB != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName(),
                                        currentUserDB.getRole().getName());
                        userDTO.setUser(userLogin);
                }

                // create refresh token
                String refresh_token = this.securityUtil.createRefreshToken(authentication.getName(), authorities);

                // update user
                this.userService.updateUserToken(loginDto.getUsername(), refresh_token);

                // set cookies
                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(userDTO);
        }
}
