package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.LoginRequestDto;
import com.br.lukisDEV.ifscore.dto.RegisterRequestDto;
import com.br.lukisDEV.ifscore.dto.TokenResponseDto;
import com.br.lukisDEV.ifscore.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register (@RequestBody @Valid RegisterRequestDto registerRequestDto) throws Exception {
        authenticationService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public TokenResponseDto login (@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception {
        return  authenticationService.login(loginRequestDto);
    }
}
