package com.scnuvem.sc.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scnuvem.sc.auth.dtos.request.LoginDto;
import com.scnuvem.sc.auth.dtos.request.RegisterUserDto;
import com.scnuvem.sc.auth.dtos.response.LoginResponseDto;
import com.scnuvem.sc.auth.service.AuthService;
import com.scnuvem.sc.user.dtos.request.PatchUserEmailDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDto data) throws Exception {

        authService.createPatientUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody @Valid LoginDto data) throws Exception {
        try{
            LoginResponseDto loginResponseDto = authService.login(data);
            return ResponseEntity.ok().body(loginResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register/receptionist")
    public ResponseEntity<?> registerSuperAdmin(@RequestBody @Valid RegisterUserDto data) throws Exception {

        authService.createRecepionistUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String code) {
        try {
            authService.verifyUser(email, code);
            return ResponseEntity.ok("Usuário verificado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Código de verificação reenviado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/request-password-reset") 
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid PatchUserEmailDto email) {
        try {
            authService.requestPasswordReset(email.email()); 
            return ResponseEntity.ok("Email de redefinição enviado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
        @RequestParam String email,
        @RequestParam String token,
        @RequestParam String newPassword
    ) {
        try {
            authService.resetPassword(email, token, newPassword);
        return ResponseEntity.ok("Senha redefinida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
