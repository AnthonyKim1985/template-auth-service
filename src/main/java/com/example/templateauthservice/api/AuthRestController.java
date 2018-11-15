package com.example.templateauthservice.api;

import com.example.templateauthservice.domain.constant.ResponseStatus;
import com.example.templateauthservice.security.util.JWTUtil;
import com.example.templateauthservice.vo.request.AuthRequest;
import com.example.templateauthservice.vo.response.AuthResponse;
import com.example.templateauthservice.vo.response.BasicResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

/**
 * @author Anthony Jinhyuk Kim
 * @version 1.0.0
 * @since 2018-11-06
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final JWTUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final ReactiveUserDetailsService userDetailsService;

    @Autowired
    public AuthRestController(@NotNull JWTUtil jwtUtil,
                              @NotNull PasswordEncoder passwordEncoder,
                              @NotNull ReactiveUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(httpMethod = "POST", value = "POST signin", response = String.class)
    public Mono<ResponseEntity<?>> signIn(@RequestBody AuthRequest authRequest) {
        return userDetailsService.findByUsername(authRequest.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException()))
                .flatMap(userDetails -> {
                    if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword()))
                        return Mono.just(ResponseEntity.badRequest().body(new BasicResponse(ResponseStatus.BAD_REQUEST_UNAUTHORIZED)));

                    return jwtUtil.generateToken(userDetails)
                            .switchIfEmpty(Mono.error(new RuntimeException()))
                            .flatMap(token -> Mono.just(ResponseEntity.ok().body(new AuthResponse(ResponseStatus.OK_TOKEN_ISSUED, token))));
                });
    }

    @GetMapping(value = "/test")
    @ApiOperation(httpMethod = "GET", value = "GET test", response = BasicResponse.class)
    public Mono<ResponseEntity<?>> test() {
        return Mono.just(ResponseEntity.ok().body(new BasicResponse(ResponseStatus.OK)));
    }
}
