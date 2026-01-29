package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.response.CloudinarySignatureResponse;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cms/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String path = uploadService.uploadFile(file);
            return ResponseBuilder.okResponse("Tải lên thành công", path, StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            // Handle exceptions like MinIO not being reachable or upload failures
            return ResponseBuilder.badRequestResponse("Có lỗi xảy ra khi upload: " + e.getMessage(),
                    StatusCodeEnum.EXCEPTION);
        }
    }

    @GetMapping("/signature")
    public ResponseEntity<ResponseDTO<CloudinarySignatureResponse>> getSignature() {
        try {
            CloudinarySignatureResponse signature = uploadService.generateSignature();
            return ResponseBuilder.okResponse("Lấy chữ ký thành công", signature, StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Có lỗi xảy ra: " + e.getMessage(),
                    StatusCodeEnum.EXCEPTION);
        }
    }
}
