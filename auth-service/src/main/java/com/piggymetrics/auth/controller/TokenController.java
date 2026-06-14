package com.piggymetrics.auth.controller;

import java.time.Instant;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final AuthenticationManager authenticationManager;
    private final JWKSource<SecurityContext> jwkSource;

    public TokenController(AuthenticationManager authenticationManager,
                           JWKSource<SecurityContext> jwkSource) {
        this.authenticationManager = authenticationManager;
        this.jwkSource = jwkSource;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<?> token(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam(name = "grant_type", required = false) String grantType,
                                   @RequestParam(required = false) String scope) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_grant",
                                 "error_description", "Bad credentials"));
        }

        Instant now = Instant.now();
        long expiresIn = 43200; // 12 hours

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("scope", scope != null ? scope : "ui")
                .issueTime(java.util.Date.from(now))
                .expirationTime(java.util.Date.from(now.plusSeconds(expiresIn)))
                .build();

        try {
            JWKSet jwkSet = new JWKSet(jwkSource.get(null, null));
            RSAKey rsaKey = (RSAKey) jwkSet.getKeys().get(0);
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .build();
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new RSASSASigner(rsaKey));

            return ResponseEntity.ok(Map.of(
                    "access_token", signedJWT.serialize(),
                    "token_type", "bearer",
                    "expires_in", expiresIn,
                    "scope", scope != null ? scope : "ui"
            ));
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "server_error",
                                 "error_description", "Failed to generate token"));
        }
    }
}
