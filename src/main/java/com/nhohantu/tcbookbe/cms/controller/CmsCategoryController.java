package com.nhohantu.tcbookbe.cms.controller;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/categories")
@RequiredArgsConstructor
public class CmsCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<Category>>> getAllCategories(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Category> categories = categoryService.getAllCategories(pageable);
        return ResponseBuilder.okResponse("Lấy danh sách danh mục thành công", categories, StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Category>> createCategory(@RequestBody Category category) {
        return ResponseBuilder.okResponse("Tạo danh mục thành công", categoryService.createCategory(category),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Category>> getCategory(@PathVariable String id) {
        return ResponseBuilder.okResponse("Lấy chi tiết danh mục thành công", categoryService.getCategory(id),
                StatusCodeEnum.SUCCESS2000);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Category>> updateCategory(@PathVariable String id,
            @RequestBody Category category) {
        return ResponseBuilder.okResponse("Cập nhật danh mục thành công", categoryService.updateCategory(id, category),
                StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseBuilder.okResponse("Xóa danh mục thành công", null, StatusCodeEnum.SUCCESS2000);
    }
}
