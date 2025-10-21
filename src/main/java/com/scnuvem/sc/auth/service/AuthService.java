package com.scnuvem.sc.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scnuvem.sc.auth.dtos.request.LoginDto;
import com.scnuvem.sc.auth.dtos.request.RegisterUserDto;
import com.scnuvem.sc.auth.dtos.response.LoginResponseDto;
import com.scnuvem.sc.auth.entity.UserDetailsImp;
import com.scnuvem.sc.auth.exceptions.AlreadyVerifiedException;
import com.scnuvem.sc.auth.exceptions.ExpiredVerificationCodeException;
import com.scnuvem.sc.auth.exceptions.InvalidVerificationCodeException;
import com.scnuvem.sc.auth.exceptions.UserEmailNotFoundException;
import com.scnuvem.sc.email.EmailService;
import com.scnuvem.sc.user.entity.User;
import com.scnuvem.sc.user.enums.Role;
import com.scnuvem.sc.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private static final long VERIFICATION_CODE_EXPIRY_MINUTES = 5;
    private static final long PASSWORD_RESET_TOKEN_EXPIRY_MINUTES = 15;

    public LoginResponseDto login(LoginDto data) throws Exception{
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var  auth = this.authenticationManager.authenticate(usernamePassword);

            UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
            User user = userDetails.getUser();
            
            if(user.isEnable()   == false){
                throw new Exception("Usuário não verificado!");
            }

            var token = jwtService.generateToken(user);
            return new LoginResponseDto(token);
        }catch (AuthenticationException e) {
            System.out.println("Erro de autenticação: " + e.getMessage()); // Log para depuração
            return new LoginResponseDto("Login failed");
        }
    }

    @Transactional
    public void createPatientUser(RegisterUserDto data) throws Exception {
        if(userRepository.findByEmail(data.email()).isPresent()) {
            throw new Exception("Email already exists!");
        }
        if(userRepository.findByUsername(data.username()).isPresent()) {
            throw new Exception("Username already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data, encryptedPassword, Role.ROLE_PACIENTE); 

        generateAndSetVerificationCode(user); 
        userRepository.save(user);
    }

    @Transactional
    public void createRecepionistUser(RegisterUserDto data) throws Exception {
        if(userRepository.findByEmail(data.email()).isPresent()) {
            throw new Exception("Email already exists!");
        }
        if(userRepository.findByUsername(data.username()).isPresent()) {
            throw new Exception("Username already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data, encryptedPassword, Role.ROLE_RECEPCIONISTA); 

        generateAndSetVerificationCode(user); 
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        
        if(user == null) {
            throw new UserEmailNotFoundException("Usuário não encontrado");
        }
        
        if(user.get().isEnable()) {
            throw new AlreadyVerifiedException("Usuário já verificado");
        }
        
        User actualUser = user.get();
        if (actualUser.getVerificationCode() != null) {
            throw new Exception("Código de verificação já enviado");
        }
        generateAndSetVerificationCode(actualUser);
        userRepository.save(actualUser);
    }

    @SuppressWarnings("UseSpecificCatch")
    private void generateAndSetVerificationCode(User user){
        String code = generateRandomCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(
            LocalDateTime.now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES)
        );

        try {
            emailService.sendVerificationEmail(user.getEmail(), code);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar o email de verificação: " + e.getMessage());
        }
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);
        
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }

    @Transactional
    public void verifyUser(String email, String code) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        
        if(user.isEmpty()){
            throw new UserEmailNotFoundException("Usuário com email:"+email +" não foi encontrado");
        }

        User actualUser = user.get();

        if (actualUser.isEnable()) {
            throw new AlreadyVerifiedException("Usuário já verificado");
        }

        if (!code.equals(actualUser.getVerificationCode())) {
            throw new InvalidVerificationCodeException("Código inválido");
        }

        if (LocalDateTime.now().isAfter(actualUser.getVerificationCodeExpiry())) {
            throw new ExpiredVerificationCodeException("Código expirado");
        }

        actualUser.setEnabled(true);
        actualUser.setVerificationCode(null);
        actualUser.setVerificationCodeExpiry(null);
        emailService.sendVerifyMessage(actualUser.getEmail());
    }

    @Transactional
    public void requestPasswordReset(String email) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserEmailNotFoundException("Email não cadastrado");
        }

        User user = userOptional.get();
        String resetToken = generateRandomToken(); // Token único
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(
            LocalDateTime.now().plusMinutes(PASSWORD_RESET_TOKEN_EXPIRY_MINUTES)
        );
        userRepository.save(user);

        // Envia email com o token
        emailService.sendForgotPasswordEmail(email, resetToken);
    }

    @Transactional
    public void resetPassword(String email, String token, String newPassword) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserEmailNotFoundException("Email não cadastrado");
        }

        User user = userOptional.get();
        
        // Verifica token e expiração
        if (!token.equals(user.getPasswordResetToken())) {
            throw new InvalidVerificationCodeException("Token inválido");
        }
        if (LocalDateTime.now().isAfter(user.getPasswordResetTokenExpiry())) {
            throw new ExpiredVerificationCodeException("Token expirado");
        }

        // Atualiza senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptedPassword);
        user.setPasswordResetToken(null); // Invalida o token após uso
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
    }

    private String generateRandomToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }
}
