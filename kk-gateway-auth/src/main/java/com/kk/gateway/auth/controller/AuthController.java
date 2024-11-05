package com.kk.gateway.auth.controller;

import com.kk.gateway.auth.service.UserDetailsServiceImpl;
import com.kk.gateway.common.JwtRequest;
import com.kk.gateway.common.JwtResponse;
import com.kk.gateway.common.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = JwtUtils.generateToken(authenticationRequest.getUsername());
			return ResponseEntity.ok(new JwtResponse(0, "", jwt));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
}
