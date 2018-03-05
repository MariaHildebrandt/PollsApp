package com.hildebrandt.polls.controller;

import com.hildebrandt.polls.exception.AppException;
import com.hildebrandt.polls.model.Role;
import com.hildebrandt.polls.model.RoleName;
import com.hildebrandt.polls.model.User;
import com.hildebrandt.polls.payload.ApiResponse;
import com.hildebrandt.polls.payload.JwtAuthenticationResponse;
import com.hildebrandt.polls.payload.LoginRequest;
import com.hildebrandt.polls.payload.SignUpRequest;
import com.hildebrandt.polls.repository.RoleRepository;
import com.hildebrandt.polls.repository.UserRepository;
import com.hildebrandt.polls.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;


public class AuthController {
}
