package com.nhohantu.tcbookbe.common.service;

import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.request.RegisterRequest;
import com.nhohantu.tcbookbe.common.model.dto.response.RegisterResponse;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import com.nhohantu.tcbookbe.common.security.AuthenticationService;
import com.nhohantu.tcbookbe.common.security.UserDetailsImpl;
import com.nhohantu.tcbookbe.common.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@CustomLog
@RequiredArgsConstructor
public class UserBasicInfoService {
    private final AuthenticationService authService;
    private final UserDetailsServiceImpl userDetailsService;
    private final BaseUserInfoRepo baseUserInfoRepo;
    private final ModelMapper mapper;

    public UserBasicInfoModel getUserInfoFromContext() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            String username = userDetails.getUsername();
            return baseUserInfoRepo.findByUsername(username).orElse(null);
        } catch (Exception e) {
            log.error("Failed when get user from context: " + e.getMessage());
            return null;
        }
    }

    public ResponseEntity<ResponseDTO<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        UserDetailsImpl registeredUser;
        try {
            UserDetails usernameExist = userDetailsService.loadUserByUsername(request.getUsername());
            if (usernameExist != null) {
                return ResponseBuilder.badRequestResponse("Create new user failed, please use other username",
                        StatusCodeEnum.ERRORCODE4000);
            }

            // UserBasicInfoModel userBasicInfoModel;
            // try {
            // userBasicInfoModel = getUserInfoFromContext();
            // if (userBasicInfoModel == null) {
            // return ResponseBuilder.badRequestResponse("Create new user failed, can not
            // get user info from context",
            // StatusCodeEnum.ERRORCODE4000);
            // }
            // } catch (Exception e) {
            // return ResponseBuilder.badRequestResponse("Create new user failed, error when
            // get user info from context",
            // StatusCodeEnum.ERRORCODE4000);
            // }

            registeredUser = authService.signup(request);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Create new user failed, unexpected error",
                    StatusCodeEnum.ERRORCODE4000);
        }

        RegisterResponse response = mapper.map(registeredUser.getUser(), RegisterResponse.class);

        return ResponseBuilder.okResponse("Create new user successfully",
                response,
                StatusCodeEnum.SUCCESS2000);
    }
}
