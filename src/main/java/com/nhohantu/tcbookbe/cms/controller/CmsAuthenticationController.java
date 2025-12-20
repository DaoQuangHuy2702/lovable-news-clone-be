package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.request.LoginRequest;
import com.nhohantu.tcbookbe.common.model.dto.response.LoginResponse;
import com.nhohantu.tcbookbe.common.model.entity.Account;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.repository.AccountRepository;
import com.nhohantu.tcbookbe.common.security.JwtService;
import com.nhohantu.tcbookbe.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/cms")
@RequiredArgsConstructor
public class CmsAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            // 1. Authenticate username/password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            // 2. Check Role ADMIN
            Account account = accountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if (!"ADMIN".equals(account.getRole())) {
                return ResponseBuilder.badRequestResponse("Access Denied: Admin role required",
                        StatusCodeEnum.ERRORCODE4003);
            }

            if (!account.isActive()) {
                return ResponseBuilder.badRequestResponse("Account is inactive", StatusCodeEnum.ERRORCODE4003);
            }

            // 3. Generate Token
            UserDetailsImpl userDetails = new UserDetailsImpl(account);
            String jwtToken = jwtService.generateToken(userDetails);

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

            return ResponseBuilder.okResponse("Login successful", loginResponse, StatusCodeEnum.SUCCESS2000);

        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Invalid credentials", StatusCodeEnum.ERRORCODE4000);
        }
    }
}
