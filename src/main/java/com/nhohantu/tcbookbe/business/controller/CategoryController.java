package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.cms.service.implement.CategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.Category;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<Category>>> getAllCategories(
            @PageableDefault(size = 100) Pageable pageable) {
        Page<Category> categories = categoryService.getAllCategories(pageable);
        return ResponseBuilder.okResponse("Get categories success", categories, StatusCodeEnum.SUCCESS2000);
    }
}
